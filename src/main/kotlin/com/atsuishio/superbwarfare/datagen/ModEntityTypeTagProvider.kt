package com.atsuishio.superbwarfare.datagen

import com.atsuishio.superbwarfare.init.ModEntities
import com.atsuishio.superbwarfare.init.ModTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EntityTypeTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import java.util.concurrent.CompletableFuture

class ModEntityTypeTagProvider(
    pOutput: PackOutput,
    pProvider: CompletableFuture<HolderLookup.Provider>
) : EntityTypeTagsProvider(pOutput, pProvider) {
    override fun addTags(pProvider: HolderLookup.Provider) {
        this.tag(ModTags.EntityTypes.AERIAL_BOMB).add(
            ModEntities.MELON_BOMB,
            ModEntities.MK_82,
            ModEntities.SC_50,
            ModEntities.SC_250
        )

        this.tag(ModTags.EntityTypes.DESTROYABLE_PROJECTILE).add(
            ModEntities.AGM_65,
            ModEntities.JAVELIN_MISSILE,
            ModEntities.MELON_BOMB,
            ModEntities.MK_82,
            ModEntities.SWARM_DRONE,
            ModEntities.WIRE_GUIDE_MISSILE
        )

        this.tag(ModTags.EntityTypes.DECOY).add(
            ModEntities.SMOKE_DECOY,
            ModEntities.FLARE_DECOY
        )

        this.tag(ModTags.EntityTypes.NO_EXPERIENCE).add(ModEntities.TARGET, ModEntities.DPS_GENERATOR)
            .addOptional(ResourceLocation("dummmmmmy", "target_dummy"))
            .addOptional(ResourceLocation("powerful_dummy", "test_dummy"))

        this.tag(ModTags.EntityTypes.CAN_REPAIR).add(
            EntityType.IRON_GOLEM
        ).addOptional(ResourceLocation("touhou_little_maid", "maid"))

        this.tag(ModTags.EntityTypes.MINE).add(
            ModEntities.BLU_43,
            ModEntities.TM_62,
            ModEntities.PTKM_1R,
            ModEntities.CLAYMORE,
            ModEntities.PTKM_PROJECTILE
        )

        this.tag(ModTags.EntityTypes.AT_ROCKET).add(
            ModEntities.RPG_ROCKET_STANDARD,
            ModEntities.RPG_ROCKET_TBG
        )

        this.tag(ModTags.EntityTypes.AA_MISSILE).add(
            ModEntities.IGLA_MISSILE,
            ModEntities.RU_9M336_MISSILE
        )

        this.tag(ModTags.EntityTypes.SEEK_BLACKLIST).add(
            EntityType.ITEM,
            EntityType.ARMOR_STAND,
            EntityType.EXPERIENCE_ORB,
            EntityType.ITEM_DISPLAY,
            EntityType.FALLING_BLOCK,
            EntityType.ITEM_FRAME,
            EntityType.FIREWORK_ROCKET,
            EntityType.GLOW_ITEM_FRAME,
            EntityType.AREA_EFFECT_CLOUD,
            ModEntities.CLAYMORE,
            ModEntities.C4
        ).addOptional(ResourceLocation("touhou_little_maid", "power_point"))
            .addOptional(ResourceLocation("evilcraft", "vengeance_spirit"))
            .addOptional(ResourceLocation("mts", "builder_rendering"))
            .addOptional(ResourceLocation("create", "carriage_contraption"))
            .addOptional(ResourceLocation("create", "stationary_contraption"))
            .addOptional(ResourceLocation("create", "gantry_contraption"))
            .addOptional(ResourceLocation("create", "super_glue"))
            .addOptional(ResourceLocation("zombiekit", "flares"))

        this.tag(ModTags.EntityTypes.BIOGAS_GENERATOR_WHITELIST).add(
            EntityType.PLAYER,
            EntityType.VILLAGER,
            EntityType.WANDERING_TRADER,
            ModEntities.SENPAI
        ).addOptional(ResourceLocation("touhou_little_maid", "maid"))
    }

    companion object {
        fun forgeTag(name: String): TagKey<EntityType<*>> {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation("forge", name))
        }
    }
}
