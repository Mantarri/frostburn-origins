package misteryman.frostburnorigins.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;

import java.util.ArrayList;
import java.util.List;

public class RaycastUtils {
    public static List<HitResult> traceBetweenPlayer(PlayerEntity pe, double maxDistance, float tickDelta, boolean includeFluids) {
        List<HitResult> results = new ArrayList<>();
        for(double i = maxDistance; i > 1; i--) {
            HitResult hr = pe.raycast(i, tickDelta, includeFluids);
            results.add(hr);
        }
        return results;
    }
}
