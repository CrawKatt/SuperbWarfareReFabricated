package com.atsuishio.superbwarfare.entity.mixin;

import net.minecraft.world.damagesource.DamageSource;

public class DamageContainer {

    private final DamageSource source;
    private float newDamage;
    private final int postAttackInvulnerabilityTicks;

    public DamageContainer(DamageSource source, float damage) {
        this.source = source;
        this.newDamage = damage;
        this.postAttackInvulnerabilityTicks = 10;
    }

    public DamageSource getSource() {
        return source;
    }

    public float getNewDamage() {
        return newDamage;
    }

    public void setNewDamage(float newDamage) {
        this.newDamage = newDamage;
    }

    public int getPostAttackInvulnerabilityTicks() {
        return postAttackInvulnerabilityTicks;
    }
}
