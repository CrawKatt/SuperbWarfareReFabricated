package com.atsuishio.superbwarfare.capability.api;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Fabric equivalent of Forge's player inventory wrappers.
 */
public class PlayerInvWrapper extends InvWrapper {
    private final Inventory inventory;
    private final boolean mainOnly;

    public PlayerInvWrapper(Inventory inventory) {
        this(inventory, false);
    }

    public PlayerInvWrapper(Inventory inventory, boolean mainOnly) {
        super(inventory);
        this.inventory = inventory;
        this.mainOnly = mainOnly;
    }

    @Override
    public int getSlots() {
        return mainOnly ? inventory.items.size() : super.getSlots();
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        int armorSlot = slot - inventory.items.size();
        if (!mainOnly && armorSlot >= 0 && armorSlot < inventory.armor.size()) {
            EquipmentSlot equipmentSlot = EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, armorSlot);
            if (stack.isEmpty() || Mob.getEquipmentSlotForItem(stack) != equipmentSlot) return stack;
        }

        ItemStack remainder = super.insertItem(slot, stack, simulate);
        if (slot < inventory.items.size() && remainder.getCount() != stack.getCount()) {
            ItemStack inSlot = getStackInSlot(slot);
            if (!inSlot.isEmpty()) {
                if (inventory.player.level().isClientSide) {
                    inSlot.setPopTime(5);
                } else if (inventory.player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.containerMenu.broadcastChanges();
                }
            }
        }
        return remainder;
    }
}
