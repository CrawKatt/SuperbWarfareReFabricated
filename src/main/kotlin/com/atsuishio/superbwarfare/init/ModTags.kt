package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.data.attachment.AttachmentSlots
import com.atsuishio.superbwarfare.data.gun.value.AttachmentType
import com.atsuishio.superbwarfare.init.ModTags.DamageTypes.GUN_DAMAGE
import com.atsuishio.superbwarfare.perk.Perk
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.block.Block

@Suppress("unused")
object ModTags {
    @JvmStatic
    fun commonItemTag(name: String): TagKey<Item> {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name))
    }

    @JvmStatic
    fun commonBlockTag(name: String): TagKey<Block> {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", name))
    }

    @JvmStatic
    fun modItemTag(name: String): TagKey<Item> {
        return TagKey.create(Registries.ITEM, Mod.loc(name))
    }

    @JvmStatic
    fun modBlockTag(name: String): TagKey<Block> {
        return TagKey.create(Registries.BLOCK, Mod.loc(name))
    }

    @JvmStatic
    fun modDamageTag(name: String): TagKey<DamageType> {
        return TagKey.create(Registries.DAMAGE_TYPE, Mod.loc(name))
    }

    @JvmStatic
    fun modEntityTag(name: String): TagKey<EntityType<*>> {
        return TagKey.create(Registries.ENTITY_TYPE, Mod.loc(name))
    }

    object Items {
        @JvmField val GUN = modItemTag("gun")
        @JvmField val SMG = modItemTag("smg")
        @JvmField val RIFLE = modItemTag("rifle")
        @JvmField val SNIPER_RIFLE = modItemTag("sniper_rifle")
        @JvmField val SHOTGUN = modItemTag("shotgun")
        @JvmField val MACHINE_GUN = modItemTag("machine_gun")
        @JvmField val LAUNCHER = modItemTag("launcher")

        @JvmField val MILITARY_ARMOR = modItemTag("military_armor")
        @JvmField val MILITARY_ARMOR_HEAVY = modItemTag("military_armor_heavy")

        @JvmField val INGOTS_CEMENTED_CARBIDE = modItemTag("ingots/cemented_carbide")
        @JvmField val STORAGE_BLOCK_CEMENTED_CARBIDE = modItemTag("storage_blocks/cemented_carbide")

        @JvmField val BLUEPRINT = modItemTag("blueprint")
        @JvmField val COMMON_BLUEPRINT = modItemTag("blueprint/common")
        @JvmField val RARE_BLUEPRINT = modItemTag("blueprint/rare")
        @JvmField val EPIC_BLUEPRINT = modItemTag("blueprint/epic")
        @JvmField val LEGENDARY_BLUEPRINT = modItemTag("blueprint/legendary")
        @JvmField val SUPERB_BLUEPRINT = modItemTag("blueprint/superb")
        @JvmField val VIRTUAL_BLUEPRINT = modItemTag("blueprint/virtual")
        @JvmField val CANNON_BLUEPRINT = modItemTag("blueprint/cannon")

        @JvmField val ENLARGED_COMMON_BLUEPRINT = modItemTag("blueprint/enlarged/common")
        @JvmField val ENLARGED_RARE_BLUEPRINT = modItemTag("blueprint/enlarged/rare")
        @JvmField val ENLARGED_EPIC_BLUEPRINT = modItemTag("blueprint/enlarged/epic")
        @JvmField val ENLARGED_LEGENDARY_BLUEPRINT = modItemTag("blueprint/enlarged/legendary")

        @JvmField val AMMO_PERK = modItemTag("perk/ammo")
        @JvmField val FUNCTIONAL_PERK = modItemTag("perk/functional")
        @JvmField val DAMAGE_PERK = modItemTag("perk/damage")

        @JvmField val RESEARCHABLE_AMMO_PERK = modItemTag("perk/researchable/ammo")
        @JvmField val RESEARCHABLE_FUNCTIONAL_PERK = modItemTag("perk/researchable/functional")
        @JvmField val RESEARCHABLE_DAMAGE_PERK = modItemTag("perk/researchable/damage")

        @JvmField val RESEARCHABLE_AMMO_PERK_COMMON = modItemTag("perk/researchable/ammo/common")
        @JvmField val RESEARCHABLE_AMMO_PERK_RARE = modItemTag("perk/researchable/ammo/rare")
        @JvmField val RESEARCHABLE_AMMO_PERK_EPIC = modItemTag("perk/researchable/ammo/epic")
        @JvmField val RESEARCHABLE_AMMO_PERK_LEGENDARY = modItemTag("perk/researchable/ammo/legendary")
        @JvmField val RESEARCHABLE_AMMO_PERK_SUPERB = modItemTag("perk/researchable/ammo/superb")

        @JvmField val RESEARCHABLE_FUNCTIONAL_PERK_COMMON = modItemTag("perk/researchable/functional/common")
        @JvmField val RESEARCHABLE_FUNCTIONAL_PERK_RARE = modItemTag("perk/researchable/functional/rare")
        @JvmField val RESEARCHABLE_FUNCTIONAL_PERK_EPIC = modItemTag("perk/researchable/functional/epic")
        @JvmField val RESEARCHABLE_FUNCTIONAL_PERK_LEGENDARY = modItemTag("perk/researchable/functional/legendary")
        @JvmField val RESEARCHABLE_FUNCTIONAL_PERK_SUPERB = modItemTag("perk/researchable/functional/superb")

        @JvmField val RESEARCHABLE_DAMAGE_PERK_COMMON = modItemTag("perk/researchable/damage/common")
        @JvmField val RESEARCHABLE_DAMAGE_PERK_RARE = modItemTag("perk/researchable/damage/rare")
        @JvmField val RESEARCHABLE_DAMAGE_PERK_EPIC = modItemTag("perk/researchable/damage/epic")
        @JvmField val RESEARCHABLE_DAMAGE_PERK_LEGENDARY = modItemTag("perk/researchable/damage/legendary")
        @JvmField val RESEARCHABLE_DAMAGE_PERK_SUPERB = modItemTag("perk/researchable/damage/superb")

        // Attachment tag
        //
        // 槽位相关的那一大片 tag（`attachment/<桶>` 与 `attachment/<桶>/<稀有度>`）**全部由
        // `AttachmentSlots` 注册表生成**：新增槽位只需要在注册表里登记一条，
        // 这里、datagen 与"可研究配件"的汇总都会自动跟上。
        @JvmField val ATTACHMENT = modItemTag("attachment")

        /** 槽位 → 该槽位的 tag（`superbwarfare:attachment/<AttachmentSlot.tagBucket>`） */
        @JvmField
        val ATTACHMENT_BY_SLOT: Map<AttachmentType, TagKey<Item>> = AttachmentSlots.ALL
            .mapNotNull { slot -> slot.tagName?.let { slot.type to modItemTag(it) } }
            .toMap()

        /**
         * 配件稀有度子 tag 的后缀，顺序决定生成文件里子 tag 的排列顺序。
         *
         * 稀有度是**物品**的属性（`Item.getRarity`），不是槽位的属性，所以这份表留在这里而不是注册表里。
         */
        @JvmField
        val ATTACHMENT_RARITY_SUFFIXES: List<Pair<Rarity, String>> = listOf(
            Rarity.COMMON to "common",
            Rarity.RARE to "rare",
            Rarity.EPIC to "epic",
            ModRarities.LEGENDARY to "legendary",
            ModRarities.SUPERB to "superb",
            ModRarities.VIRTUAL to "virtual",
        )

        @JvmField val ATTACHMENT_RESEARCHABLE_COMMON = modItemTag("attachment/researchable/common")
        @JvmField val ATTACHMENT_RESEARCHABLE_RARE = modItemTag("attachment/researchable/rare")
        @JvmField val ATTACHMENT_RESEARCHABLE_EPIC = modItemTag("attachment/researchable/epic")
        @JvmField val ATTACHMENT_RESEARCHABLE_LEGENDARY = modItemTag("attachment/researchable/legendary")
        @JvmField val ATTACHMENT_RESEARCHABLE_SUPERB = modItemTag("attachment/researchable/superb")
        @JvmField val ATTACHMENT_RESEARCHABLE_VIRTUAL = modItemTag("attachment/researchable/virtual")

        /** 可研究配件 tag 按稀有度汇总（各槽位同稀有度的子 tag 的并集） */
        @JvmField
        val ATTACHMENT_RESEARCHABLE_BY_RARITY: Map<Rarity, TagKey<Item>> = mapOf(
            Rarity.COMMON to ATTACHMENT_RESEARCHABLE_COMMON,
            Rarity.RARE to ATTACHMENT_RESEARCHABLE_RARE,
            Rarity.EPIC to ATTACHMENT_RESEARCHABLE_EPIC,
            ModRarities.LEGENDARY to ATTACHMENT_RESEARCHABLE_LEGENDARY,
            ModRarities.SUPERB to ATTACHMENT_RESEARCHABLE_SUPERB,
            ModRarities.VIRTUAL to ATTACHMENT_RESEARCHABLE_VIRTUAL,
        )

        /** 槽位 [type] 在 [rarity] 稀有度下的子 tag（`attachment/<桶>/<后缀>`）；该组合无 tag 时返回 `null` */
        @JvmStatic
        fun attachmentRarityTag(type: AttachmentType, rarity: Rarity): TagKey<Item>? {
            val path = ATTACHMENT_BY_SLOT[type]?.location?.path ?: return null
            val suffix = ATTACHMENT_RARITY_SUFFIXES.firstOrNull { it.first == rarity }?.second ?: return null
            return modItemTag("$path/$suffix")
        }

        @JvmField val HAMMER = modItemTag("hammer")
        @JvmField val WRENCHES = commonItemTag("wrenches")
        @JvmField val TOOLS_WRENCH = commonItemTag("tools/wrench")
        @JvmField val TOOLS_CROWBAR = commonItemTag("tools/crowbar")
        @JvmField val TOOLS_HAMMER = commonItemTag("tools/hammer")

        @JvmField val RESEARCH_FUEL = modItemTag("research_fuel")

        @JvmField val ANIMATED_PISTOL = modItemTag("animated/pistol")
        @JvmField val ANIMATED_SNIPER = modItemTag("animated/sniper")
        @JvmField val ANIMATED_RIFLE = modItemTag("animated/rifle")
        @JvmField val ANIMATED_SHOTGUN = modItemTag("animated/shotgun")
        @JvmField val ANIMATED_SMG = modItemTag("animated/smg")
        @JvmField val ANIMATED_RPG = modItemTag("animated/rpg")
        @JvmField val ANIMATED_MG = modItemTag("animated/mg")
        @JvmField val ANIMATED_MINIGUN = modItemTag("animated/minigun")

        // Common/convention tags
        @JvmField val DUSTS = commonItemTag("dusts")
        @JvmField val INGOTS = commonItemTag("ingots")
        @JvmField val STORAGE_BLOCKS = commonItemTag("storage_blocks")
        @JvmField val ORES = commonItemTag("ores")
        @JvmField val RAW_MATERIALS = commonItemTag("raw_materials")
        @JvmField val ORE_RATES_SINGULAR = commonItemTag("ore_rates/singular")
        @JvmField val ORES_IN_GROUND_STONE = commonItemTag("ores_in_ground/stone")
        @JvmField val ORES_IN_GROUND_DEEPSLATE = commonItemTag("ores_in_ground/deepslate")
        @JvmField val ARMORS = commonItemTag("armors")
        @JvmField val INGOTS_GOLD = commonItemTag("ingots/gold")
        @JvmField val INGOTS_IRON = commonItemTag("ingots/iron")
        @JvmField val INGOTS_COPPER = commonItemTag("ingots/copper")
        @JvmField val INGOTS_NETHERITE = commonItemTag("ingots/netherite")
        @JvmField val STORAGE_BLOCKS_IRON = commonItemTag("storage_blocks/iron")
        @JvmField val STORAGE_BLOCKS_GOLD = commonItemTag("storage_blocks/gold")
        @JvmField val STORAGE_BLOCKS_DIAMOND = commonItemTag("storage_blocks/diamond")
        @JvmField val STORAGE_BLOCKS_NETHERITE = commonItemTag("storage_blocks/netherite")
        @JvmField val STORAGE_BLOCKS_LAPIS = commonItemTag("storage_blocks/lapis")
        @JvmField val STORAGE_BLOCKS_COPPER = commonItemTag("storage_blocks/copper")
        @JvmField val STORAGE_BLOCKS_REDSTONE = commonItemTag("storage_blocks/redstone")
        @JvmField val NUGGETS_IRON = commonItemTag("nuggets/iron")
        @JvmField val NUGGETS_GOLD = commonItemTag("nuggets/gold")
        @JvmField val GEMS_DIAMOND = commonItemTag("gems/diamond")
        @JvmField val GEMS_AMETHYST = commonItemTag("gems/amethyst")
        @JvmField val GEMS_EMERALD = commonItemTag("gems/emerald")
        @JvmField val GEMS_LAPIS = commonItemTag("gems/lapis")
        @JvmField val GEMS_QUARTZ = commonItemTag("gems/quartz")
        @JvmField val DUSTS_REDSTONE = commonItemTag("dusts/redstone")
        @JvmField val GLASS_BLOCKS = commonItemTag("glass_blocks")
        @JvmField val GLASS_PANES = commonItemTag("glass_panes")
        @JvmField val SANDS = commonItemTag("sands")
        @JvmField val DYES_BLACK = commonItemTag("dyes/black")
        @JvmField val DYES_GREEN = commonItemTag("dyes/green")
        @JvmField val ORES_NETHERITE_SCRAP = commonItemTag("ores/netherite_scrap")
        @JvmField val CHESTS_ENDER = commonItemTag("chests/ender")
        @JvmField val CHESTS_WOODEN = commonItemTag("chests/wooden")
        @JvmField val ENDER_PEARLS = commonItemTag("ender_pearls")
    }

    object Blocks {
        @JvmField val SOFT_COLLISION = modBlockTag("soft_collision")
        @JvmField val NORMAL_COLLISION = modBlockTag("normal_collision")
        @JvmField val HARD_COLLISION = modBlockTag("hard_collision")

        @JvmField val BULLET_IGNORE = modBlockTag("bullet_ignore")
        @JvmField val BULLET_CAN_DESTROY = modBlockTag("bullet_can_destroy")
        @JvmField val CANNON_SHOT_CAN_DESTROY = modBlockTag("cannon_shot_can_destroy")

        @JvmField val AUTO_LANDING = modBlockTag("auto_landing")
        @JvmField val VEHICLE_PASS_THROUGH = modBlockTag("vehicle_pass_through")
        @JvmField val MINEABLE_WITH_MILITARY_SHOVEL = modBlockTag("mineable/military_shovel")

        // Common/convention tags
        @JvmField val GLASS_BLOCKS = commonBlockTag("glass_blocks")
        @JvmField val GLASS_PANES = commonBlockTag("glass_panes")
        @JvmField val ORES = commonBlockTag("ores")
        @JvmField val ORES_IN_GROUND_STONE = commonBlockTag("ores_in_ground/stone")
        @JvmField val ORES_IN_GROUND_DEEPSLATE = commonBlockTag("ores_in_ground/deepslate")
    }

    object DamageTypes {
        @JvmField val PROJECTILE = modDamageTag("projectile")
        @JvmField val PROJECTILE_ABSOLUTE = modDamageTag("projectile_absolute")

        @JvmField val VEHICLE_IGNORE = modDamageTag("vehicle_ignore")
        @JvmField val VEHICLE_NOT_ABSORB = modDamageTag("vehicle_not_absorb")
        @JvmField val VEHICLE_IMMUNE = modDamageTag("vehicle_immune")

        @JvmField val GUN_DAMAGE = modDamageTag("gun_damage")
        @JvmField val SBW_GUN_FIRE_DAMAGE = modDamageTag("sbw_gun_fire_damage")
        @JvmField val NO_HURT_EFFECT = modDamageTag("no_hurt_effect")
        @JvmField val BYPASSES_VEHICLE = modDamageTag("bypasses_vehicle")
        /**
         * 近战伤害（枪械近战 + 原版 `minecraft:player_attack`）。
         *
         * 与 [GUN_DAMAGE] 同一套路，数据包/其它模组可以自行往里加自己的近战伤害类型。
         * 判定入口是 [com.atsuishio.superbwarfare.tools.DamageTypeTool.isMeleeDamage]。
         */
        @JvmField
        val MELEE = modDamageTag("melee")
    }

    object EntityTypes {
        @JvmField val AERIAL_BOMB = modEntityTag("aerial_bomb")
        @JvmField val DESTROYABLE_PROJECTILE = modEntityTag("destroyable_projectile")
        @JvmField val DECOY = modEntityTag("decoy")
        @JvmField val NO_EXPERIENCE = modEntityTag("no_experience")
        @JvmField val CAN_REPAIR = modEntityTag("can_repair")
        @JvmField val MINE = modEntityTag("mine")
        @JvmField val AT_ROCKET = modEntityTag("at_rocket")
        @JvmField val AA_MISSILE = modEntityTag("aa_missile")
        @JvmField val SEEK_BLACKLIST = modEntityTag("seek_blacklist")
        @JvmField val BIOGAS_GENERATOR_WHITELIST = modEntityTag("biogas_generator_whitelist")
    }

    object Perks {
        @JvmField
        val TEST: TagKey<Perk> = TagKey.create(ModPerks.PERK_KEY, Mod.loc("test"))
    }

    @JvmStatic
    fun init() {
    }
}
