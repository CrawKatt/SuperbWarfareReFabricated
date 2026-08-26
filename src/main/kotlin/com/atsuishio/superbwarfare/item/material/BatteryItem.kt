package com.atsuishio.superbwarfare.item.material

import com.atsuishio.superbwarfare.client.tooltip.component.CellImageComponent
import com.atsuishio.superbwarfare.init.ModCapabilities
import com.atsuishio.superbwarfare.item.EnergyStorageItem
import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.SlotAccess
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import java.util.*
import kotlin.math.min
import kotlin.math.roundToInt

open class BatteryItem(var maxEnergy: Int, properties: Properties) :
    Item(properties.stacksTo(1)), EnergyStorageItem {

    companion object {
        const val TAG_ENABLED = "Enabled"
    }

    override fun isBarVisible(pStack: ItemStack): Boolean {
        return (ModCapabilities.ENERGY_ITEM.find(pStack, null)?.energyStored ?: 0) != maxEnergy
    }

    override fun getBarWidth(pStack: ItemStack): Int {
        val energy = ModCapabilities.ENERGY_ITEM.find(pStack, null)?.energyStored ?: 0

        return (energy * 13f / maxEnergy).roundToInt()
    }

    override fun getBarColor(pStack: ItemStack): Int {
        return 0xFFFF00
    }

    override fun getTooltipImage(pStack: ItemStack): Optional<TooltipComponent> {
        return Optional.of<TooltipComponent>(CellImageComponent(pStack))
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {
        val flag = pStack.tag == null || !pStack.tag!!.getBoolean(TAG_ENABLED)
        pTooltipComponents.add(
            Component.translatable("des.superbwarfare.battery.${if (flag) "disable" else "enable"}").withStyle(
                if (flag) ChatFormatting.GRAY else ChatFormatting.GREEN
            )
        )
    }

    fun makeFullEnergyStack(): ItemStack {
        val stack = ItemStack(this)
        ModCapabilities.ENERGY_ITEM.find(stack, null)?.receiveEnergy(maxEnergy, false)
        return stack
    }

    override fun inventoryTick(pStack: ItemStack, pLevel: Level, entity: Entity, pSlotId: Int, pIsSelected: Boolean) {
        super.inventoryTick(pStack, pLevel, entity, pSlotId, pIsSelected)
        if (pStack.tag == null || !pStack.tag!!.getBoolean(TAG_ENABLED)) return
        if (entity !is Player) return
        val energyStorage = ModCapabilities.ENERGY_ITEM.find(pStack, null) ?: return

        for (stack in entity.inventory.items) {
            if (stack.item is BatteryItem) continue
            val toCharge = ModCapabilities.ENERGY_ITEM.find(stack, null) ?: continue
            if (!toCharge.canReceive()) continue

            val cellEnergy = energyStorage.energyStored
            if (cellEnergy <= 0) break

            val stackEnergyNeed =
                min(cellEnergy.toDouble(), (toCharge.maxEnergyStored - toCharge.energyStored).toDouble()).toInt()

            val received = toCharge.receiveEnergy(stackEnergyNeed, false)
            energyStorage.extractEnergy(received, false)
        }

        TrinketsApi.getTrinketComponent(entity).ifPresent { component ->
            component.inventory.values.forEach { group ->
                group.values.forEach { inventory ->
                    for (slot in 0 until inventory.containerSize) {
                        val stack = inventory.getItem(slot)
                        if (stack.isEmpty || stack.item is BatteryItem) continue
                        val toCharge = ModCapabilities.ENERGY_ITEM.find(stack, null) ?: continue
                        if (!toCharge.canReceive()) continue

                        val cellEnergy = energyStorage.energyStored
                        if (cellEnergy <= 0) continue

                        val stackEnergyNeed =
                            min(cellEnergy.toDouble(), (toCharge.maxEnergyStored - toCharge.energyStored).toDouble()).toInt()

                        val received = toCharge.receiveEnergy(stackEnergyNeed, false)
                        energyStorage.extractEnergy(received, false)
                    }
                }
            }
        }
    }

    override fun overrideOtherStackedOnMe(
        stack: ItemStack,
        other: ItemStack,
        slot: Slot,
        action: ClickAction,
        player: Player,
        access: SlotAccess
    ): Boolean {
        if (other.isEmpty && action == ClickAction.SECONDARY) {
            stack.orCreateTag.putBoolean(TAG_ENABLED, !stack.orCreateTag.getBoolean(TAG_ENABLED))
            return true
        }
        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access)
    }

    override fun getMaxEnergy(stack: ItemStack): Int = maxEnergy
}
