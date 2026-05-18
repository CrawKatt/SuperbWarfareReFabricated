package com.atsuishio.superbwarfare.client.language;

import com.atsuishio.superbwarfare.Mod;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;


public class ClientLanguageGetter {

    public static ClientLanguage EN_US;

    public static void register() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new ReloadListener());
    }

    static class ReloadListener extends SimplePreparableReloadListener<ClientLanguage> implements IdentifiableResourceReloadListener {
        @Override
        public ResourceLocation getFabricId() {
            return Mod.loc("client_language_getter");
        }

        @Override
        @ParametersAreNonnullByDefault
        protected @NotNull ClientLanguage prepare(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
            return ClientLanguage.loadFrom(pResourceManager, List.of("en_us"), false);
        }

        @Override
        @ParametersAreNonnullByDefault
        protected void apply(ClientLanguage pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
            EN_US = pObject;
        }
    }
}
