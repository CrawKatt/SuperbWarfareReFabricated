package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModSounds {

    public static final Supplier<SoundEvent> SHOCK = Registration.sound("shock", () -> SoundEvent.createVariableRangeEvent(Mod.loc("shock")));
    public static final Supplier<SoundEvent> ELECTRIC = Registration.sound("electric", () -> SoundEvent.createVariableRangeEvent(Mod.loc("electric")));
    public static final Supplier<SoundEvent> MELEE_HIT = Registration.sound("melee_hit", () -> SoundEvent.createVariableRangeEvent(Mod.loc("melee_hit")));

    public static final Supplier<SoundEvent> TRIGGER_CLICK = Registration.sound("trigger_click", () -> SoundEvent.createVariableRangeEvent(Mod.loc("trigger_click")));
    public static final Supplier<SoundEvent> HIT = Registration.sound("hit", () -> SoundEvent.createVariableRangeEvent(Mod.loc("hit")));
    public static final Supplier<SoundEvent> TARGET_DOWN = Registration.sound("targetdown", () -> SoundEvent.createVariableRangeEvent(Mod.loc("targetdown")));
    public static final Supplier<SoundEvent> INDICATION = Registration.sound("indication", () -> SoundEvent.createVariableRangeEvent(Mod.loc("indication")));
    public static final Supplier<SoundEvent> INDICATION_VEHICLE = Registration.sound("indication_vehicle", () -> SoundEvent.createVariableRangeEvent(Mod.loc("indication_vehicle")));
    public static final Supplier<SoundEvent> JUMP = Registration.sound("jump", () -> SoundEvent.createVariableRangeEvent(Mod.loc("jump")));
    public static final Supplier<SoundEvent> DOUBLE_JUMP = Registration.sound("doublejump", () -> SoundEvent.createVariableRangeEvent(Mod.loc("doublejump")));

    public static final Supplier<SoundEvent> EXPLOSION_CLOSE = Registration.sound("explosion_close", () -> SoundEvent.createVariableRangeEvent(Mod.loc("explosion_close")));
    public static final Supplier<SoundEvent> EXPLOSION_FAR = Registration.sound("explosion_far", () -> SoundEvent.createVariableRangeEvent(Mod.loc("explosion_far")));
    public static final Supplier<SoundEvent> EXPLOSION_VERY_FAR = Registration.sound("explosion_very_far", () -> SoundEvent.createVariableRangeEvent(Mod.loc("explosion_very_far")));
    public static final Supplier<SoundEvent> HUGE_EXPLOSION_CLOSE = Registration.sound("huge_explosion_close", () -> SoundEvent.createVariableRangeEvent(Mod.loc("huge_explosion_close")));
    public static final Supplier<SoundEvent> HUGE_EXPLOSION_FAR = Registration.sound("huge_explosion_far", () -> SoundEvent.createVariableRangeEvent(Mod.loc("huge_explosion_far")));
    public static final Supplier<SoundEvent> HUGE_EXPLOSION_VERY_FAR = Registration.sound("huge_explosion_very_far", () -> SoundEvent.createVariableRangeEvent(Mod.loc("huge_explosion_very_far")));
    public static final Supplier<SoundEvent> EXPLOSION_WATER = Registration.sound("explosion_water", () -> SoundEvent.createVariableRangeEvent(Mod.loc("explosion_water")));
    public static final Supplier<SoundEvent> EXPLOSION_AIR = Registration.sound("explosion_air", () -> SoundEvent.createVariableRangeEvent(Mod.loc("explosion_air")));

    public static final Supplier<SoundEvent> OUCH = Registration.sound("ouch", () -> SoundEvent.createVariableRangeEvent(Mod.loc("ouch")));
    public static final Supplier<SoundEvent> STEP = Registration.sound("step", () -> SoundEvent.createVariableRangeEvent(Mod.loc("step")));
    public static final Supplier<SoundEvent> GROWL = Registration.sound("growl", () -> SoundEvent.createVariableRangeEvent(Mod.loc("growl")));
    public static final Supplier<SoundEvent> IDLE = Registration.sound("idle", () -> SoundEvent.createVariableRangeEvent(Mod.loc("idle")));
    public static final Supplier<SoundEvent> HENG = Registration.sound("heng", () -> SoundEvent.createVariableRangeEvent(Mod.loc("heng")));

    public static final Supplier<SoundEvent> LAND = Registration.sound("land", () -> SoundEvent.createVariableRangeEvent(Mod.loc("land")));
    public static final Supplier<SoundEvent> HIT_WATER = Registration.sound("hit_water", () -> SoundEvent.createVariableRangeEvent(Mod.loc("hit_water")));
    public static final Supplier<SoundEvent> HEADSHOT = Registration.sound("headshot", () -> SoundEvent.createVariableRangeEvent(Mod.loc("headshot")));

    public static final Supplier<SoundEvent> MORTAR_FIRE = Registration.sound("mortar_fire", () -> SoundEvent.createVariableRangeEvent(Mod.loc("mortar_fire")));

    public static final Supplier<SoundEvent> FIRE_RATE = Registration.sound("firerate", () -> SoundEvent.createVariableRangeEvent(Mod.loc("firerate")));

    public static final Supplier<SoundEvent> CANNON_ZOOM_IN = Registration.sound("cannon_zoom_in", () -> SoundEvent.createVariableRangeEvent(Mod.loc("cannon_zoom_in")));
    public static final Supplier<SoundEvent> CANNON_ZOOM_OUT = Registration.sound("cannon_zoom_out", () -> SoundEvent.createVariableRangeEvent(Mod.loc("cannon_zoom_out")));

    public static final Supplier<SoundEvent> BULLET_SUPPLY = Registration.sound("bullet_supply", () -> SoundEvent.createVariableRangeEvent(Mod.loc("bullet_supply")));
    public static final Supplier<SoundEvent> ADJUST_FOV = Registration.sound("adjust_fov", () -> SoundEvent.createVariableRangeEvent(Mod.loc("adjust_fov")));
    public static final Supplier<SoundEvent> GRENADE_PULL = Registration.sound("grenade_pull", () -> SoundEvent.createVariableRangeEvent(Mod.loc("grenade_pull")));
    public static final Supplier<SoundEvent> GRENADE_THROW = Registration.sound("grenade_throw", () -> SoundEvent.createVariableRangeEvent(Mod.loc("grenade_throw")));

    public static final Supplier<SoundEvent> EDIT_MODE = Registration.sound("edit_mode", () -> SoundEvent.createVariableRangeEvent(Mod.loc("edit_mode")));
    public static final Supplier<SoundEvent> EDIT = Registration.sound("edit", () -> SoundEvent.createVariableRangeEvent(Mod.loc("edit")));
    public static final Supplier<SoundEvent> SHELL_CASING_NORMAL = Registration.sound("shell_casing_normal", () -> SoundEvent.createVariableRangeEvent(Mod.loc("shell_casing_normal")));
    public static final Supplier<SoundEvent> SHELL_CASING_SHOTGUN = Registration.sound("shell_casing_shotgun", () -> SoundEvent.createVariableRangeEvent(Mod.loc("shell_casing_shotgun")));
    public static final Supplier<SoundEvent> SHELL_CASING_50CAL = Registration.sound("shell_casing_50cal", () -> SoundEvent.createVariableRangeEvent(Mod.loc("shell_casing_50cal")));
    public static final Supplier<SoundEvent> OPEN = Registration.sound("open", () -> SoundEvent.createVariableRangeEvent(Mod.loc("open")));
    public static final Supplier<SoundEvent> ANNIHILATOR_RELOAD = Registration.sound("annihilator_reload", () -> SoundEvent.createVariableRangeEvent(Mod.loc("annihilator_reload")));

    public static final Supplier<SoundEvent> RADAR_SEARCH_START = Registration.sound("radar_search_start", () -> SoundEvent.createVariableRangeEvent(Mod.loc("radar_search_start")));
    public static final Supplier<SoundEvent> RADAR_SEARCH_IDLE = Registration.sound("radar_search_idle", () -> SoundEvent.createVariableRangeEvent(Mod.loc("radar_search_idle")));
    public static final Supplier<SoundEvent> RADAR_SEARCH_END = Registration.sound("radar_search_end", () -> SoundEvent.createVariableRangeEvent(Mod.loc("radar_search_end")));

    public static final Supplier<SoundEvent> INTO_CANNON = Registration.sound("into_cannon", () -> SoundEvent.createVariableRangeEvent(Mod.loc("into_cannon")));
    public static final Supplier<SoundEvent> LOW_HEALTH = Registration.sound("low_health", () -> SoundEvent.createVariableRangeEvent(Mod.loc("low_health")));
    public static final Supplier<SoundEvent> NO_HEALTH = Registration.sound("no_health", () -> SoundEvent.createVariableRangeEvent(Mod.loc("no_health")));

    public static final Supplier<SoundEvent> LOCKING_WARNING = Registration.sound("locking_warning", () -> SoundEvent.createVariableRangeEvent(Mod.loc("locking_warning")));
    public static final Supplier<SoundEvent> LOCKED_WARNING = Registration.sound("locked_warning", () -> SoundEvent.createVariableRangeEvent(Mod.loc("locked_warning")));
    public static final Supplier<SoundEvent> MISSILE_WARNING = Registration.sound("missile_warning", () -> SoundEvent.createVariableRangeEvent(Mod.loc("missile_warning")));

    public static final Supplier<SoundEvent> LUNGE_MINE_GROWL = Registration.sound("lunge_mine_growl", () -> SoundEvent.createVariableRangeEvent(Mod.loc("lunge_mine_growl")));

    public static final Supplier<SoundEvent> TURRET_TURN = Registration.sound("turret_turn", () -> SoundEvent.createVariableRangeEvent(Mod.loc("turret_turn")));
    public static final Supplier<SoundEvent> C4_BEEP = Registration.sound("c4_beep", () -> SoundEvent.createVariableRangeEvent(Mod.loc("c4_beep")));
    public static final Supplier<SoundEvent> C4_FINAL = Registration.sound("c4_final", () -> SoundEvent.createVariableRangeEvent(Mod.loc("c4_final")));
    public static final Supplier<SoundEvent> C4_THROW = Registration.sound("c4_throw", () -> SoundEvent.createVariableRangeEvent(Mod.loc("c4_throw")));
    public static final Supplier<SoundEvent> C4_DETONATOR_CLICK = Registration.sound("c4_detonator_click", () -> SoundEvent.createVariableRangeEvent(Mod.loc("c4_detonator_click")));

    public static final Supplier<SoundEvent> SMOKE_FIRE = Registration.sound("smoke_fire", () -> SoundEvent.createVariableRangeEvent(Mod.loc("smoke_fire")));
    public static final Supplier<SoundEvent> ROCKET_FLY = Registration.sound("rocket_fly", () -> SoundEvent.createVariableRangeEvent(Mod.loc("rocket_fly")));
    public static final Supplier<SoundEvent> SHELL_FLY = Registration.sound("shell_fly", () -> SoundEvent.createVariableRangeEvent(Mod.loc("shell_fly")));
    public static final Supplier<SoundEvent> ROCKET_ENGINE = Registration.sound("rocket_engine", () -> SoundEvent.createVariableRangeEvent(Mod.loc("rocket_engine")));

    public static final Supplier<SoundEvent> BOMB_RELEASE = Registration.sound("bomb_release", () -> SoundEvent.createVariableRangeEvent(Mod.loc("bomb_release")));
    public static final Supplier<SoundEvent> MISSILE_START = Registration.sound("missile_start", () -> SoundEvent.createVariableRangeEvent(Mod.loc("missile_start")));

    // Guns
    // Common Gun Sounds
    public static final Supplier<SoundEvent> OVERHEAT = Registration.sound("overheat", () -> SoundEvent.createVariableRangeEvent(Mod.loc("overheat")));

    // bocek
    public static final Supplier<SoundEvent> BOCEK_ZOOM_FIRE_1P = Registration.sound("bocek_zoom_fire_1p", () -> SoundEvent.createVariableRangeEvent(Mod.loc("bocek_zoom_fire_1p")));
    public static final Supplier<SoundEvent> BOCEK_ZOOM_FIRE_3P = Registration.sound("bocek_zoom_fire_3p", () -> SoundEvent.createVariableRangeEvent(Mod.loc("bocek_zoom_fire_3p")));
    public static final Supplier<SoundEvent> BOCEK_SHATTER_CAP_FIRE_1P = Registration.sound("bocek_shatter_cap_fire_1p", () -> SoundEvent.createVariableRangeEvent(Mod.loc("bocek_shatter_cap_fire_1p")));
    public static final Supplier<SoundEvent> BOCEK_SHATTER_CAP_FIRE_3P = Registration.sound("bocek_shatter_cap_fire_3p", () -> SoundEvent.createVariableRangeEvent(Mod.loc("bocek_shatter_cap_fire_3p")));
    public static final Supplier<SoundEvent> BOCEK_PULL_1P = Registration.sound("bocek_pull_1p", () -> SoundEvent.createVariableRangeEvent(Mod.loc("bocek_pull_1p")));
    public static final Supplier<SoundEvent> BOCEK_PULL_3P = Registration.sound("bocek_pull_3p", () -> SoundEvent.createVariableRangeEvent(Mod.loc("bocek_pull_3p")));


    public static final Supplier<SoundEvent> IGLA_FIRE_1P = Registration.sound("igla_9k38_fire_1p", () -> SoundEvent.createVariableRangeEvent(Mod.loc("igla_9k38_fire_1p")));
    public static final Supplier<SoundEvent> IGLA_FIRE_3P = Registration.sound("igla_9k38_fire_3p", () -> SoundEvent.createVariableRangeEvent(Mod.loc("igla_9k38_fire_3p")));
    public static final Supplier<SoundEvent> IGLA_FAR = Registration.sound("igla_9k38_far", () -> SoundEvent.createVariableRangeEvent(Mod.loc("igla_9k38_far")));

    public static final Supplier<SoundEvent> JAVELIN_FIRE_1P = Registration.sound("javelin_fire_1p", () -> SoundEvent.createVariableRangeEvent(Mod.loc("javelin_fire_1p")));
    public static final Supplier<SoundEvent> JAVELIN_FIRE_3P = Registration.sound("javelin_fire_3p", () -> SoundEvent.createVariableRangeEvent(Mod.loc("javelin_fire_3p")));
    public static final Supplier<SoundEvent> JAVELIN_FAR = Registration.sound("javelin_far", () -> SoundEvent.createVariableRangeEvent(Mod.loc("javelin_far")));

    public static final Supplier<SoundEvent> MINIGUN_ROTATE = Registration.sound("minigun_rotate", () -> SoundEvent.createVariableRangeEvent(Mod.loc("minigun_rotate")));

    public static final Supplier<SoundEvent> QL_1031_CHARGE = Registration.sound("ql_1031_charge", () -> SoundEvent.createVariableRangeEvent(Mod.loc("ql_1031_charge")));
    public static final Supplier<SoundEvent> REPAIRING = Registration.sound("repairing", () -> SoundEvent.createVariableRangeEvent(Mod.loc("repairing")));

    public static final Supplier<SoundEvent> RPG_FIRE_3P = Registration.sound("rpg_fire_3p", () -> SoundEvent.createVariableRangeEvent(Mod.loc("rpg_fire_3p")));

    public static final Supplier<SoundEvent> SECONDARY_CATACLYSM_FIRE_1P_CHARGE = Registration.sound("secondary_cataclysm_fire_1p_charge", () -> SoundEvent.createVariableRangeEvent(Mod.loc("secondary_cataclysm_fire_1p_charge")));
    public static final Supplier<SoundEvent> SECONDARY_CATACLYSM_FIRE_3P_CHARGE = Registration.sound("secondary_cataclysm_fire_3p_charge", () -> SoundEvent.createVariableRangeEvent(Mod.loc("secondary_cataclysm_fire_3p_charge")));
    public static final Supplier<SoundEvent> SECONDARY_CATACLYSM_FAR_CHARGE = Registration.sound("secondary_cataclysm_far_charge", () -> SoundEvent.createVariableRangeEvent(Mod.loc("secondary_cataclysm_far_charge")));
    public static final Supplier<SoundEvent> SECONDARY_CATACLYSM_VERYFAR_CHARGE = Registration.sound("secondary_cataclysm_veryfar_charge", () -> SoundEvent.createVariableRangeEvent(Mod.loc("secondary_cataclysm_veryfar_charge")));

    public static final Supplier<SoundEvent> SENTINEL_CHARGE_FIRE_1P = Registration.sound("sentinel_charge_fire_1p", () -> SoundEvent.createVariableRangeEvent(Mod.loc("sentinel_charge_fire_1p")));
    public static final Supplier<SoundEvent> SENTINEL_CHARGE_FIRE_3P = Registration.sound("sentinel_charge_fire_3p", () -> SoundEvent.createVariableRangeEvent(Mod.loc("sentinel_charge_fire_3p")));
    public static final Supplier<SoundEvent> SENTINEL_CHARGE_FAR = Registration.sound("sentinel_charge_far", () -> SoundEvent.createVariableRangeEvent(Mod.loc("sentinel_charge_far")));
    public static final Supplier<SoundEvent> SENTINEL_CHARGE_VERYFAR = Registration.sound("sentinel_charge_veryfar", () -> SoundEvent.createVariableRangeEvent(Mod.loc("sentinel_charge_veryfar")));
    public static final Supplier<SoundEvent> SENTINEL_CHARGE = Registration.sound("sentinel_charge", () -> SoundEvent.createVariableRangeEvent(Mod.loc("sentinel_charge")));

    // Vehicles
    // Common Vehicle Sounds
    public static final Supplier<SoundEvent> MISSILE_LOCKING = Registration.sound("missile_locking", () -> SoundEvent.createVariableRangeEvent(Mod.loc("missile_locking")));
    public static final Supplier<SoundEvent> MISSILE_LOCKED = Registration.sound("missile_locked", () -> SoundEvent.createVariableRangeEvent(Mod.loc("missile_locked")));

    public static final Supplier<SoundEvent> SMALL_ROCKET_FIRE_3P = Registration.sound("small_rocket_fire_3p", () -> SoundEvent.createVariableRangeEvent(Mod.loc("small_rocket_fire_3p")));
    public static final Supplier<SoundEvent> DECOY_RELEASE = Registration.sound("decoy_release", () -> SoundEvent.createVariableRangeEvent(Mod.loc("decoy_release")));
    public static final Supplier<SoundEvent> DECOY_RELEASE_FIRST = Registration.sound("decoy_release_first", () -> SoundEvent.createVariableRangeEvent(Mod.loc("decoy_release_first")));
    public static final Supplier<SoundEvent> DECOY_RELOAD = Registration.sound("decoy_reload", () -> SoundEvent.createVariableRangeEvent(Mod.loc("decoy_reload")));

    public static final Supplier<SoundEvent> WHEEL_VEHICLE_STEP = Registration.sound("wheel_vehicle_step", () -> SoundEvent.createVariableRangeEvent(Mod.loc("wheel_vehicle_step")));
    public static final Supplier<SoundEvent> TRACK_VEHICLE_STEP = Registration.sound("track_vehicle_step", () -> SoundEvent.createVariableRangeEvent(Mod.loc("track_vehicle_step")));
    public static final Supplier<SoundEvent> VEHICLE_SWIM = Registration.sound("vehicle_swim", () -> SoundEvent.createVariableRangeEvent(Mod.loc("vehicle_swim")));
    public static final Supplier<SoundEvent> VEHICLE_STRIKE = Registration.sound("vehicle_strike", () -> SoundEvent.createVariableRangeEvent(Mod.loc("vehicle_strike")));


    // drone
    public static final Supplier<SoundEvent> DRONE_ENGINE = Registration.sound("drone_engine", () -> SoundEvent.createVariableRangeEvent(Mod.loc("drone_engine")));


    public static final Supplier<SoundEvent> WHEEL_CHAIR_JUMP = Registration.sound("wheel_chair_jump", () -> SoundEvent.createVariableRangeEvent(Mod.loc("wheel_chair_jump")));



    public static final Supplier<SoundEvent> DPS_GENERATOR_EVOLVE = Registration.sound("dps_generator_evolve", () -> SoundEvent.createVariableRangeEvent(Mod.loc("dps_generator_evolve")));
    public static final Supplier<SoundEvent> STEEL_PIPE_HIT = Registration.sound("steel_pipe_hit", () -> SoundEvent.createVariableRangeEvent(Mod.loc("steel_pipe_hit")));
    public static final Supplier<SoundEvent> STEEL_PIPE_DROP = Registration.sound("steel_pipe_drop", () -> SoundEvent.createVariableRangeEvent(Mod.loc("steel_pipe_drop")));
    public static final Supplier<SoundEvent> SM0KE_GRENADE_RELEASE = Registration.sound("smoke_grenade_release", () -> SoundEvent.createVariableRangeEvent(Mod.loc("smoke_grenade_release")));

    public static final Supplier<SoundEvent> HAND_WHEEL_ROT = Registration.sound("hand_wheel_rot", () -> SoundEvent.createVariableRangeEvent(Mod.loc("hand_wheel_rot")));
    public static final Supplier<SoundEvent> MEDIUM_ROCKET_FIRE = Registration.sound("medium_rocket_fire", () -> SoundEvent.createVariableRangeEvent(Mod.loc("medium_rocket_fire")));
    public static final Supplier<SoundEvent> TYPE_63_RELOAD = Registration.sound("ty63_reload", () -> SoundEvent.createVariableRangeEvent(Mod.loc("ty63_reload")));

    public static final Supplier<SoundEvent> PARACHUTE_OPEN = Registration.sound("parachute_open", () -> SoundEvent.createVariableRangeEvent(Mod.loc("parachute_open")));
    public static final Supplier<SoundEvent> PARACHUTE_CLOSE = Registration.sound("parachute_close", () -> SoundEvent.createVariableRangeEvent(Mod.loc("parachute_close")));

    public static final Supplier<SoundEvent> PTKM_1R_DEPLOY = Registration.sound("ptkm_1r_deploy", () -> SoundEvent.createVariableRangeEvent(Mod.loc("ptkm_1r_deploy")));

    public static final Supplier<SoundEvent> TERRAIN = Registration.sound("terrain", () -> SoundEvent.createVariableRangeEvent(Mod.loc("terrain")));
    public static final Supplier<SoundEvent> PULL_UP = Registration.sound("pull_up", () -> SoundEvent.createVariableRangeEvent(Mod.loc("pull_up")));

    public static void register() {

    }
}
