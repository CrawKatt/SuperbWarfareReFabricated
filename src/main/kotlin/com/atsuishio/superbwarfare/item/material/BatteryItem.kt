package com.atsuishio.superbwarfare.item.material

import com.atsuishio.superbwarfare.client.tooltip.component.CellImageComponent
import com.atsuishio.superbwarfare.init.ModCapabilities
import com.atsuishio.superbwarfare.capability.energy.EnergyStorageHelper
import com.atsuishio.superbwarfare.item.EnergyStorageItem
import dev.emi.trinkets.api.TrinketsApi
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage
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
        val cap = ModCapabilities.ENERGY_ITEM.find(pStack, null) ?: return false
        return cap.amount != cap.capacity
    }

    override fun getBarWidth(pStack: ItemStack): Int {
        var energy = 0
        val cap = ModCapabilities.ENERGY_ITEM.find(pStack, null)
        if (cap != null) {
            energy = cap.amount.toInt()
        }

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
        val cap = ModCapabilities.ENERGY_ITEM.find(stack, null) ?: return stack
        EnergyStorageHelper.insert(cap, maxEnergy.toLong())
        return stack
    }

    override fun inventoryTick(pStack: ItemStack, pLevel: Level, entity: Entity, pSlotId: Int, pIsSelected: Boolean) {
        super.inventoryTick(pStack, pLevel, entity, pSlotId, pIsSelected)
        if (pStack.tag == null || !pStack.tag!!.getBoolean(TAG_ENABLED)) return
        if (entity !is Player) return
        val energyStorage = ModCapabilities.ENERGY_ITEM.find(pStack, null) ?: return

        val playerInventoryStorage = PlayerInventoryStorage.of(entity)
        for ((slot, stack) in entity.inventory.items.withIndex()) {
            if (stack.item is BatteryItem) continue
            val toCharge = ContainerItemContext.ofSingleSlot(playerInventoryStorage.getSlot(slot))
                .find(ModCapabilities.ENERGY_ITEM) ?: continue
            if (!toCharge.supportsInsertion()) continue

            val cellEnergy = energyStorage.amount
            if (cellEnergy <= 0) break

            val stackEnergyNeed =
                min(cellEnergy.toDouble(), (toCharge.capacity - toCharge.amount).toDouble()).toInt()

            val received = EnergyStorageHelper.insert(toCharge, stackEnergyNeed.toLong())
            EnergyStorageHelper.extract(energyStorage, received)
        }

        TrinketsApi.getTrinketComponent(entity).ifPresent { component ->
            component.inventory.values.forEach { group ->
                group.values.forEach { inventory ->
                    val inventoryStorage = InventoryStorage.of(inventory, null)
                    for (i in 0..<inventory.containerSize) {
                        val stack = inventory.getItem(i)
                        if (stack.isEmpty) continue
                        if (stack.item is BatteryItem) continue

                        val toCharge = ContainerItemContext.ofSingleSlot(inventoryStorage.getSlot(i))
                            .find(ModCapabilities.ENERGY_ITEM) ?: continue
                        if (!toCharge.supportsInsertion()) continue

                        val cellEnergy = energyStorage.amount
                        if (cellEnergy <= 0) continue

                        val stackEnergyNeed =
                            min(cellEnergy.toDouble(), (toCharge.capacity - toCharge.amount).toDouble()).toInt()

                        val received = EnergyStorageHelper.insert(toCharge, stackEnergyNeed.toLong())
                        EnergyStorageHelper.extract(energyStorage, received)
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
