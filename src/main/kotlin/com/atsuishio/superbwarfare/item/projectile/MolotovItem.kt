package com.atsuishio.superbwarfare.item.projectile

import com.atsuishio.superbwarfare.client.renderer.item.MolotovItemRenderer
import com.atsuishio.superbwarfare.entity.projectile.MolotovEntity
import com.atsuishio.superbwarfare.init.ModEntities
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.item.DispenserLaunchable
import com.atsuishio.superbwarfare.tools.mc
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.minecraft.core.Position
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level

@RegistryName("molotov")
class MolotovItem : Item(Properties()), DispenserLaunchable {
    companion object {
        @JvmStatic
        @Environment(EnvType.CLIENT)
        fun registerRenderer() {
            val renderer by lazy { MolotovItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) }
            BuiltinItemRendererRegistry.INSTANCE.register(ModItems.MOLOTOV) {
                stack, displayContext, poseStack, buffer, packedLight, packedOverlay ->
                renderer.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay)
            }
        }
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        player.startUsingItem(hand)
        return InteractionResultHolder.consume(player.getItemInHand(hand))
    }

    override fun getUseAnimation(stack: ItemStack) = UseAnim.SPEAR

    override fun getUseDuration(stack: ItemStack) = 72000

    override fun releaseUsing(stack: ItemStack, level: Level, living: LivingEntity, timeLeft: Int) {
        if (level.isClientSide || living !is Player) return
        val held = getUseDuration(stack) - timeLeft
        if (held <= 3) return
        val grenade = MolotovEntity(ModEntities.MOLOTOV, level)
        grenade.owner = living
        grenade.setPos(living.x, living.eyeY - 0.1, living.z)
        grenade.shootFromRotation(living, living.xRot, living.yRot, 0f, (held / 8f).coerceAtMost(1.6f), 0f)
        level.addFreshEntity(grenade)
        level.playSound(null, living.onPos, ModSounds.GRENADE_THROW, SoundSource.PLAYERS, 1f, 1f)
        living.cooldowns.addCooldown(this, 20)
        if (!living.isCreative) stack.shrink(1)
    }

    override fun getLaunchBehavior() = object : AbstractProjectileDispenseBehavior() {
        override fun getProjectile(level: Level, position: Position, stack: ItemStack): Projectile =
            MolotovEntity(ModEntities.MOLOTOV, level).apply { setPos(position.x(), position.y(), position.z()) }
    }
}
