package com.atsuishio.superbwarfare.perk.functional;

import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.init.ModPerks;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.tools.DamageTypeTool;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;

public class PowerfulAttraction extends Perk {

    public PowerfulAttraction() {
        super("powerful_attraction", Perk.Type.FUNCTIONAL);
    }

    public static void handleDrops(DamageSource source, Entity sourceEntity, Collection<Entity> drops) {
        if (!(sourceEntity instanceof Player player)) return;
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return;
        int level = GunData.from(stack).perk.getLevel(ModPerks.POWERFUL_ATTRACTION);
        if (level > 0 && (DamageTypeTool.isGunDamage(source) || DamageTypeTool.isExplosionDamage(source))) {
            for (var itemEntity : List.copyOf(drops)) {
                // TODO: handle entity-based drops properly
            }
        }
    }

    public static int handleExperienceDrop(Player player, int originalXp) {
        if (player == null) return originalXp;

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return originalXp;

        int level = GunData.from(stack).perk.getLevel(ModPerks.POWERFUL_ATTRACTION);
        if (level > 0) {
            player.giveExperiencePoints((int) (originalXp * (0.8f + 0.2f * level)));
            return 0;
        }
        return originalXp;
    }
}
