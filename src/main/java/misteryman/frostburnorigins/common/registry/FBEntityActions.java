package misteryman.frostburnorigins.common.registry;

import io.github.apace100.origins.power.factory.action.ActionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import misteryman.frostburnorigins.common.FrostburnOrigins;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FBEntityActions {
    public static void register(){
        register_action(new ActionFactory<>(new Identifier("frostburnorigins:change_held_item"), new SerializableData(),
                (data, entity) -> {
                    if(entity instanceof PlayerEntity) {
                        PlayerEntity pe = (PlayerEntity) entity;
                        int numOfItems = pe.getStackInHand(Hand.MAIN_HAND).getCount();
                        pe.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.ARROW, numOfItems));
                    }
                }));
    }

    private static void register_action(ActionFactory<Entity> actionFactory) {
        Registry.register(ModRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
