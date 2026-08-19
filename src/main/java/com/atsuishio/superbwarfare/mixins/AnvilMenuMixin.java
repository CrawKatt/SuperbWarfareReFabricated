package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.PlayerEventHandler;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {

    @Shadow
    @Final
    private DataSlot cost;

    @Shadow
    private int repairItemCountCost;

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void superbWarfare$createShortcutPackResult(CallbackInfo ci) {
        AnvilMenu menu = (AnvilMenu) (Object) this;
        ItemStack output = PlayerEventHandler.getShortcutPackAnvilOutput(
                menu.getSlot(AnvilMenu.INPUT_SLOT).getItem(),
                menu.getSlot(AnvilMenu.ADDITIONAL_SLOT).getItem()
        );

        if (!output.isEmpty()) {
            menu.getSlot(AnvilMenu.RESULT_SLOT).set(output);
            this.cost.set(10);
            this.repairItemCountCost = 1;
            menu.broadcastChanges();
            ci.cancel();
        }
    }
}
