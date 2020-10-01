package misteryman.frostburnorigins.mixin.goal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/entity/mob/VindicatorEntity$FollowEntityGoal")
public class VindicatorFollowEntityGoalMixin {
    @Inject( at = @At("HEAD"), method = "canStart()Z", cancellable = true)
    private void modifyCanStart(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
