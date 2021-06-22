package misteryman.frostburnorigins.common.registry;

import misteryman.frostburnorigins.common.FrostburnOrigins;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public class FBTags {
    public static final Tag<Item> PIGLIN_FOOD_BARTERING_ITEMS = TagRegistry.item(FrostburnOrigins.id("piglin_food_bartering_items"));
    public static final Tag<Item> GOLD_ARMOR = TagRegistry.item(FrostburnOrigins.id("gold_armour"));
    public static final Tag<Item> CONSUMABLE_FIRE = TagRegistry.item(FrostburnOrigins.id("consumable_fire"));

    public static void init() {

    }
}
