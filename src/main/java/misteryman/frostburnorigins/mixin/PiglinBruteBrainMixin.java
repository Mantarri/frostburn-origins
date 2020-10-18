package misteryman.frostburnorigins.mixin;

import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.mob.PiglinBruteBrain;
import net.minecraft.entity.mob.PiglinBruteEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinBruteBrain.class)
public class PiglinBruteBrainMixin {
    @Inject(at = @At("HEAD"), method = "method_30260")
    private static void modifyMethod30260(PiglinBruteEntity piglinBruteEntity, Brain<PiglinBruteEntity> brain, CallbackInfo ci) {

    }
}
