package misteryman.frostburnorigins.mixin;

import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;


@Mixin(PiglinBrain.class)
public interface PiglinBrainInvoker {
    @Invoker()
    static boolean invokeAcceptsForBarter(Item item) {
        throw new AssertionError();
    }

    @Invoker()
    static void invokeDoBarter(PiglinEntity piglin, List<ItemStack> list) {
        throw new AssertionError();
    }
}
