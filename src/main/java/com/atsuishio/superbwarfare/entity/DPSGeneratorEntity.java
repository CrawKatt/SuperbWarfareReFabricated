package com.atsuishio.superbwarfare.entity;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.capability.energy.ModEnergyApi;
import com.atsuishio.superbwarfare.capability.energy.SyncedEntityEnergyStorage;
import com.atsuishio.superbwarfare.event.custom.LivingDeathCallback;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.tools.FormatTool;
import com.atsuishio.superbwarfare.tools.SoundTool;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;

public class DPSGeneratorEntity extends LivingEntity implements GeoEntity {

    public static final EntityDataAccessor<Integer> DOWN_TIME = SynchedEntityData.defineId(DPSGeneratorEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> ENERGY = SynchedEntityData.defineId(DPSGeneratorEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> LEVEL = SynchedEntityData.defineId(DPSGeneratorEntity.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public final SyncedEntityEnergyStorage energyStorage = new SyncedEntityEnergyStorage(5120, 0, 2560, this.entityData, ENERGY);

    private float damageDealt = 0;

    public DPSGeneratorEntity(EntityType<DPSGeneratorEntity> type, Level world) {
        super(type, world);
        this.noCulling = true;
    }

    public static void registerEvents() {
        LivingDeathCallback.EVENT.register(DPSGeneratorEntity::onTargetDown);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DOWN_TIME, 0);
        this.entityData.define(ENERGY, 0);
        this.entityData.define(LEVEL, 0);
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot pSlot, @NotNull ItemStack pStack) {
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pPose, @NotNull EntityDimensions pSize) {
        return 1.57F;
    }

    @Override
    public boolean causeFallDamage(float l, float d, @NotNull DamageSource source) {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putLong("Energy", energyStorage.getAmount());
        pCompound.putInt("Level", this.entityData.get(LEVEL));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(LEVEL, pCompound.getInt("Level"));

        energyStorage.setCapacity(this.getMaxEnergy());
        energyStorage.setMaxExtract(this.getMaxTransfer());

        if (pCompound.contains("Energy")) {
            energyStorage.setEnergy((int) Mth.clamp(pCompound.getLong("Energy"), 0L, this.getMaxEnergy()));
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (source.is(DamageTypes.GENERIC_KILL)) {
            this.remove(RemovalReason.KILLED);
            return super.hurt(source, amount);
        }

        damageDealt += amount;

        if (this.getHealth() < 0.01) {
            amount = 0;
        }

        if (!this.level().isClientSide()) {
            this.level().playSound(null, BlockPos.containing(this.getX(), this.getY(), this.getZ()), ModSounds.HIT.get(), SoundSource.BLOCKS, 1, 1);
        } else {
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.HIT.get(), SoundSource.BLOCKS, 1, 1, false);
        }
        return super.hurt(source, (float) (amount / Math.pow(2, getGeneratorLevel())));
    }

    public static void onTargetDown(LivingDeathCallback.Event event) {
        var entity = event.getEntity();
        if (event.getSource().is(DamageTypes.GENERIC_KILL)) return;
        var sourceEntity = event.getSource().getEntity();

        if (entity instanceof DPSGeneratorEntity generatorEntity) {
            event.setCanceled(true);
            generatorEntity.setHealth(0.00001F);

            if (sourceEntity == null) return;

            if (sourceEntity instanceof Player player) {
                SoundTool.playLocalSound(player, ModSounds.TARGET_DOWN.get(), 1, 1);
                generatorEntity.entityData.set(DOWN_TIME, 40);
            }
        }
    }

    @Override
    public boolean isPickable() {
        return this.entityData.get(DOWN_TIME) == 0;
    }

    @Override
    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        if (!player.getMainHandItem().isEmpty() && !player.getMainHandItem().is(ModTags.Items.TOOLS_CROWBAR)) {
            return InteractionResult.PASS;
        }

        if (player.isShiftKeyDown()) {
            if (!this.level().isClientSide()) {
                this.discard();
            }

            if (!player.getAbilities().instabuild) {
                var stack = new ItemStack(ModItems.DPS_GENERATOR_DEPLOYER.get());
                if (!player.addItem(stack)) {
                    player.drop(stack, false);
                }
            }
        } else {
            this.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3((player.getX()), this.getY(), (player.getZ())));
            this.setXRot(0);
            this.xRotO = this.getXRot();
            this.entityData.set(DOWN_TIME, 0);
        }

        return InteractionResult.sidedSuccess(this.level().isClientSide());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entityData.get(DOWN_TIME) > 0) {
            this.entityData.set(DOWN_TIME, this.entityData.get(DOWN_TIME) - 1);
        }

        if (this.tickCount % 20 == 0) {
            var damage = this.getMaxHealth() - this.getHealth();

            if (damage > 0) {
                if (getLastDamageSource() != null) {
                    var attacker = getLastDamageSource().getEntity();
                    if (attacker instanceof Player player && !this.level().isClientSide) {
                        var displayDamage = getLastDamageSource().is(ModDamageTypes.BEAST) ? Float.POSITIVE_INFINITY : damageDealt;
                        player.displayClientMessage(Component.translatable("tips.superbwarfare.dps_generator.dps",
                                FormatTool.format1DZ(displayDamage)), true);
                    }
                }

                energyStorage.setMaxReceive(getMaxEnergy());
                ModEnergyApi.receiveEnergy(energyStorage, (int) Math.round(128d * Math.max(getGeneratorLevel(), 1) * Math.pow(2, getGeneratorLevel()) * damage), false);
                energyStorage.setMaxReceive(0);
            }

            this.chargeBlockBelow();

            if (this.getHealth() < 0.01) {
                this.entityData.set(LEVEL, Math.min(this.entityData.get(LEVEL) + 1, 7));
                energyStorage.setCapacity(this.getMaxEnergy());
                energyStorage.setMaxExtract(this.getMaxTransfer());

                if (!this.level().isClientSide()) {
                    this.level().playSound(null, BlockPos.containing(this.getX(), this.getY(), this.getZ()), ModSounds.DPS_GENERATOR_EVOLVE.get(), SoundSource.BLOCKS, 0.5f, 1);
                } else {
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.DPS_GENERATOR_EVOLVE.get(), SoundSource.BLOCKS, 0.5f, 1, false);
                }
            }
            this.setHealth(this.getMaxHealth());
            damageDealt = 0;
        }
    }

    @Override
    public @NotNull Vec3 getDeltaMovement() {
        return new Vec3(0, 0, 0);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    protected void doPush(@NotNull Entity entityIn) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public void setNoGravity(boolean ignored) {
        super.setNoGravity(true);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.ATTACK_DAMAGE, 0)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10)
                .add(Attributes.FLYING_SPEED, 0);
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime >= 100) {
            this.spawnAtLocation(new ItemStack(ModItems.DPS_GENERATOR_DEPLOYER.get()));
            this.remove(RemovalReason.KILLED);
        }
    }

    private PlayState movementPredicate(AnimationState<DPSGeneratorEntity> event) {
        if (this.entityData.get(DOWN_TIME) > 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.target.down"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.target.idle"));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 0, this::movementPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    protected void chargeBlockBelow() {
        if (!energyStorage.supportsExtraction() || energyStorage.getAmount() <= 0) return;

        var blockPos = this.blockPosition().below();
        var blockEntity = this.level().getBlockEntity(blockPos);
        if (blockEntity == null) return;

        var blockEnergy = EnergyStorage.SIDED.find(this.level(), blockPos, Direction.UP);
        if (blockEnergy == null || !blockEnergy.supportsInsertion()) return;

        try (var t = Transaction.openOuter()) {
            long transferred = EnergyStorageUtil.move(energyStorage, blockEnergy, energyStorage.getAmount(), t);
            if (transferred > 0) {
                t.commit();
                this.level().blockEntityChanged(blockPos);
            }
        }
    }

    public int getGeneratorLevel() {
        return this.entityData.get(LEVEL);
    }

    public SyncedEntityEnergyStorage getEnergy() {
        return this.energyStorage;
    }

    public int getMaxEnergy() {
        return switch (getGeneratorLevel()) {
            case 1 -> 25600;
            case 2 -> 102400;
            case 3 -> 409600;
            case 4 -> 1638400;
            case 5 -> 6553600;
            case 6 -> 26214400;
            case 7 -> 104857600;
            default -> 5120;
        };
    }

    public int getMaxTransfer() {
        return getMaxEnergy() / 2;
    }

    public void beastCharge() {
        if (this.entityData.get(LEVEL) < 7) {
            this.entityData.set(LEVEL, 7);
            energyStorage.setCapacity(this.getMaxEnergy());
            energyStorage.setMaxExtract(this.getMaxTransfer());
            energyStorage.setEnergy(this.getMaxEnergy());
        }
    }

    @Override
    @Nullable
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.DPS_GENERATOR_DEPLOYER.get());
    }
}
