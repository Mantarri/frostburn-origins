package misteryman.frostburnorigins.client;

import misteryman.frostburnorigins.client.render.BlazeKingEntityRenderer;
import misteryman.frostburnorigins.client.render.BlazeLimbEntityRenderer;
import misteryman.frostburnorigins.common.FrostburnOrigins;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class FrostburnOriginsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(FrostburnOrigins.BLAZE_LIMB, (dispatcher, context) -> {
            return new BlazeLimbEntityRenderer(dispatcher);
        });

        EntityRendererRegistry.INSTANCE.register(FrostburnOrigins.BLAZE_KING, (dispatcher, context) -> {
            return new BlazeKingEntityRenderer(dispatcher);
        });
    }
}
