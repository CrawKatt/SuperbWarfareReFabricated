package com.atsuishio.superbwarfare.client.renderer.gun

import com.atsuishio.superbwarfare.data.attachment.AttachmentDefinition
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.profiling.ProfilerFiller

object AttachmentSkins : SimplePreparableReloadListener<Unit>() {

    private const val GEO_SUFFIX = ".geo.json"

    private const val PNG_SUFFIX = ".png"

    // 值 null = 查过了，这把枪没有这个配件的皮肤
    private val cache = HashMap<String, MutableMap<ResourceLocation, ResourceLocation?>>()

    fun modelPath(definition: AttachmentDefinition, gunId: String): ResourceLocation? =
        skin(definition.model, gunId, GEO_SUFFIX)

    fun texture(definition: AttachmentDefinition, gunId: String): ResourceLocation? =
        skin(definition.texture, gunId, PNG_SUFFIX)

    private fun skin(base: ResourceLocation?, gunId: String, suffix: String): ResourceLocation? {
        if (base == null || !base.path.endsWith(suffix)) return base

        val perGun = cache.getOrPut(gunId) { HashMap() }
        if (perGun.containsKey(base)) return perGun[base] ?: base

        // 资源管理器还没起来时先不缓存，下次进来重新查
        val resourceManager = Minecraft.getInstance().resourceManager ?: return base
        val resolved = resolve(base, gunId, suffix, resourceManager)
        perGun[base] = resolved
        return resolved ?: base
    }

    private fun resolve(
        base: ResourceLocation,
        gunId: String,
        suffix: String,
        resourceManager: ResourceManager
    ): ResourceLocation? {
        val gun = ResourceLocation.tryParse(gunId) ?: return null
        val stem = base.path.removeSuffix(suffix) + "_" + gun.path + suffix

        // 先查配件自己的命名空间，再查枪的命名空间
        val own = ResourceLocation.fromNamespaceAndPath(base.namespace, stem)
        if (resourceManager.getResource(own).isPresent) return own

        if (gun.namespace == base.namespace) return null
        val foreign = ResourceLocation.fromNamespaceAndPath(gun.namespace, stem)
        return if (resourceManager.getResource(foreign).isPresent) foreign else null
    }

    override fun prepare(resourceManager: ResourceManager, profiler: ProfilerFiller) {
    }

    override fun apply(unit: Unit, resourceManager: ResourceManager, profiler: ProfilerFiller) {
        cache.clear()
    }
}
