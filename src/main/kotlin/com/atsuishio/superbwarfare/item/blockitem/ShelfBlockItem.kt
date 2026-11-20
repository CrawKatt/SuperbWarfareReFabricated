package com.atsuishio.superbwarfare.item.blockitem

import com.atsuishio.superbwarfare.client.renderer.item.ShelfBlockItemRenderer
import com.atsuishio.superbwarfare.init.ModBlocks
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.tools.mc
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.minecraft.world.item.BlockItem

@RegistryName("shelf")
class ShelfBlockItem : BlockItem(ModBlocks.SHELF, Properties()) {
    companion object {
        @Environment(EnvType.CLIENT)
        fun registerRenderer() {
            val renderer by lazy { ShelfBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) }
            BuiltinItemRendererRegistry.INSTANCE.register(ModItems.SHELF) { stack, context, pose, buffer, light, overlay ->
                renderer.renderByItem(stack, context, pose, buffer, light, overlay)
            }
        }
    }
}
