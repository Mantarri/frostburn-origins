package misteryman.frostburnorigins.client.render;

import misteryman.frostburnorigins.client.models.BlazeLimbEntityModel;
import misteryman.frostburnorigins.common.FrostburnOrigins;
import misteryman.frostburnorigins.entity.BlazeLimbEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class BlazeLimbEntityRenderer extends MobEntityRenderer<BlazeLimbEntity, BlazeLimbEntityModel> {
    public BlazeLimbEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new BlazeLimbEntityModel(), 0.0F);
    }

    @Override
    public Identifier getTexture(BlazeLimbEntity entity) {
        return(FrostburnOrigins.id("textures/blaze_limb.png"));
    }
}
