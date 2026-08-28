package com.atsuishio.superbwarfare.block.entity

import com.atsuishio.superbwarfare.block.ChargingStationBlock
import com.atsuishio.superbwarfare.capability.api.EnergyStorage as ModEnergyStorage
import com.atsuishio.superbwarfare.capability.energy.EnergyStorageHelper
import com.atsuishio.superbwarfare.config.server.MiscConfig
import com.atsuishio.superbwarfare.init.ModBlockEntities
import com.atsuishio.superbwarfare.init.ModCapabilities
import com.atsuishio.superbwarfare.init.ModDataComponents
import com.atsuishio.superbwarfare.inventory.menu.ChargingStationMenu
import com.atsuishio.superbwarfare.network.dataslot.ContainerEnergyData
import com.atsuishio.superbwarfare.tools.isSameItemStack
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.LongTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.Container
import net.minecraft.world.ContainerHelper
import net.minecraft.world.MenuProvider
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import team.reborn.energy.api.EnergyStorage
import kotlin.math.min

open class ChargingStationBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(ModBlockEntities.CHARGING_STATION, pos, state),
    WorldlyContainer,
    MenuProvider {

    @JvmField
    protected var items: NonNullList<ItemStack> = NonNullList.withSize(2, ItemStack.EMPTY)

    var fuelTick: Int = 0
    var maxFuelTick: Int = DEFAULT_FUEL_TIME
    var showRange: Boolean = false

    private val energyStorage = ModEnergyStorage(MAX_ENERGY.toLong())

    protected val dataAccess: ContainerEnergyData = object : ContainerEnergyData {
        override fun get(index: Int): Long {
            return when (index) {
                0 -> this@ChargingStationBlockEntity.fuelTick.toLong()
                1 -> this@ChargingStationBlockEntity.maxFuelTick.toLong()
                2 -> {
                    this@ChargingStationBlockEntity.energyStorage.amount
                }

                3 -> if (this@ChargingStationBlockEntity.showRange) 1L else 0L
                else -> 0L
            }
        }

        override fun set(index: Int, value: Long) {
            when (index) {
                0 -> this@ChargingStationBlockEntity.fuelTick = value.toInt()
                1 -> this@ChargingStationBlockEntity.maxFuelTick = value.toInt()
                2 -> {
                    EnergyStorageHelper.insert(this@ChargingStationBlockEntity.energyStorage, value)
                }

                3 -> this@ChargingStationBlockEntity.showRange = value == 1L
            }
        }

        override fun getCount(): Int {
            return MAX_DATA_COUNT
        }
    }

    fun getItems(): NonNullList<ItemStack> {
        return this.items
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)

        if (this.level != null) {
            this.energyStorage.deserializeNBT(
                level!!.registryAccess(),
                IntTag.valueOf(componentInput.getOrDefault(ModDataComponents.ENERGY, 0))
            )
        }
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)

        components.set(ModDataComponents.ENERGY, this.energyStorage.amount.toInt())
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        if (tag.contains("Energy")) {
            val energy = tag.get("Energy")
            if (energy is IntTag || energy is LongTag) {
                this.energyStorage.deserializeNBT(registries, energy)
            }
        }

        this.fuelTick = tag.getInt("FuelTick")
        this.maxFuelTick = tag.getInt("MaxFuelTick")
        this.showRange = tag.getBoolean("ShowRange")
        this.items = NonNullList.withSize(this.containerSize, ItemStack.EMPTY)

        ContainerHelper.loadAllItems(tag, this.items, registries)
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)

        tag.putInt("Energy", this.energyStorage.amount.toInt())
        tag.putInt("FuelTick", this.fuelTick)
        tag.putInt("MaxFuelTick", this.maxFuelTick)
        tag.putBoolean("ShowRange", this.showRange)

        ContainerHelper.saveAllItems(tag, this.items, registries)
    }

    override fun getSlotsForFace(side: Direction): IntArray {
        return intArrayOf(SLOT_FUEL)
    }

    override fun canPlaceItemThroughFace(index: Int, stack: ItemStack, direction: Direction?): Boolean {
        return index == SLOT_FUEL
    }

    override fun canTakeItemThroughFace(index: Int, stack: ItemStack, direction: Direction): Boolean {
        return false
    }

    override fun getContainerSize(): Int {
        return this.items.size
    }

    override fun isEmpty(): Boolean {
        for (stack in this.items) {
            if (!stack.isEmpty) {
                return false
            }
        }

        return true
    }

    override fun getItem(slot: Int): ItemStack {
        return this.items[slot]
    }

    override fun removeItem(slot: Int, amount: Int): ItemStack {
        return ContainerHelper.removeItem(this.items, slot, amount)
    }

    override fun removeItemNoUpdate(slot: Int): ItemStack {
        return ContainerHelper.takeItem(this.items, slot)
    }

    override fun setItem(slot: Int, stack: ItemStack) {
        val oldStack = this.items[slot]
        val flag = !stack.isEmpty && isSameItemStack(oldStack, stack)

        this.items[slot] = stack

        if (stack.count > this.maxStackSize) {
            stack.count = this.maxStackSize
        }

        if (slot == SLOT_FUEL && !flag) {
            this.setChanged()
        }
    }

    override fun stillValid(player: Player): Boolean {
        return Container.stillValidBlockEntity(this, player)
    }

    override fun clearContent() {
        this.items.clear()
    }

    override fun getDisplayName(): Component {
        return Component.translatable("container.superbwarfare.charging_station")
    }

    override fun createMenu(
        containerId: Int,
        playerInventory: Inventory,
        player: Player
    ): AbstractContainerMenu {
        return ChargingStationMenu(containerId, playerInventory, this, this.dataAccess)
    }

    override fun getUpdatePacket(): ClientboundBlockEntityDataPacket {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        val tag = CompoundTag()

        ContainerHelper.saveAllItems(tag, this.items, registries)
        tag.putBoolean("ShowRange", this.showRange)

        return tag
    }

    override fun saveToItem(stack: ItemStack, registries: HolderLookup.Provider) {
        val tag = CompoundTag()

        if (this.level != null) {
            tag.put("Energy", this.energyStorage.serializeNBT(registries))
        }

        BlockItem.setBlockEntityData(stack, this.type, tag)
    }

    fun getEnergyStorage(side: Direction?): EnergyStorage {
        return this.energyStorage
    }

    private fun chargeEntity(handler: EnergyStorage) {
        val level = this.level ?: return
        if (level.gameTime % 20L != 0L) return

        val entities = level.getEntitiesOfClass(
            Entity::class.java,
            AABB(this.blockPos).inflate(CHARGE_RADIUS.toDouble())
        )

        entities.forEach { entity ->
            val cap = ModCapabilities.getEntityEnergyStorage(entity)
            if (cap == null || !cap.supportsInsertion()) return@forEach

            val charged = EnergyStorageHelper.insert(
                cap,
                min(handler.amount, (CHARGE_OTHER_SPEED * 20).toLong())
            )

            EnergyStorageHelper.extract(handler, charged)
        }

        this.setChanged()
    }

    private fun chargeItemStack(handler: EnergyStorage) {
        val stack = this.getItem(SLOT_CHARGE)
        if (stack.isEmpty) return

        val consumer = ContainerItemContext.ofSingleSlot(
            InventoryStorage.of(this, null).getSlot(SLOT_CHARGE)
        ).find(EnergyStorage.ITEM)

        if (consumer != null) {
            if (consumer.amount < consumer.capacity) {
                val charged = EnergyStorageHelper.insert(
                    consumer,
                    min(CHARGE_OTHER_SPEED.toLong(), handler.amount)
                )

                EnergyStorageHelper.extract(handler, min(charged, handler.amount))
            }
        }

        this.setChanged()
    }

    private fun chargeBlock(handler: EnergyStorage) {
        val level = this.level ?: return

        for (direction in Direction.entries) {
            val blockEntity = level.getBlockEntity(this.blockPos.relative(direction)) ?: continue

            val energy = EnergyStorage.SIDED.find(
                level,
                blockEntity.blockPos,
                direction
            )

            if (energy == null || blockEntity is ChargingStationBlockEntity) continue

            if (energy.supportsInsertion() && energy.amount < energy.capacity) {
                val received = EnergyStorageHelper.insert(
                    energy,
                    min(handler.amount, CHARGE_OTHER_SPEED.toLong())
                )

                EnergyStorageHelper.extract(handler, received)

                blockEntity.setChanged()
                this.setChanged()
            }
        }
    }

    companion object {
        protected const val SLOT_FUEL: Int = 0
        protected const val SLOT_CHARGE: Int = 1
        const val MAX_DATA_COUNT: Int = 4

        @JvmField
        val MAX_ENERGY: Int = MiscConfig.CHARGING_STATION_MAX_ENERGY.get()

        @JvmField
        val DEFAULT_FUEL_TIME: Int = MiscConfig.CHARGING_STATION_DEFAULT_FUEL_TIME.get()

        @JvmField
        val CHARGE_SPEED: Int = MiscConfig.CHARGING_STATION_GENERATE_SPEED.get()

        @JvmField
        val CHARGE_OTHER_SPEED: Int = MiscConfig.CHARGING_STATION_TRANSFER_SPEED.get()

        @JvmField
        val CHARGE_RADIUS: Int = MiscConfig.CHARGING_STATION_CHARGE_RADIUS.get()

        @JvmStatic
        fun serverTick(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: ChargingStationBlockEntity
        ) {
            if (blockEntity.showRange != state.getValue(ChargingStationBlock.SHOW_RANGE)) {
                level.setBlockAndUpdate(
                    pos,
                    state.setValue(ChargingStationBlock.SHOW_RANGE, blockEntity.showRange)
                )

                setChanged(level, pos, state)
            }

            val handler = blockEntity.getEnergyStorage(null)

            val energy = handler.amount

            if (energy > 0) {
                blockEntity.chargeEntity(handler)
            }

            if (handler.amount > 0) {
                blockEntity.chargeItemStack(handler)
            }

            if (handler.amount > 0) {
                blockEntity.chargeBlock(handler)
            }

            if (blockEntity.fuelTick > 0) {
                blockEntity.fuelTick--

                if (energy < handler.capacity) {
                    EnergyStorageHelper.insert(handler, CHARGE_SPEED.toLong())
                }
            } else if (!blockEntity.getItem(SLOT_FUEL).isEmpty) {
                if (handler.amount >= handler.capacity) return

                val fuel = blockEntity.getItem(SLOT_FUEL)
                val burnTime = AbstractFurnaceBlockEntity.getFuel().getOrDefault(fuel.item, 0)

                val fuelEnergy = ContainerItemContext.ofSingleSlot(
                    InventoryStorage.of(blockEntity, null).getSlot(SLOT_FUEL)
                ).find(EnergyStorage.ITEM)

                if (fuelEnergy != null) {
                    val energyToExtract = min(
                        CHARGE_OTHER_SPEED,
                        (handler.capacity - handler.amount).toInt()
                    )

                    if (fuelEnergy.supportsExtraction() && handler.supportsInsertion()) {
                        EnergyStorageHelper.insert(
                            handler,
                            EnergyStorageHelper.extract(fuelEnergy, energyToExtract.toLong())
                        )
                    }

                    blockEntity.setChanged()
                } else if (burnTime > 0) {
                    blockEntity.fuelTick = burnTime
                    blockEntity.maxFuelTick = burnTime

                    if (fuel.item.hasCraftingRemainingItem()) {
                        if (fuel.count <= 1) {
                            blockEntity.setItem(
                                SLOT_FUEL,
                                ItemStack(fuel.item.craftingRemainingItem)
                            )
                        } else {
                            val copy = ItemStack(fuel.item.craftingRemainingItem)
                            copy.count = 1

                            val itemEntity = ItemEntity(
                                level,
                                pos.x + 0.5,
                                pos.y + 0.2,
                                pos.z + 0.5,
                                copy
                            )

                            level.addFreshEntity(itemEntity)
                            fuel.shrink(1)
                        }
                    } else {
                        fuel.shrink(1)
                    }

                    blockEntity.setChanged()
                } else if (fuel.get(DataComponents.FOOD) != null) {
                    val foodComponent = fuel.get(DataComponents.FOOD) ?: return

                    val nutrition = foodComponent.nutrition()
                    val saturation = foodComponent.saturation() * 2.0f * nutrition
                    var tick = nutrition * 80 + (saturation * 200).toInt()

                    if (fuel.item.hasCraftingRemainingItem()) {
                        tick += 400
                    }

                    fuel.shrink(1)

                    blockEntity.fuelTick = tick
                    blockEntity.maxFuelTick = tick
                    blockEntity.setChanged()
                }
            }
        }
    }
}
