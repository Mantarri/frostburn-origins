package misteryman.frostburnorigins.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class BlazeKingEntity extends PathAwareEntity {
    public BlazeKingEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    /*
    public static DefaultAttributeContainer.Builder createLivingAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15D).add(EntityAttributes.GENERIC_MAX_HEALTH, 90.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D);
    }

     */
}
