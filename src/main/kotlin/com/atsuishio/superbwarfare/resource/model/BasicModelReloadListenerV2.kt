package com.atsuishio.superbwarfare.resource.model

import com.github.mcmodderanchor.simplebedrockmodel.v1.common.animation.BedrockAnimation
import com.github.mcmodderanchor.simplebedrockmodel.v1.common.resource.pojo.BedrockModelPOJO
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.baked.BakedBedrockModel
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller

// TODO 替换成原本的loader
open class BasicModelReloadListenerV2(path: String) : BedrockModelReloadListener<BakedBedrockModel>(
    "models/bedrock/$path",
    "animations/bedrock/$path"
) {
    override fun apply(
        map: Map<ResourceLocation, BedrockModelPOJO>,
        resourceManager: ResourceManager,
        profiler: ProfilerFiller
    ) {
        super.apply(map, resourceManager, profiler)
        map.forEach { (location, pojo) ->
            this.models[location] = BakedBedrockModel.bake(pojo)
        }
        this.animFiles.forEach { (location, file) ->
            val id = this.animPathToIds[location] ?: return@forEach
            val path = this.idToModelPaths[id] ?: return@forEach
            val model = this.models[path] ?: return@forEach
            this.animations[location] = BedrockAnimation.createAnimation(file, model)
        }
        this.animFiles.clear()
    }
}
