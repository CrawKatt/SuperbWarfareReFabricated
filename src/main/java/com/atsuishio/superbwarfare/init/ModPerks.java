package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.compat.CompatHolder;
import com.atsuishio.superbwarfare.perk.AmmoPerk;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.perk.ammo.*;
import com.atsuishio.superbwarfare.perk.damage.*;
import com.atsuishio.superbwarfare.perk.functional.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModPerks {

    /**
     * Ammo Perks
     */
    public static final List<NamedSupplier<Perk>> AMMO_PERKS = new ArrayList<>();

    public static final Supplier<Perk> AP_BULLET = ammo("ap_bullet", APBullet::new);
    public static final Supplier<Perk> JHP_BULLET = ammo("jhp_bullet", JHPBullet::new);
    public static final Supplier<Perk> HE_BULLET = ammo("he_bullet", HEBullet::new);
    public static final Supplier<Perk> SILVER_BULLET = ammo("silver_bullet", SilverBullet::new);
    public static final Supplier<Perk> POISONOUS_BULLET = ammo("poisonous_bullet",
            () -> new AmmoPerk(new AmmoPerk.Builder("poisonous_bullet", Perk.Type.AMMO).bypassArmorRate(0.0f).damageRate(1.0f).speedRate(1.0f).rgb(48, 131, 6)
                    .mobEffect(() -> MobEffects.POISON)));
    public static final Supplier<Perk> BEAST_BULLET = ammo("beast_bullet", BeastBullet::new);
    public static final Supplier<Perk> LONGER_WIRE = ammo("longer_wire", LongerWire::new);
    public static final Supplier<Perk> INCENDIARY_BULLET = ammo("incendiary_bullet", IncendiaryBullet::new);
    public static final Supplier<Perk> MICRO_MISSILE = ammo("micro_missile", MicroMissile::new);
    public static final Supplier<Perk> CUPID_ARROW = ammo("cupid_arrow", CupidArrow::new);
    public static final Supplier<Perk> RIOT_BULLET = ammo("riot_bullet", RiotBullet::new);
    public static final Supplier<Perk> PHASE_PENETRATING_BULLET = ammo("phase_penetrating_bullet", PhasePenetratingBullet::new);
    public static final Supplier<Perk> BLADE_BULLET = ammo("blade_bullet", BladeBullet::new);

    /**
     * Functional Perks
     */
    public static final List<NamedSupplier<Perk>> FUNC_PERKS = new ArrayList<>();

    public static final Supplier<Perk> HEAL_CLIP = func("heal_clip", HealClip::new);
    public static final Supplier<Perk> FOURTH_TIMES_CHARM = func("fourth_times_charm", FourthTimesCharm::new);
    public static final Supplier<Perk> SUBSISTENCE = func("subsistence", Subsistence::new);
    public static final Supplier<Perk> FIELD_DOCTOR = func("field_doctor", FieldDoctor::new);
    public static final Supplier<Perk> REGENERATION = func("regeneration", Regeneration::new);
    public static final Supplier<Perk> TURBO_CHARGER = func("turbo_charger", TurboCharger::new);
    public static final Supplier<Perk> POWERFUL_ATTRACTION = func("powerful_attraction", PowerfulAttraction::new);
    public static final Supplier<Perk> INTELLIGENT_CHIP = func("intelligent_chip", () -> new Perk("intelligent_chip", Perk.Type.FUNCTIONAL));
    public static final Supplier<Perk> BACKPACK_LINKED_MAGAZINE = func("backpack_linked_magazine", BackpackLinkedMagazine::new);
    public static final Supplier<Perk> POWERFUL_COOLER = func("powerful_cooler", PowerfulCooler::new);

    /**
     * Damage Perks
     */
    public static final List<NamedSupplier<Perk>> DAMAGE_PERKS = new ArrayList<>();

    public static final Supplier<Perk> KILL_CLIP = damage("kill_clip", KillClip::new);
    public static final Supplier<Perk> GUTSHOT_STRAIGHT = damage("gutshot_straight", GutshotStraight::new);
    public static final Supplier<Perk> KILLING_TALLY = damage("killing_tally", KillingTally::new);
    public static final Supplier<Perk> HEAD_SEEKER = damage("head_seeker", HeadSeeker::new);
    public static final Supplier<Perk> MONSTER_HUNTER = damage("monster_hunter", MonsterHunter::new);
    public static final Supplier<Perk> VOLT_OVERLOAD = damage("volt_overload", VoltOverload::new);
    public static final Supplier<Perk> DESPERADO = damage("desperado", Desperado::new);
    public static final Supplier<Perk> VORPAL_WEAPON = damage("vorpal_weapon", VorpalWeapon::new);
    public static final Supplier<Perk> MAGNIFICENT_HOWL = damage("magnificent_howl", MagnificentHowl::new);
    public static final Supplier<Perk> FIREFLY = damage("firefly", Firefly::new);
    public static final Supplier<Perk> FAIR_MEANS = damage("fair_means", FairMeans::new);
    public static final Supplier<Perk> HIGH_IMPACT_RESERVES = damage("high_impact_reserves", HighImpactReserves::new);
    public static final Supplier<Perk> ONE_TWO_PUNCH = damage("one_two_punch", OneTwoPunch::new);

    public static void register() {
        registerAll(AMMO_PERKS);
        registerAll(FUNC_PERKS);
        registerAll(DAMAGE_PERKS);
    }

    private static void registerAll(List<NamedSupplier<Perk>> perks) {
        for (NamedSupplier<Perk> perk : perks) {
            Registry.register(PERK_REGISTRY, Mod.loc(perk.name()), perk.get());
        }
    }

    public static final class NamedSupplier<T> implements Supplier<T> {
        private final String name;
        private final Supplier<T> factory;
        private T value;

        public NamedSupplier(String name, Supplier<T> factory) {
            this.name = name;
            this.factory = factory;
        }

        public String name() {
            return this.name;
        }

        @Override
        public T get() {
            if (this.value == null) {
                this.value = this.factory.get();
            }
            return this.value;
        }
    }

    private static <T extends Perk> Supplier<T> ammo(String name, Supplier<T> factory) {
        NamedSupplier<T> holder = new NamedSupplier<>(name, factory);
        AMMO_PERKS.add((NamedSupplier<Perk>) (Supplier<Perk>) holder);
        return holder;
    }

    private static <T extends Perk> Supplier<T> func(String name, Supplier<T> factory) {
        NamedSupplier<T> holder = new NamedSupplier<>(name, factory);
        FUNC_PERKS.add((NamedSupplier<Perk>) (Supplier<Perk>) holder);
        return holder;
    }

    private static <T extends Perk> Supplier<T> damage(String name, Supplier<T> factory) {
        NamedSupplier<T> holder = new NamedSupplier<>(name, factory);
        DAMAGE_PERKS.add((NamedSupplier<Perk>) (Supplier<Perk>) holder);
        return holder;
    }

    public static void registerCompatPerks() {
        if (FabricLoader.getInstance().isModLoaded(CompatHolder.DMV)) {
            AMMO_PERKS.add(new NamedSupplier<>("bread_bullet", BreadBullet::new));
        }
        if (FabricLoader.getInstance().isModLoaded(CompatHolder.VRC)) {
            AMMO_PERKS.add(new NamedSupplier<>("curse_flame_bullet",
                    () -> new AmmoPerk(new AmmoPerk.Builder("curse_flame_bullet", Perk.Type.AMMO)
                            .bypassArmorRate(0.0f).damageRate(1.2f).speedRate(0.9f).rgb(0xB1, 0xC1, 0xF2).mobEffect(CompatHolder::getVrcCurseFlame))));
            AMMO_PERKS.add(new NamedSupplier<>("butterfly_bullet",
                    () -> new AmmoPerk(new AmmoPerk.Builder("butterfly_bullet", Perk.Type.AMMO)
                            .bypassArmorRate(0.0f))));
        }
    }

    public static final ResourceKey<Registry<Perk>> PERK_KEY =
            ResourceKey.createRegistryKey(Mod.loc("perk"));

    public static final Registry<Perk> PERK_REGISTRY =
            FabricRegistryBuilder.createSimple(PERK_KEY).buildAndRegister();
}
