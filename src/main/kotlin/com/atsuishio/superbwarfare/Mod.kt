package com.atsuishio.superbwarfare

import com.atsuishio.superbwarfare.init.ModCapabilities

import com.atsuishio.superbwarfare.capability.player.PlayerVariable
import com.atsuishio.superbwarfare.command.CommandRegister
import com.atsuishio.superbwarfare.compat.thermoo.ThermooCompatHandler
import com.atsuishio.superbwarfare.config.CLIENT_CONFIG
import com.atsuishio.superbwarfare.config.COMMON_CONFIG
import com.atsuishio.superbwarfare.config.SERVER_CONFIG
import com.atsuishio.superbwarfare.data.CustomData
import com.atsuishio.superbwarfare.data.DataLoader
import com.atsuishio.superbwarfare.data.container.ContainerDataManager
import com.atsuishio.superbwarfare.data.loot.WreckageLootDataManager
import com.atsuishio.superbwarfare.event.CustomEventHandler
import com.atsuishio.superbwarfare.event.HitboxHelperEventHandler
import com.atsuishio.superbwarfare.event.ModVersionEventHandler
import com.atsuishio.superbwarfare.event.PlayerEventHandler
import com.atsuishio.superbwarfare.entity.projectile.FastProjectileManualTicker
import com.atsuishio.superbwarfare.init.*
import com.atsuishio.superbwarfare.item.container.ContainerBlockItem
import com.atsuishio.superbwarfare.item.misc.TowBarItem
import com.atsuishio.superbwarfare.item.misc.TowlineItem
import com.atsuishio.superbwarfare.item.trinket.IffItem
import com.atsuishio.superbwarfare.mobeffect.PhosphorusFireMobEffect
import com.atsuishio.superbwarfare.network.registerPayloads
import com.atsuishio.superbwarfare.perk.damage.BattleOfWits
import com.atsuishio.superbwarfare.recipe.ModPotionRecipes
import com.atsuishio.superbwarfare.resource.BedrockModelLoader
import com.atsuishio.superbwarfare.tiers.ModArmorMaterials
import com.atsuishio.superbwarfare.tools.ResourceOnceLogger
import com.atsuishio.superbwarfare.tools.registerMinecraftUtil
import com.atsuishio.superbwarfare.world.saveddata.ChunkPosSavedData
import com.atsuishio.superbwarfare.world.saveddata.ProjectileChunkSavedData
import com.atsuishio.superbwarfare.world.saveddata.TDMSavedData
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry
import net.fabricmc.api.EnvType
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.ResourcePackActivationType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.AbstractMap
import java.util.concurrent.ConcurrentLinkedQueue

private typealias Task = AbstractMap.SimpleEntry<Runnable, Int>

class Mod : ModInitializer {
    override fun onInitialize() {
        LOGGER.info("Initializing Superb Warfare (Fabric)")

        registerClientBedrockModels()
        triggerInit()
        callInits()
        ModWorldGen.init()
        CustomEventHandler.register()

        registerPayloads()
        DataLoader.register()
        WreckageLootDataManager.register()
        ModLootModifier.init()
        IffItem.init()
        PhosphorusFireMobEffect.registerEvents()
        PlayerVariable.registerEvents()

        ContainerBlockItem.registerContainers()
        TowlineItem.init()
        TowBarItem.init()
        ProjectileChunkSavedData.init()
        ModCapabilities.init()
        CustomData.load()
        registerMinecraftUtil()

        CommandRegister.register()
        ContainerDataManager.register()
        ChunkPosSavedData.register()
        TDMSavedData.register()
        ModVersionEventHandler.register()
        ResourceOnceLogger.register()
        ThermooCompatHandler.init()
        registerBuiltinResourcePacks()
        registerTicks()
    }

    // Necessary for loading SBM models before common content initializes.
    private fun registerClientBedrockModels() {
        if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
            BedrockModelLoader.init()
        }
    }

    private fun triggerInit() {
        ModPerks.init()
        ModDataComponents.init()
        ModItems.init()
        ModItems.registerDispenserBehavior()

        NeoForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.CLIENT, CLIENT_CONFIG)
        NeoForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.COMMON, COMMON_CONFIG)
        NeoForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.SERVER, SERVER_CONFIG)
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
        ModArmorMaterials.register()
        ModAttributes.init()
        ModCriteriaTriggers.init()
        ModCommandArguments.init()
        ModTabs.init()
        BattleOfWits.register()
        ModVillagers.init()
        ModSerializers.init()
        ModDamageTypes.init()
        ModEventHandlers.init()
        FastProjectileManualTicker.init()
        ModTags.init()
        ModGameRules.bootstrap()
    }

    private fun registerTicks() {
        ServerTickEvents.START_SERVER_TICK.register { server ->
            for (player in server.playerList.players) {
                ThermooCompatHandler.onPlayerInVehicle(player)
            }
        }

        ServerTickEvents.END_SERVER_TICK.register { server ->
            tickServer()

            for (player in server.playerList.players) {
                PlayerEventHandler.onPlayerTick(player)
                HitboxHelperEventHandler.onPlayerTick(player)
            }
        }
    }

    private fun registerBuiltinResourcePacks() {
        FabricLoader.getInstance().getModContainer(MODID).ifPresent { container ->
            ResourceManagerHelper.registerBuiltinResourcePack(
                loc("sbw_legacy"),
                container,
                Component.translatable("pack.superbwarfare.sbw_legacy"),
                ResourcePackActivationType.NORMAL
            )
        }
    }

    companion object {
        const val MODID: String = "superbwarfare"

        @JvmField
        val ATTRIBUTE_MODIFIER: ResourceLocation = loc("attribute_modifier")

        @JvmField
        val LOGGER: Logger = LogManager.getLogger(Mod::class.java)

        @JvmStatic
        fun loc(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MODID, path)

        private val SERVER_QUEUE: MutableCollection<Task> = ConcurrentLinkedQueue()
        private val CLIENT_QUEUE: MutableCollection<Task> = ConcurrentLinkedQueue()

        @JvmStatic
        fun queueServerWork(tick: Int, action: Runnable) {
            SERVER_QUEUE.add(AbstractMap.SimpleEntry(action, tick))
        }

        @JvmStatic
        fun queueClientWork(tick: Int, action: Runnable) {
            CLIENT_QUEUE.add(AbstractMap.SimpleEntry(action, tick))
        }

        @JvmStatic
        fun tickServer() = executeWork(SERVER_QUEUE)

        @JvmStatic
        fun tickClient() = executeWork(CLIENT_QUEUE)

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
