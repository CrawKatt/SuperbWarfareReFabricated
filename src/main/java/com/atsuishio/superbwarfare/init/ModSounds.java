package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

@SuppressWarnings("unused")
public class ModSounds {

    public static final SoundEvent SHOCK = register("shock", SoundEvent.createVariableRangeEvent(Mod.loc("shock")));
    public static final SoundEvent ELECTRIC = register("electric", SoundEvent.createVariableRangeEvent(Mod.loc("electric")));
    public static final SoundEvent MELEE_HIT = register("melee_hit", SoundEvent.createVariableRangeEvent(Mod.loc("melee_hit")));

    public static final SoundEvent TRIGGER_CLICK = register("trigger_click", SoundEvent.createVariableRangeEvent(Mod.loc("trigger_click")));
    public static final SoundEvent HIT = register("hit", SoundEvent.createVariableRangeEvent(Mod.loc("hit")));
    public static final SoundEvent TARGET_DOWN = register("targetdown", SoundEvent.createVariableRangeEvent(Mod.loc("targetdown")));
    public static final SoundEvent INDICATION = register("indication", SoundEvent.createVariableRangeEvent(Mod.loc("indication")));
    public static final SoundEvent INDICATION_VEHICLE = register("indication_vehicle", SoundEvent.createVariableRangeEvent(Mod.loc("indication_vehicle")));
    public static final SoundEvent JUMP = register("jump", SoundEvent.createVariableRangeEvent(Mod.loc("jump")));
    public static final SoundEvent DOUBLE_JUMP = register("doublejump", SoundEvent.createVariableRangeEvent(Mod.loc("doublejump")));

    public static final SoundEvent EXPLOSION_CLOSE = register("explosion_close", SoundEvent.createVariableRangeEvent(Mod.loc("explosion_close")));
    public static final SoundEvent EXPLOSION_FAR = register("explosion_far", SoundEvent.createVariableRangeEvent(Mod.loc("explosion_far")));
    public static final SoundEvent EXPLOSION_VERY_FAR = register("explosion_very_far", SoundEvent.createVariableRangeEvent(Mod.loc("explosion_very_far")));
    public static final SoundEvent HUGE_EXPLOSION_CLOSE = register("huge_explosion_close", SoundEvent.createVariableRangeEvent(Mod.loc("huge_explosion_close")));
    public static final SoundEvent HUGE_EXPLOSION_FAR = register("huge_explosion_far", SoundEvent.createVariableRangeEvent(Mod.loc("huge_explosion_far")));
    public static final SoundEvent HUGE_EXPLOSION_VERY_FAR = register("huge_explosion_very_far", SoundEvent.createVariableRangeEvent(Mod.loc("huge_explosion_very_far")));
    public static final SoundEvent EXPLOSION_WATER = register("explosion_water", SoundEvent.createVariableRangeEvent(Mod.loc("explosion_water")));
    public static final SoundEvent EXPLOSION_AIR = register("explosion_air", SoundEvent.createVariableRangeEvent(Mod.loc("explosion_air")));

    public static final SoundEvent OUCH = register("ouch", SoundEvent.createVariableRangeEvent(Mod.loc("ouch")));
    public static final SoundEvent STEP = register("step", SoundEvent.createVariableRangeEvent(Mod.loc("step")));
    public static final SoundEvent GROWL = register("growl", SoundEvent.createVariableRangeEvent(Mod.loc("growl")));
    public static final SoundEvent IDLE = register("idle", SoundEvent.createVariableRangeEvent(Mod.loc("idle")));
    public static final SoundEvent HENG = register("heng", SoundEvent.createVariableRangeEvent(Mod.loc("heng")));

    public static final SoundEvent LAND = register("land", SoundEvent.createVariableRangeEvent(Mod.loc("land")));
    public static final SoundEvent HIT_WATER = register("hit_water", SoundEvent.createVariableRangeEvent(Mod.loc("hit_water")));
    public static final SoundEvent HEADSHOT = register("headshot", SoundEvent.createVariableRangeEvent(Mod.loc("headshot")));

    public static final SoundEvent MORTAR_FIRE = register("mortar_fire", SoundEvent.createVariableRangeEvent(Mod.loc("mortar_fire")));

    public static final SoundEvent FIRE_RATE = register("firerate", SoundEvent.createVariableRangeEvent(Mod.loc("firerate")));

    public static final SoundEvent CANNON_ZOOM_IN = register("cannon_zoom_in", SoundEvent.createVariableRangeEvent(Mod.loc("cannon_zoom_in")));
    public static final SoundEvent CANNON_ZOOM_OUT = register("cannon_zoom_out", SoundEvent.createVariableRangeEvent(Mod.loc("cannon_zoom_out")));

    public static final SoundEvent BULLET_SUPPLY = register("bullet_supply", SoundEvent.createVariableRangeEvent(Mod.loc("bullet_supply")));
    public static final SoundEvent ADJUST_FOV = register("adjust_fov", SoundEvent.createVariableRangeEvent(Mod.loc("adjust_fov")));
    public static final SoundEvent GRENADE_PULL = register("grenade_pull", SoundEvent.createVariableRangeEvent(Mod.loc("grenade_pull")));
    public static final SoundEvent GRENADE_THROW = register("grenade_throw", SoundEvent.createVariableRangeEvent(Mod.loc("grenade_throw")));

    public static final SoundEvent EDIT_MODE = register("edit_mode", SoundEvent.createVariableRangeEvent(Mod.loc("edit_mode")));
    public static final SoundEvent EDIT = register("edit", SoundEvent.createVariableRangeEvent(Mod.loc("edit")));
    public static final SoundEvent SHELL_CASING_NORMAL = register("shell_casing_normal", SoundEvent.createVariableRangeEvent(Mod.loc("shell_casing_normal")));
    public static final SoundEvent SHELL_CASING_SHOTGUN = register("shell_casing_shotgun", SoundEvent.createVariableRangeEvent(Mod.loc("shell_casing_shotgun")));
    public static final SoundEvent SHELL_CASING_50CAL = register("shell_casing_50cal", SoundEvent.createVariableRangeEvent(Mod.loc("shell_casing_50cal")));
    public static final SoundEvent OPEN = register("open", SoundEvent.createVariableRangeEvent(Mod.loc("open")));
    public static final SoundEvent ANNIHILATOR_RELOAD = register("annihilator_reload", SoundEvent.createVariableRangeEvent(Mod.loc("annihilator_reload")));

    public static final SoundEvent RADAR_SEARCH_START = register("radar_search_start", SoundEvent.createVariableRangeEvent(Mod.loc("radar_search_start")));
    public static final SoundEvent RADAR_SEARCH_IDLE = register("radar_search_idle", SoundEvent.createVariableRangeEvent(Mod.loc("radar_search_idle")));
    public static final SoundEvent RADAR_SEARCH_END = register("radar_search_end", SoundEvent.createVariableRangeEvent(Mod.loc("radar_search_end")));

    public static final SoundEvent INTO_CANNON = register("into_cannon", SoundEvent.createVariableRangeEvent(Mod.loc("into_cannon")));
    public static final SoundEvent LOW_HEALTH = register("low_health", SoundEvent.createVariableRangeEvent(Mod.loc("low_health")));
    public static final SoundEvent NO_HEALTH = register("no_health", SoundEvent.createVariableRangeEvent(Mod.loc("no_health")));

    public static final SoundEvent LOCKING_WARNING = register("locking_warning", SoundEvent.createVariableRangeEvent(Mod.loc("locking_warning")));
    public static final SoundEvent LOCKED_WARNING = register("locked_warning", SoundEvent.createVariableRangeEvent(Mod.loc("locked_warning")));
    public static final SoundEvent MISSILE_WARNING = register("missile_warning", SoundEvent.createVariableRangeEvent(Mod.loc("missile_warning")));

    public static final SoundEvent LUNGE_MINE_GROWL = register("lunge_mine_growl", SoundEvent.createVariableRangeEvent(Mod.loc("lunge_mine_growl")));

    public static final SoundEvent TURRET_TURN = register("turret_turn", SoundEvent.createVariableRangeEvent(Mod.loc("turret_turn")));
    public static final SoundEvent C4_BEEP = register("c4_beep", SoundEvent.createVariableRangeEvent(Mod.loc("c4_beep")));
    public static final SoundEvent C4_FINAL = register("c4_final", SoundEvent.createVariableRangeEvent(Mod.loc("c4_final")));
    public static final SoundEvent C4_THROW = register("c4_throw", SoundEvent.createVariableRangeEvent(Mod.loc("c4_throw")));
    public static final SoundEvent C4_DETONATOR_CLICK = register("c4_detonator_click", SoundEvent.createVariableRangeEvent(Mod.loc("c4_detonator_click")));

    public static final SoundEvent SMOKE_FIRE = register("smoke_fire", SoundEvent.createVariableRangeEvent(Mod.loc("smoke_fire")));
    public static final SoundEvent ROCKET_FLY = register("rocket_fly", SoundEvent.createVariableRangeEvent(Mod.loc("rocket_fly")));
    public static final SoundEvent SHELL_FLY = register("shell_fly", SoundEvent.createVariableRangeEvent(Mod.loc("shell_fly")));
    public static final SoundEvent ROCKET_ENGINE = register("rocket_engine", SoundEvent.createVariableRangeEvent(Mod.loc("rocket_engine")));

    public static final SoundEvent BOMB_RELEASE = register("bomb_release", SoundEvent.createVariableRangeEvent(Mod.loc("bomb_release")));
    public static final SoundEvent MISSILE_START = register("missile_start", SoundEvent.createVariableRangeEvent(Mod.loc("missile_start")));

    // Guns
    // Common Gun Sounds
    public static final SoundEvent OVERHEAT = register("overheat", SoundEvent.createVariableRangeEvent(Mod.loc("overheat")));

    // bocek
    public static final SoundEvent BOCEK_ZOOM_FIRE_1P = register("bocek_zoom_fire_1p", SoundEvent.createVariableRangeEvent(Mod.loc("bocek_zoom_fire_1p")));
    public static final SoundEvent BOCEK_ZOOM_FIRE_3P = register("bocek_zoom_fire_3p", SoundEvent.createVariableRangeEvent(Mod.loc("bocek_zoom_fire_3p")));
    public static final SoundEvent BOCEK_SHATTER_CAP_FIRE_1P = register("bocek_shatter_cap_fire_1p", SoundEvent.createVariableRangeEvent(Mod.loc("bocek_shatter_cap_fire_1p")));
    public static final SoundEvent BOCEK_SHATTER_CAP_FIRE_3P = register("bocek_shatter_cap_fire_3p", SoundEvent.createVariableRangeEvent(Mod.loc("bocek_shatter_cap_fire_3p")));
    public static final SoundEvent BOCEK_PULL_1P = register("bocek_pull_1p", SoundEvent.createVariableRangeEvent(Mod.loc("bocek_pull_1p")));
    public static final SoundEvent BOCEK_PULL_3P = register("bocek_pull_3p", SoundEvent.createVariableRangeEvent(Mod.loc("bocek_pull_3p")));


    public static final SoundEvent IGLA_FIRE_1P = register("igla_9k38_fire_1p", SoundEvent.createVariableRangeEvent(Mod.loc("igla_9k38_fire_1p")));
    public static final SoundEvent IGLA_FIRE_3P = register("igla_9k38_fire_3p", SoundEvent.createVariableRangeEvent(Mod.loc("igla_9k38_fire_3p")));
    public static final SoundEvent IGLA_FAR = register("igla_9k38_far", SoundEvent.createVariableRangeEvent(Mod.loc("igla_9k38_far")));

    public static final SoundEvent JAVELIN_FIRE_1P = register("javelin_fire_1p", SoundEvent.createVariableRangeEvent(Mod.loc("javelin_fire_1p")));
    public static final SoundEvent JAVELIN_FIRE_3P = register("javelin_fire_3p", SoundEvent.createVariableRangeEvent(Mod.loc("javelin_fire_3p")));
    public static final SoundEvent JAVELIN_FAR = register("javelin_far", SoundEvent.createVariableRangeEvent(Mod.loc("javelin_far")));

    public static final SoundEvent MINIGUN_ROTATE = register("minigun_rotate", SoundEvent.createVariableRangeEvent(Mod.loc("minigun_rotate")));

    public static final SoundEvent QL_1031_CHARGE = register("ql_1031_charge", SoundEvent.createVariableRangeEvent(Mod.loc("ql_1031_charge")));
    public static final SoundEvent REPAIRING = register("repairing", SoundEvent.createVariableRangeEvent(Mod.loc("repairing")));

    public static final SoundEvent RPG_FIRE_3P = register("rpg_fire_3p", SoundEvent.createVariableRangeEvent(Mod.loc("rpg_fire_3p")));

    public static final SoundEvent SECONDARY_CATACLYSM_FIRE_1P_CHARGE = register("secondary_cataclysm_fire_1p_charge", SoundEvent.createVariableRangeEvent(Mod.loc("secondary_cataclysm_fire_1p_charge")));
    public static final SoundEvent SECONDARY_CATACLYSM_FIRE_3P_CHARGE = register("secondary_cataclysm_fire_3p_charge", SoundEvent.createVariableRangeEvent(Mod.loc("secondary_cataclysm_fire_3p_charge")));
    public static final SoundEvent SECONDARY_CATACLYSM_FAR_CHARGE = register("secondary_cataclysm_far_charge", SoundEvent.createVariableRangeEvent(Mod.loc("secondary_cataclysm_far_charge")));
    public static final SoundEvent SECONDARY_CATACLYSM_VERYFAR_CHARGE = register("secondary_cataclysm_veryfar_charge", SoundEvent.createVariableRangeEvent(Mod.loc("secondary_cataclysm_veryfar_charge")));

    public static final SoundEvent SENTINEL_CHARGE_FIRE_1P = register("sentinel_charge_fire_1p", SoundEvent.createVariableRangeEvent(Mod.loc("sentinel_charge_fire_1p")));
    public static final SoundEvent SENTINEL_CHARGE_FIRE_3P = register("sentinel_charge_fire_3p", SoundEvent.createVariableRangeEvent(Mod.loc("sentinel_charge_fire_3p")));
    public static final SoundEvent SENTINEL_CHARGE_FAR = register("sentinel_charge_far", SoundEvent.createVariableRangeEvent(Mod.loc("sentinel_charge_far")));
    public static final SoundEvent SENTINEL_CHARGE_VERYFAR = register("sentinel_charge_veryfar", SoundEvent.createVariableRangeEvent(Mod.loc("sentinel_charge_veryfar")));
    public static final SoundEvent SENTINEL_CHARGE = register("sentinel_charge", SoundEvent.createVariableRangeEvent(Mod.loc("sentinel_charge")));

    // Vehicles
    // Common Vehicle Sounds
    public static final SoundEvent MISSILE_LOCKING = register("missile_locking", SoundEvent.createVariableRangeEvent(Mod.loc("missile_locking")));
    public static final SoundEvent MISSILE_LOCKED = register("missile_locked", SoundEvent.createVariableRangeEvent(Mod.loc("missile_locked")));

    public static final SoundEvent SMALL_ROCKET_FIRE_3P = register("small_rocket_fire_3p", SoundEvent.createVariableRangeEvent(Mod.loc("small_rocket_fire_3p")));
    public static final SoundEvent DECOY_RELEASE = register("decoy_release", SoundEvent.createVariableRangeEvent(Mod.loc("decoy_release")));
    public static final SoundEvent DECOY_RELEASE_FIRST = register("decoy_release_first", SoundEvent.createVariableRangeEvent(Mod.loc("decoy_release_first")));
    public static final SoundEvent DECOY_RELOAD = register("decoy_reload", SoundEvent.createVariableRangeEvent(Mod.loc("decoy_reload")));

    public static final SoundEvent WHEEL_VEHICLE_STEP = register("wheel_vehicle_step", SoundEvent.createVariableRangeEvent(Mod.loc("wheel_vehicle_step")));
    public static final SoundEvent TRACK_VEHICLE_STEP = register("track_vehicle_step", SoundEvent.createVariableRangeEvent(Mod.loc("track_vehicle_step")));
    public static final SoundEvent VEHICLE_SWIM = register("vehicle_swim", SoundEvent.createVariableRangeEvent(Mod.loc("vehicle_swim")));
    public static final SoundEvent VEHICLE_STRIKE = register("vehicle_strike", SoundEvent.createVariableRangeEvent(Mod.loc("vehicle_strike")));


    // drone
    public static final SoundEvent DRONE_ENGINE = register("drone_engine", SoundEvent.createVariableRangeEvent(Mod.loc("drone_engine")));


    public static final SoundEvent WHEEL_CHAIR_JUMP = register("wheel_chair_jump", SoundEvent.createVariableRangeEvent(Mod.loc("wheel_chair_jump")));



    public static final SoundEvent DPS_GENERATOR_EVOLVE = register("dps_generator_evolve", SoundEvent.createVariableRangeEvent(Mod.loc("dps_generator_evolve")));
    public static final SoundEvent STEEL_PIPE_HIT = register("steel_pipe_hit", SoundEvent.createVariableRangeEvent(Mod.loc("steel_pipe_hit")));
    public static final SoundEvent STEEL_PIPE_DROP = register("steel_pipe_drop", SoundEvent.createVariableRangeEvent(Mod.loc("steel_pipe_drop")));
    public static final SoundEvent SM0KE_GRENADE_RELEASE = register("smoke_grenade_release", SoundEvent.createVariableRangeEvent(Mod.loc("smoke_grenade_release")));

    public static final SoundEvent HAND_WHEEL_ROT = register("hand_wheel_rot", SoundEvent.createVariableRangeEvent(Mod.loc("hand_wheel_rot")));
    public static final SoundEvent MEDIUM_ROCKET_FIRE = register("medium_rocket_fire", SoundEvent.createVariableRangeEvent(Mod.loc("medium_rocket_fire")));
    public static final SoundEvent TYPE_63_RELOAD = register("ty63_reload", SoundEvent.createVariableRangeEvent(Mod.loc("ty63_reload")));

    public static final SoundEvent PARACHUTE_OPEN = register("parachute_open", SoundEvent.createVariableRangeEvent(Mod.loc("parachute_open")));
    public static final SoundEvent PARACHUTE_CLOSE = register("parachute_close", SoundEvent.createVariableRangeEvent(Mod.loc("parachute_close")));

    public static final SoundEvent PTKM_1R_DEPLOY = register("ptkm_1r_deploy", SoundEvent.createVariableRangeEvent(Mod.loc("ptkm_1r_deploy")));

    public static final SoundEvent TERRAIN = register("terrain", SoundEvent.createVariableRangeEvent(Mod.loc("terrain")));
    public static final SoundEvent PULL_UP = register("pull_up", SoundEvent.createVariableRangeEvent(Mod.loc("pull_up")));
    public static final SoundEvent MORTAR_SHELL_EXPLODE = register("mortar_shell_explode", SoundEvent.createVariableRangeEvent(Mod.loc("mortar_shell_explode")));

    private static SoundEvent register(String name, SoundEvent event) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, Mod.loc(name), event);
    }

    public static void init() {
    }
}
