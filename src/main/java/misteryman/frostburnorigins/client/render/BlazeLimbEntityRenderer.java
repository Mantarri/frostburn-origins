package misteryman.frostburnorigins.client.render;

import misteryman.frostburnorigins.client.models.BlazeLimbEntityModel;
import misteryman.frostburnorigins.entity.BlazeLimbEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class BlazeLimbEntityRenderer extends EntityRenderer<BlazeLimbEntity> {
    public BlazeLimbEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Override
    public Identifier getTexture(BlazeLimbEntity entity) {
        return null;
    }
}
