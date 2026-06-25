package com.atsuishio.superbwarfare.compat.realcamera

import com.atsuishio.superbwarfare.compat.CompatHolder
import com.xtracr.realcamera.RealCameraCore
import com.xtracr.realcamera.util.CrosshairUtil
import net.fabricmc.loader.api.FabricLoader

object RealCameraCompatHolder {
    @JvmStatic
    fun hasMod(): Boolean {
        return FabricLoader.getInstance().isModLoaded(CompatHolder.REALCAMERA)
    }

    @JvmStatic
    fun getCompatMoveX(moveX: Float): Float {
        var moveX = moveX
        if (RealCameraCore.isActive()) {
            moveX += CrosshairUtil.offset.x().toFloat()
        }
        return moveX
    }

    @JvmStatic
    fun getCompatMoveY(moveY: Float): Float {
        var moveY = moveY
        if (RealCameraCore.isActive()) {
            moveY -= CrosshairUtil.offset.y().toFloat()
        }
        return moveY
    }
}