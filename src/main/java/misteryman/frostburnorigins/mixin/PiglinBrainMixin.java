package misteryman.frostburnorigins.mixin;

import misteryman.frostburnorigins.common.FrostburnOrigins;
import misteryman.frostburnorigins.common.PlayerSpecificBartering;
import misteryman.frostburnorigins.common.registry.FBTags;
import misteryman.frostburnorigins.common.registry.FBPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.UUID;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {

    @Inject(method = "wearsGoldArmor", at = @At("HEAD"), cancellable = true)
    private static void modifyShouldAttack(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if(FBPowers.FOOLS_BOLD.isActive(target)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "consumeOffHandItem", at = @At(
        value = "INVOKE_ASSIGN",
        target = "Lnet/minecraft/entity/mob/PiglinEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"
        ), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private static void modifyConsumeOffhandItem(PiglinEntity piglin, boolean bl, CallbackInfo ci, ItemStack itemStack) {
        piglin.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        boolean bl2;
        boolean bl3;

        bl3 = itemStack.getItem().isIn(FBTags.PIGLIN_FOOD_BARTERING_ITEMS);

        if (piglin.isAdult() && bl3) {
            bl2 = PiglinBrainInvoker.invokeAcceptsForBarter(itemStack.getItem());
            Entity player = ((PlayerSpecificBartering)piglin).getBarteringEntity();
            if (bl && bl2 && bl3 && player instanceof ServerPlayerEntity && FBPowers.FOOD_BARTERING.isActive(player)) {
                LootTable lootTable = piglin.world.getServer().getLootManager().getTable(
                        FrostburnOrigins.id("gameplay/piglin_food_bartering"));
                List<ItemStack> list = lootTable.generateLoot((new LootContext.Builder(
                        (ServerWorld)piglin.world)).parameter(LootContextParameters.THIS_ENTITY, piglin).random(piglin.world.random).build(LootContextTypes.BARTER));
                PiglinBrainInvoker.invokeDoBarter(piglin, list);
                ci.cancel();
                return;
            }
        }
    }

    @Inject(method = "playerInteract", at = @At(value = "RETURN", ordinal = 0))
    private static void savePlayerInteract(PiglinEntity piglin, PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ((PlayerSpecificBartering)piglin).setBarteringEntity(player);
    }

    @Inject(method = "loot", at = @At("HEAD"))
    private static void savePlayerDropped(PiglinEntity piglin, ItemEntity drop, CallbackInfo ci) {
        UUID throwerUUID = drop.getThrower();
        ((PlayerSpecificBartering)piglin).setBarteringEntity(piglin.world.getPlayerByUuid(throwerUUID));
    }
}
