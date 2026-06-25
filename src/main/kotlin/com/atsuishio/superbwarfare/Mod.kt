package com.atsuishio.superbwarfare

import com.atsuishio.superbwarfare.block.entity.FuMO25BlockEntity
import com.atsuishio.superbwarfare.command.CommandRegister
import com.atsuishio.superbwarfare.compat.thermoo.ThermooCompatHandler
import com.atsuishio.superbwarfare.config.CommonConfig
import com.atsuishio.superbwarfare.config.ServerConfig
import com.atsuishio.superbwarfare.data.CustomData
import com.atsuishio.superbwarfare.data.DataLoader
import com.atsuishio.superbwarfare.data.container.ContainerDataManager
import com.atsuishio.superbwarfare.data.vehicle.VehicleDataTool
import com.atsuishio.superbwarfare.event.HitboxHelperEventHandler
import com.atsuishio.superbwarfare.event.PlayerEventHandler
import com.atsuishio.superbwarfare.init.ModAttributes
import com.atsuishio.superbwarfare.init.ModBlockEntities
import com.atsuishio.superbwarfare.init.ModBlocks
import com.atsuishio.superbwarfare.init.ModCapabilities
import com.atsuishio.superbwarfare.init.ModCommandArguments
import com.atsuishio.superbwarfare.init.ModCriteriaTriggers
import com.atsuishio.superbwarfare.init.ModDamageTypes
import com.atsuishio.superbwarfare.init.ModDataComponents
import com.atsuishio.superbwarfare.init.ModEntities
import com.atsuishio.superbwarfare.init.ModEventHandlers
import com.atsuishio.superbwarfare.init.ModGameRules
import com.atsuishio.superbwarfare.init.ModItems
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
import com.atsuishio.superbwarfare.item.container.ContainerBlockItem
import com.atsuishio.superbwarfare.network.registerPayloads
import com.atsuishio.superbwarfare.tiers.ModArmorMaterials
import com.atsuishio.superbwarfare.tools.GunsTool
import com.atsuishio.superbwarfare.tools.ResourceOnceLogger
import com.atsuishio.superbwarfare.world.saveddata.TDMSavedData
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry
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
import software.bernie.geckolib.constant.dataticket.SerializableDataTicket
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.AbstractMap
import java.util.concurrent.ConcurrentLinkedQueue

private typealias Task = AbstractMap.SimpleEntry<Runnable, Int>

class Mod : ModInitializer {
    override fun onInitialize() {
        LOGGER.info("Initializing Superb Warfare (Fabric)")

        triggerInit()
        callInits()

        registerPayloads()
        VehicleDataTool.register()
        GunsTool.register()
        DataLoader.register()

        ContainerBlockItem.registerContainers()
        ModCapabilities.init()
        CustomData.load()

        CommandRegister.register()
        ContainerDataManager.register()
        TDMSavedData.register()
        ModDataComponents.init()

        ResourceOnceLogger.register()
        ThermooCompatHandler.init()
        registerBuiltinResourcePacks()
        registerDataTickets()
        registerTicks()
    }

    private fun triggerInit() {
        ModItems.init()
        ModItems.registerDispenserBehavior()

        NeoForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.COMMON, CommonConfig.init())
        NeoForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.SERVER, ServerConfig.init())
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
        ModRecipes.init()
        ModArmorMaterials.register()
        ModAttributes.init()
        ModCriteriaTriggers.init()
        ModCommandArguments.init()
        ModTabs.init()
        ModPerks.init()
        ModVillagers.init()
        ModSerializers.init()
        ModDamageTypes.init()
        ModEventHandlers.init()
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

    private fun registerDataTickets() {
        FuMO25BlockEntity.FUMO25_TICK = GeckoLibUtil.addDataTicket(
            SerializableDataTicket.ofInt(loc("fumo25_tick"))
        )
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
        fun loc(path: String): ResourceLocation {
            return ResourceLocation.fromNamespaceAndPath(MODID, path)
        }

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
