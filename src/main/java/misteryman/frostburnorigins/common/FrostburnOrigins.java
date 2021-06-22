package misteryman.frostburnorigins.common;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import misteryman.frostburnorigins.common.registry.FBEntityActions;
import misteryman.frostburnorigins.common.registry.FBTags;
import misteryman.frostburnorigins.common.registry.FBPowers;
import misteryman.frostburnorigins.common.registry.FBStatusEffects;
import misteryman.frostburnorigins.config.ModConfig;
import misteryman.frostburnorigins.entity.BlazeKingEntity;
import misteryman.frostburnorigins.entity.BlazeLimbEntity;
import misteryman.frostburnorigins.entity.IceballEntity;
import misteryman.frostburnorigins.items.IceballItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FrostburnOrigins implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(FrostburnOrigins.class);
    public static final String MODID = "frostburnorigins";
    public static boolean configRegistered = false;

    public static final EntityType<BlazeLimbEntity> BLAZE_LIMB = Registry.register(
            Registry.ENTITY_TYPE,
            id("blaze_limb"),
            FabricEntityTypeBuilder.<BlazeLimbEntity>create(SpawnGroup.CREATURE, BlazeLimbEntity::new).dimensions(EntityDimensions.fixed(0.75F, 0.75F)).build()
    );
    public static final EntityType<BlazeKingEntity> BLAZE_KING = Registry.register(
            Registry.ENTITY_TYPE,
            id("blaze_king"),
            FabricEntityTypeBuilder.<BlazeKingEntity>create(SpawnGroup.MONSTER, BlazeKingEntity::new).dimensions(EntityDimensions.fixed(0.75F, 0.75F)).build()
    );

    public static final EntityType<IceballEntity> ICEBALL = Registry.register(
            Registry.ENTITY_TYPE,
            id("iceball"),
            FabricEntityTypeBuilder.<IceballEntity>create(SpawnGroup.MISC, (type, world) -> new IceballEntity(type, world)).dimensions(EntityDimensions.fixed(0.25f, 0.25f)).trackRangeBlocks(64).trackedUpdateRate(10).forceTrackedVelocityUpdates(true).build()
    );



    public static final Item ICEBALL_ITEM = new IceballItem(new FabricItemSettings().group(ItemGroup.COMBAT));

    public static Identifier id(String id) {
        return(new Identifier(MODID, id));
    }

    @Override
    public void onInitialize() {
        if(!configRegistered) {
            AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
            configRegistered = true;
        }

        Registry.register(Registry.ITEM, id("iceball"), ICEBALL_ITEM);

        FabricDefaultAttributeRegistry.register(BLAZE_KING, BlazeKingEntity.createMobAttributes());

        FBStatusEffects.init();

        FBTags.init();

        FBPowers.init();

        FBEntityActions.register();
    }
}
