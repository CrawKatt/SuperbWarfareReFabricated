package com.atsuishio.superbwarfare

import com.atsuishio.superbwarfare.advancement.CriteriaRegister
import com.atsuishio.superbwarfare.block.entity.FuMO25BlockEntity
import com.atsuishio.superbwarfare.capability.player.PlayerVariable
import com.atsuishio.superbwarfare.command.CommandRegister
import com.atsuishio.superbwarfare.compat.tacz.TACZGunEventHandler
import com.atsuishio.superbwarfare.config.CommonConfig
import com.atsuishio.superbwarfare.config.ServerConfig
import com.atsuishio.superbwarfare.data.CustomData
import com.atsuishio.superbwarfare.data.DataLoader
import com.atsuishio.superbwarfare.data.container.ContainerDataManager
import com.atsuishio.superbwarfare.data.loot.WreckageLootDataManager
import com.atsuishio.superbwarfare.entity.projectile.FastProjectileManualTicker
import com.atsuishio.superbwarfare.entity.projectile.FastThrowableProjectile
import com.atsuishio.superbwarfare.entity.living.DPSGeneratorEntity
import com.atsuishio.superbwarfare.entity.living.TargetEntity
import com.atsuishio.superbwarfare.event.CustomEventHandler
import com.atsuishio.superbwarfare.event.HitboxHelperEventHandler
import com.atsuishio.superbwarfare.event.LivingEventHandler
import com.atsuishio.superbwarfare.event.ModVersionEventHandler
import com.atsuishio.superbwarfare.tools.ServerSyncedEntityHandler
import com.atsuishio.superbwarfare.world.saveddata.ProjectileChunkSavedData
import com.atsuishio.superbwarfare.event.PlayerEventHandler
import com.atsuishio.superbwarfare.event.custom.LivingAttackCallback
import com.atsuishio.superbwarfare.event.custom.LivingDropsCallback
import com.atsuishio.superbwarfare.event.custom.LivingExperienceDropCallback
import com.atsuishio.superbwarfare.event.custom.LivingHurtCallback
import com.atsuishio.superbwarfare.event.custom.LivingTickCallback
import com.atsuishio.superbwarfare.event.custom.LootingLevelCallback
import com.atsuishio.superbwarfare.event.custom.MobEffectAddedCallback
import com.atsuishio.superbwarfare.event.custom.MobEffectRemovedCallback
import com.atsuishio.superbwarfare.init.ModAttributes
import com.atsuishio.superbwarfare.init.ModBlockEntities
import com.atsuishio.superbwarfare.init.ModBlocks
import com.atsuishio.superbwarfare.init.ModCommandArguments
import com.atsuishio.superbwarfare.init.ModCapabilities
import com.atsuishio.superbwarfare.init.ModDamageTypes
import com.atsuishio.superbwarfare.init.ModEntities
import com.atsuishio.superbwarfare.init.ModGameRules
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.ModLootModifier
import com.atsuishio.superbwarfare.init.ModMenuTypes
import com.atsuishio.superbwarfare.init.ModMobEffects
import com.atsuishio.superbwarfare.init.ModParticleTypes
import com.atsuishio.superbwarfare.init.ModPerks
import com.atsuishio.superbwarfare.init.ModPotions
import com.atsuishio.superbwarfare.init.ModRecipes
import com.atsuishio.superbwarfare.init.ModSerializers
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.init.ModTabs
import com.atsuishio.superbwarfare.init.ModTags
import com.atsuishio.superbwarfare.init.ModVillagers
import com.atsuishio.superbwarfare.init.ModWorldgen
import com.atsuishio.superbwarfare.item.container.ContainerBlockItem
import com.atsuishio.superbwarfare.item.weapon.BeastItem
import com.atsuishio.superbwarfare.mobeffect.BurnMobEffect
import com.atsuishio.superbwarfare.mobeffect.PhosphorusFireMobEffect
import com.atsuishio.superbwarfare.mobeffect.ShockMobEffect
import com.atsuishio.superbwarfare.mobeffect.TraumaMobEffect
import com.atsuishio.superbwarfare.network.NetworkRegistry
import com.atsuishio.superbwarfare.sound.SoundLimit
import com.atsuishio.superbwarfare.perk.damage.BattleOfWits
import com.atsuishio.superbwarfare.perk.functional.PowerfulAttraction
import com.atsuishio.superbwarfare.procedures.WelcomeProcedure
import com.atsuishio.superbwarfare.recipe.ModPotionRecipes
import com.atsuishio.superbwarfare.resource.BedrockModelLoader
import com.atsuishio.superbwarfare.world.saveddata.ChunkPosSavedData
import com.atsuishio.superbwarfare.world.saveddata.TDMSavedData
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry
import net.fabricmc.api.EnvType
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import software.bernie.geckolib.network.SerializableDataTicket
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.AbstractMap
import java.util.concurrent.ConcurrentLinkedQueue

private typealias Task = AbstractMap.SimpleEntry<Runnable, Int>

class Mod : ModInitializer {
    override fun onInitialize() {
        LOGGER.info("Initializing Superb Warfare (Fabric)")
        WelcomeProcedure.onCommonSetup()

        registerClientBedrockModels()
        triggerInit()
        callInits()
        ModWorldgen.register()
        CustomEventHandler.register()
        if (FabricLoader.getInstance().isModLoaded("tacz")) {
            TACZGunEventHandler.registerEvents()
        }

        NetworkRegistry.register()
        DataLoader.register()
        WreckageLootDataManager.register()
        ModLootModifier.register()
        FastThrowableProjectile.init()
        FastProjectileManualTicker.register()
        TraumaMobEffect.registerEvents()
        registerLivingEvents()
        PlayerVariable.registerEvents()

        ContainerBlockItem.registerContainers()
        ModCapabilities.init()
        CustomData.load()
        CommandRegister.register()
        ModCommandArguments.init()
        ContainerDataManager.register()
        ChunkPosSavedData.register()
        TDMSavedData.register()
        ModVersionEventHandler.register()
        ServerSyncedEntityHandler.register()
        ProjectileChunkSavedData.register()

        registerDataTickets()
        registerServerLifecycle()
        registerTicks()
    }

    private fun registerClientBedrockModels() {
        if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
            BedrockModelLoader.init()
        }
    }

    private fun triggerInit() {
        ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.COMMON, CommonConfig.init())
        ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.SERVER, ServerConfig.init())

        ModItems.init()
        ModItems.registerDispenserBehavior()

        if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
            SoundLimit.init()
        }
    }

    private fun callInits() {
        ModBlocks.init()
        ModBlockEntities.init()
        ModEntities.init()
        ModMenuTypes.init()
        ModSounds.init()
        ModMobEffects.init()
        ModParticleTypes.init()
        ModPotions.init()
        ModPotionRecipes.register()
        ModRecipes.init()
        ModAttributes.init()
        ModCommandArguments.init()
        ModTabs.init()
        ModPerks.init()
        LootingLevelCallback.EVENT.register(PowerfulAttraction::onLootingLevel)
        ServerLivingEntityEvents.ALLOW_DEATH.register(TargetEntity::onTargetDown)
        ServerLivingEntityEvents.ALLOW_DEATH.register(DPSGeneratorEntity::onDPSGeneratorDown)
        BattleOfWits.register()
        ModVillagers.init()
        ModSerializers.init()
        ModDamageTypes.init()
        CriteriaRegister.setup()
        registerCommonEvents()
        ModTags.init()
        ModGameRules.bootstrap()
    }

    private fun registerCommonEvents() {
        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            PlayerEventHandler.onPlayerLoggedIn(handler.player)
        }

        ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
            HitboxHelperEventHandler.onPlayerLoggedOut(handler.player)
        }

        ServerPlayerEvents.AFTER_RESPAWN.register { _, player, alive ->
            PlayerEventHandler.onPlayerRespawned(player, alive)
        }

        ServerEntityEvents.EQUIPMENT_CHANGE.register(LivingEventHandler::handleChangeSlot)

        AttackEntityCallback.EVENT.register { player, _, _, entity, _ ->
            if (player.mainHandItem.item is BeastItem) {
                BeastItem.onLeftClickEntity(player.mainHandItem, player, entity)
            }
            PlayerEventHandler.onAttackEntity(player, entity)
            InteractionResult.PASS
        }
    }

    private fun registerServerLifecycle() {
        ServerLifecycleEvents.SERVER_STARTING.register { serverInstance = it }
        ServerLifecycleEvents.SERVER_STOPPING.register { serverInstance = null }
    }

    private fun registerLivingEvents() {
        LivingAttackCallback.EVENT.register { entity, source, amount ->
            val attacker = source.directEntity as? LivingEntity
            (attacker == null || !ShockMobEffect.shouldCancelDamage(attacker)) &&
                    !LivingEventHandler.onEntityAttacked(entity, source, amount)
        }
        LivingHurtCallback.EVENT.register { event ->
            event.amount = LivingEventHandler.onEntityHurt(event.entity, event.source, event.amount)
        }
        LivingDropsCallback.EVENT.register { event ->
            LivingEventHandler.onLivingDrops(event.entity, event.source, event.drops)
        }
        LivingDropsCallback.EVENT.register(PowerfulAttraction::onLivingDrops)
        LivingExperienceDropCallback.EVENT.register { event ->
            val experience = PowerfulAttraction.handleExperienceDrop(
                event.attackingPlayer,
                event.entity.lastDamageSource,
                event.droppedExperience
            )
            event.droppedExperience = experience
            if (LivingEventHandler.onLivingExperienceDrop(event.entity, event.attackingPlayer, experience)) {
                event.isCanceled = true
            }
        }
        MobEffectAddedCallback.EVENT.register { entity, instance, source ->
            BurnMobEffect.onBurnAdded(entity, instance, source)
            PhosphorusFireMobEffect.onPhosphorusFireAdded(entity, instance, source)
            ShockMobEffect.onShockAdded(entity, instance, source)
        }
        MobEffectRemovedCallback.EVENT.register { entity, instance ->
            BurnMobEffect.onBurnRemoved(entity, instance)
            PhosphorusFireMobEffect.onPhosphorusFireRemoved(entity, instance)
            ShockMobEffect.onShockRemoved(entity, instance)
        }
        LivingTickCallback.EVENT.register(BurnMobEffect::onLivingTick)
        LivingTickCallback.EVENT.register(PhosphorusFireMobEffect::onLivingTick)
        LivingTickCallback.EVENT.register(ShockMobEffect::onLivingTick)
        PhosphorusFireMobEffect.registerEvents()
    }

    private fun registerTicks() {
        ServerTickEvents.END_SERVER_TICK.register { server ->
            tickServer()

            for (player in server.playerList.players) {
                HitboxHelperEventHandler.onPlayerTick(player)
            }
        }
    }

    private fun registerDataTickets() {
        FuMO25BlockEntity.FUMO25_TICK = GeckoLibUtil.addDataTicket(
            SerializableDataTicket.ofInt(loc("fumo25_tick"))
        )
    }

    companion object {
        const val MODID = "superbwarfare"
        const val ATTRIBUTE_MODIFIER = "superbwarfare_attribute_modifier"

        @JvmField
        val LOGGER: Logger = LogManager.getLogger(com.atsuishio.superbwarfare.Mod::class.java)

        @JvmStatic
        fun loc(path: String): ResourceLocation {
            return ResourceLocation(MODID, path)
        }

        private var serverInstance: MinecraftServer? = null
        private val SERVER_QUEUE = ConcurrentLinkedQueue<Task>()
        private val CLIENT_QUEUE = ConcurrentLinkedQueue<Task>()

        @JvmStatic
        fun getServer(): MinecraftServer? = serverInstance

        @JvmStatic
        fun queueServerWork(tick: Int, action: Runnable) {
            SERVER_QUEUE.add(AbstractMap.SimpleEntry(action, tick))
        }

        @JvmStatic
        fun queueClientWork(tick: Int, action: Runnable) {
            CLIENT_QUEUE.add(AbstractMap.SimpleEntry(action, tick))
        }

        @JvmStatic
        fun tickServer() {
            executeWork(SERVER_QUEUE)
        }

        @JvmStatic
        fun tickClient() {
            executeWork(CLIENT_QUEUE)
        }

        private fun executeWork(workQueue: MutableCollection<Task>) {
            val actions = workQueue
                .onEach { it.setValue(it.value - 1) }
                .filter { it.value <= 0 }
                .toSet()

            actions.forEach { it.key.run() }
            workQueue.removeAll(actions)
        }
    }
}
