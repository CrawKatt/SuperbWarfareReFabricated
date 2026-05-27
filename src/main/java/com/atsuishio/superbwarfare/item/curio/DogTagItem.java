package com.atsuishio.superbwarfare.item.curio;

import com.atsuishio.superbwarfare.client.TooltipTool;
import com.atsuishio.superbwarfare.client.screens.DogTagEditorScreen;
import com.atsuishio.superbwarfare.client.tooltip.component.DogTagImageComponent;
import com.atsuishio.superbwarfare.item.ItemScreenProvider;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DogTagItem extends Item implements Trinket, ItemScreenProvider {

    public DogTagItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        TooltipTool.addScreenProviderText(pTooltipComponents);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return TrinketsApi.getTrinketComponent(entity)
                .map(c -> !c.isEquipped(this))
                .orElse(true);
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack pStack) {
        return Optional.of(new DogTagImageComponent(pStack));
    }

    public static short[][] getColors(ItemStack stack) {
        short[][] colors = new short[16][16];
        for (var el : colors) {
            Arrays.fill(el, (short) -1);
        }

        if (stack.getTag() == null) return colors;
        CompoundTag tag = stack.getTag().getCompound("Colors");
        for (int i = 0; i < 16; i++) {
            int[] color = tag.getIntArray("Color" + i);
            for (int j = 0; j < color.length; j++) {
                colors[i][j] = (short) color[j];
            }
        }

        return colors;
    }

    @Override
    public @Nullable Screen getItemScreen(ItemStack stack, Player player, InteractionHand hand) {
        return new DogTagEditorScreen(stack, hand);
    }
}
