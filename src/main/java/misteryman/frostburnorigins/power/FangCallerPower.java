package misteryman.frostburnorigins.power;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class FangCallerPower {
    public double getValidHeight(double height, PlayerEntity p, Vec3d fangPos) {
        int increase = +1;
        int decrease = -1;

        if(
            p.world.getBlockState(new BlockPos(fangPos.x, height, fangPos.z)).isSolidBlock(p.world, new BlockPos(fangPos.x, height, fangPos.z)) ||
            p.world.getBlockState(new BlockPos(fangPos.x, height + 1, fangPos.z)).isSolidBlock(p.world, new BlockPos(fangPos.x, height, fangPos.z)) ||
            !p.world.getBlockState(new BlockPos(fangPos.x, height - 1, fangPos.z)).isSolidBlock(p.world, new BlockPos(fangPos.x, height, fangPos.z))) {
            boolean validHeightFound = false;
            while(!validHeightFound) {
                if(
                    !p.world.getBlockState(new BlockPos(fangPos.x, (height + increase), fangPos.z)).isSolidBlock(p.world, new BlockPos(fangPos.x, height, fangPos.z)) &&
                    !p.world.getBlockState(new BlockPos(fangPos.x, (height + increase) + 1, fangPos.z)).isSolidBlock(p.world, new BlockPos(fangPos.x, height, fangPos.z)) &&
                    p.world.getBlockState(new BlockPos(fangPos.x, (height + increase) - 1, fangPos.z)).isSolidBlock(p.world, new BlockPos(fangPos.x, height, fangPos.z))) {
                    validHeightFound = true;
                    height += increase;
                }else{
                    increase +=1;
                }
                if(!validHeightFound) {
                    if(
                        !p.world.getBlockState(new BlockPos(fangPos.x, (height + decrease), fangPos.z)).isSolidBlock(p.world, new BlockPos(fangPos.x, height, fangPos.z)) &&
                        !p.world.getBlockState(new BlockPos(fangPos.x, (height + decrease) + 1, fangPos.z)).isSolidBlock(p.world, new BlockPos(fangPos.x, height, fangPos.z)) &&
                        p.world.getBlockState(new BlockPos(fangPos.x, (height + decrease) - 1, fangPos.z)).isSolidBlock(p.world, new BlockPos(fangPos.x, height, fangPos.z))) {
                        validHeightFound = true;
                        height += decrease;
                    }else{
                        decrease -=1;
                    }
                }
            }
        }

        return(height);
    }
}
