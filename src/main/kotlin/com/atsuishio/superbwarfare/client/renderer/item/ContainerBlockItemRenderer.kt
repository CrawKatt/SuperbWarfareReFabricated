package com.atsuishio.superbwarfare.client.renderer.item

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.client.decorator.ContainerItemDecorator
import com.atsuishio.superbwarfare.item.container.ContainerBlockItem
import com.atsuishio.superbwarfare.resource.model.BlockModelReloadListener
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class ContainerBlockItemRenderer(dispatcher: BlockEntityRenderDispatcher, set: EntityModelSet) :
    BlockEntityWithoutLevelRenderer(dispatcher, set) {

    private val modelInstance by lazy { BlockModelReloadListener.getModel(MODEL)?.createInstance() }

    override fun renderByItem(
        stack: ItemStack,
        transformType: ItemDisplayContext,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        if (stack.item !is ContainerBlockItem) return

        val icon = ContainerItemDecorator.getCustomIcon(stack)
        if (icon != null && transformType == ItemDisplayContext.GUI) return
        val instance = modelInstance ?: return

        poseStack.pushPose()

        poseStack.translate(0.5f, 0.5f, 0.5f)

        instance.resetPose()

        instance.renderToBuffer(
            poseStack,
            bufferSource.getBuffer(RenderType.entityCutout(TEXTURE)),
            packedLight,
            packedOverlay
        )

        poseStack.popPose()
    }

    companion object {
        val TEXTURE = loc("textures/bedrock/block/container.png")
        val MODEL = loc("models/bedrock/block/container.geo.json")
    }
}
