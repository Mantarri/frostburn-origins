package misteryman.frostburnorigins.mixin;

import io.github.apace100.origins.power.CooldownPower;
import misteryman.frostburnorigins.common.registry.FBPowers;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract boolean isDead();

    @ModifyVariable(method = "damage", at = @At("HEAD"))
    private float damage(float amount, DamageSource damageSource) {
        Entity attacker = damageSource.getAttacker();
        if(FBPowers.FROSTBITE.isActive(attacker)) {
            Entity projectile = damageSource.getSource();
            if(projectile instanceof ProjectileEntity) {
                ((LivingEntity) (Object) this).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 4));
            }
        }

        if(FBPowers.AXE_CRAZY.isActive(attacker)) {
            if(attacker instanceof ServerPlayerEntity) {
                ItemStack mainHandItemStack = ((ServerPlayerEntity) (Object) attacker).getMainHandStack();
                Item mainHandItem = mainHandItemStack.getItem();
                if (mainHandItem.isIn(FabricToolTags.AXES)) {
                    return amount * 2;
                }
            }
        }

        if(FBPowers.CROSSBOW_MASTER.isActive(attacker)) {
            Entity projectile = damageSource.getSource();
            if(projectile instanceof ProjectileEntity) {
                return amount * 2;
            }
        }

        if(FBPowers.CRACKABLE.isActive(this)) {
            if(attacker instanceof ServerPlayerEntity) {
                ItemStack mainHandItemStack = ((ServerPlayerEntity) (Object) attacker).getMainHandStack();
                Item mainHandItem = mainHandItemStack.getItem();
                if (mainHandItem.isIn(FabricToolTags.PICKAXES)) {
                    return amount * 1.5F;
                }
            }
        }

        return amount;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isDead()Z", shift = At.Shift.BEFORE), method = "damage")
    public void modifyDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(((LivingEntity) (Object) this) instanceof ServerPlayerEntity){
            if(isDead()) {
                // `isOutOfWorld` is only true if the source of damage was a command, or void damage.
               if(!source.isOutOfWorld()) {
                    if (FBPowers.PHOENIX.isActive(((LivingEntity) (Object) this))) {
                        CooldownPower power = FBPowers.PHOENIX.get(this);
                        if(power.canUse()) {
                            if (!this.world.isClient) {
                                SoundEvent soundEvent = SoundEvents.ENTITY_BLAZE_AMBIENT;
                                this.world.playSound(
                                        null,
                                        ((PlayerEntity) (Object) this).getX(),
                                        ((PlayerEntity) (Object) this).getY(),
                                        ((PlayerEntity) (Object) this).getZ(),
                                        soundEvent,
                                        SoundCategory.PLAYERS,
                                        1.5F,
                                        0.25F);
                            }
                            ((LivingEntity) (Object) this).setHealth(1.0F);
                            ((LivingEntity) (Object) this).clearStatusEffects();
                            ((LivingEntity) (Object) this).addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
                            ((LivingEntity) (Object) this).addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
                            power.use();
                        }
                    }
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "addStatusEffect", cancellable = true)
    public void modifyAddStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {

    }

}
