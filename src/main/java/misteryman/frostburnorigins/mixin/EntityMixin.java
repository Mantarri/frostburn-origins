package misteryman.frostburnorigins.mixin;

import misteryman.frostburnorigins.common.registry.FBPowers;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow
    public World world;

    @Inject(method = "getAir", at = @At("HEAD"), cancellable = true)
    private void getAir(CallbackInfoReturnable<Integer> callbackInfo) {
        Object obj = this;
        //noinspection ConstantConditions
        if (FBPowers.GOLEM.isActive((Entity) obj) && world.isClient) {
            callbackInfo.setReturnValue(0);
        }
    }
}
