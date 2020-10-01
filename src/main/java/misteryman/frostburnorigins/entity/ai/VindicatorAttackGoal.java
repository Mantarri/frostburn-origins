package misteryman.frostburnorigins.entity.ai;

import misteryman.frostburnorigins.common.registry.FBPowers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Shadow;

public class VindicatorAttackGoal extends MeleeAttackGoal {
    public VindicatorAttackGoal(VindicatorEntity entity) {
        super(entity, 1D, false);
    }

    public boolean canStart() {
        if (((VindicatorEntity) this.mob).getTarget() instanceof ServerPlayerEntity && FBPowers.ILLAGER.isActive(((VindicatorEntity) this.mob).getTarget())) {
            return false;
        } else {
            return super.canStart();
        }
    }
}
