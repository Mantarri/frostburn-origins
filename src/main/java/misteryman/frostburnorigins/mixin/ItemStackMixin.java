package misteryman.frostburnorigins.mixin;

import misteryman.frostburnorigins.common.registry.FBPowers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At("HEAD"), cancellable = true)
    private <T extends LivingEntity> void modifyDamage(int amount, T entity, Consumer<T> breakCallback, CallbackInfo callbackInfo) {
        if(FBPowers.FOOLS_BOLD.isActive(entity)) {
            Item item = getItem();
            if(item instanceof ToolItem && ((ToolItem) item).getMaterial() == ToolMaterials.GOLD && entity.world.random.nextFloat() < 15 / 16f) {
                callbackInfo.cancel();
            }
            if(item instanceof ArmorItem && ((ArmorItem) item).getMaterial() == ArmorMaterials.GOLD && entity.world.random.nextFloat() < 3 / 4f) {
                callbackInfo.cancel();
            }
        }
    }
}
