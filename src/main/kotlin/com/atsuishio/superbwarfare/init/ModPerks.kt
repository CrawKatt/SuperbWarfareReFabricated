package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.perk.AmmoPerk
import com.atsuishio.superbwarfare.perk.EmptyPerk
import com.atsuishio.superbwarfare.perk.Perk
import com.atsuishio.superbwarfare.perk.ammo.*
import com.atsuishio.superbwarfare.perk.damage.*
import com.atsuishio.superbwarfare.perk.functional.*
import com.atsuishio.superbwarfare.perk.js.JsPerk
import com.atsuishio.superbwarfare.perk.js.PerkDescriptor
import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.world.effect.MobEffects
import java.nio.file.Files

@Suppress("unused")
object ModPerks {
    @JvmField
    val LOCATION = loc("perk")

    @JvmField
    val PERK_KEY: ResourceKey<Registry<Perk>> = ResourceKey.createRegistryKey(LOCATION)

    @JvmField
    val PERK_REGISTRY: Registry<Perk> = FabricRegistryBuilder
        .createDefaulted(PERK_KEY, loc("ap_bullet"))
        .attribute(RegistryAttribute.SYNCED)
        .buildAndRegister()

    private val registeredIds = mutableSetOf<String>()
    private val autoRegistryObjects = mutableMapOf<String, Perk>()

    /**
     * Ammo Perks
     */
    @JvmField
    val AMMO_PERKS: MutableList<Perk> = arrayListOf()

    /**
     * Functional Perks
     */
    @JvmField
    val FUNC_PERKS: MutableList<Perk> = arrayListOf()

    /**
     * Damage Perks
     */
    @JvmField
    val DAMAGE_PERKS: MutableList<Perk> = arrayListOf()

    init {
        // Runs after the perk lists above are initialized: JSON-defined perks take
        // precedence over the hardcoded fallbacks below (same as upstream).
        autoRegisterFromJsons()
    }

    private fun registerAmmoPerk(id: String, perk: () -> Perk): Perk {
        registeredIds.add(id)
        val registered = Registry.register(PERK_REGISTRY, loc(id), perk())
        AMMO_PERKS.add(registered)
        return registered
    }

    // @formatter:off
    @JvmField val AP_BULLET = autoRegistryObjects["ap_bullet"] ?: registerAmmoPerk("ap_bullet") { APBullet }
    @JvmField val JHP_BULLET = autoRegistryObjects["jhp_bullet"] ?: registerAmmoPerk("jhp_bullet") { JHPBullet }
    @JvmField val HE_BULLET = autoRegistryObjects["he_bullet"] ?: registerAmmoPerk("he_bullet") { HEBullet }
    @JvmField val SILVER_BULLET = autoRegistryObjects["silver_bullet"] ?: registerAmmoPerk("silver_bullet") { SilverBullet }
    @JvmField val POISONOUS_BULLET = autoRegistryObjects["poisonous_bullet"] ?: registerAmmoPerk("poisonous_bullet") {
        AmmoPerk(
            AmmoPerk.Builder("poisonous_bullet", Perk.Type.AMMO).bypassArmorRate(0.0).damageRate(1.0)
                .speedRate(1.0).rgb(48, 131, 6)
                .mobEffect { MobEffects.POISON }
        )
    }
    @JvmField val BEAST_BULLET = autoRegistryObjects["beast_bullet"] ?: registerAmmoPerk("beast_bullet") { BeastBullet }
    @JvmField val LONGER_WIRE = autoRegistryObjects["longer_wire"] ?: registerAmmoPerk("longer_wire") { LongerWire }
    @JvmField val INCENDIARY_BULLET = autoRegistryObjects["incendiary_bullet"] ?: registerAmmoPerk("incendiary_bullet") { IncendiaryBullet }
    @JvmField val MICRO_MISSILE = autoRegistryObjects["micro_missile"] ?: registerAmmoPerk("micro_missile") { MicroMissile }
    @JvmField val CUPID_ARROW = autoRegistryObjects["cupid_arrow"] ?: registerAmmoPerk("cupid_arrow") { CupidArrow }
    @JvmField val RIOT_BULLET = autoRegistryObjects["riot_bullet"] ?: registerAmmoPerk("riot_bullet") { RiotBullet }
    @JvmField val PHASE_PENETRATING_BULLET = autoRegistryObjects["phase_penetrating_bullet"]
        ?: registerAmmoPerk("phase_penetrating_bullet") { PhasePenetratingBullet }
    @JvmField val BLADE_BULLET = autoRegistryObjects["blade_bullet"] ?: registerAmmoPerk("blade_bullet") { BladeBullet }
    @JvmField val PHOSPHORUS_FLAME_BULLET = autoRegistryObjects["phosphorus_flame_bullet"]
        ?: registerAmmoPerk("phosphorus_flame_bullet") { PhosphorusFlameBullet }
    @JvmField val AQUA_BULLET = autoRegistryObjects["aqua_bullet"] ?: registerAmmoPerk("aqua_bullet") {
        EmptyPerk("aqua_bullet", Perk.Type.AMMO)
    }
    // @formatter:on

    private fun registerFuncPerk(id: String, perk: () -> Perk): Perk {
        registeredIds.add(id)
        val registered = Registry.register(PERK_REGISTRY, loc(id), perk())
        FUNC_PERKS.add(registered)
        return registered
    }

    // @formatter:off
    @JvmField val HEAL_CLIP = autoRegistryObjects["heal_clip"] ?: registerFuncPerk("heal_clip") { HealClip }
    @JvmField val FOURTH_TIMES_CHARM = autoRegistryObjects["fourth_times_charm"] ?: registerFuncPerk("fourth_times_charm") { FourthTimesCharm }
    @JvmField val SUBSISTENCE = autoRegistryObjects["subsistence"] ?: registerFuncPerk("subsistence") { Subsistence }
    @JvmField val FIELD_DOCTOR = autoRegistryObjects["field_doctor"] ?: registerFuncPerk("field_doctor") { FieldDoctor }
    @JvmField val REGENERATION = autoRegistryObjects["regeneration"] ?: registerFuncPerk("regeneration") { Regeneration }
    @JvmField val TURBO_CHARGER = autoRegistryObjects["turbo_charger"] ?: registerFuncPerk("turbo_charger") { TurboCharger }
    @JvmField val POWERFUL_ATTRACTION = autoRegistryObjects["powerful_attraction"] ?: registerFuncPerk("powerful_attraction") { PowerfulAttraction }
    @JvmField val INTELLIGENT_CHIP = autoRegistryObjects["intelligent_chip"] ?: registerFuncPerk("intelligent_chip") {
        Perk(
            "intelligent_chip",
            Perk.Type.FUNCTIONAL
        )
    }
    @JvmField val BACKPACK_LINKED_MAGAZINE = autoRegistryObjects["backpack_linked_magazine"]
        ?: registerFuncPerk("backpack_linked_magazine") { BackpackLinkedMagazine }
    @JvmField val POWERFUL_COOLER = autoRegistryObjects["powerful_cooler"] ?: registerFuncPerk("powerful_cooler") { PowerfulCooler }
    @JvmField val CAST_NO_SHADOWS = autoRegistryObjects["cast_no_shadows"] ?: registerFuncPerk("cast_no_shadows") { CastNoShadows }
    @JvmField val EAGER_EDGE = autoRegistryObjects["eager_edge"] ?: registerFuncPerk("eager_edge") {
        EmptyPerk("eager_edge", Perk.Type.FUNCTIONAL)
    }
    @JvmField val ADRENALINE_RUSH = autoRegistryObjects["adrenaline_rush"] ?: registerFuncPerk("adrenaline_rush") {
        EmptyPerk("adrenaline_rush", Perk.Type.FUNCTIONAL)
    }
    @JvmField val QUICKDRAW = autoRegistryObjects["quickdraw"] ?: registerFuncPerk("quickdraw") {
        EmptyPerk("quickdraw", Perk.Type.FUNCTIONAL)
    }
    @JvmField val SNAPSHOT_SIGHTS = autoRegistryObjects["snapshot_sights"] ?: registerFuncPerk("snapshot_sights") {
        EmptyPerk("snapshot_sights", Perk.Type.FUNCTIONAL)
    }
    @JvmField val TRIPLE_TAP = autoRegistryObjects["triple_tap"] ?: registerFuncPerk("triple_tap") {
        EmptyPerk("triple_tap", Perk.Type.FUNCTIONAL)
    }
    // @formatter:on

    private fun registerDamagePerk(id: String, perk: () -> Perk): Perk {
        registeredIds.add(id)
        val registered = Registry.register(PERK_REGISTRY, loc(id), perk())
        DAMAGE_PERKS.add(registered)
        return registered
    }

    // @formatter:off
    @JvmField val KILL_CLIP = autoRegistryObjects["kill_clip"] ?: registerDamagePerk("kill_clip") { KillClip }
    @JvmField val GUTSHOT_STRAIGHT = autoRegistryObjects["gutshot_straight"] ?: registerDamagePerk("gutshot_straight") { GutshotStraight }
    @JvmField val KILLING_TALLY = autoRegistryObjects["killing_tally"] ?: registerDamagePerk("killing_tally") { KillingTally }
    @JvmField val HEAD_SEEKER = autoRegistryObjects["head_seeker"] ?: registerDamagePerk("head_seeker") { HeadSeeker }
    @JvmField val MONSTER_HUNTER = autoRegistryObjects["monster_hunter"] ?: registerDamagePerk("monster_hunter") { MonsterHunter }
    @JvmField val VOLT_OVERLOAD = autoRegistryObjects["volt_overload"] ?: registerDamagePerk("volt_overload") { VoltOverload }
    @JvmField val DESPERADO = autoRegistryObjects["desperado"] ?: registerDamagePerk("desperado") { Desperado }
    @JvmField val VORPAL_WEAPON = autoRegistryObjects["vorpal_weapon"] ?: registerDamagePerk("vorpal_weapon") { VorpalWeapon }
    @JvmField val MAGNIFICENT_HOWL = autoRegistryObjects["magnificent_howl"] ?: registerDamagePerk("magnificent_howl") { MagnificentHowl }
    @JvmField val FIREFLY = autoRegistryObjects["firefly"] ?: registerDamagePerk("firefly") { Firefly }
    @JvmField val FAIR_MEANS = autoRegistryObjects["fair_means"] ?: registerDamagePerk("fair_means") { FairMeans }
    @JvmField val HIGH_IMPACT_RESERVES = autoRegistryObjects["high_impact_reserves"]
        ?: registerDamagePerk("high_impact_reserves") { HighImpactReserves }
    @JvmField val ONE_TWO_PUNCH = autoRegistryObjects["one_two_punch"] ?: registerDamagePerk("one_two_punch") { OneTwoPunch }
    @JvmField val BRAIN_STORM = autoRegistryObjects["brain_storm"] ?: registerDamagePerk("brain_storm") { BrainStorm }
    @JvmField val BATTLE_OF_WITS = autoRegistryObjects["battle_of_wits"] ?: registerDamagePerk("battle_of_wits") { BattleOfWits }
    @JvmField val TARGET_LOCK = autoRegistryObjects["target_lock"] ?: registerDamagePerk("target_lock") {
        EmptyPerk("target_lock", Perk.Type.DAMAGE)
    }
    @JvmField val SOUL_REAVER = autoRegistryObjects["soul_reaver"] ?: registerDamagePerk("soul_reaver") {
        EmptyPerk("soul_reaver", Perk.Type.DAMAGE)
    }
    @JvmField val STEADY_RESOLVE = autoRegistryObjects["steady_resolve"] ?: registerDamagePerk("steady_resolve") {
        EmptyPerk("steady_resolve", Perk.Type.DAMAGE)
    }
    // @formatter:on

    private fun autoRegisterFromJsons() {
        try {
            val container = FabricLoader.getInstance().getModContainer(Mod.MODID).orElse(null) ?: return
            for (root in container.rootPaths) {
                val perksDir = root.resolve("data/${Mod.MODID}/sbw/perks")
                if (!Files.isDirectory(perksDir)) continue
                Files.list(perksDir).use { stream ->
                    stream.filter { it.fileName.toString().endsWith(".json") }
                        .forEach { path ->
                            val id = path.fileName.toString().substringBeforeLast(".json")
                            if (id in registeredIds) return@forEach
                            val descriptor = parsePerkJson(path) ?: return@forEach
                            val perk = JsPerk(id, descriptor)
                            when (descriptor.perkType) {
                                Perk.Type.AMMO -> registerAmmoPerk(id) { perk }
                                Perk.Type.FUNCTIONAL -> registerFuncPerk(id) { perk }
                                Perk.Type.DAMAGE -> registerDamagePerk(id) { perk }
                            }
                            autoRegistryObjects[id] = perk
                            Mod.LOGGER.debug("Auto-registered perk '{}' from JSON", id)
                        }
                }
            }
        } catch (e: Exception) {
            Mod.LOGGER.warn("Failed to auto-discover perk JSONs: {}", e.toString())
        }
    }

    private fun parsePerkJson(path: java.nio.file.Path): PerkDescriptor? {
        return try {
            Files.newBufferedReader(path).use { reader ->
                val element = JsonParser.parseReader(reader)
                PerkDescriptor.CODEC.parse(JsonOps.INSTANCE, element)
                    .resultOrPartial { error ->
                        Mod.LOGGER.error(
                            "Failed to parse perk JSON '{}': {}",
                            path.fileName,
                            error
                        )
                    }
                    .orElse(null)
            }
        } catch (e: Exception) {
            Mod.LOGGER.error("Failed to load perk JSON: {}", path, e)
            null
        }
    }

    @JvmStatic
    fun init() = Unit
}
