package misteryman.frostburnorigins.mixin;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.PowerTypeReference;
import io.github.apace100.origins.power.VariableIntPower;
import misteryman.frostburnorigins.common.FrostburnOrigins;
import misteryman.frostburnorigins.common.registry.FBPowers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    // Flaming Body
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;onAttacking(Lnet/minecraft/entity/Entity;)V"), method = "attack")
    public void modifyOnAttacking(Entity target, CallbackInfo info) {
        if(target instanceof LivingEntity) {
            if(FBPowers.FLAMING_BODY.isActive(((Entity) (Object) this))) {
                target.setOnFireFor(4);
            }

            if(FBPowers.FLAMING_BODY.isActive(((Entity) (Object) this))) {
                target.setOnFireFor(4);
            }
        }
    }

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;abilities:Lnet/minecraft/entity/player/PlayerAbilities;"), method = "getArrowType", cancellable = true)
    private void modifyGetArrowType(ItemStack weapon, CallbackInfoReturnable<ItemStack> cir) {

        if(FBPowers.FROZEN_QUIVER.isActive(((ServerPlayerEntity) (Object) this))) {
            if(FBPowers.ENDLESS_QUIVER.isActive(((ServerPlayerEntity) (Object) this))){
            }
            /*
            ItemStack tippedArrow = new ItemStack(Items.TIPPED_ARROW);
            Potion slownessPotion = new Potion(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 8, 1));
            StatusEffectInstance slowness = new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 8, 1);
            List<StatusEffectInstance> effects = new ArrayList<StatusEffectInstance>();
            effects.add(slowness);
            PotionUtil.setPotion(tippedArrow, slownessPotion);
            PotionUtil.setCustomPotionEffects(tippedArrow, effects);
            tippedArrow.getOrCreateTag().putInt("CustomPotionColor", slowness.getEffectType().getColor());
            cir.setReturnValue(tippedArrow);
             */
        }
    }
}
