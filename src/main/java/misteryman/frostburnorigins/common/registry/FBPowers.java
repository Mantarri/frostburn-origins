package misteryman.frostburnorigins.common.registry;

import com.google.gson.*;
import io.github.apace100.origins.Origins;
import io.github.apace100.origins.power.*;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.MultiJsonDataLoader;
import misteryman.frostburnorigins.common.FrostburnOrigins;
import misteryman.frostburnorigins.common.ModTags;
import misteryman.frostburnorigins.power.FangCallerPower;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FBPowers extends MultiJsonDataLoader implements IdentifiableResourceReloadListener {
    public static final PowerType<Power> KINGS_SHIELD;
    public static final PowerType<Power> FLAMING_BODY;

    public static final PowerType<Power> WITHERING_VENOM;

    public static final PowerType<Power> FROSTBITE;
    public static final PowerType<Power> FROZEN_QUIVER;
    public static final PowerType<Power> FROZEN_HEART;

    public static final PowerType<Power> THERMOPHOBIC;

    public static final PowerType<Power> AXE_CRAZY;
    public static final PowerType<Power> CROSSBOW_MASTER;
    public static final PowerType<Power> BRETHREN;

    public static final PowerType<Power> CRACKABLE;
    public static final PowerType<AttributePower> WROUGHT_IRON;

    public static final PowerType<CooldownPower> FANG_CALLER;

    static {
        FLAMING_BODY = new PowerTypeReference<>(new Identifier(FrostburnOrigins.MODID, "flaming_body"));

        WITHERING_VENOM = new PowerTypeReference<>(new Identifier(FrostburnOrigins.MODID, "wither_venom"));

        FROSTBITE = register("frostbite", new PowerType<>(Power::new));
        FROZEN_QUIVER = register("frozen_quiver", new PowerType<>(Power::new));
        FROZEN_HEART = register("frozen_heart", new PowerType<>(Power::new));

        THERMOPHOBIC = register("thermophobic", new PowerType<>((type, player) -> new PreventItemUsePower(type, player, (stack -> stack.isFood() && stack.getItem().isIn(ModTags.CONSUMABLE_FIRE)))));

        AXE_CRAZY = register("axe_crazy", new PowerType<>(Power::new));
        CROSSBOW_MASTER = register("crossbow_master", new PowerType<>(Power::new));
        BRETHREN = register("brethren", new PowerType<>(Power::new));

        CRACKABLE = register("crackable", new PowerType<>(Power::new));
        WROUGHT_IRON = register("wrought_iron", new PowerType<>((type, player) -> new AttributePower(type, player, EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier("power_type:wrought_iron", +10, EntityAttributeModifier.Operation.ADDITION))));

        FANG_CALLER = register("fang_caller", new PowerType<>((type, player) -> new ActiveCooldownPower(type, player, 20 * 10, 4, p -> {
            p.world.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.NEUTRAL, 0.75F, 1.0F / (p.getRandom().nextFloat() * 0.4F + 0.8F));

            if(!p.world.isClient) {
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
        })));

        KINGS_SHIELD = register("kings_shield", new PowerType<>((type, player) -> new ActiveCooldownPower(type, player, 20 * 2, 2, p -> {
            ItemFrameEntity itemFrame = new ItemFrameEntity(p.world, new BlockPos(p.getX(), p.getY(), p.getZ()), Direction.UP);
            p.world.spawnEntity(itemFrame);
        })));
    }

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    private HashMap<Identifier, Integer> loadingPriorities = new HashMap<>();

    public PowerTypes() {
        super(GSON, "powers");
    }

    @Override
    protected void apply(Map<Identifier, List<JsonElement>> loader, ResourceManager manager, Profiler profiler) {
        PowerTypeRegistry.reset();
        loadingPriorities.clear();
        loader.forEach((id, jel) -> {
            jel.forEach(je -> {
                try {
                    JsonObject jo = je.getAsJsonObject();
                    Identifier factoryId = Identifier.tryParse(JsonHelper.getString(jo, "type"));
                    Optional<PowerFactory> optionalFactory = ModRegistries.POWER_FACTORY.getOrEmpty(factoryId);
                    if(!optionalFactory.isPresent()) {
                        throw new JsonSyntaxException("Power type \"" + factoryId.toString() + "\" is not defined.");
                    }
                    PowerFactory.Instance factoryInstance = optionalFactory.get().read(jo);
                    PowerType type = new PowerType(id, factoryInstance);
                    int priority = JsonHelper.getInt(jo, "loading_priority", 0);
                    String name = JsonHelper.getString(jo, "name", "");
                    String description = JsonHelper.getString(jo, "description", "");
                    boolean hidden = JsonHelper.getBoolean(jo, "hidden", false);
                    if(hidden) {
                        type.setHidden();
                    }
                    type.setTranslationKeys(name, description);
                    if(!PowerTypeRegistry.contains(id)) {
                        PowerTypeRegistry.register(id, type);
                        loadingPriorities.put(id, priority);
                    } else {
                        if(loadingPriorities.get(id) < priority) {
                            PowerTypeRegistry.register(id, type);
                            loadingPriorities.put(id, priority);
                        }
                    }
                } catch(Exception e) {
                    Origins.LOGGER.error("There was a problem reading power file " + id.toString() + " (skipping): " + e.getMessage());
                }
            });
        });
        loadingPriorities.clear();
        Origins.LOGGER.info("Finished loading powers from data files. Registry contains " + PowerTypeRegistry.size() + " powers.");
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(Origins.MODID, "powers");
    }

    private static <T extends Power> PowerType<T> register(String path, PowerType<T> type) {
        return new PowerTypeReference<>(new Identifier(Origins.MODID, path));
        //return PowerTypeRegistry.register(new Identifier(Origins.MODID, path), type);
    }
}
