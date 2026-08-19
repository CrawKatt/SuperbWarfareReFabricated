package com.atsuishio.superbwarfare.item.armor

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModAttributes
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.resource.BedrockModelLoader
import com.atsuishio.superbwarfare.tiers.ModArmorMaterial
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.renderer.GeoArmorRenderer
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ItemStack
import java.util.UUID
import kotlin.math.max

class RuChest6b43Item : ArmorItem(
    ModArmorMaterial.CEMENTED_CARBIDE,
    Type.CHESTPLATE,
    Properties()
) {
    override fun getAttributeModifiers(stack: ItemStack, slot: EquipmentSlot): Multimap<Attribute, AttributeModifier> {
        var modifiers = super.getDefaultAttributeModifiers(slot)
        if (slot == EquipmentSlot.CHEST) {
            modifiers = HashMultimap.create(modifiers)
            modifiers.put(
                ModAttributes.BULLET_RESISTANCE,
                AttributeModifier(
                    UUID(slot.toString().hashCode().toLong(), 0), Mod.ATTRIBUTE_MODIFIER,
                    0.5 * max(0.0, 1 - stack.damageValue.toDouble() / stack.maxDamage),
                    AttributeModifier.Operation.ADDITION
                )
            )
        }
        return modifiers
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
