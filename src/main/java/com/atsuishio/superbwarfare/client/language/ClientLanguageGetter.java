package com.atsuishio.superbwarfare.client.language;

import com.atsuishio.superbwarfare.Mod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientLanguageGetter {

    public static ClientLanguage EN_US;

    public static void registerReloadListeners() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new ReloadListener());
    }

    private static class ReloadListener extends SimplePreparableReloadListener<ClientLanguage>
            implements IdentifiableResourceReloadListener {

        @Override
        public ResourceLocation getFabricId() {
            return Mod.loc("client_language_getter");
        }

        @Override
        protected @NotNull ClientLanguage prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
            return ClientLanguage.loadFrom(resourceManager, List.of("en_us"), false);
        }

        @Override
        protected void apply(ClientLanguage language, ResourceManager resourceManager, ProfilerFiller profiler) {
            EN_US = language;
        }
    }
}
