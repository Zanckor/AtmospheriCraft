package dev.zanckor.atmosphericraft.core.item.food;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FrozenFood extends Item {
    public FrozenFood(Properties properties) {
        super(properties);
    }

    //TODO: Apply freezing effect
    //Add a little % to give hunger to player
    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, (int) (Mth.randomBetween(RandomSource.createNewThreadLocalInstance(), 4, 20)) * 20, 0));

        return super.finishUsingItem(itemStack, level, livingEntity);
    }
}
