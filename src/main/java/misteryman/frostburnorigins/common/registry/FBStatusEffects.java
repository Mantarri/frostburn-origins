package misteryman.frostburnorigins.common.registry;

import misteryman.frostburnorigins.common.FrostburnOrigins;
import misteryman.frostburnorigins.status_effects.FrostbiteStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FBStatusEffects {
    public static final StatusEffect FROSTBITE;
    public static final StatusEffectInstance FROSTBITE_INSTANCE;
    public static final Potion FROSTBITE_POTION;

    static {
        FROSTBITE = registerStatusEffect("frostbite", new FrostbiteStatusEffect());
        FROSTBITE_INSTANCE = registerStatusEffectInstance(FROSTBITE, 1800, 1);
        FROSTBITE_POTION = registerPotion("frostbite", FROSTBITE_INSTANCE);
    }

    public static StatusEffect registerStatusEffect(String id, StatusEffect entry) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(FrostburnOrigins.MODID, id), entry);
    }
    public static StatusEffectInstance registerStatusEffectInstance(StatusEffect effect, int duration, int amplifier) {
        return new StatusEffectInstance(effect, duration, amplifier);
    }
    public static Potion registerPotion(String id, StatusEffectInstance entry) {
        return Registry.register(Registry.POTION, new Identifier(FrostburnOrigins.MODID, id), new Potion(id, entry));
    }

    public static void init() {

    }
}
