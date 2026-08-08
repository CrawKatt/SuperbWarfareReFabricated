package com.atsuishio.superbwarfare.item.gun;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.client.PoseTool;
import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.item.CustomRendererItem;
import com.atsuishio.superbwarfare.resource.gun.GunResource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

public abstract class GunGeoItem extends GunItem implements GeoItem, CustomRendererItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected final RandomSource random = RandomSource.create();

    public GunGeoItem(Properties properties) {
        super(properties.stacksTo(1));
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public boolean isPerspectiveAware() {
        return true;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    
    @Environment(EnvType.CLIENT)
    public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack stack) {
        return PoseTool.pose(entityLiving, hand, stack);
    }

    
    @Environment(EnvType.CLIENT)
    protected PlayState animationPredicate(AnimationState<GunGeoItem> event) {
        var player = Minecraft.getInstance().player;
        if (player == null) return PlayState.STOP;
        var stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return PlayState.STOP;

        var resource = GunResource.from(stack);
        var data = GunData.from(stack);

        var defaultResource = resource.compute();
        if (defaultResource == null) return PlayState.STOP;

        var animation = defaultResource.animation;
        if (animation == null || animation.idle == null) return PlayState.STOP;

        // Idle
        if (event.getData(DataTickets.ITEM_RENDER_PERSPECTIVE) != ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
            return event.setAndContinue(RawAnimation.begin().thenLoop(animation.idle));
        }

        // Edit
        if (animation.edit != null && ClientEventHandler.isEditing) {
            return event.setAndContinue(RawAnimation.begin().thenPlay(animation.edit));
        }

        // Bolt
        if (animation.bolt != null && data.bolt.actionTimer.get() > 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay(animation.bolt));
        }

        // Reload
        if (data.reloading()) {
            if (animation.reload != null) {
                return event.setAndContinue(RawAnimation.begin().thenPlay(animation.reload));
            } else if (animation.reloadNormal != null && data.reload.normal()) {
                return event.setAndContinue(RawAnimation.begin().thenPlay(animation.reloadNormal));
            } else if (animation.reloadEmpty != null && data.reload.empty()) {
                return event.setAndContinue(RawAnimation.begin().thenPlay(animation.reloadEmpty));
            }
        }

        // Melee
        if (animation.melee != null && ClientEventHandler.gunMelee > 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay(animation.melee));
        }

        // Fire
        if (animation.fire != null && ClientEventHandler.holdingFireKey && data.canShoot(player)) {
            return event.setAndContinue(RawAnimation.begin().thenLoop(animation.fire));
        }

        // Run
        if (player.isSprinting() && player.onGround() && ClientEventHandler.noSprintTicks == 0 && ClientEventHandler.drawTime < 0.01) {
            if (animation.run != null) {
                return event.setAndContinue(RawAnimation.begin().thenLoop(animation.run));
            }
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop(animation.idle));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
        controllers.add(new AnimationController<>(this, "animationController", 1, this::animationPredicate));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoItemRenderer<?> renderer;

            @Override
            public GeoItemRenderer<?> getGeoItemRenderer() {
                if (this.renderer == null) {
                    this.renderer = (GeoItemRenderer<?>) GunGeoItem.this.getRenderer().get();
                }
                return this.renderer;
            }
        });
    }

}
