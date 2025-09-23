import 'package:fl_clash/models/app.dart';
import 'package:fl_clash/plugins/tile.dart';
import 'package:fl_clash/providers/state.dart';
import 'package:fl_clash/state.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class TileManager extends ConsumerStatefulWidget {
  final Widget child;

  const TileManager({super.key, required this.child});

  @override
  ConsumerState<TileManager> createState() => _TileContainerState();
}

class _TileContainerState extends ConsumerState<TileManager> with TileListener {
  @override
  Widget build(BuildContext context) {
    return widget.child;
  }

  void onToggle() {
    // Implement proper toggle behavior like the tray and start button
    globalState.appController.updateStart();
  }

  @override
  void onStart() {
    // Android sends specific start event - handle as toggle for better reliability
    onToggle();
    super.onStart();
  }

  @override
  Future<void> onStop() async {
    // Android sends specific stop event - handle as toggle for better reliability
    onToggle();
    super.onStop();
  }

  @override
  void initState() {
    super.initState();
    tile?.addListener(this);
  }

  @override
  void dispose() {
    tile?.removeListener(this);
    super.dispose();
  }
}
