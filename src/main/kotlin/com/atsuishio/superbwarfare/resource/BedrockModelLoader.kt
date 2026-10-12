package com.atsuishio.superbwarfare.resource

import com.atsuishio.superbwarfare.client.renderer.gun.GunEmissiveTextures
import com.atsuishio.superbwarfare.resource.model.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.server.packs.PackType

@Environment(EnvType.CLIENT)
object BedrockModelLoader {
    private var initialized = false

    @JvmStatic
    fun init() {
        if (initialized) return
        initialized = true

        val helper = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
        helper.registerReloadListener(VehicleModelReloadListener)
        helper.registerReloadListener(VehicleLODModelReloadListener)
        helper.registerReloadListener(ProjectileModelReloadListener)
        helper.registerReloadListener(EntityModelReloadListener)
        helper.registerReloadListener(ArmorModelReloadListener)
        helper.registerReloadListener(BlockModelReloadListener)
        helper.registerReloadListener(ItemModelReloadListener)
        helper.registerReloadListener(GunModelReloadListener)
        helper.registerReloadListener(GunLODModelReloadListener)
        helper.registerReloadListener(ShellModelReloadListener)
        helper.registerReloadListener(AttachmentModelReloadListener)
        // 只是清一下"哪张枪械贴图有 _e 自发光层"的缓存：这个答案只在换资源包时才会变。
        helper.registerReloadListener(GunEmissiveTextures)
    }
}
