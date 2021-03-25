package misteryman.frostburnorigins.common.registry;

import io.github.apace100.origins.power.*;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.HudRender;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import misteryman.frostburnorigins.common.FrostburnOrigins;
import misteryman.frostburnorigins.power.FangCallerPower;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
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
    public static final PowerType<CooldownPower> CHEMICAL_BURN;

    public static final PowerType<Power> WITHERING_VENOM;

    public static final PowerType<Power> FOOLS_BOLD;
    public static final PowerType<Power> SWORD_SWINE;
    public static final PowerType<Power> MARKSHOG;
    public static final PowerType<Power> MIGHTY_AXE;

    public static final PowerType<Power> FROSTBITE;
    public static final PowerType<Power> FROZEN_QUIVER;
    public static final PowerType<Power> FROZEN_HEART;
    public static final PowerType<TogglePower> FREEZE_FRAME;
    public static final PowerType<Power> FREEZE_FRAME_TOGGLE;
    public static final PowerType<VariableIntPower> COLD_SHOULDER_TIMER;

    public static final PowerType<Power> AXE_CRAZY;
    public static final PowerType<Power> CROSSBOW_MASTER;
    public static final PowerType<Power> BRETHREN;
    public static final PowerType<Power> ARMED;

    public static final PowerType<Power> CRACKABLE;
    public static final PowerType<Power> GOLEM;

    public static final PowerType<VariableIntPower> ENDLESS_QUIVER;
    public static final PowerType<Power> FOOD_BARTERING;

    

    static {
        BLAZEBORN = new PowerTypeReference<>(FrostburnOrigins.id("blazeborn"));
        FLAMING_BODY = new PowerTypeReference<>(FrostburnOrigins.id("flaming_body"));
        PHOENIX = new PowerTypeReference<>(FrostburnOrigins.id("phoenix"));
        CHEMICAL_BURN = new PowerTypeReference<>(FrostburnOrigins.id("chemical_burn"));

        WITHERING_VENOM = new PowerTypeReference<>(FrostburnOrigins.id("wither_venom"));

        FOOLS_BOLD = new PowerTypeReference<>(FrostburnOrigins.id("fools_bold"));
        SWORD_SWINE = new PowerTypeReference<>(FrostburnOrigins.id("sword_swine"));
        MARKSHOG = new PowerTypeReference<>(FrostburnOrigins.id("markshog"));
        MIGHTY_AXE = new PowerTypeReference<>(FrostburnOrigins.id("mighty_axe"));

        FROSTBITE = new PowerTypeReference<>(FrostburnOrigins.id("frostbite"));
        FROZEN_QUIVER = new PowerTypeReference<>(FrostburnOrigins.id("frozen_quiver"));
        FROZEN_HEART = new PowerTypeReference<>(FrostburnOrigins.id("frozen_heart"));
        FREEZE_FRAME = new PowerTypeReference<>(FrostburnOrigins.id("freeze_frame"));
        FREEZE_FRAME_TOGGLE = new PowerTypeReference<>(FrostburnOrigins.id("freeze_frame_toggle"));
        COLD_SHOULDER_TIMER = new PowerTypeReference<>(FrostburnOrigins.id("cold_shoulder_timer"));

        AXE_CRAZY = new PowerTypeReference<>(FrostburnOrigins.id("axe_crazy"));
        CROSSBOW_MASTER = new PowerTypeReference<>(FrostburnOrigins.id("crossbow_master"));
        BRETHREN = new PowerTypeReference<>(FrostburnOrigins.id("brethren"));
        ARMED = new PowerTypeReference<>(FrostburnOrigins.id("armed"));

        CRACKABLE = new PowerTypeReference<>(FrostburnOrigins.id("crackable"));
        GOLEM = new PowerTypeReference<>(FrostburnOrigins.id("golem"));

        ENDLESS_QUIVER = new PowerTypeReference<>(FrostburnOrigins.id("endless_quiver"));
        FOOD_BARTERING = new PowerTypeReference<>(FrostburnOrigins.id("food_bartering"));



        register(new PowerFactory<>(FrostburnOrigins.id("jaws"),
                new SerializableData()
                        .add("cooldown", SerializableDataType.INT)
                        .add("speed", SerializableDataType.FLOAT)
                        .add("sound", SerializableDataType.SOUND_EVENT, null)
                        .add("hud_render", SerializableDataType.HUD_RENDER)
                        .add("key", SerializableDataType.KEY, new Active.Key()),
                data -> {
                    SoundEvent soundEvent = (SoundEvent)data.get("sound");
                    return (type, player) -> {
                        ActiveCooldownPower power = new ActiveCooldownPower(type, player,
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
                        power.setKey((Active.Key)data.get("key"));
                        return power;
                    };
                }).allowCondition());

        register(new PowerFactory<>(FrostburnOrigins.id("teleport"),
                new SerializableData()
                        .add("cooldown", SerializableDataType.INT)
                        .add("sound", SerializableDataType.SOUND_EVENT, null)
                        .add("hud_render", SerializableDataType.HUD_RENDER)
                        .add("key", SerializableDataType.KEY, new Active.Key()),
                data -> {
                    SoundEvent soundEvent = (SoundEvent) data.get("sound");
                    return (type, player) -> {
                        ActiveCooldownPower power = new ActiveCooldownPower(type, player,
                            data.getInt("cooldown"),
                            (HudRender) data.get("hud_render"),
                            e -> {
                                if (!e.world.isClient && e instanceof PlayerEntity) {
                                    PlayerEntity p = (PlayerEntity) e;
                                    Vec3d pLookVec = new Vec3d(MathHelper.sin(-p.yaw * 0.017453292F), 1.0F, MathHelper.cos(-p.yaw * 0.017453292F));
                                    Vec3d pPos = new Vec3d(p.getX(), p.getY(), p.getZ());
                                    Vec3d fangPos;
                                    float headYaw = p.headYaw * (1F / 57.295776F);
                                    double y = pPos.y;

                                    EvokerFangsEntity fangs = new EvokerFangsEntity(p.world, pPos.x + (pLookVec.x * 4), pPos.y - p.pitch, pPos.z + (pLookVec.z * 4), headYaw, 1, p);
                                    System.out.println(p.pitch);
                                    p.world.spawnEntity(fangs);
                                }
                            });
                        power.setKey((Active.Key) data.get("key"));
                        return power;
                    };
                }).allowCondition());

        register(new PowerFactory<>(FrostburnOrigins.id("entity_shield"),
                new SerializableData()
                        .add("cooldown", SerializableDataType.INT)
                        .add("speed", SerializableDataType.FLOAT)
                        .add("sound", SerializableDataType.SOUND_EVENT, null)
                        .add("hud_render", SerializableDataType.HUD_RENDER)
                        .add("key", SerializableDataType.KEY, new Active.Key()),
                data -> {
                    SoundEvent soundEvent = (SoundEvent) data.get("sound");
                    return (type, player) -> {
                        ActiveCooldownPower power = new ActiveCooldownPower(type, player,
                            data.getInt("cooldown"),
                            (HudRender) data.get("hud_render"),
                            e -> {
                                if (!e.world.isClient && e instanceof PlayerEntity) {
                                    PlayerEntity p = (PlayerEntity) e;
                                    ItemFrameEntity itemFrame = new ItemFrameEntity(p.world, new BlockPos(p.getX(), p.getY(), p.getZ()), Direction.UP);
                                    p.world.spawnEntity(itemFrame);
                                }
                            });
                    power.setKey((Active.Key) data.get("key"));
                    return power;
                    };
                }).allowCondition());

    }
    private static void register(PowerFactory serializer) {
        Registry.register(ModRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }

    public static void init() {

    }
}
