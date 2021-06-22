package misteryman.frostburnorigins.common.registry;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import misteryman.frostburnorigins.common.FrostburnOrigins;
import misteryman.frostburnorigins.mixin.StatusEffectInstanceAccessor;
import misteryman.frostburnorigins.util.Teleport;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Iterator;
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
                    int range = 256;
                    // (di, dj) is a vector - direction in which we move right now
                    int dx = 1;
                    int dz = 0;
                    // length of current segment
                    int segmentLength = 1;
                    BlockPos.Mutable mutable = blockPos.mutableCopy();
                    // center of our starting position
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

        register_action(new ActionFactory<>(FrostburnOrigins.id("teleport_toward_look"), new SerializableData()
            .add("unsafe_blocks", SerializableDataType.BLOCK_TAG)
            .add("range", SerializableDataType.DOUBLE),
            (data, entity) -> {
                PlayerEntity pe = (PlayerEntity) entity;

                if(!Teleport.towardDir(pe, data.getDouble("range"))) {
                    FrostburnOrigins.LOGGER.warn("Could not find valid teleport location");
                }
            }));

        register_action(new ActionFactory<>(FrostburnOrigins.id("spawn_entity_formation"), new SerializableData()
            .add("entity_type", SerializableDataType.ENTITY_TYPE)
            .add("entity_count", SerializableDataType.INT)
            .add("radius", SerializableDataType.FLOAT),
            (data, entity) -> {
                PlayerEntity pe = (PlayerEntity) entity;
                Entity fe; // formation entity
                int entityCount = data.getInt("entity_count");
                float radius = data.getFloat("radius");

                World world = pe.getEntityWorld();

                for(int i = 0; i < entityCount; i++) {
                    double radians = 2 * Math.PI / entityCount * i;

                    double vertical = Math.sin(radians);
                    double horizontal = Math.cos(radians);
                    Vec3d spawnDir = new Vec3d(horizontal, 0, vertical);
                    Vec3d spawnPos = spawnDir.multiply(radius).add(pe.getPos());

                    fe = ((EntityType<?>) data.get("entity_type")).create(world);

                    if(fe != null) {
                        fe.refreshPositionAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), fe.yaw, fe.pitch);
                        //float rotation = Math.atan2(pe.getPos().getY(), fe.getPos().getY());
                        pe.getEntityWorld().spawnEntity(fe);
                    }
                }
            }));

        register_action(new ActionFactory<>(FrostburnOrigins.id("spawn_effect_cloud"), new SerializableData()
            .add("potion", SerializableDataType.registry(Potion.class, Registry.POTION))
            .add("radius_end_size", SerializableDataType.FLOAT)
            .add("radius_start_size", SerializableDataType.FLOAT)
            .add("warm_up_time", SerializableDataType.INT)
            .add("cloud_duration", SerializableDataType.INT),
            (data, entity) -> {
                PlayerEntity pe = (PlayerEntity) entity;

                AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(
                    pe.world, pe.getX(), pe.getY(), pe.getZ());
                areaEffectCloudEntity.setOwner(pe);

                areaEffectCloudEntity.setRadius(data.getFloat("radius_end_size"));
                areaEffectCloudEntity.setRadiusOnUse(data.getFloat("radius_start_size"));
                areaEffectCloudEntity.setWaitTime(data.getInt("warm_up_time"));
                areaEffectCloudEntity.setDuration(data.getInt("cloud_duration"));
                areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / (float)areaEffectCloudEntity.getDuration());
                areaEffectCloudEntity.setPotion(((Potion) data.get("potion")));

                for(StatusEffectInstance statusEffectInstance : ((Potion) data.get("potion")).getEffects()) {
                    areaEffectCloudEntity.addEffect(new StatusEffectInstance(statusEffectInstance));
                }

                pe.world.spawnEntity(areaEffectCloudEntity);

            }));
    }



    private static void register_action(ActionFactory<Entity> actionFactory) {
        Registry.register(ModRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
