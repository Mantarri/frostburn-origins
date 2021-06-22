package misteryman.frostburnorigins.util;

import misteryman.frostburnorigins.common.FrostburnOrigins;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class Teleport {

    public static void simple(Vec3d vec3d, PlayerEntity pe) {
        pe.swingHand(Hand.MAIN_HAND);
        pe.teleport(vec3d.getX(), vec3d.getY(), vec3d.getZ());
        pe.world.sendEntityStatus(pe, (byte)46);
        pe.world.playSound(null,
                pe.getX(),
                pe.getY(),
                pe.getZ(),
                SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.PLAYERS,
                1.5F,
                0.25F);
    }

    public static boolean towardDir(PlayerEntity pe, double range) {
        Vec3d pPos = pe.getPos();
        Vec3d eye = Util.getEyePos(pe);
        Vec3d look = pe.getRotationVector();
        double pHeight = pe.getHeightOffset();
        double lookComp = -look.getY() * pHeight;
        Vec3d sample = look;

        sample = sample.multiply(new Vec3d(range, range, range));
        sample = sample.add(eye);

        Vec3d eye3 = new Vec3d(eye.getX(), eye.getY(), eye.getZ());
        Vec3d end = new Vec3d(sample.getX(), sample.getY(), sample.getZ());

        double maxDistance = range + lookComp;

        HitResult hitResult = pe.raycast(eye3.distanceTo(end), MinecraftClient.getInstance().getTickDelta(), false);
        if(hitResult.getType().equals(HitResult.Type.MISS)) {
            for(double i = maxDistance; i > 1; i--) {
                sample = look;
                sample = sample.multiply(new Vec3d(i, i, i));
                sample = sample.add(eye);
                sample = sample.add(new Vec3d(0, -pHeight, 0));

                if(towardAroundDir(pe, sample, false, maxDistance)) {
                    return true;
                }
            }
            return false;
        } else if (hitResult.getType().equals(HitResult.Type.BLOCK)) {
            List<HitResult> results = RaycastUtils.traceBetweenPlayer(
                pe,
                pe.getPos().distanceTo(end),
                MinecraftClient.getInstance().getTickDelta(),
                false
            );

            for(HitResult pos : results) {
                if(!pos.getType().equals(HitResult.Type.MISS)) {
                    if(pos.getType().equals(HitResult.Type.BLOCK)) {
                        BlockPos bPos = new BlockPos(pos.getPos());
                        maxDistance = Math.min(
                            maxDistance,
                            eye.distanceTo(new Vec3d(bPos.getX(), bPos.getY(), bPos.getZ())) - 1.5 - lookComp);
                    } else {
                        FrostburnOrigins.LOGGER.warn("I have no purpose. ~ Teleport 82:0");
                    }
                }
            }

            Vec3d targetBc = pe.getPos();
            double sampleDistance = 1.5;
            double teleDistance = eye.distanceTo(pPos) + sampleDistance;

            while(teleDistance < maxDistance) {
                sample = look;
                sample = sample.multiply(new Vec3d(sampleDistance, sampleDistance, sampleDistance));
                sample = sample.add(targetBc);
                sample = sample.add(new Vec3d(0, -pHeight, 0));

                if(towardAroundDir(pe, sample, false, maxDistance)) {
                    return true;
                }

                teleDistance++;
                sampleDistance++;
            }
            sampleDistance = -0.5;
            teleDistance = eye.distanceTo(pPos) + sampleDistance;
            while(teleDistance > 1) {
                sample = look;
                sample = sample.multiply(new Vec3d(sampleDistance, sampleDistance, sampleDistance));
                sample = sample.add(targetBc);
                sample = sample.add(new Vec3d(0, -pHeight, 0));

                if(towardAroundDir(pe, sample, false, maxDistance)) {
                    return true;
                }

                sampleDistance--;
                teleDistance--;
            }
        }

        return false;
    }

    public static boolean towardAroundDir(PlayerEntity pe, Vec3d sample, boolean conserveMomentum, double maxDistance) {
        if(
            towardDir(pe,
            new BlockPos(Math.floor(sample.getX()), Math.floor(sample.getY()) - 1, Math.floor(sample.getZ())),
            conserveMomentum,
                    maxDistance)
        ) {
            return true;
        }

        if(
            towardDir(pe,
                new BlockPos(Math.floor(sample.getX()), Math.floor(sample.getY()), Math.floor(sample.getZ())),
                conserveMomentum,
                maxDistance)
        ) {
            return true;
        }

        if(
            towardDir(pe,
                new BlockPos(Math.floor(sample.getX()), Math.floor(sample.getY()) + 1, Math.floor(sample.getZ())),
                conserveMomentum,
                maxDistance)
        ) {
            return true;
        }

        //If no valid position found
        return false;
    }

    public static boolean towardDir(PlayerEntity pe, BlockPos bPos, boolean conserveMomentum, double maxDistance) {
        return goToLocation(pe, bPos, conserveMomentum, maxDistance);
    }

    public static boolean goToLocation(PlayerEntity pe, BlockPos blockPos, boolean conserveMomentum, double maxDistance) {
        if(!isInTargetRange(pe, new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), maxDistance)) {
            return false;
        }

        if(!canTeleportTo(pe, new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), pe.getEntityWorld())) {
            return false;
        }

        pe.swingHand(Hand.MAIN_HAND);
        if(doClientTeleport(pe, new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), conserveMomentum)) {
            pe.world.sendEntityStatus(pe, (byte)46);
            pe.world.playSound(null,
                    pe.getX(),
                    pe.getY(),
                    pe.getZ(),
                    SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                    SoundCategory.PLAYERS,
                    1.5F,
                    0.25F);
        }
        return true;
    }

    private static boolean isInTargetRange(PlayerEntity pe, Vec3d pos, double maxDistance) {
        return getDistanceSquared(pe, pos) <= maxDistance;
    }

    private static double getDistanceSquared(PlayerEntity pe, Vec3d pos) {
        Vec3d eye = Util.getEyePos(pe);
        return eye.distanceTo(pos);
    }

    private static boolean canTeleportTo(PlayerEntity pe, Vec3d pos, World world) {
        if(pos.getY() < 1) {
            return false;
        }
        Vec3d start = Util.getEyePos(pe);

        if(!canBlinkTo(pos, world, start, pos, pe)) {
            return false;
        }

        BlockState bs = world.getBlockState(new BlockPos(pos));
        Block block = bs.getBlock();

        if(bs.isAir()) {
            return true;
        }
        final Box box = bs.getCollisionShape(world, new BlockPos(pos)).getBoundingBox();
        return box.getAverageSideLength() < 0.7;
    }

    private static boolean canBlinkTo(Vec3d pos, World world, Vec3d start, Vec3d target, PlayerEntity pe) {
        HitResult p = pe.raycast(start.distanceTo(target), MinecraftClient.getInstance().getTickDelta(), false);
        if(p.getType().equals(HitResult.Type.BLOCK)) {
            BlockPos bPos = new BlockPos(p.getPos());
            BlockState bs = world.getBlockState(bPos);
            Block block = bs.getBlock();
            if(isClear(world, bs, block, bPos)) {
                if(bPos.equals(new BlockPos(pos))) {
                    return true;
                }

                Vec3d sv = new Vec3d(start.getX(), start.getY(), start.getZ());
                Vec3d rayDir = new Vec3d(target.getX(), target.getY(), target.getZ());
                rayDir = rayDir.subtract(sv);
                rayDir = rayDir.normalize();
                rayDir = rayDir.add(sv);
                return canBlinkTo(pos, world, new Vec3d(rayDir.getX(), rayDir.getY(), rayDir.getZ()), target, pe);

            } else {
                return false;
            }

        }
        return true;
    }

    private static boolean isClear(World w, BlockState bs, Block block, BlockPos bPos) {
        if(bs.isAir()) {
            return true;
        }
        final Box box = bs.getCollisionShape(w, bPos).getBoundingBox();
        if(box.getAverageSideLength() < 0.7) {
            return true;
        }

        return bs.getAmbientOcclusionLightLevel(w, bPos) < 2;
    }

    private static boolean doClientTeleport(Entity entity, Vec3d pos, boolean conserveMomentum) {
        //Pre-teleport position
        Vec3d prePos = entity.getPos();
        entity.teleport(pos.getX(), pos.getY(), pos.getZ());

        return !prePos.equals(entity.getPos());
    }
}
