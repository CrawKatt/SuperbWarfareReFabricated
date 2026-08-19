package com.atsuishio.superbwarfare.item.misc

import com.atsuishio.superbwarfare.capability.PersistentDataAccessor
import com.atsuishio.superbwarfare.client.item.MonitorClient
import com.atsuishio.superbwarfare.entity.vehicle.DroneEntity
import com.atsuishio.superbwarfare.network.message.receive.ResetCameraTypeMessage
import com.atsuishio.superbwarfare.tools.EntityFindUtil
import com.atsuishio.superbwarfare.tools.sendPacket
import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

open class MonitorItem : Item(Properties().stacksTo(1)) {
    private fun resetDroneData(drone: DroneEntity?) {
        if (drone == null) return

        val data = (drone as PersistentDataAccessor).`superbwarfare$getPersistentData`()
        data.putBoolean("left", false)
        data.putBoolean("right", false)
        data.putBoolean("forward", false)
        data.putBoolean("backward", false)
        data.putBoolean("up", false)
        data.putBoolean("down", false)
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.mainHandItem

        if (stack.tag == null || !stack.tag!!.getBoolean(LINKED)) {
            return super.use(level, player, hand)
        }

        if (stack.getOrCreateTag().getBoolean(USING)) {
            stack.getOrCreateTag().putBoolean(USING, false)
            if (level.isClientSide) {
                MonitorClient.stopUsing()
            }
        } else {
            stack.getOrCreateTag().putBoolean(USING, true)
            if (level.isClientSide) {
                MonitorClient.startUsing()
            }
        }

        val drone = EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString(LINKED_DRONE))
        this.resetDroneData(drone)

        return super.use(level, player, hand)
    }

    override fun getAttributeModifiers(stack: ItemStack, slot: EquipmentSlot): Multimap<Attribute, AttributeModifier> {
        if (slot == EquipmentSlot.MAINHAND) {
            val builder = ImmutableMultimap.builder<Attribute, AttributeModifier>()
            builder.putAll(super.getAttributeModifiers(stack, slot))
            builder.put(
                Attributes.ATTACK_DAMAGE,
                AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Item modifier", 2.0, AttributeModifier.Operation.ADDITION)
            )
            builder.put(
                Attributes.ATTACK_SPEED,
                AttributeModifier(BASE_ATTACK_SPEED_UUID, "Item modifier", -2.4, AttributeModifier.Operation.ADDITION)
            )
            return builder.build()
        }

        return super.getAttributeModifiers(stack, slot)
    }

    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        if (!stack.getOrCreateTag().contains(LINKED_DRONE)
            || stack.getOrCreateTag().getString(LINKED_DRONE) == "none"
        ) return
        MonitorClient.appendHoverText(stack, tooltipComponents)
    }

    override fun inventoryTick(itemstack: ItemStack, world: Level, entity: Entity, slot: Int, selected: Boolean) {
        super.inventoryTick(itemstack, world, entity, slot, selected)
        val drone = EntityFindUtil.findDrone(entity.level(), itemstack.getOrCreateTag().getString(LINKED_DRONE))

        if (!selected) {
            if (itemstack.getOrCreateTag().getBoolean(USING)) {
                itemstack.getOrCreateTag().putBoolean(USING, false)
                if (entity.level().isClientSide) {
                    MonitorClient.stopUsing()
                }
            }
            this.resetDroneData(drone)
        } else if (drone == null) {
            if (itemstack.getOrCreateTag().getBoolean(USING)) {
                itemstack.getOrCreateTag().putBoolean(USING, false)
                if (entity.level().isClientSide) {
                    MonitorClient.stopUsing()
                }
            }
        }
    }

    companion object {
        const val LINKED: String = "Linked"
        const val LINKED_DRONE: String = "LinkedDrone"
        const val USING: String = "Using"

        @JvmStatic
        fun link(itemstack: ItemStack, id: String) {
            itemstack.getOrCreateTag().putBoolean(LINKED, true)
            itemstack.getOrCreateTag().putString(LINKED_DRONE, id)
        }

        @JvmStatic
        fun disLink(itemstack: ItemStack, player: Player?) {
            itemstack.getOrCreateTag().putBoolean(LINKED, false)
            itemstack.getOrCreateTag().putString(LINKED_DRONE, "none")
            if (player is ServerPlayer) {
                player.sendPacket(ResetCameraTypeMessage)
            }
        }

        @JvmStatic
        fun getDronePos(itemstack: ItemStack, vec3: Vec3) {
            itemstack.getOrCreateTag().putDouble("PosX", vec3.x)
            itemstack.getOrCreateTag().putDouble("PosY", vec3.y)
            itemstack.getOrCreateTag().putDouble("PosZ", vec3.z)
        }
    }
}
