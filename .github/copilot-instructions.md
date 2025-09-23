# FlClash - Multi-Platform Proxy Client
FlClash is a Flutter-based cross-platform proxy client based on ClashMeta, with a Go core, Rust helper service, and native platform plugins. It supports Android, Windows, macOS, and Linux.

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Working Effectively
Bootstrap, build, and test the repository:

1. **CRITICAL**: Update submodules first (SSH access might fail - use HTTPS):
   ```bash
   git submodule update --init --recursive
   ```
   **If SSH fails**: Convert .gitmodules to HTTPS URLs:
   ```bash
   sed -i 's|git@github.com:|https://github.com/|g' .gitmodules
   git submodule sync
   git submodule update --init --recursive
   ```

2. **Install required dependencies** (exact order is critical):
   - **Flutter SDK**: Download latest stable Flutter SDK for your platform
   - **Golang 1.25+**: Required for core component
   - **Rust/Cargo**: Required for helper service
   - **Platform-specific dependencies** (see platform sections below)

3. **Linux dependencies** (required even for cross-compilation):
   ```bash
   sudo apt update -y
   sudo apt install -y ninja-build libgtk-3-dev libayatana-appindicator3-dev libkeybinder-3.0-dev locate
   ```

4. **Install Flutter dependencies**:
   ```bash
   flutter pub get
   ```

5. **Build components**:
   
   **Go Core** (takes ~2 minutes with dependencies download, ~10 seconds incremental):
   ```bash
   cd core
   # For executable (Linux/Windows/macOS):
   CGO_ENABLED=0 go build -ldflags="-w -s" -tags=with_gvisor -o FlClashCore
   
   # For library (Android):
   CGO_ENABLED=1 go build -ldflags="-w -s" -tags=with_gvisor -buildmode=c-shared -o libclash.so
   ```
   
   **Rust Helper Service** (takes ~40 seconds clean build, ~5 seconds incremental):
   ```bash
   cd services/helper
   cargo build --release
   # NEVER CANCEL - Build takes 45 seconds. Set timeout to 120+ seconds.
   ```

6. **Platform-specific builds** using the setup.dart script:
   
   **Android**:
   ```bash
   # Requires ANDROID_SDK, ANDROID_NDK, and ANDROID_NDK environment variable
   dart setup.dart android
   # Takes 10-15 minutes. NEVER CANCEL. Set timeout to 30+ minutes.
   ```
   
   **Linux**:
   ```bash
   dart setup.dart linux --arch amd64
   # OR dart setup.dart linux --arch arm64
   # Takes 15-20 minutes. NEVER CANCEL. Set timeout to 45+ minutes.
   ```
   
   **Windows** (Windows host required):
   ```bash
   # Requires Gcc and Inno Setup
   dart setup.dart windows --arch amd64
   # OR dart setup.dart windows --arch arm64
   # Takes 15-20 minutes. NEVER CANCEL. Set timeout to 45+ minutes.
   ```
   
   **macOS** (macOS host required):
   ```bash
   dart setup.dart macos --arch arm64
   # OR dart setup.dart macos --arch amd64
   # Takes 15-20 minutes. NEVER CANCEL. Set timeout to 45+ minutes.
   ```

## Validation
- **Always manually test after changes**: The application requires specific runtime environment setup
- **Cannot easily test GUI functionality** in sandboxed environments
- Always run `flutter analyze` before committing (requires Flutter dependencies)
- Always run `dart format lib/` to format code before committing
- For Go core: Run `go test ./...` in the core directory
- For Rust helper: Run `cargo test` in services/helper directory

## Common Issues and Solutions
- **"Arguments error" from FlClashCore**: Normal - the core requires connection arguments to run
- **Flutter dependencies missing**: Run `flutter pub get` in project root
- **Submodule clone failures**: Convert SSH URLs to HTTPS in .gitmodules
- **Build timeouts**: Use adequate timeouts - builds can take 20+ minutes
- **Missing native dependencies**: Install platform-specific packages listed above

## Key Project Structure
```
.
├── android/           # Android platform code
├── core/             # Go-based ClashMeta core
│   ├── Clash.Meta/   # ClashMeta submodule
│   ├── go.mod        # Go dependencies
│   └── *.go          # Core implementation
├── lib/              # Flutter/Dart application code
├── linux/            # Linux platform code
├── macos/            # macOS platform code
├── plugins/          # Flutter plugins
│   ├── proxy/        # Native proxy plugin
│   ├── window_ext/   # Window management
│   └── flutter_distributor/ # Build distribution submodule
├── services/         # Background services
│   └── helper/       # Rust helper service
├── windows/          # Windows platform code
├── setup.dart        # Main build script
├── Makefile          # Build shortcuts
└── pubspec.yaml      # Flutter dependencies
```

## Critical Build Requirements
- **NEVER CANCEL BUILDS**: Core builds: 2-10 minutes, Helper builds: 45 seconds, Full app builds: 15-45 minutes
- **Always use adequate timeouts**: Set 60+ minutes for full builds, 30+ minutes for incremental builds
- **Cross-platform builds require host platform**: Windows builds need Windows, macOS builds need macOS
- **Android builds require**: Android SDK, Android NDK, ANDROID_NDK environment variable set
- **The application requires specific runtime dependencies** on target platforms (see README.md)

## Environment Variables and Configuration
- `ANDROID_NDK`: Required for Android builds - path to Android NDK
- `CGO_ENABLED`: Set to "1" for library builds, "0" for executable builds
- `GOOS`, `GOARCH`: Set automatically by build scripts for cross-compilation
- `TOKEN`: Used by Rust helper service for validation

## Testing and Validation Scenarios
Since this is a proxy client application, manual testing requires:
1. **Network configuration**: Valid proxy servers for testing
2. **Platform permissions**: The app requires system-level network permissions
3. **Multiple platforms**: Test on actual target platforms when possible
4. **Core functionality**: Test proxy connection establishment and traffic routing

Always validate that core components build and basic commands execute without critical errors, even if full functional testing isn't possible in sandboxed environments.