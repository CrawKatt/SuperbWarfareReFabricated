package com.atsuishio.superbwarfare.perk.functional;

import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.perk.PerkInstance;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

public class Regeneration extends Perk {

    public Regeneration() {
        super("regeneration", Perk.Type.FUNCTIONAL);
    }

    @Override
    public void tick(GunData data, PerkInstance instance, @Nullable Entity entity) {
        ItemStack stack = data.stack;
        EnergyStorage cap = EnergyStorage.ITEM.find(stack, ContainerItemContext.withConstant(stack));
        if (cap != null && cap.supportsInsertion()) {
            try (Transaction tx = Transaction.openOuter()) {
                long maxReceive = (long) (instance.level() * cap.getCapacity() / 2000d);
                cap.insert(maxReceive, tx);
                tx.commit();
            }
        }
    }
}
