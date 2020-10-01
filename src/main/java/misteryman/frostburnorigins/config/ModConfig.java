package misteryman.frostburnorigins.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import misteryman.frostburnorigins.common.ConfigFoodItem;
import misteryman.frostburnorigins.common.FrostburnOrigins;

import java.util.ArrayList;
import java.util.Arrays;

@Config(name = FrostburnOrigins.MODID)
public class ModConfig implements ConfigData {
    public ArrayList<ConfigFoodItem> hot_food = new ArrayList<ConfigFoodItem>(Arrays.asList(
            new ConfigFoodItem("minecraft:magma_cream", 6, 6F),
            new ConfigFoodItem("minecraft:blaze_rod", 6, 6F),
            new ConfigFoodItem("minecraft:blaze_powder", 3, 3F)
    ));

    @ConfigEntry.Gui.CollapsibleObject
    InnerStuff stuff = new InnerStuff();

    static class InnerStuff {
           int a = 0;
           int b = 1;
    }
}
