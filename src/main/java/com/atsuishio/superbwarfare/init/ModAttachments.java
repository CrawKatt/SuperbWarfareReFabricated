package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.capability.player.PlayerVariable;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.nbt.CompoundTag;

public class ModAttachments {

    public static final AttachmentType<PlayerVariable> PLAYER_VARIABLE = AttachmentRegistry.create(
            Mod.loc("player_variable"),
            builder -> builder
                    .initializer(PlayerVariable::new)
                    .persistent(CompoundTag.CODEC.xmap(
                            tag -> {
                                var var = new PlayerVariable();
                                var.readFromNBT(tag);
                                return var;
                            },
                            PlayerVariable::writeToNBT
                    ))
                    .copyOnDeath()
    );

    public static void init() {
    }
}
