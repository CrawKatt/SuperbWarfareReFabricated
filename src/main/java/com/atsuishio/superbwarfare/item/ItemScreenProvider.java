package com.atsuishio.superbwarfare.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ItemScreenProvider {

    @Environment(EnvType.CLIENT)
    @Nullable Screen getItemScreen(ItemStack stack, Player player, InteractionHand hand);
}
