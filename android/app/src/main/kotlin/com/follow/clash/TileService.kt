package com.follow.clash

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TileService : TileService() {
    private var scope: CoroutineScope? = null
    private fun updateTile(runState: RunState) {
        if (qsTile != null) {
            qsTile.state = when (runState) {
                RunState.START -> Tile.STATE_ACTIVE
                RunState.PENDING -> Tile.STATE_UNAVAILABLE
                RunState.STOP -> Tile.STATE_INACTIVE
            }
            qsTile.updateTile()
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        scope?.cancel()
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        scope?.launch {
            State.handleSyncState()
            State.runStateFlow.collect {
                updateTile(it)
            }
        }
    }

    private fun handleToggle() {
        // 直接在TileService中处理开关逻辑，避免启动Activity
        // 这样可以保持通知栏/快速设置面板打开状态
        scope?.launch {
            // 立即更新Tile状态以提供用户反馈
            withContext(Dispatchers.Main) {
                qsTile?.state = Tile.STATE_UNAVAILABLE
                qsTile?.updateTile()
            }

            // 执行开关逻辑
            State.handleToggleAction()
        }
    }

    override fun onClick() {
        super.onClick()
        handleToggle()
    }

    override fun onStopListening() {
        scope?.cancel()
        super.onStopListening()
    }
}
