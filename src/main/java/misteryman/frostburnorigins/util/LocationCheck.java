package misteryman.frostburnorigins.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LocationCheck {
    public static boolean fitsPlayer(World world, Vec3d vec3d, Tag<Block> unSafeTargetBlocks) {
        Block legs = world.getBlockState(new BlockPos(vec3d.getX(), vec3d.getY() - 1, vec3d.getZ())).getBlock();
        Block torso = world.getBlockState(new BlockPos(vec3d.getX(), vec3d.getY(), vec3d.getZ())).getBlock();
        Block standingOn = world.getBlockState(new BlockPos(vec3d.getX(), vec3d.getY() - 1, vec3d.getZ())).getBlock();

        for (Block iBlock : unSafeTargetBlocks.values()) {
            if (standingOn.equals(iBlock)) {
                return false;
            }
        }

        return(torso.equals(Blocks.AIR) || torso.equals(Blocks.CAVE_AIR) && (legs.equals(Blocks.AIR) || legs.equals(Blocks.CAVE_AIR)));
    }

    public static float getDistance(Vec3d point1, Vec3d point2) {
        return(new Float(Math.sqrt(Math.pow(point2.getX() - point1.getX(), 2)) +
                Math.pow(point2.getY() - point1.getY(), 2) +
                Math.pow(point2.getZ() - point1.getZ(), 2) * 1.0));
    }
}
