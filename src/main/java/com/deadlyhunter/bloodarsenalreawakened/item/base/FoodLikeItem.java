package com.deadlyhunter.bloodarsenalreawakened.item.base;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FoodLikeItem extends Item {
    public FoodLikeItem(Properties props, FoodProperties food) {
        super(props.food(food));
    }
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {

        if (!level.isClientSide && entity instanceof Player p) {
            p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0));
        }
        return super.finishUsingItem(stack, level, entity);
    }
}
