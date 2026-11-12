package com.atsuishio.superbwarfare.item.projectile

import com.atsuishio.superbwarfare.client.renderer.item.NuclearBombItemRenderer
import com.atsuishio.superbwarfare.entity.projectile.NuclearBombEntity
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.tools.mc
import net.minecraft.nbt.Tag
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.Level
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry

open class NuclearBombItem(
    private val entityType: () -> EntityType<NuclearBombEntity>,
    private val modelId: String
) : Item(Properties().stacksTo(1).rarity(Rarity.EPIC)) {
    @Environment(EnvType.CLIENT)
    fun registerRenderer() {
        val renderer by lazy { NuclearBombItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels, modelId) }
        BuiltinItemRendererRegistry.INSTANCE.register(this) {
            stack, displayContext, poseStack, buffer, packedLight, packedOverlay ->
            renderer.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay)
        }
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)
        if (!level.isClientSide) {
            val bomb = entityType().create(level) ?: return InteractionResultHolder.fail(stack)
            stack.tag?.let { tag ->
                if (tag.contains("Radius", Tag.TAG_ANY_NUMERIC.toInt())) bomb.setExplosionRadius(tag.getFloat("Radius"))
                if (tag.contains("ExplosionDamage", Tag.TAG_ANY_NUMERIC.toInt())) bomb.setExplosionDamage(tag.getFloat("ExplosionDamage"))
                if (tag.contains("ExplosionDestroy")) bomb.setExplosionDestroy(tag.getBoolean("ExplosionDestroy"))
                bomb.readNuclearEffectData(tag)
            }
            bomb.owner = player
            bomb.setPos(player.x, player.eyeY - 0.1, player.z)
            bomb.shootFromRotation(player, player.xRot, player.yRot, 0f, 2.5f, 0f)
            if (level.addFreshEntity(bomb)) {
                level.playSound(null, player.onPos, ModSounds.GRENADE_THROW, SoundSource.PLAYERS, 1f, 1f)
                player.cooldowns.addCooldown(this, 10)
                player.awardStat(Stats.ITEM_USED.get(this))
                if (!player.isCreative) stack.shrink(1)
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }
}
