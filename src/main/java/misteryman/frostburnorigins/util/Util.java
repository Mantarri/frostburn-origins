package misteryman.frostburnorigins.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class Util {
	public static Vec3d getEyePos(LivingEntity le) {
		return new Vec3d(le.getPos().getX(), le.getEyeY(), le.getPos().getZ());
	}
}
