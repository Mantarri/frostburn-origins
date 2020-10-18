package misteryman.frostburnorigins.client.models;

import misteryman.frostburnorigins.entity.BlazeKingEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class BlazeKingEntityModel extends EntityModel<BlazeKingEntity> {
    private final ModelPart base;

    public BlazeKingEntityModel() {
        this.textureHeight = 64;
        this.textureWidth = 16;

        base = new ModelPart(this, 0, 0);
        base.addCuboid(-6, -6, -6, 12, 12, 12);
    }

    @Override
    public void setAngles(BlazeKingEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.translate(0, 1.125, 0);

        base.render(matrices, vertices, light, overlay);
    }
}
