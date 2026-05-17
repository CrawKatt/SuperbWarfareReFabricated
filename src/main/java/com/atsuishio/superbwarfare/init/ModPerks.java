package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.perk.AmmoPerk;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.perk.ammo.*;
import com.atsuishio.superbwarfare.perk.damage.*;
import com.atsuishio.superbwarfare.perk.functional.*;
import net.minecraft.world.effect.MobEffects;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ModPerks {

    /**
     * Ammo Perks
     */
    public static final List<Perk> AMMO_PERKS = new ArrayList<>();

    public static final Perk AP_BULLET = registerAmmo(new APBullet());
    public static final Perk JHP_BULLET = registerAmmo(new JHPBullet());
    public static final Perk HE_BULLET = registerAmmo(new HEBullet());
    public static final Perk SILVER_BULLET = registerAmmo(new SilverBullet());
    public static final Perk POISONOUS_BULLET = registerAmmo(
            new AmmoPerk(new AmmoPerk.Builder("poisonous_bullet", Perk.Type.AMMO).bypassArmorRate(0.0f).damageRate(1.0f).speedRate(1.0f).rgb(48, 131, 6)
                    .mobEffect(() -> MobEffects.POISON)));
    public static final Perk BEAST_BULLET = registerAmmo(new BeastBullet());
    public static final Perk LONGER_WIRE = registerAmmo(new LongerWire());
    public static final Perk INCENDIARY_BULLET = registerAmmo(new IncendiaryBullet());
    public static final Perk MICRO_MISSILE = registerAmmo(new MicroMissile());
    public static final Perk CUPID_ARROW = registerAmmo(new CupidArrow());
    public static final Perk RIOT_BULLET = registerAmmo(new RiotBullet());
    public static final Perk PHASE_PENETRATING_BULLET = registerAmmo(new PhasePenetratingBullet());
    public static final Perk BLADE_BULLET = registerAmmo(new BladeBullet());

    /**
     * Functional Perks
     */
    public static final List<Perk> FUNC_PERKS = new ArrayList<>();

    public static final Perk HEAL_CLIP = registerFunc(new HealClip());
    public static final Perk FOURTH_TIMES_CHARM = registerFunc(new FourthTimesCharm());
    public static final Perk SUBSISTENCE = registerFunc(new Subsistence());
    public static final Perk FIELD_DOCTOR = registerFunc(new FieldDoctor());
    public static final Perk REGENERATION = registerFunc(new Regeneration());
    public static final Perk TURBO_CHARGER = registerFunc(new TurboCharger());
    public static final Perk POWERFUL_ATTRACTION = registerFunc(new PowerfulAttraction());
    public static final Perk INTELLIGENT_CHIP = registerFunc(new Perk("intelligent_chip", Perk.Type.FUNCTIONAL));
    public static final Perk BACKPACK_LINKED_MAGAZINE = registerFunc(new BackpackLinkedMagazine());
    public static final Perk POWERFUL_COOLER = registerFunc(new PowerfulCooler());

    /**
     * Damage Perks
     */
    public static final List<Perk> DAMAGE_PERKS = new ArrayList<>();

    public static final Perk KILL_CLIP = registerDamage(new KillClip());
    public static final Perk GUTSHOT_STRAIGHT = registerDamage(new GutshotStraight());
    public static final Perk KILLING_TALLY = registerDamage(new KillingTally());
    public static final Perk HEAD_SEEKER = registerDamage(new HeadSeeker());
    public static final Perk MONSTER_HUNTER = registerDamage(new MonsterHunter());
    public static final Perk VOLT_OVERLOAD = registerDamage(new VoltOverload());
    public static final Perk DESPERADO = registerDamage(new Desperado());
    public static final Perk VORPAL_WEAPON = registerDamage(new VorpalWeapon());
    public static final Perk MAGNIFICENT_HOWL = registerDamage(new MagnificentHowl());
    public static final Perk FIREFLY = registerDamage(new Firefly());
    public static final Perk FAIR_MEANS = registerDamage(new FairMeans());
    public static final Perk HIGH_IMPACT_RESERVES = registerDamage(new HighImpactReserves());
    public static final Perk ONE_TWO_PUNCH = registerDamage(new OneTwoPunch());

    private static Perk registerAmmo(Perk perk) {
        AMMO_PERKS.add(perk);
        return perk;
    }

    private static Perk registerFunc(Perk perk) {
        FUNC_PERKS.add(perk);
        return perk;
    }

    private static Perk registerDamage(Perk perk) {
        DAMAGE_PERKS.add(perk);
        return perk;
    }

    public static void init() {
    }
}
