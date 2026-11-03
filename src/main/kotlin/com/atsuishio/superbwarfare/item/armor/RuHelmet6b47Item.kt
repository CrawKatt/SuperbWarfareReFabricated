package com.atsuishio.superbwarfare.item.armor

import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.resource.model.ArmorModelReloadListener
import com.atsuishio.superbwarfare.tiers.ModArmorMaterial
import com.github.mcmodderanchor.simplebedrockmodel.v2.client.renderer.GeoArmorRendererV2
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer

@RegistryName("ru_helmet_6b47")
class RuHelmet6b47Item : BulletproofArmorItem(ModArmorMaterial.CEMENTED_CARBIDE, Type.HELMET, 0.2) {
    companion object {
        val TEXTURE = loc("textures/bedrock/armor/ru_helmet_6b47.png")
        val MODEL = loc("models/bedrock/armor/ru_helmet_6b47.geo.json")

        @Environment(EnvType.CLIENT)
        private var renderer: GeoArmorRendererV2? = null

        @Environment(EnvType.CLIENT)
        @JvmStatic
        fun registerRenderer() {
            ArmorRenderer.register(
                { matrices, vertexConsumers, stack, entity, slot, light, contextModel ->
                    var armorRenderer = renderer

                    if (armorRenderer == null) {
                        val model = ArmorModelReloadListener.getModel(MODEL)

                        if (model != null) {
                            armorRenderer = GeoArmorRendererV2(model, slot, TEXTURE)
                            renderer = armorRenderer
                        }
                    }

                    armorRenderer?.let {
                        it.preparePose(entity, stack, slot, contextModel)
                        it.young = entity.isBaby

                        ArmorRenderer.renderPart(
                            matrices,
                            vertexConsumers,
                            light,
                            stack,
                            it,
                            TEXTURE
                        )
                    }
                },
                ModItems.RU_HELMET_6B47
            )
        }
    }
}
