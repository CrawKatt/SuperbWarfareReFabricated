package com.atsuishio.superbwarfare.client.language;

import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import java.util.List;

@Mod.EventBusSubscriber(modid = com.atsuishio.superbwarfare.Mod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientLanguageGetter {

    public static ClientLanguage EN_US;

    @SubscribeEvent
    public static void onResourcePackReload(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new SimplePreparableReloadListener<ClientLanguage>() {
            @Override
            protected @NotNull ClientLanguage prepare(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
                return ClientLanguage.loadFrom(pResourceManager, List.of("en_us"), false);
            }

            @Override
            protected void apply(ClientLanguage pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
                EN_US = pObject;
            }
        });
    }
}
