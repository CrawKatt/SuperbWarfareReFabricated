package com.atsuishio.superbwarfare.client.decorator;

import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.item.common.container.LuckyContainerBlockItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class LuckyContainerItemDecorator {

    public static boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        if (!(stack.getItem() instanceof LuckyContainerBlockItem)) return false;
        var tag = BlockItem.getBlockEntityData(stack);
        if (tag == null) return false;
        if (!tag.contains("Icon")) return false;
        var iconTag = tag.getString("Icon");
        ResourceLocation icon = ResourceLocation.tryParse(iconTag);
        if (icon == null) return false;

        var pose = guiGraphics.pose();
        pose.pushPose();
        RenderHelper.preciseBlit(guiGraphics, icon, xOffset, yOffset, 200, 0, 0, 8, 8, 8, 8);
        pose.popPose();

        return true;
    }
}
