package misteryman.frostburnorigins.client.render;

import misteryman.frostburnorigins.common.FrostburnOrigins;
import misteryman.frostburnorigins.entity.IceballEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class IceballEntityRenderer extends EntityRenderer<IceballEntity> {

    public static final ItemStack STACK = new ItemStack(FrostburnOrigins.ICEBALL_ITEM);

    public IceballEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(IceballEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(entity.age));
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                STACK,
                ModelTransformation.Mode.FIXED,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers
        );

        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(IceballEntity entity) {
        return null;
    }
}
