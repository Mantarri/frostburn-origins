package misteryman.frostburnorigins.power;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.util.ElytraPowerFallFlying;
import misteryman.frostburnorigins.common.registry.FBPowers;
import net.adriantodt.fallflyinglib.FallFlyingAbility;
import net.adriantodt.fallflyinglib.FallFlyingLib;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class FallFlyingPower implements FallFlyingAbility {

    private final LivingEntity entity;

    public FallFlyingPower(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean allowFallFlying() {
        return FBPowers.FALL_FLYING.isActive(entity);
    }

    @Override
    public boolean shouldHideCape() {
        return false;
    }
}
