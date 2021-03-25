package misteryman.frostburnorigins.client.render;

import misteryman.frostburnorigins.client.models.BlazeKingEntityModel;
import misteryman.frostburnorigins.common.FrostburnOrigins;
import misteryman.frostburnorigins.entity.BlazeKingEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class BlazeKingEntityRenderer extends MobEntityRenderer<BlazeKingEntity, BlazeKingEntityModel> {
    public BlazeKingEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new BlazeKingEntityModel(), 0.0F);
    }

    @Override
    public Identifier getTexture(BlazeKingEntity entity) {
        return(FrostburnOrigins.id("textures/blaze_limb.png"));
    }
}
