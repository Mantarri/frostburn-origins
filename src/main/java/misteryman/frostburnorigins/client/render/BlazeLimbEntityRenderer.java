package misteryman.frostburnorigins.client.render;

import misteryman.frostburnorigins.client.models.BlazeLimbEntityModel;
import misteryman.frostburnorigins.common.FrostburnOrigins;
import misteryman.frostburnorigins.entity.BlazeLimbEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BlazeLimbEntityRenderer extends EntityRenderer<BlazeLimbEntity> {

    public final BlazeLimbEntityModel MODEL = new BlazeLimbEntityModel();

    public BlazeLimbEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Override
    public void render(BlazeLimbEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        this.MODEL.setAngles(entity, tickDelta, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.MODEL.getLayer(this.getTexture(entity)));
        this.MODEL.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(BlazeLimbEntity entity) {
        return(FrostburnOrigins.id("textures/blaze_limb.png"));
    }
}
