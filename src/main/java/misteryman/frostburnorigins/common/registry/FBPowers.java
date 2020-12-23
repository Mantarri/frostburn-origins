package misteryman.frostburnorigins.common.registry;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.power.*;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.HudRender;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import misteryman.frostburnorigins.common.FrostburnOrigins;
import io.github.apace100.origins.power.factory.PowerFactories;
import misteryman.frostburnorigins.power.FangCallerPower;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

public class FBPowers {
    //public static final PowerType<Power> KINGS_SHIELD;
    public static final PowerType<Power> BLAZEBORN;
    public static final PowerType<Power> FLAMING_BODY;
    public static final PowerType<CooldownPower> PHOENIX;

    public static final PowerType<Power> WITHERING_VENOM;

    public static final PowerType<Power> FROSTBITE;
    public static final PowerType<Power> FROZEN_QUIVER;
    public static final PowerType<Power> FROZEN_HEART;

    public static final PowerType<Power> AXE_CRAZY;
    public static final PowerType<Power> CROSSBOW_MASTER;
    public static final PowerType<Power> BRETHREN;

    public static final PowerType<Power> CRACKABLE;

    //public static final PowerType<CooldownPower> EARTHEN_MAW;

    static {
        BLAZEBORN = new PowerTypeReference<>(new Identifier(FrostburnOrigins.MODID, "blazeborn"));
        FLAMING_BODY = new PowerTypeReference<>(new Identifier(FrostburnOrigins.MODID, "flaming_body"));
        PHOENIX = new PowerTypeReference<>(new Identifier(FrostburnOrigins.MODID, "phoenix"));

        WITHERING_VENOM = new PowerTypeReference<>(new Identifier(FrostburnOrigins.MODID, "wither_venom"));

        FROSTBITE = new PowerTypeReference<>(new Identifier(FrostburnOrigins.MODID, "frostbite"));
        FROZEN_QUIVER = new PowerTypeReference<>(new Identifier(FrostburnOrigins.MODID, "frozen_quiver"));
        FROZEN_HEART = new PowerTypeReference<>(new Identifier(FrostburnOrigins.MODID, "frozen_heart"));

        AXE_CRAZY = new PowerTypeReference<>(new Identifier(FrostburnOrigins.MODID, "axe_crazy"));
        CROSSBOW_MASTER = new PowerTypeReference<>(new Identifier(FrostburnOrigins.MODID, "crossbow_master"));
        BRETHREN = new PowerTypeReference<>(new Identifier(FrostburnOrigins.MODID, "brethren"));

        CRACKABLE = new PowerTypeReference<>(new Identifier(FrostburnOrigins.MODID, "crackable"));



        register(new PowerFactory<>(new Identifier(FrostburnOrigins.MODID, "jaws"),
                new SerializableData()
                        .add("cooldown", SerializableDataType.INT)
                        .add("speed", SerializableDataType.FLOAT)
                        .add("sound", SerializableDataType.SOUND_EVENT, null)
                        .add("hud_render", SerializableDataType.HUD_RENDER),
                data -> {
                    SoundEvent soundEvent = (SoundEvent)data.get("sound");
                    return (type, player) -> new ActiveCooldownPower(type, player,
                            data.getInt("cooldown"),
                            (HudRender)data.get("hud_render"),
                            e -> {
                                if(!e.world.isClient && e instanceof PlayerEntity) {
                                    PlayerEntity p = (PlayerEntity)e;
                                    Vec3d pLookVec = new Vec3d(MathHelper.sin(-p.yaw * 0.017453292F), 1.0F, MathHelper.cos(-p.yaw * 0.017453292F));
                                    Vec3d pPos = new Vec3d(p.getX(), p.getY(), p.getZ());
                                    Vec3d fangPos;
                                    float headYaw = p.headYaw * (1F / 57.295776F);
                                    double y = pPos.y;

                                    for(int i = 0, j = 2; i < 6; i++, j+= 2) {
                                        fangPos = new Vec3d(pPos.x + (pLookVec.x * j), y, pPos.z + (pLookVec.z * j));
                                        FangCallerPower fangCallerPower = new FangCallerPower();
                                        y = fangCallerPower.getValidHeight(y, p, fangPos);
                                        EvokerFangsEntity fangs = new EvokerFangsEntity(p.world, pPos.x + (pLookVec.x * j), y, pPos.z + (pLookVec.z * j), headYaw, 1, p);
                                        p.world.spawnEntity(fangs);
                                    }
                                }
                            });
                }).allowCondition());

        register(new PowerFactory<>(new Identifier(FrostburnOrigins.MODID, "entity_shield"),
                new SerializableData()
                        .add("cooldown", SerializableDataType.INT)
                        .add("speed", SerializableDataType.FLOAT)
                        .add("sound", SerializableDataType.SOUND_EVENT, null)
                        .add("hud_render", SerializableDataType.HUD_RENDER),
                data -> {
                    SoundEvent soundEvent = (SoundEvent)data.get("sound");
                    return (type, player) -> new ActiveCooldownPower(type, player,
                            data.getInt("cooldown"),
                            (HudRender)data.get("hud_render"),
                            e -> {
                                if(!e.world.isClient && e instanceof PlayerEntity) {
                                    PlayerEntity p = (PlayerEntity)e;
                                    ItemFrameEntity itemFrame = new ItemFrameEntity(p.world, new BlockPos(p.getX(), p.getY(), p.getZ()), Direction.UP);
                                    p.world.spawnEntity(itemFrame);
                                }
                            });
                }).allowCondition());
    }
    private static void register(PowerFactory serializer) {
        Registry.register(ModRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }

    public static void init() {

    }
}
