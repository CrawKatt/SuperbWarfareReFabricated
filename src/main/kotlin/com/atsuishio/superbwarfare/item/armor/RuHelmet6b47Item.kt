package com.atsuishio.superbwarfare.item.armor

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModAttributes
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.resource.model.ArmorModelReloadListener
import com.atsuishio.superbwarfare.tiers.ModArmorMaterials
import com.github.mcmodderanchor.simplebedrockmodel.v2.client.renderer.GeoArmorRendererV2
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer
import net.minecraft.core.Holder
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.component.ItemAttributeModifiers

class RuHelmet6b47Item : ArmorItem(
    Holder.direct(ModArmorMaterials.CEMENTED_CARBIDE),
    Type.HELMET,
    Properties().durability(Type.HELMET.getDurability(50))
) {
    override fun getDefaultAttributeModifiers(): ItemAttributeModifiers {
        val modifiers = super.getDefaultAttributeModifiers()
        val list = ArrayList<ItemAttributeModifiers.Entry>(modifiers.modifiers())

        list.add(
            ItemAttributeModifiers.Entry(
                ModAttributes.bulletResistanceHolder(),
                AttributeModifier(
                    Mod.ATTRIBUTE_MODIFIER,
                    0.2,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.bySlot(this.type.slot)
            )
        )

        return ItemAttributeModifiers(list, true)
    }

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
