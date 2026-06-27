package com.atsuishio.superbwarfare.item.armor

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModAttributes
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.resource.BedrockModelLoader
import com.atsuishio.superbwarfare.tiers.ModArmorMaterials
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.renderer.GeoArmorRenderer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer
import net.minecraft.core.Holder
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.component.ItemAttributeModifiers

class GeHelmetM35Item :
    ArmorItem(Holder.direct(ModArmorMaterials.STEEL), Type.HELMET, Properties().durability(Type.HELMET.getDurability(35))) {

    override fun getDefaultAttributeModifiers(): ItemAttributeModifiers {
        val modifiers = super.getDefaultAttributeModifiers()
        val list = ArrayList<ItemAttributeModifiers.Entry>(modifiers.modifiers())

        list.add(
            ItemAttributeModifiers.Entry(
                ModAttributes.bulletResistanceHolder(),
                AttributeModifier(
                    Mod.ATTRIBUTE_MODIFIER,
                    0.1,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.bySlot(this.type.slot)
            )
        )

        return ItemAttributeModifiers(list, true)
    }

    companion object {
        val TEXTURE = loc("textures/bedrock/armor/ge_helmet_m_35.png")

        @Environment(EnvType.CLIENT)
        private var renderer: GeoArmorRenderer? = null

        @Environment(EnvType.CLIENT)
        @JvmStatic
        fun registerRenderer() {
            ArmorRenderer.register(
                { matrices, vertexConsumers, stack, entity, slot, light, contextModel ->
                    var armorRenderer = renderer

                    if (armorRenderer == null) {
                        val model = BedrockModelLoader.getArmorModel(BedrockModelLoader.GE_HELMET_M_35_MODEL)
                            ?: return@register

                        armorRenderer = GeoArmorRenderer(model, TEXTURE)
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
                ModItems.GE_HELMET_M_35
            )
        }
    }
}