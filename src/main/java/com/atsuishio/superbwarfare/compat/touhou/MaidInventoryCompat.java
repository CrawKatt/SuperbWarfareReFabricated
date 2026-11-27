package com.atsuishio.superbwarfare.compat.touhou;

import cn.sh1rocu.touhoulittlemaid.util.itemhandler.CombinedInvWrapper;
import com.atsuishio.superbwarfare.capability.api.IItemHandler;
import com.atsuishio.superbwarfare.init.ModCapabilities;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

public final class MaidInventoryCompat {
    private MaidInventoryCompat() {
    }

    public static void register() {
        ModCapabilities.ITEM_HANDLER_ENTITY.registerFallback((entity, context) -> {
            if (!(entity instanceof EntityMaid maid)) return null;
            return new MaidItemHandler(maid);
        });
    }

    private static final class MaidItemHandler extends CombinedInvWrapper implements IItemHandler {
        private MaidItemHandler(EntityMaid maid) {
            super(maid.getArmorInvWrapper(), maid.getHandsInvWrapper(), maid.getMaidInv(), maid.getMaidBauble());
        }
    }
}
