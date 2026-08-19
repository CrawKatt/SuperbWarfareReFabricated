package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.init.ModAttributes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;

public final class PlayerReachTool {
    private static final ResourceLocation REACH_ATTRIBUTE = new ResourceLocation("reach-entity-attributes", "reach");
    private static final ResourceLocation ATTACK_RANGE_ATTRIBUTE = new ResourceLocation("reach-entity-attributes", "attack_range");

    public static double getBlockReach(Player player) {
        double reach = player.getAttributeValue(ModAttributes.BLOCK_REACH);
        Attribute reachAttribute = BuiltInRegistries.ATTRIBUTE.get(REACH_ATTRIBUTE);
        if (reachAttribute != null && player.getAttributes().hasAttribute(reachAttribute)) {
            reach += player.getAttributeValue(reachAttribute);
        }
        return reach == 0 ? 0 : reach + (player.isCreative() ? 0.5D : 0);
    }

    public static double getEntityReach(Player player) {
        double reach = player.getAttributeValue(ModAttributes.ENTITY_REACH);
        Attribute attackRangeAttribute = BuiltInRegistries.ATTRIBUTE.get(ATTACK_RANGE_ATTRIBUTE);
        if (attackRangeAttribute != null && player.getAttributes().hasAttribute(attackRangeAttribute)) {
            reach += player.getAttributeValue(attackRangeAttribute);
        }
        return reach == 0 ? 0 : reach + (player.isCreative() ? 3.0D : 0);
    }

    public static double getBlockInteractionDistanceSqr(Player player) {
        double reach = getBlockReach(player) + 1.5D;
        return reach * reach;
    }
}
