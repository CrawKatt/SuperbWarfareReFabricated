package com.atsuishio.superbwarfare.api.event.converter;

import com.atsuishio.superbwarfare.api.event.PreKillEvent;
import com.atsuishio.superbwarfare.api.event.ProjectileHitEvent;
import com.atsuishio.superbwarfare.api.event.ReloadEvent;
import com.atsuishio.superbwarfare.api.event.RenderPlayerArmEvent;
import com.atsuishio.superbwarfare.api.event.ShootEvent;
import com.atsuishio.superbwarfare.api.event.wrapper.PreKillEventWrapper;
import com.atsuishio.superbwarfare.api.event.wrapper.ProjectileHitEventWrapper;
import com.atsuishio.superbwarfare.api.event.wrapper.ReloadEventWrapper;
import com.atsuishio.superbwarfare.api.event.wrapper.RenderPlayerArmEventWrapper;
import com.atsuishio.superbwarfare.api.event.wrapper.ShootEventWrapper;
import io.github.lounode.eventwrapper.forge.event.ForgeEventMappings;

public class EventWrapperMappings {

    public static void register() {
        ForgeEventMappings.makeLink(PreKillEvent.class, PreKillEventWrapper.class, new PreKillEventConverter());
        ForgeEventMappings.makeLink(PreKillEvent.SendKillMessage.class, PreKillEventWrapper.SendKillMessage.class, new PreKillEventConverter.SendKillMessageConverter());
        ForgeEventMappings.makeLink(PreKillEvent.Indicator.class, PreKillEventWrapper.Indicator.class, new PreKillEventConverter.IndicatorConverter());

        ForgeEventMappings.makeLink(ProjectileHitEvent.class, ProjectileHitEventWrapper.class, new ProjectileHitEventConverter());
        ForgeEventMappings.makeLink(ProjectileHitEvent.HitEntity.class, ProjectileHitEventWrapper.HitEntity.class, new ProjectileHitEventConverter.HitEntityConverter());
        ForgeEventMappings.makeLink(ProjectileHitEvent.HitBlock.class, ProjectileHitEventWrapper.HitBlock.class, new ProjectileHitEventConverter.HitBlockConverter());

        ForgeEventMappings.makeLink(ReloadEvent.class, ReloadEventWrapper.class, new ReloadEventConverter());
        ForgeEventMappings.makeLink(ReloadEvent.Pre.class, ReloadEventWrapper.Pre.class, new ReloadEventConverter.PreConverter());
        ForgeEventMappings.makeLink(ReloadEvent.Post.class, ReloadEventWrapper.Post.class, new ReloadEventConverter.PostConverter());

        ForgeEventMappings.makeLink(ShootEvent.class, ShootEventWrapper.class, new ShootEventConverter());
        ForgeEventMappings.makeLink(ShootEvent.Pre.class, ShootEventWrapper.Pre.class, new ShootEventConverter.PreConverter());
        ForgeEventMappings.makeLink(ShootEvent.Post.class, ShootEventWrapper.Post.class, new ShootEventConverter.PostConverter());

        ForgeEventMappings.makeLink(RenderPlayerArmEvent.class, RenderPlayerArmEventWrapper.class, new RenderPlayerArmEventConverter());
    }
}
