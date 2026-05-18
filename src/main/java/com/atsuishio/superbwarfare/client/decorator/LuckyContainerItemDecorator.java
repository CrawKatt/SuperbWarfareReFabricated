package com.atsuishio.superbwarfare.client.decorator;

import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.item.common.container.LuckyContainerBlockItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;


public class LuckyContainerItemDecorator {

    @ParametersAreNonnullByDefault
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        if (!(stack.getItem() instanceof LuckyContainerBlockItem)) return false;
        var data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data == null) return false;

        var tag = data.copyTag();
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
