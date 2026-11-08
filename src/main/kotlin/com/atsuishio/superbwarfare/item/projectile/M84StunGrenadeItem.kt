package com.atsuishio.superbwarfare.item.projectile

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.entity.projectile.M84StunGrenadeEntity
import com.atsuishio.superbwarfare.init.ModEntities
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.item.DispenserLaunchable
import com.atsuishio.superbwarfare.resource.model.ProjectileModelReloadListener
import com.atsuishio.superbwarfare.tools.mc
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.renderer.BedrockModelRenderTypes
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.baked.BakedBedrockModel
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
import net.minecraft.core.Position
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.*
import net.minecraft.world.level.Level

@RegistryName("m84_stun_grenade")
class M84StunGrenadeItem : Item(Properties().rarity(Rarity.UNCOMMON)), DispenserLaunchable {
    companion object {
        @JvmStatic
        @Environment(EnvType.CLIENT)
        fun registerRenderer() {
            val renderer by lazy { M84StunGrenadeItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) }
            BuiltinItemRendererRegistry.INSTANCE.register(ModItems.M84_STUN_GRENADE) {
                stack, displayContext, poseStack, buffer, packedLight, packedOverlay ->
                renderer.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay)
            }
        }
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        player.startUsingItem(hand)
        if (!level.isClientSide) {
            level.playSound(null, player.onPos, ModSounds.GRENADE_PULL, SoundSource.PLAYERS, 1f, 1f)
        }
        return InteractionResultHolder.consume(player.getItemInHand(hand))
    }

    override fun getUseAnimation(stack: ItemStack) = UseAnim.SPEAR
    override fun getUseDuration(stack: ItemStack) = 72000

    override fun releaseUsing(stack: ItemStack, level: Level, living: LivingEntity, timeLeft: Int) {
        if (level.isClientSide || living !is Player) return
        val held = getUseDuration(stack) - timeLeft
        if (held <= 3) return
        val grenade = M84StunGrenadeEntity(ModEntities.M84_STUN_GRENADE, level)
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
            M84StunGrenadeEntity(ModEntities.M84_STUN_GRENADE, level).apply {
                setPos(position.x(), position.y(), position.z())
            }
    }
}

@Environment(EnvType.CLIENT)
private class M84StunGrenadeItemRenderer(dispatcher: BlockEntityRenderDispatcher, models: EntityModelSet) :
    BlockEntityWithoutLevelRenderer(dispatcher, models) {
    private val modelPath = loc("models/bedrock/projectile/m84_stun_grenade.geo.json")
    private val texture = loc("textures/bedrock/projectile/m84_stun_grenade.png")
    private var bakedModel: BakedBedrockModel? = null
    private var instance: BakedModelInstance? = null

    override fun renderByItem(
        stack: ItemStack, displayContext: ItemDisplayContext, poseStack: PoseStack,
        buffer: MultiBufferSource, packedLight: Int, packedOverlay: Int
    ) {
        val model = ProjectileModelReloadListener.getModel(modelPath) ?: return
        // Rebuild after resource reload, while reusing the instance between item draws.
        if (bakedModel !== model) {
            bakedModel = model
            instance = model.createInstance()
        }
        val current = instance ?: return
        poseStack.pushPose()
        try {
            poseStack.translate(0.5, 0.5, 0.5)
            current.applyPose(current.bindPose)
            current.renderToBuffer(poseStack, buffer, RenderType.entityCutout(texture),
                BedrockModelRenderTypes.polyMeshCutout(texture), packedLight, packedOverlay)
        } finally {
            poseStack.popPose()
        }
    }
}
