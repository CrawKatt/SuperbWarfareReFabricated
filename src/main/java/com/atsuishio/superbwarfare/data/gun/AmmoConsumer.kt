package com.atsuishio.superbwarfare.data.gun

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.annotation.ServerOnly
import com.atsuishio.superbwarfare.capability.api.IItemHandler
import com.atsuishio.superbwarfare.data.*
import com.atsuishio.superbwarfare.init.ModCapabilities
import com.atsuishio.superbwarfare.serialization.kserializer.SerializedGsonObject
import com.atsuishio.superbwarfare.tools.InventoryTool
import com.atsuishio.superbwarfare.tools.isSameItemStack
import com.google.gson.annotations.SerializedName
import com.mojang.brigadier.exceptions.CommandSyntaxException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.nbt.NbtUtils
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import java.util.function.Consumer
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.min

@STOFactory(AmmoConsumer.AmmoConsumerInstanceBuilder::class)
@Serializable
class AmmoConsumer : DeserializeFromString, PropertyModifier<GunData, DefaultGunData> {
    @JvmField
    @SerializedName("Ammo")
    @SerialName("Ammo")
    var ammo: String? = null

    @JvmField
    @SerializedName("AmmoSlot")
    @SerialName("AmmoSlot")
    var ammoSlot: String = "Default"

    @JvmField
    @ServerOnly
    @SerializedName("Projectile")
    @SerialName("Projectile")
    var projectile: StringToObject<ProjectileInfo>? = null

    @JvmField
    @SerializedName("Override")
    @SerialName("Override")
    var override: SerializedGsonObject? = null

    @JvmField
    @SerializedName("Icon")
    @SerialName("Icon")
    var icon: String = Mod.loc("textures/overlay/vehicle/weapon/icons/empty.png").toString()

    @JvmField
    @SerializedName("ShouldUnload")
    @SerialName("ShouldUnload")
    var shouldUnload: Boolean = true

    @JvmField
    @Transient
    @kotlinx.serialization.Transient
    var type: AmmoConsumeType = AmmoConsumeType.EMPTY

    @JvmField
    @Transient
    @kotlinx.serialization.Transient
    var loadAmount: Int = 1

    @Transient
    @kotlinx.serialization.Transient
    private var initialized = false

    @Transient
    @kotlinx.serialization.Transient
    var playerAmmoType: Ammo? = null
        private set

    @Transient
    @kotlinx.serialization.Transient
    private var stack: ItemStack = ItemStack.EMPTY

    fun stack(): ItemStack = this.stack

    fun initialized(): Boolean = this.initialized

    enum class AmmoConsumeType {
        INVALID,
        EMPTY,
        INFINITE,
        PLAYER_AMMO,
        ITEM,
        ENERGY,
    }

    fun isAmmoItem(stack: ItemStack): Boolean {
        return isSameItemStack(stack, this.stack)
    }

    fun consume(data: GunData, shooter: Entity, count: Int): Int {
        var count = count
        if (!initialized) init()
        if (count <= 0 || this.type == AmmoConsumeType.INFINITE || shooter is Player && shooter.isCreative) return 0

        if (type == AmmoConsumeType.INVALID) {
            Mod.LOGGER.warn("consume ammo failed: invalid AmmoConsumeType")
            return 0
        }

        var consumed = 0

        if (type == AmmoConsumeType.PLAYER_AMMO) {
            if (shooter is Player) {
                if (playerAmmoType != null) {
                    val current = playerAmmoType!!.get(shooter)
                    consumed = min(current, count)
                    count -= consumed
                    playerAmmoType!!.add(shooter, -consumed)
                } else {
                    Mod.LOGGER.warn("consume player ammo failed: invalid player ammo type")
                }
            }
        }

        if (type == AmmoConsumeType.ENERGY) {
            return data.getEnergyProvider(shooter)?.extractEnergy(count, false) ?: 0
        }

        val handler = ModCapabilities.ITEM_HANDLER_ENTITY.find(shooter, null)

        if (handler != null) {
            return consumed + consume(data, handler, count)
        } else {
            Mod.LOGGER.warn("consume ammo failed: invalid item handler for entity {}", shooter)
            return consumed
        }
    }

    fun consume(data: GunData, handler: IItemHandler, count: Int): Int {
        if (!initialized) init()
        if (type == AmmoConsumeType.INVALID || type == AmmoConsumeType.INFINITE || type == AmmoConsumeType.EMPTY || count <= 0) return 0

        return when (type) {
            AmmoConsumeType.PLAYER_AMMO -> {
                val consumed = InventoryTool.consumeAmmoItem(handler, this.playerAmmoType, count)
                val rest = consumed - count
                data.virtualAmmo.add(rest)
                count
            }

            AmmoConsumeType.ENERGY -> {
                val energyStorage = ModCapabilities.ENERGY_ITEM.find(data.stack, null) ?: return 0
                energyStorage.extractEnergy(count, false)
            }

            else -> {
                InventoryTool.consumeItem(
                    handler,
                    { stack -> this.isAmmoItem(stack) },
                    count
                )
            }
        }
    }

    fun count(data: GunData, entity: Entity?): Int {
        if (!initialized) init()
        if (this.type == AmmoConsumeType.INFINITE) return Int.MAX_VALUE
        if (entity == null || type == AmmoConsumeType.EMPTY) return 0

        var playerAmmoCount = 0

        if (type == AmmoConsumeType.PLAYER_AMMO && entity is Player) {
            playerAmmoCount = playerAmmoType!!.get(entity)
        } else if (type == AmmoConsumeType.ENERGY) {
            return data.getEnergyProvider(entity)?.energyStored ?: 0
        }

        return playerAmmoCount + count(data, ModCapabilities.ITEM_HANDLER_ENTITY.find(entity, null))
    }

    fun count(data: GunData, handler: IItemHandler?): Int {
        if (!initialized) init()
        if (this.type == AmmoConsumeType.INFINITE) return Int.MAX_VALUE
        if (handler == null || type == AmmoConsumeType.EMPTY) return 0

        if (type == AmmoConsumeType.ITEM) {
            return InventoryTool.countItem(handler) { stack -> this.isAmmoItem(stack) }
        } else if (type == AmmoConsumeType.ENERGY) {
            val energyStorage = ModCapabilities.ENERGY_ITEM.find(data.stack, null) ?: return 0
            return energyStorage.energyStored
        }

        return InventoryTool.countAmmoItem(handler, this.playerAmmoType)
    }

    fun withdraw(ammoSupplier: Entity, count: Int): Int {
        if (!initialized) init()
        if (type == AmmoConsumeType.INVALID ||
            type == AmmoConsumeType.INFINITE ||
            type == AmmoConsumeType.EMPTY ||
            type == AmmoConsumeType.ENERGY ||
            count <= 0
        ) {
            return 0
        }

        if (type == AmmoConsumeType.PLAYER_AMMO) {
            if (ammoSupplier is Player) {
                if (playerAmmoType != null) {
                    val countToWithdraw = min(count, playerAmmoType!!.limit - playerAmmoType!!.get(ammoSupplier))
                    playerAmmoType!!.add(ammoSupplier, countToWithdraw)

                    val restItemCount = count - countToWithdraw
                    if (restItemCount > 0) {
                        InventoryTool.insertItem(ammoSupplier, playerAmmoType!!.itemStack, restItemCount)
                    }

                    return count
                } else {
                    Mod.LOGGER.warn("withdraw player ammo failed: invalid player ammo type")
                }
            } else {
                val itemHandler = ModCapabilities.ITEM_HANDLER_ENTITY.find(ammoSupplier, null)
                if (itemHandler != null) {
                    return withdraw(itemHandler, count)
                } else {
                    Mod.LOGGER.warn("withdraw ammo failed: invalid item handler")
                }
            }
        } else {
            if (ammoSupplier is Player) {
                InventoryTool.insertItem(ammoSupplier, this.stack, count)
                return count
            } else {
                val itemHandler = ModCapabilities.ITEM_HANDLER_ENTITY.find(ammoSupplier, null)
                if (itemHandler != null) {
                    return withdraw(itemHandler, count)
                } else {
                    Mod.LOGGER.warn("withdraw ammo failed: invalid item handler")
                }
            }
        }

        return 0
    }

    fun withdraw(handler: IItemHandler, count: Int): Int {
        if (!initialized) init()
        if (type == AmmoConsumeType.INVALID ||
            type == AmmoConsumeType.INFINITE ||
            type == AmmoConsumeType.EMPTY ||
            type == AmmoConsumeType.ENERGY ||
            count <= 0
        ) {
            return 0
        }

        val stackToInsert = if (type == AmmoConsumeType.PLAYER_AMMO) {
            this.playerAmmoType!!.itemStack
        } else {
            this.stack
        }

        return InventoryTool.insertItem(handler, stackToInsert, count)
    }

    @Transient
    @kotlinx.serialization.Transient
    private val jsonPropModifier = JsonPropertyModifier(GunProp.entries)

    override fun modifyProperty(modifier: PMC<GunData, DefaultGunData>) {
        if (this.projectile != null) {
            modifier[GunProp.PROJECTILE] = projectile!!.value
        }

        jsonPropModifier.update(override)
        jsonPropModifier.modifyProperty(modifier)
    }

    fun init() {
        if (ammo == null) return

        val matcher: Matcher = AMMO_PATTERN.matcher(ammo!!.trim { it <= ' ' })
        if (!matcher.matches()) {
            Mod.LOGGER.warn("invalid ammo value: {}", ammo)
            return
        }

        val numStr = matcher.group("count").trim { it <= ' ' }
        this.loadAmount = Mth.clamp(if (numStr.isEmpty()) 1 else numStr.toInt(), 1, Int.MAX_VALUE)

        val prefix = matcher.group("prefix")
        val id = matcher.group("id")
        val data = matcher.group("data")

        if (prefix.isBlank()) {
            this.type = when (id.lowercase()) {
                "infinity", "infinite" -> AmmoConsumeType.INFINITE
                "empty" -> AmmoConsumeType.EMPTY
                "fe", "rf", "energy" -> AmmoConsumeType.ENERGY
                else -> AmmoConsumeType.INVALID
            }

            if (this.type != AmmoConsumeType.INVALID) return
        }

        if ("@" == prefix) {
            this.playerAmmoType = Ammo.getType(id)
            if (this.playerAmmoType == null) {
                Mod.LOGGER.warn("invalid player ammo type: {}", id)
                return
            }

            this.type = AmmoConsumeType.PLAYER_AMMO
            this.stack = this.playerAmmoType!!.itemStack
        } else {
            val location = ResourceLocation.tryParse(id)
            if (location == null) {
                Mod.LOGGER.warn("invalid item id: {}", id)
                return
            }

            val item = BuiltInRegistries.ITEM.get(location)
            if (item === Items.AIR) {
                Mod.LOGGER.warn("invalid item: {}", id)
                return
            }

            this.stack = ItemStack(item!!)
            if (!data.isEmpty()) {
                try {
                    val tag = NbtUtils.snbtToStructure(data)
                    tag.putString("id", location.toString())
                    tag.putInt("count", 1)
                    this.stack = ItemStack.of(tag)
                } catch (exception: CommandSyntaxException) {
                    Mod.LOGGER.warn("invalid item data {}: {}", data, exception.message)
                    return
                }
            }

            this.type = AmmoConsumeType.ITEM
        }

        this.initialized = true
    }

    override fun deserializeFromString(str: String?) {
        this.ammo = str
        init()
    }

    object AmmoConsumerInstanceBuilder : StringInstanceBuilder<AmmoConsumer> {
        override fun fromString(value: String) = AmmoConsumer().apply {
            this.ammo = value
            init()
        }
    }

    companion object {
        val INVALID: AmmoConsumer = AmmoConsumer()

        private val AMMO_PATTERN: Pattern =
            Pattern.compile("^(?<count>(\\d+)?)\\s*(?<prefix>[@#]?)(?<id>\\w+(:\\w+)?)\\s*(?<data>(\\{.*})?)$")
    }
}
