package misteryman.frostburnorigins.mixin;

import io.github.apace100.origins.power.CooldownPower;
import misteryman.frostburnorigins.common.registry.FBItemTags;
import misteryman.frostburnorigins.common.registry.FBPowers;
import misteryman.frostburnorigins.common.registry.FBStatusEffects;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
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
                ((LivingEntity) (Object) this).addStatusEffect(FBStatusEffects.FROSTBITE_INSTANCE);
            }
        }

        if(FBPowers.AXE_CRAZY.isActive(attacker)) {
            if(attacker instanceof ServerPlayerEntity) {
                ItemStack mainHandItemStack = ((ServerPlayerEntity) (Object) attacker).getMainHandStack();
                Item mainHandItem = mainHandItemStack.getItem();
                if (mainHandItem.isIn(FabricToolTags.AXES)) {
                    amount *= 2;
                }
            }
        }

        if(FBPowers.CROSSBOW_MASTER.isActive(attacker)) {
            if(attacker instanceof ServerPlayerEntity) {
                Entity projectile = damageSource.getSource();
                if (projectile instanceof ProjectileEntity) {
                    amount *= 2;
                }
            }
        }

        if(FBPowers.SWORD_SWINE.isActive(attacker)) {
            if(attacker instanceof ServerPlayerEntity) {
                ItemStack mainHandItemStack = ((ServerPlayerEntity) (Object) attacker).getMainHandStack();
                Item mainHandItem = mainHandItemStack.getItem();
                if (mainHandItem.isIn(FabricToolTags.SWORDS)) {
                    amount *= 1.5F;
                }
                
            }
        }

        if(FBPowers.MARKSHOG.isActive(attacker)) {
            if(attacker instanceof ServerPlayerEntity) {
                ItemStack mainHandItemStack = ((ServerPlayerEntity) (Object) attacker).getMainHandStack();
                Item mainHandItem = mainHandItemStack.getItem();
                if (mainHandItem instanceof BowItem) {
                    amount *= 1.5F;
                } else if(mainHandItem instanceof CrossbowItem) {
                    amount *= 2.0F;
                }
            }
        }

        if(FBPowers.MIGHTY_AXE.isActive(attacker)) {
            if(attacker instanceof ServerPlayerEntity) {
                ItemStack mainHandItemStack = ((ServerPlayerEntity) (Object) attacker).getMainHandStack();
                Item mainHandItem = mainHandItemStack.getItem();
                if (mainHandItem.isIn(FabricToolTags.AXES)) {
                    amount *= 1.75F;
                }
            }
        }

        if(FBPowers.CRACKABLE.isActive(this)) {
            ItemStack mainHandItemStack = ((ServerPlayerEntity) (Object) attacker).getMainHandStack();
            Item mainHandItem = mainHandItemStack.getItem();
            if (mainHandItem.isIn(FabricToolTags.PICKAXES)) {
                amount *= 1.5F;
            }
        }

        if(FBPowers.FOOLS_BOLD.isActive(this)) {
            int armorPieces = 0;
            for(ItemStack stack : getArmorItems()) {
                if(FBItemTags.GOLD_ARMOR.contains(stack.getItem())) {
                    armorPieces++;
                }
            }
            amount *= (1 - (armorPieces * 0.08f));
        }

        if(FBPowers.ARMED.isActive(attacker)) {
            if(attacker instanceof ServerPlayerEntity) {
                if(((ServerPlayerEntity) (Object) attacker).getMainHandStack().equals(ItemStack.EMPTY)) {
                    System.out.println("Dealt 7HP damage");
                    amount = 7;
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

    // Freeze-Frame positive effect immunity while active
    @Inject(at = @At("HEAD"), method= "canHaveStatusEffect", cancellable = true)
    private void modifyCanHaveStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> info) {
        if(((LivingEntity) (Object) this) instanceof PlayerEntity) {
            if(FBPowers.FREEZE_FRAME_TOGGLE.isActive((LivingEntity) (Object) this)) {
                if(effect.getEffectType().isBeneficial()) {
                    info.setReturnValue(false);
                    return;
                }
            }
        }
    }
}
