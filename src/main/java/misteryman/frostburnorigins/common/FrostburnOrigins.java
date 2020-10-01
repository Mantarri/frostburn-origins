package misteryman.frostburnorigins.common;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;
import misteryman.frostburnorigins.common.registry.FBPowers;
import misteryman.frostburnorigins.config.ModConfig;
import misteryman.frostburnorigins.entity.BlazeLimbEntity;
import misteryman.frostburnorigins.power.FallFlyingPower;
import net.adriantodt.fallflyinglib.FallFlyingLib;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FrostburnOrigins implements ModInitializer {
    public static final String MODID = "frostburnorigins";
    public static boolean configRegistered = false;
    public static final EntityType<BlazeLimbEntity> BLAZE_LIMB = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("frostburnorigins", "blaze_limb"),
            FabricEntityTypeBuilder.<BlazeLimbEntity>create(SpawnGroup.CREATURE, BlazeLimbEntity::new).dimensions(EntityDimensions.fixed(0.75F, 0.75F)).build()
    );

    @Override
    public void onInitialize() {
        if(!configRegistered) {
            AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
            configRegistered = true;
        }

        ModTags.register();
        FBPowers.init();
        FallFlyingLib.registerAccessor((le) -> new FallFlyingPower(le));

    }
}