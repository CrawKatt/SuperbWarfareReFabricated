package com.atsuishio.superbwarfare.item.gun.launcher;

import com.atsuishio.superbwarfare.init.ModSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import com.atsuishio.superbwarfare.capability.energy.EnergyStorageHelper;
import team.reborn.energy.api.EnergyStorage;

import com.atsuishio.superbwarfare.client.GunRendererBuilder;
import com.atsuishio.superbwarfare.client.TooltipTool;
import com.atsuishio.superbwarfare.client.model.item.SecondaryCataclysmItemModel;
import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.data.gun.GunProp;
import com.atsuishio.superbwarfare.data.gun.ShootParameters;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModCapabilities;
import com.atsuishio.superbwarfare.init.ModRarities;
import com.atsuishio.superbwarfare.item.gun.GunGeoItem;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

public class SecondaryCataclysmItem extends GunGeoItem {

    public SecondaryCataclysmItem() {
        super(new Properties().fireResistant().rarity(ModRarities.VIRTUAL));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.empty());
        list.add(Component.translatable("des.superbwarfare.secondary_cataclysm_1").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));

        TooltipTool.addHideText(list, Component.empty());
        TooltipTool.addHideText(list, Component.translatable("des.superbwarfare.trachelium_3").withStyle(ChatFormatting.WHITE));
        TooltipTool.addHideText(list, Component.translatable("des.superbwarfare.secondary_cataclysm_2").withStyle(Style.EMPTY.withColor(0x68B9F6)));
    }

    @Override
    public Supplier<? extends GeoItemRenderer<? extends Item>> getRenderer() {
        return GunRendererBuilder.simple(SecondaryCataclysmItemModel::new);
    }

    @Environment(EnvType.CLIENT)
    private PlayState reloadAnimPredicate(AnimationState<SecondaryCataclysmItem> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return PlayState.STOP;
        if (event.getData(DataTickets.ITEM_RENDER_PERSPECTIVE) != ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.secondary_cataclysm.idle"));

        var data = GunData.from(stack);

        if (data.reload.stage() == 1 && data.reload.prepareLoadTimer.get() > 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.secondary_cataclysm.prepare"));
        }

        if (data.loadIndex.get() == 0 && data.reload.stage() == 2) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.secondary_cataclysm.iterativeload"));
        }

        if (data.loadIndex.get() == 1 && data.reload.stage() == 2) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.secondary_cataclysm.iterativeload2"));
        }

        if (data.reload.stage() == 3) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.secondary_cataclysm.finish"));
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.secondary_cataclysm.idle"));
    }

    @Environment(EnvType.CLIENT)
    private PlayState meleePredicate(AnimationState<SecondaryCataclysmItem> event) {
        if (event.getData(DataTickets.ITEM_RENDER_PERSPECTIVE) != ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.secondary_cataclysm.idle"));

        if (ClientEventHandler.gunMelee > 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.secondary_cataclysm.hit"));
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.secondary_cataclysm.idle"));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
        var reloadAnimController = new AnimationController<>(this, "reloadAnimController", 1, this::reloadAnimPredicate);
        data.add(reloadAnimController);
        var meleeController = new AnimationController<>(this, "meleeController", 0, this::meleePredicate);
        data.add(meleeController);
    }

    @Override
    public double getCustomDamage(GunData data) {
        var stack = data.stack;
        var cap = EnergyStorage.ITEM.find(stack, null);
        if (cap != null && cap.getAmount() > 0) {
            return 2.5 * data.getDefault().damage;
        }
        return 0;
    }

    @Override
    public boolean shootBullet(@NotNull ShootParameters parameters) {
        var data = parameters.data;
        var level = parameters.level;
        var shootPosition = parameters.shootPosition;
        var shootDirection = parameters.shootDirection;
        var zoom = parameters.zoom;

        var stack = data.stack;

        var stackCap = EnergyStorage.ITEM.find(stack, null);
        var hasEnoughEnergy = stackCap != null && stackCap.getAmount() >= 3000;

        boolean isChargedFire = hasEnoughEnergy;

        if (!super.shootBullet(parameters)) return false;

        ParticleTool.sendParticle(level, ParticleTypes.CLOUD, shootPosition.x + 1.8 * shootDirection.x,
                shootPosition.y - 0.35 + 1.8 * shootDirection.y,
                shootPosition.z + 1.8 * shootDirection.z,
                4, 0.1, 0.1, 0.1, 0.002, true);

        if (isChargedFire) {
            var itemCap = EnergyStorage.ITEM.find(stack, null);
            if (itemCap != null) {
                EnergyStorageHelper.extract(itemCap, 3000);
            }
        }

        return true;
    }

    @Override
    public void playFireSounds(GunData data, Entity shooter, boolean zoom) {
        var cap = EnergyStorage.ITEM.find(data.stack, null);

        if (cap != null && cap.getAmount() > 3000) {
            float soundRadius = data.get(GunProp.SOUND_RADIUS).floatValue();

            shooter.playSound(ModSounds.SECONDARY_CATACLYSM_FIRE_3P_CHARGE, soundRadius * 0.4f, 1f);
            shooter.playSound(ModSounds.SECONDARY_CATACLYSM_FAR_CHARGE, soundRadius * 0.7f, 1f);
            shooter.playSound(ModSounds.SECONDARY_CATACLYSM_VERYFAR_CHARGE, soundRadius, 1f);
        } else {
            super.playFireSounds(data, shooter, zoom);
        }
    }
}
