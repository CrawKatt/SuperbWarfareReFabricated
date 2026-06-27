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

class RuChest6b43Item : ArmorItem(
    Holder.direct(ModArmorMaterials.CEMENTED_CARBIDE),
    Type.CHESTPLATE,
    Properties().durability(Type.CHESTPLATE.getDurability(50))
) {
    override fun getDefaultAttributeModifiers(): ItemAttributeModifiers {
        val modifiers = super.getDefaultAttributeModifiers()
        val list = ArrayList<ItemAttributeModifiers.Entry>(modifiers.modifiers())

        list.add(
            ItemAttributeModifiers.Entry(
                ModAttributes.bulletResistanceHolder(),
                AttributeModifier(
                    Mod.ATTRIBUTE_MODIFIER,
                    0.5,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.bySlot(this.type.slot)
            )
        )

        return ItemAttributeModifiers(list, true)
    }

    companion object {
        val TEXTURE = loc("textures/bedrock/armor/ru_chest_6b43.png")

        @Environment(EnvType.CLIENT)
        private var renderer: GeoArmorRenderer? = null

        @Environment(EnvType.CLIENT)
        @JvmStatic
        fun registerRenderer() {
            ArmorRenderer.register(
                { matrices, vertexConsumers, stack, entity, slot, light, contextModel ->
                    var armorRenderer = renderer

                    if (armorRenderer == null) {
                        val model = BedrockModelLoader.getArmorModel(BedrockModelLoader.RU_CHEST_6B43_MODEL)
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
                ModItems.RU_CHEST_6B43
            )
        }
    }
}