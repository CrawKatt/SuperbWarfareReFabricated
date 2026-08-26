package com.atsuishio.superbwarfare.entity.vehicle

import com.atsuishio.superbwarfare.entity.getValue
import com.atsuishio.superbwarfare.entity.setValue
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level

open class AirSheepEntity(type: EntityType<AirSheepEntity>, world: Level) : VehicleEntity(type, world) {
    companion object {
        @JvmField
        val COLOR_ID: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(AirSheepEntity::class.java, EntityDataSerializers.INT)

        /** Maps dye color ID to wool item, mirroring vanilla [net.minecraft.world.entity.animal.Sheep.ITEM_BY_DYE] */
        private val WOOL_BY_DYE_COLOR = arrayOf(
            Items.WHITE_WOOL, Items.ORANGE_WOOL, Items.MAGENTA_WOOL, Items.LIGHT_BLUE_WOOL,
            Items.YELLOW_WOOL, Items.LIME_WOOL, Items.PINK_WOOL, Items.GRAY_WOOL,
            Items.LIGHT_GRAY_WOOL, Items.CYAN_WOOL, Items.PURPLE_WOOL, Items.BLUE_WOOL,
            Items.BROWN_WOOL, Items.GREEN_WOOL, Items.RED_WOOL, Items.BLACK_WOOL
        )
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        entityData.define(COLOR_ID, 0)
    }

    var colorId by COLOR_ID

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        compound.putInt("ColorId", colorId)
    }

    public override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        colorId = compound.getInt("ColorId")
    }

    override fun interact(player: Player, hand: InteractionHand): InteractionResult {
        val stack = player.mainHandItem
        if (stack.item is DyeItem) {
            if (customName != null && customName!!.string == "jeb_") return InteractionResult.PASS
            val stackColor = (stack.item as DyeItem).dyeColor.id

            if (colorId == stackColor) {
                return super.interact(player, hand)
            }

            colorId = stackColor

            if (!player.isCreative) {
                stack.shrink(1)
            }
            this.level().playSound(null, this, SoundEvents.BONE_MEAL_USE, this.soundSource, 1f, 1f)
            return InteractionResult.sidedSuccess(this.level().isClientSide())
        }
        return super.interact(player, hand)
    }

    override fun destroy() {
        val level = this.level()
        if (level is ServerLevel) {
            val x = this.x
            val y = this.y
            val z = this.z
            level.explode(null, x, y, z, 0f, Level.ExplosionInteraction.NONE)

            // Drop raw mutton and color-matched wool (mirrors vanilla Sheep drops)
            val woolItem = WOOL_BY_DYE_COLOR.getOrElse(colorId) { Items.WHITE_WOOL }
            val mutton = ItemEntity(level, x, (y + 1), z, ItemStack(Items.MUTTON))
            val wool = ItemEntity(level, x, (y + 1), z, ItemStack(woolItem))
            val boat = ItemEntity(level, x, (y + 1), z, ItemStack(Items.OAK_BOAT))

            mutton.setPickUpDelay(10)
            wool.setPickUpDelay(10)

            boat.setPickUpDelay(10)

            level.addFreshEntity(mutton)
            level.addFreshEntity(wool)
            level.addFreshEntity(boat)
        }
        super.destroy()
        this.discard()
    }
}
