package misteryman.frostburnorigins.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class BlazeLimbEntity extends PathAwareEntity {
    public BlazeLimbEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }
    /*
    @Override
    public boolean damage() {
        return false;
    }
     */
}
