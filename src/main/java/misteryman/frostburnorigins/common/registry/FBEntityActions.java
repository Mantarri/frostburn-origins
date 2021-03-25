package misteryman.frostburnorigins.common.registry;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import misteryman.frostburnorigins.common.FrostburnOrigins;
import misteryman.frostburnorigins.mixin.StatusEffectInstanceAccessor;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class FBEntityActions {
    public static void register(){
        register_action(new ActionFactory<>(FrostburnOrigins.id("change_held_item"), new SerializableData(),
            (data, entity) -> {
                if(entity instanceof PlayerEntity) {
                    PlayerEntity pe = (PlayerEntity) entity;
                    int numOfItems = pe.getStackInHand(Hand.MAIN_HAND).getCount();
                    pe.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.ARROW, numOfItems));
                }
            }));

    register_action(new ActionFactory<>(FrostburnOrigins.id("freeze_frame_freeze"), new SerializableData(),
        (data, entity) -> {
            PlayerEntity pe = (PlayerEntity) entity;
            Map<StatusEffect, StatusEffectInstance> activeStatusEffects = pe.getActiveStatusEffects();
            for(Map.Entry<StatusEffect, StatusEffectInstance> entry : activeStatusEffects.entrySet()) {
                StatusEffectInstance statusEffectInstance = entry.getValue();
                statusEffectInstance.setPermanent(true);
            }
        }));

    register_action(new ActionFactory<>(FrostburnOrigins.id("freeze_frame_clear"), new SerializableData(),
        (data, entity) -> {
            PlayerEntity pe = (PlayerEntity) entity;

            Map<StatusEffect, StatusEffectInstance> activeStatusEffects = pe.getActiveStatusEffects();

            activeStatusEffects.entrySet().removeIf(key -> key.getKey().isBeneficial());

            for(Map.Entry<StatusEffect, StatusEffectInstance> entry : activeStatusEffects.entrySet()) {
                StatusEffectInstance statusEffectInstance = entry.getValue();
                statusEffectInstance.setPermanent(false);
            }
    }));

    register_action(new ActionFactory<>(FrostburnOrigins.id("chemical_burn"), new SerializableData(),
        (data, entity) -> {
            PlayerEntity pe = (PlayerEntity) entity;
            Map<StatusEffect, StatusEffectInstance> activeStatusEffects = pe.getActiveStatusEffects();

            activeStatusEffects.entrySet().removeIf(key -> !key.getKey().isBeneficial());

            for(Map.Entry<StatusEffect, StatusEffectInstance> entry : activeStatusEffects.entrySet()) {
                StatusEffectInstance statusEffectInstance = entry.getValue();
                ((StatusEffectInstanceAccessor) statusEffectInstance).setAmplifier(entry.getValue().getAmplifier() + 1);
            }
        }));

        register_action(new ActionFactory<>(FrostburnOrigins.id("random_safe_teleport"), new SerializableData(),
            (data, entity) -> {
                ServerPlayerEntity serverPlayer;
                if(entity instanceof ServerPlayerEntity) {
                    serverPlayer = (ServerPlayerEntity)entity;
                    // Starting position for teleport
                    BlockPos blockPos = serverPlayer.getBlockPos();
                    // Entity world
                    ServerWorld world = serverPlayer.getServerWorld();
                    // Max distance to teleport
                    int range = 64;
                    // (di, dj) is a vector - direction in which we move right now
                    int dx = 1;
                    int dz = 0;
                    // length of current segment
                    int segmentLength = 1;
                    BlockPos.Mutable mutable = blockPos.mutableCopy();
                    // center of our starting structure, or dimension
                    int center = blockPos.getY();
                    // Our valid spawn location
                    Vec3d tpPos;

                    // current position (x, z) and how much of current segment we passed
                    int x = blockPos.getX();
                    int z = blockPos.getZ();
                    //position to check up, or down
                    int segmentPassed = 0;
                    // increase y check
                    int i = 0;
                    // Decrease y check
                    int d = 0;
                    while(i < world.getDimensionHeight() || d > 0) {
                        for (int coordinateCount = 0; coordinateCount < range; ++coordinateCount) {
                            // make a step, add 'direction' vector (di, dj) to current position (i, j)
                            x += dx;
                            z += dz;
                            ++segmentPassed;mutable.setX(x);
                            mutable.setZ(z);
                            mutable.setY(center + i);
                            tpPos = Dismounting.method_30769(EntityType.PLAYER, world, mutable, true);
                            if (tpPos != null) {
                                serverPlayer.fallDistance = 0;
                                serverPlayer.teleport(
                                        world,
                                        tpPos.getX(),
                                        tpPos.getY(),
                                        tpPos.getZ(),
                                        serverPlayer.pitch,
                                        serverPlayer.yaw);
                            } else {
                                mutable.setY(center + d);
                                tpPos = Dismounting.method_30769(EntityType.PLAYER, world, mutable, true);
                                if (tpPos != null) {
                                    serverPlayer.fallDistance = 0;
                                    serverPlayer.teleport(
                                            world,
                                            tpPos.getX(),
                                            tpPos.getY(),
                                            tpPos.getZ(),
                                            serverPlayer.pitch,
                                            serverPlayer.yaw);
                                }
                            }

                            if (segmentPassed == segmentLength) {
                                // done with current segment
                                segmentPassed = 0;

                                // 'rotate' directions
                                int buffer = dx;
                                dx = -dz;
                                dz = buffer;

                                // increase segment length if necessary
                                if (dz == 0) {
                                    ++segmentLength;
                                }
                            }
                        }
                        i++;
                        d--;
                    }
                    FrostburnOrigins.LOGGER.warn("Could not find valid spot to teleport to for Random Safe Teleport action.");
                } else {
                    FrostburnOrigins.LOGGER.warn("Random Safe Teleport attempted to run on a non-PlayerEntity, stopping Random Safe Teleport action.");
                }
            }));
    }



    private static void register_action(ActionFactory<Entity> actionFactory) {
        Registry.register(ModRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
