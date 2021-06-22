package misteryman.frostburnorigins.entity;

import io.netty.buffer.Unpooled;
import misteryman.frostburnorigins.common.FrostburnOrigins;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class BlazeLimbEntity extends Entity {

    public static final Identifier SPAWN_PACKET = FrostburnOrigins.id("blaze_limb");

    public BlazeLimbEntity(EntityType<? extends BlazeLimbEntity> entityType, World world) {
        super(entityType, world);
    }

    public BlazeLimbEntity(World world, double x, double y, double z, int id, UUID uuid) {
        this(FrostburnOrigins.BLAZE_LIMB, world);
        this.updatePosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        updateTrackedPosition(x, y, z);
        setEntityId(id);
        setUuid(uuid);
    }

    private UUID ownerUuid = null;

    public void setOwnerUuid(UUID uuid) {
        ownerUuid = uuid;
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public void tick() {
        this.updatePosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        updateTrackedPosition(x, y, z);
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag tag) {

    }

    @Override
    protected void writeCustomDataToTag(CompoundTag tag) {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());

        // entity position
        packet.writeDouble(getX());
        packet.writeDouble(getY());
        packet.writeDouble(getZ());

        // entity id & uuid
        packet.writeInt(getEntityId());
        packet.writeUuid(getUuid());

        return ServerSidePacketRegistry.INSTANCE.toPacket(SPAWN_PACKET, packet);
    }
}
