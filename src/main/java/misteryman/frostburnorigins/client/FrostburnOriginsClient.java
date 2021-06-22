package misteryman.frostburnorigins.client;

import misteryman.frostburnorigins.client.render.BlazeKingEntityRenderer;
import misteryman.frostburnorigins.client.render.BlazeLimbEntityRenderer;
import misteryman.frostburnorigins.client.render.IceballEntityRenderer;
import misteryman.frostburnorigins.common.FrostburnOrigins;
import misteryman.frostburnorigins.entity.BlazeLimbEntity;
import misteryman.frostburnorigins.entity.IceballEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.impl.networking.ClientSidePacketRegistryImpl;
import net.minecraft.client.MinecraftClient;

import java.util.UUID;

public class FrostburnOriginsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(
            FrostburnOrigins.BLAZE_LIMB,
            (dispatcher, context) -> new BlazeLimbEntityRenderer(dispatcher));

        EntityRendererRegistry.INSTANCE.register(
            FrostburnOrigins.BLAZE_KING,
            (dispatcher, context) -> new BlazeKingEntityRenderer(dispatcher));

        EntityRendererRegistry.INSTANCE.register(
            FrostburnOrigins.ICEBALL,
            (dispatcher, context) -> new IceballEntityRenderer(dispatcher));


        ClientSidePacketRegistryImpl.INSTANCE.register(IceballEntity.SPAWN_PACKET, ((context, buffer) -> {
            double x = buffer.readDouble();
            double y = buffer.readDouble();
            double z = buffer.readDouble();

            int entityID = buffer.readInt();
            UUID entityUUID = buffer.readUuid();

            context.getTaskQueue().execute(() -> {
                IceballEntity projectile = new IceballEntity(MinecraftClient.getInstance().world, x, y, z, entityID, entityUUID);

                MinecraftClient.getInstance().world.addEntity(entityID, projectile);
            });
        }));

        ClientSidePacketRegistryImpl.INSTANCE.register(BlazeLimbEntity.SPAWN_PACKET, ((context, buffer) -> {
            double x = buffer.readDouble();
            double y = buffer.readDouble();
            double z = buffer.readDouble();

            int entityID = buffer.readInt();
            UUID entityUUID = buffer.readUuid();

            context.getTaskQueue().execute(() -> {
                BlazeLimbEntity limb = new BlazeLimbEntity(MinecraftClient.getInstance().world, x, y, z, entityID, entityUUID);

                MinecraftClient.getInstance().world.addEntity(entityID, limb);
            });
        }));
    }
}
