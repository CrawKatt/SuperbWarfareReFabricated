package com.atsuishio.superbwarfare.item.common.container;

import net.fabricmc.loader.api.FabricLoader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.client.renderer.item.LuckyContainerBlockItemRenderer;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import com.atsuishio.superbwarfare.init.ModBlocks;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LuckyContainerBlockItem extends BlockItem implements GeoItem {

    public static final List<Supplier<ItemStack>> LUCKY_CONTAINERS = List.of(
            () -> LuckyContainerBlockItem.createInstance(Mod.loc("mobile_vehicles"), Mod.loc("textures/gui/vehicle/type/civilian.png")),
            () -> LuckyContainerBlockItem.createInstance(Mod.loc("land_vehicles"), Mod.loc("textures/gui/vehicle/type/land.png")),
            () -> LuckyContainerBlockItem.createInstance(Mod.loc("aircraft"), Mod.loc("textures/gui/vehicle/type/aircraft.png")),
            () -> LuckyContainerBlockItem.createInstance(Mod.loc("controllable_turrets"), Mod.loc("textures/gui/vehicle/type/defense.png"))
    );

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public LuckyContainerBlockItem() {
        super(ModBlocks.LUCKY_CONTAINER.get(), new Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }

    @Override
    public boolean canBeHurtBy(DamageSource pDamageSource) {
        return super.canBeHurtBy(pDamageSource) && !pDamageSource.is(DamageTypeTags.IS_EXPLOSION) && !pDamageSource.is(DamageTypes.CACTUS);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        BlockHitResult playerPOVHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (playerPOVHitResult.getType() == HitResult.Type.MISS) {
            return super.use(level, player, hand);
        }
        BlockHitResult blockHitResult = playerPOVHitResult.withPosition(playerPOVHitResult.getBlockPos().above());
        InteractionResult interactionresult = super.useOn(new UseOnContext(player, hand, blockHitResult));
        return new InteractionResultHolder<>(interactionresult, player.getItemInHand(hand));
    }

    @Environment(EnvType.CLIENT)
    private PlayState predicate(AnimationState<LuckyContainerBlockItem> event) {
        return PlayState.CONTINUE;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new LuckyContainerBlockItemRenderer();
                }
                return renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) return;
        data.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static ItemStack createInstance(ResourceLocation location, @Nullable ResourceLocation icon) {
        ItemStack stack = new ItemStack(ModBlocks.LUCKY_CONTAINER.get());
        CompoundTag tag = new CompoundTag();
        tag.putString("Location", location.toString());
        if (icon != null) {
            tag.putString("Icon", icon.toString());
        }
        BlockItem.setBlockEntityData(stack, ModBlockEntities.LUCKY_CONTAINER.get(), tag);
        return stack;
    }

    public static ItemStack createInstance(ResourceLocation location) {
        return createInstance(location, null);
    }
}
