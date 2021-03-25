package misteryman.frostburnorigins.items;

import misteryman.frostburnorigins.common.FrostburnOrigins;
import misteryman.frostburnorigins.entity.IceballEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class IceballItem extends Item {

    public IceballItem(Settings settings) { super(settings); }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(
            null,
            user.getX(),
            user.getY(),
            user.getZ(),
            SoundEvents.ENTITY_SNOWBALL_THROW,
            SoundCategory.NEUTRAL,
            0.5F,
            1 / (RANDOM.nextFloat() * 0.4F  + 0.8F));
        if(!world.isClient) {
            IceballEntity projectile = new IceballEntity(FrostburnOrigins.ICEBALL, world);
            projectile.setPos(user.getX(), user.getY(), user.getZ());
            projectile.setOwner(user);
            projectile.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 0.25F);
            if(world.spawnEntity(projectile)) {
                FrostburnOrigins.LOGGER.warn("LET'S THROOOOW!!!");
            }
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if(!user.abilities.creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient);
    }
}
