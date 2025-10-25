package com.follow.clash.common

/**
 * Performance and battery optimization configuration
 * 性能与省电优化配置
 *
 * All settings can be toggled to measure impact or rollback changes
 */
object PerformanceConfig {
    /**
     * TUN interface MTU size
     * 1500: Standard MTU, avoids fragmentation (recommended for battery)
     * 9000: Jumbo frames, may cause fragmentation on most networks
     */
    const val TUN_MTU = 1500

    /**
     * Notification update interval in milliseconds
     * Higher values reduce CPU wake-ups but decrease UI responsiveness
     *
     * 1000: Update every second (legacy)
     * 2000: Update every 2 seconds (recommended)
     * 5000: Update every 5 seconds (aggressive battery saving)
     */
    const val NOTIFICATION_UPDATE_INTERVAL_MS = 2000L

    /**
     * Enable notification content change detection
     * Only update notification if traffic stats actually changed
     *
     * true: Skip redundant updates (recommended)
     * false: Always update (legacy behavior)
     */
    const val NOTIFICATION_SKIP_IDENTICAL_UPDATES = true

    /**
     * DNS update debounce delay in milliseconds
     * Prevents rapid DNS updates during network transitions
     *
     * 0: No debounce (legacy)
     * 400: Wait 400ms to batch updates (recommended)
     * 800: Wait 800ms (aggressive batching)
     */
    const val DNS_UPDATE_DEBOUNCE_MS = 400L

    /**
     * UID-to-package name cache max size (LRU)
     * Prevents unbounded memory growth during long VPN sessions
     *
     * 1024: Recommended for typical usage
     * 2048: For devices with many apps
     */
    const val UID_CACHE_MAX_SIZE = 1024

    /**
     * Enable debug logging in VpnService
     * Logs route/address setup details
     *
     * Only effective in debug builds (BuildConfig.DEBUG)
     */
    const val ENABLE_VPN_DEBUG_LOGS = true
}
