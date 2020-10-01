package misteryman.frostburnorigins.common;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class ModTags {
    public static final Tag<Item> CONSUMABLE_FIRE = TagRegistry.item(new Identifier(FrostburnOrigins.MODID, "consumable_fire"));

    public static void register() {

    }
}
