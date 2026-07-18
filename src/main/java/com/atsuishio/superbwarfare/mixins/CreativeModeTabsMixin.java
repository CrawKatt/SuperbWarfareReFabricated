package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.init.ModTabs;
import com.atsuishio.superbwarfare.mixins.accessor.CreativeModeTabAccessor;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Mixin(value = CreativeModeTabs.class, priority = 900)
public class CreativeModeTabsMixin {

    @Inject(method = "validate", at = @At("RETURN"))
    private static void superbwarfare$restoreForgeTabOrder(CallbackInfo ci) {
        List<CreativeModeTab> forgeOrder = List.of(
                ModTabs.GUN_TAB.get(),
                ModTabs.PERK_TAB.get(),
                ModTabs.AMMO_TAB.get(),
                ModTabs.ITEM_TAB.get(),
                ModTabs.BLOCK_TAB.get(),
                ModTabs.VEHICLE_TAB.get()
        );

        List<TabPosition> assignedPositions = new ArrayList<>(forgeOrder.size());
        for (CreativeModeTab tab : forgeOrder) {
            assignedPositions.add(new TabPosition(
                    ((FabricItemGroup) tab).getPage(),
                    tab.row(),
                    tab.column()
            ));
        }

        if (new HashSet<>(assignedPositions).size() != forgeOrder.size()) {
            throw new IllegalStateException("Superb Warfare creative tabs were assigned overlapping positions");
        }

        assignedPositions.sort(Comparator
                .comparingInt(TabPosition::page)
                .thenComparingInt(position -> position.row() == CreativeModeTab.Row.TOP ? 0 : 1)
                .thenComparingInt(TabPosition::column));

        for (int i = 0; i < forgeOrder.size(); i++) {
            CreativeModeTab tab = forgeOrder.get(i);
            TabPosition position = assignedPositions.get(i);
            ((FabricItemGroup) tab).setPage(position.page());
            ((CreativeModeTabAccessor) tab).setRow(position.row());
            ((CreativeModeTabAccessor) tab).setColumn(position.column());
        }
    }

    private record TabPosition(int page, CreativeModeTab.Row row, int column) {
    }
}
