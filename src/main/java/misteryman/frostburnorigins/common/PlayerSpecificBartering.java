package misteryman.frostburnorigins.common;

import net.minecraft.entity.Entity;

public interface PlayerSpecificBartering {

    void setBarteringEntity(Entity entity);
    Entity getBarteringEntity();
}
