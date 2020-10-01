package misteryman.frostburnorigins.mixin;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HungerManager.class)

public abstract class HungerManagerMixin {
    @Shadow public abstract void add(int food, float f);

    @Inject(at = @At(value = "HEAD"), method = "eat", cancellable = true)
    public void modifyEat(Item item, ItemStack itemStack, CallbackInfo info) {
        // First if check will be modified to check player power
        List<Pair<StatusEffectInstance, Float>> statusEffects = item.getFoodComponent().getStatusEffects();
        if (true == true) {
            for(Integer i = 0; i < statusEffects.size(); i++) {
                StatusEffectInstance statusEffect = statusEffects.get(i).getFirst();
                if(statusEffect.getEffectType().equals(StatusEffects.HUNGER)) {

                }
            }
        }
    }

}
