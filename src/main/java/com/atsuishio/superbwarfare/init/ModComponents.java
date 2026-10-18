package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.capability.entity.InfiniteAmmoCapability;
import com.atsuishio.superbwarfare.capability.living.PhosphorusFireCapability;
import com.atsuishio.superbwarfare.capability.living.RadiationCapability;
import com.atsuishio.superbwarfare.capability.living.RadiationComponent;
import com.atsuishio.superbwarfare.capability.player.PlayerSpinState;
import com.atsuishio.superbwarfare.capability.player.PlayerVariable;
import com.atsuishio.superbwarfare.capability.player.PlayerDeployComponent;
import com.atsuishio.superbwarfare.capability.player.PlayerChargeComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ModComponents implements EntityComponentInitializer {

    public static final ComponentKey<PlayerDeployComponent> PLAYER_DEPLOY_STATE = ComponentRegistry.getOrCreate(
            Mod.loc("player_deploy_state"), PlayerDeployComponent.class
    );

    public static final ComponentKey<PlayerChargeComponent> PLAYER_CHARGE_STATE = ComponentRegistry.getOrCreate(
            Mod.loc("player_charge_state"), PlayerChargeComponent.class
    );

    public static final ComponentKey<RadiationComponent> RADIATION = ComponentRegistry.getOrCreate(
            RadiationCapability.ID,
            RadiationComponent.class
    );

    public static final ComponentKey<PlayerVariable> PLAYER_VARIABLE = ComponentRegistry.getOrCreate(
            Mod.loc("player_variable"),
            PlayerVariable.class
    );

    public static final ComponentKey<PhosphorusFireCapability> PHOSPHORUS_FIRE = ComponentRegistry.getOrCreate(
            PhosphorusFireCapability.ID,
            PhosphorusFireCapability.class
    );

    public static final ComponentKey<InfiniteAmmoCapability> INFINITE_AMMO = ComponentRegistry.getOrCreate(
            InfiniteAmmoCapability.ID,
            InfiniteAmmoCapability.class
    );

    public static final ComponentKey<PlayerSpinState> PLAYER_SPIN_STATE = ComponentRegistry.getOrCreate(
            Mod.loc("player_spin_state"), PlayerSpinState.class
    );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(Entity.class, PLAYER_DEPLOY_STATE, entity -> new PlayerDeployComponent());
        registry.registerForPlayers(PLAYER_DEPLOY_STATE, player -> new PlayerDeployComponent(), RespawnCopyStrategy.NEVER_COPY);
        registry.registerFor(Entity.class, PLAYER_CHARGE_STATE, entity -> new PlayerChargeComponent());
        registry.registerForPlayers(PLAYER_CHARGE_STATE, player -> new PlayerChargeComponent(), RespawnCopyStrategy.NEVER_COPY);
        registry.registerFor(Entity.class, RADIATION, entity -> new RadiationComponent());
        registry.registerForPlayers(RADIATION, player -> new RadiationComponent(), RespawnCopyStrategy.NEVER_COPY);
        registry.registerForPlayers(PLAYER_VARIABLE, PlayerVariable::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerFor(LivingEntity.class, PHOSPHORUS_FIRE, living -> new PhosphorusFireCapability());
        registry.registerFor(Entity.class, INFINITE_AMMO, entity -> new InfiniteAmmoCapability());
        registry.registerForPlayers(INFINITE_AMMO, player -> new InfiniteAmmoCapability(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerFor(Entity.class, PLAYER_SPIN_STATE, entity -> new PlayerSpinState());
        registry.registerForPlayers(PLAYER_SPIN_STATE, player -> new PlayerSpinState(), RespawnCopyStrategy.NEVER_COPY);
    }
}
