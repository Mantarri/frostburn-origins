package misteryman.frostburnorigins.mixin;

import misteryman.frostburnorigins.common.registry.FBPowers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EvokerEntity.class)
public abstract class EvokerEntityMixin {
    @Redirect(at = @At(value = "NEW", target = "net/minecraft/entity/ai/goal/FollowTargetGoal", ordinal = 0), method = "initGoals")
    private <T extends LivingEntity> FollowTargetGoal<T> replaceFollowPlayerGoal(MobEntity mobEntity, Class<T> clazz, boolean checkVisibility) {
        return new FollowTargetGoal<>(mobEntity, clazz, 10, checkVisibility, false, e ->!FBPowers.BRETHREN.isActive(e));
    }
    @Redirect(at = @At(value = "NEW", target = "net/minecraft/entity/ai/goal/FleeEntityGoal", ordinal = 0), method = "initGoals")
    private <T extends LivingEntity> FleeEntityGoal<T> replaceFleeEntityGoal(PathAwareEntity mob, Class<T> clazz, float distance, double slowSpeed, double fastSpeed) {
        return new FleeEntityGoal<>(mob, clazz, 8.0F, 0.6D, 1.0D, e ->!FBPowers.BRETHREN.isActive(e));
    }
}
