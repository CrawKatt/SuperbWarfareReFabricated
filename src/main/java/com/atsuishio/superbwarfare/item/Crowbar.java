package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.init.ModBlocks;
import com.atsuishio.superbwarfare.init.ModItems;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Crowbar extends SwordItem {

    public Crowbar() {
        super(new Tier() {
            public int getUses() {
                return 400;
            }

            public float getSpeed() {
                return 4f;
            }

            public float getAttackDamageBonus() {
                return 3.5f;
            }

            public int getLevel() {
                return 1;
            }

            public int getEnchantmentValue() {
                return 9;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of(new ItemStack(Items.IRON_INGOT));
            }
        }, 2, -2f, new Properties());
    }

    @Override
    public boolean isRepairable(ItemStack itemstack) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        super.useOn(context);
        if ((context.getLevel().getBlockState(BlockPos.containing(context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ()))).getBlock() == ModBlocks.JUMP_PAD.get()) {
            context.getLevel().setBlock(BlockPos.containing(context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ()), Blocks.AIR.defaultBlockState(), 3);
            var player = context.getPlayer();
            if (player != null && !player.addItem(new ItemStack(ModItems.JUMP_PAD.get()))) {
                player.drop(new ItemStack(ModItems.JUMP_PAD.get()), false);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = super.getDefaultAttributeModifiers(slot);
        return map;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("des.superbwarfare.crowbar").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable("des.superbwarfare.crowbar_2").withStyle(ChatFormatting.GRAY));
    }
}
