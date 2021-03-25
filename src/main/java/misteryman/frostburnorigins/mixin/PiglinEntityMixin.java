package misteryman.frostburnorigins.mixin;

import misteryman.frostburnorigins.common.PlayerSpecificBartering;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.PiglinEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PiglinEntity.class)
public class PiglinEntityMixin implements PlayerSpecificBartering {

    private Entity barteringEntity;

    @Override
    public void setBarteringEntity(Entity entity) {
        this.barteringEntity = entity;
    }

    @Override
    public Entity getBarteringEntity() {
        return barteringEntity;
    }
}
