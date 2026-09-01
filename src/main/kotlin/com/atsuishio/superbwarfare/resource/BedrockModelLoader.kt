package com.atsuishio.superbwarfare.resource

import com.atsuishio.superbwarfare.resource.model.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.server.packs.PackType

@Environment(EnvType.CLIENT)
object BedrockModelLoader {
    @JvmStatic
    fun init() {
        val resources = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
        resources.registerReloadListener(VehicleModelReloadListener)
        resources.registerReloadListener(VehicleLODModelReloadListener)
        resources.registerReloadListener(ProjectileModelReloadListener)
        resources.registerReloadListener(EntityModelReloadListener)
        resources.registerReloadListener(ArmorModelReloadListener)
        resources.registerReloadListener(BlockModelReloadListener)
        resources.registerReloadListener(ItemModelReloadListener)
        resources.registerReloadListener(GunModelReloadListener)
        resources.registerReloadListener(GunLODModelReloadListener)
        resources.registerReloadListener(ShellModelReloadListener)
        resources.registerReloadListener(AttachmentModelReloadListener)
    }
}
