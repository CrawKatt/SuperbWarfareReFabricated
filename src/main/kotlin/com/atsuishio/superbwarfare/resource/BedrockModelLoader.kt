package com.atsuishio.superbwarfare.resource

import com.atsuishio.superbwarfare.resource.model.ArmorModelReloadListener
import com.atsuishio.superbwarfare.resource.model.BlockModelReloadListener
import com.atsuishio.superbwarfare.resource.model.EntityModelReloadListener
import com.atsuishio.superbwarfare.resource.model.GunLODModelReloadListener
import com.atsuishio.superbwarfare.resource.model.GunModelReloadListener
import com.atsuishio.superbwarfare.resource.model.ItemModelReloadListener
import com.atsuishio.superbwarfare.resource.model.ProjectileModelReloadListener
import com.atsuishio.superbwarfare.resource.model.ShellModelReloadListener
import com.atsuishio.superbwarfare.resource.model.VehicleLODModelReloadListener
import com.atsuishio.superbwarfare.resource.model.VehicleModelReloadListener
import com.atsuishio.superbwarfare.resource.model.AttachmentModelReloadListener
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
    }
}
