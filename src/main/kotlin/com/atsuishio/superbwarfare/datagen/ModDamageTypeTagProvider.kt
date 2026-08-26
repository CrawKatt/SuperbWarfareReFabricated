package com.atsuishio.superbwarfare.datagen

import com.atsuishio.superbwarfare.init.ModDamageTypes
import com.atsuishio.superbwarfare.init.ModTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.DamageTypeTagsProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.DamageTypeTags
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DamageTypes
import java.util.concurrent.CompletableFuture

class ModDamageTypeTagProvider(
    pOutput: PackOutput,
    pLookupProvider: CompletableFuture<HolderLookup.Provider>
) : DamageTypeTagsProvider(pOutput, pLookupProvider) {
    override fun addTags(pProvider: HolderLookup.Provider) {
        val mod = { key: ResourceKey<DamageType> -> key.location() }

        this.tag(ModTags.DamageTypes.PROJECTILE)
            .addOptional(mod(ModDamageTypes.GUN_FIRE))
            .addOptional(mod(ModDamageTypes.GUN_FIRE_HEADSHOT))
            .addOptional(mod(ModDamageTypes.SUPER_STAR_HIT))
            .addOptional(mod(ModDamageTypes.SUPER_STAR_SLASH))
            .add(DamageTypes.ARROW, DamageTypes.TRIDENT, DamageTypes.THROWN)
            .addOptional(ResourceLocation("tacz", "bullet"))
            .addOptional(ResourceLocation("tacz", "bullet_void"))
            .addOptional(ResourceLocation("virtuarealcraft", "rain_crystal"))
            .addOptional(ResourceLocation("virtuarealcraft", "rain_shower_butterfly"))
            .addOptional(ResourceLocation("virtuarealcraft", "sparkle_butterfly"))
            .addOptional(ResourceLocation("dreamaticvoyage", "blood_crystal"))
            .addOptional(ResourceLocation("dreamaticvoyage", "leviy_beam"))
        this.tag(ModTags.DamageTypes.PROJECTILE_ABSOLUTE)
            .addOptional(mod(ModDamageTypes.GUN_FIRE_ABSOLUTE))
            .addOptional(mod(ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE))
            .addOptional(ResourceLocation("tacz", "bullet_ignore_armor"))
            .addOptional(ResourceLocation("tacz", "bullet_void_ignore_armor"))
            .addOptional(ResourceLocation("dreamaticvoyage", "leviy_beam_absolute"))
        this.tag(ModTags.DamageTypes.VEHICLE_IGNORE)
            .addOptional(ResourceLocation("sona", "injury"))
        this.tag(ModTags.DamageTypes.VEHICLE_NOT_ABSORB)
            .add(
                DamageTypes.EXPLOSION,
                DamageTypes.PLAYER_EXPLOSION,
                ModDamageTypes.CUSTOM_EXPLOSION,
                ModDamageTypes.MINE,
                ModDamageTypes.PROJECTILE_EXPLOSION,
                ModDamageTypes.AMMO_CONSUMPTION
            )
        this.tag(ModTags.DamageTypes.VEHICLE_IMMUNE)
            .add(DamageTypes.CACTUS, DamageTypes.SWEET_BERRY_BUSH, DamageTypes.IN_WALL)
            .addOptional(ResourceLocation("iceandfire", "gorgon"))
        this.tag(ModTags.DamageTypes.GUN_DAMAGE).add(
            ModDamageTypes.GUN_FIRE,
            ModDamageTypes.GUN_FIRE_HEADSHOT,
            ModDamageTypes.GUN_FIRE_ABSOLUTE,
            ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE,
            ModDamageTypes.LASER,
            ModDamageTypes.LASER_HEADSHOT,
            ModDamageTypes.SHOCK,
            ModDamageTypes.BURN,
            ModDamageTypes.REPAIR_TOOL,
            ModDamageTypes.PROJECTILE_HIT,
            ModDamageTypes.PROJECTILE_EXPLOSION,
            ModDamageTypes.SUPER_STAR_HIT,
            ModDamageTypes.SUPER_STAR_SLASH,
            ModDamageTypes.PHOSPHORUS_FIRE,
            ModDamageTypes.CUSTOM_EXPLOSION,
            ModDamageTypes.PROJECTILE_EXPLOSION
        )
        this.tag(ModTags.DamageTypes.SBW_GUN_FIRE_DAMAGE).add(
            ModDamageTypes.GUN_FIRE,
            ModDamageTypes.GUN_FIRE_HEADSHOT,
            ModDamageTypes.GUN_FIRE_ABSOLUTE,
            ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE
        )

        this.tag(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS).add(
            ModDamageTypes.PROJECTILE_EXPLOSION,
            ModDamageTypes.CUSTOM_EXPLOSION,
            ModDamageTypes.PROJECTILE_HIT,
            ModDamageTypes.GRAPESHOT_HIT,
            ModDamageTypes.LASER,
            ModDamageTypes.LASER_HEADSHOT,
            ModDamageTypes.LASER_STATIC,
            ModDamageTypes.REPAIR_TOOL,
            ModDamageTypes.SUPER_STAR_HIT,
            ModDamageTypes.SUPER_STAR_SLASH,
            ModDamageTypes.PHOSPHORUS_FIRE
        )
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(
            ModDamageTypes.GUN_FIRE_ABSOLUTE,
            ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE,
            ModDamageTypes.SHOCK,
            ModDamageTypes.PROJECTILE_HIT,
            ModDamageTypes.GRAPESHOT_HIT,
            ModDamageTypes.LASER,
            ModDamageTypes.LASER_HEADSHOT,
            ModDamageTypes.LASER_STATIC,
            ModDamageTypes.VEHICLE_STRIKE,
            ModDamageTypes.VEHICLE_EXPLOSION,
            ModDamageTypes.AIR_CRASH,
            ModDamageTypes.REPAIR_TOOL,
            ModDamageTypes.SUPER_STAR_HIT,
            ModDamageTypes.SUPER_STAR_SLASH,
            ModDamageTypes.PHOSPHORUS_FIRE,
            ModDamageTypes.AMMO_CONSUMPTION
        )
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(
            ModDamageTypes.SHOCK,
            ModDamageTypes.PHOSPHORUS_FIRE,
            ModDamageTypes.AMMO_CONSUMPTION
        )
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(
            ModDamageTypes.GUN_FIRE_ABSOLUTE,
            ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE,
            ModDamageTypes.SHOCK,
            ModDamageTypes.PROJECTILE_HIT,
            ModDamageTypes.GRAPESHOT_HIT,
            ModDamageTypes.LASER,
            ModDamageTypes.LASER_HEADSHOT,
            ModDamageTypes.LASER_STATIC,
            ModDamageTypes.VEHICLE_STRIKE,
            ModDamageTypes.VEHICLE_EXPLOSION,
            ModDamageTypes.AIR_CRASH,
            ModDamageTypes.SUPER_STAR_HIT,
            ModDamageTypes.SUPER_STAR_SLASH,
            ModDamageTypes.PHOSPHORUS_FIRE,
            ModDamageTypes.AMMO_CONSUMPTION
        )
        this.tag(DamageTypeTags.IS_EXPLOSION).add(
            ModDamageTypes.PROJECTILE_EXPLOSION,
            ModDamageTypes.CUSTOM_EXPLOSION,
            ModDamageTypes.LUNGE_MINE,
            ModDamageTypes.AMMO_CONSUMPTION
        )
        this.tag(DamageTypeTags.IS_FIRE).add(ModDamageTypes.BURN)
        this.tag(ModTags.DamageTypes.BYPASSES_VEHICLE).add(ModDamageTypes.REPAIR_TOOL)
        this.tag(ModTags.DamageTypes.NO_HURT_EFFECT).add(ModDamageTypes.AMMO_CONSUMPTION)

        val cataclysm = otherModTag("cataclysm", "bypasses_hurt_time")
        this.tag(cataclysm)
            .addOptional(mod(ModDamageTypes.GUN_FIRE_ABSOLUTE))
            .addOptional(mod(ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE))
            .addOptional(mod(ModDamageTypes.AIR_CRASH))
            .addOptional(mod(ModDamageTypes.BURN))
            .addOptional(mod(ModDamageTypes.REPAIR_TOOL))
            .addOptional(mod(ModDamageTypes.PROJECTILE_HIT))
            .addOptional(mod(ModDamageTypes.GRAPESHOT_HIT))
            .addOptional(mod(ModDamageTypes.CUSTOM_EXPLOSION))
            .addOptional(mod(ModDamageTypes.DRONE_HIT))
            .addOptional(mod(ModDamageTypes.LASER))
            .addOptional(mod(ModDamageTypes.LASER_HEADSHOT))
            .addOptional(mod(ModDamageTypes.LASER_STATIC))
            .addOptional(mod(ModDamageTypes.LUNGE_MINE))
            .addOptional(mod(ModDamageTypes.MINE))
            .addOptional(mod(ModDamageTypes.PROJECTILE_EXPLOSION))
            .addOptional(mod(ModDamageTypes.SHOCK))
            .addOptional(mod(ModDamageTypes.VEHICLE_EXPLOSION))
            .addOptional(mod(ModDamageTypes.VEHICLE_STRIKE))
            .addOptional(mod(ModDamageTypes.SUPER_STAR_HIT))
            .addOptional(mod(ModDamageTypes.SUPER_STAR_SLASH))
            .addOptional(mod(ModDamageTypes.PHOSPHORUS_FIRE))
    }

    companion object {
        fun otherModTag(modId: String, name: String): TagKey<DamageType> {
            return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation(modId, name))
        }
    }
}
