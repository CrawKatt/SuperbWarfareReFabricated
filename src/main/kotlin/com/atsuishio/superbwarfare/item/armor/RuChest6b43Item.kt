package com.atsuishio.superbwarfare.item.armor

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.resource.model.ArmorModelReloadListener
import com.atsuishio.superbwarfare.tiers.ModArmorMaterials
import com.github.mcmodderanchor.simplebedrockmodel.v2.client.renderer.GeoArmorRendererV2
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer
import net.minecraft.core.Holder

@RegistryName("ru_chest_6b43")
class RuChest6b43Item : BulletproofArmorItem(Holder.direct(ModArmorMaterials.CEMENTED_CARBIDE), Type.CHESTPLATE, 0.5) {
    companion object {
        val TEXTURE = loc("textures/bedrock/armor/ru_chest_6b43.png")
        val MODEL = loc("models/bedrock/armor/ru_chest_6b43.geo.json")

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
                            ?: return@register

                        armorRenderer = GeoArmorRendererV2(model, slot, TEXTURE)
                        renderer = armorRenderer
                    }

                    armorRenderer.preparePose(entity, stack, slot, contextModel)
                    armorRenderer.young = entity.isBaby

                    ArmorRenderer.renderPart(
                        matrices,
                        vertexConsumers,
                        light,
                        stack,
                        armorRenderer,
                        TEXTURE
                    )
                },
                ModItems.RU_CHEST_6B43
            )
        }
    }
}
