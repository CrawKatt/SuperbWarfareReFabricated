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
            ModEntities.MK_84,
            ModEntities.BOR_57,
            ModEntities.SC_50,
            ModEntities.SC_250,
            ModEntities.RU_3M14_MISSILE
        )

        this.tag(ModTags.EntityTypes.DESTROYABLE_PROJECTILE).add(
            ModEntities.AGM_65,
            ModEntities.JAVELIN_MISSILE,
            ModEntities.MELON_BOMB,
            ModEntities.MK_82,
            ModEntities.MK_84,
            ModEntities.BOR_57,
            ModEntities.SWARM_DRONE,
            ModEntities.WIRE_GUIDE_MISSILE,
            ModEntities.RU_3M14_MISSILE
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
            ModEntities.RU_9M336_MISSILE,
            ModEntities.RU_9M100_MISSILE,
            ModEntities.FIM_92_MISSILE
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
            ModEntities.SENPAI,
            ModEntities.CREEPING_SENPAI
        ).addOptional(ResourceLocation("touhou_little_maid", "maid"))

        this.tag(ModTags.EntityTypes.SENPAI).add(
            ModEntities.SENPAI,
            ModEntities.CREEPING_SENPAI
        )

        this.tag(
            TagKey.create(
                Registries.ENTITY_TYPE,
                ResourceLocation("touhou_little_maid", "maid_vehicle_rotate_blocklist")
            )
        ).add(
            ModEntities.TYPE_63,
            ModEntities.MK_42,
            ModEntities.HPJ_11,
            ModEntities.MLE_1934,
            ModEntities.BL_132,
            ModEntities.ANNIHILATOR,
            ModEntities.LASER_TOWER,
            ModEntities.WAVEFORCE_TOWER,
            ModEntities.TOW,
            ModEntities.SPEEDBOAT,
            ModEntities.TINY_SPEEDBOAT,
            ModEntities.WHEEL_CHAIR,
            ModEntities.LAV_150,
            ModEntities.LAV_AD,
            ModEntities.LAV_25,
            ModEntities.BMP_2,
            ModEntities.BRADLEY,
            ModEntities.ZTZ_99A,
            ModEntities.T_90A,
            ModEntities.M_1A_2,
            ModEntities.YX_100,
            ModEntities.PRISM_TANK,
            ModEntities.PLZ_05,
            ModEntities.FH_77BW,
            ModEntities.TOM_6,
            ModEntities.AH_6,
            ModEntities.MI_28,
            ModEntities.KV_16,
            ModEntities.JU_87,
            ModEntities.A_10A,
            ModEntities.J_16,
            ModEntities.AC_130H,
            ModEntities.AIR_SHEEP,
            ModEntities.HAPPIEST_GHAST,
            ModEntities.KIROV,
            ModEntities.DRONE,
            ModEntities.MORTAR,
            ModEntities.VEHICLE_ASSEMBLING_TABLE,
            ModEntities.SODAYO_PICK_UP,
            ModEntities.SODAYO_PICK_UP_HMG,
            ModEntities.SODAYO_PICK_UP_ROCKET,
            ModEntities.SODAYO_PICK_UP_TOW,
            ModEntities.TRUCK,
        )
    }
}
