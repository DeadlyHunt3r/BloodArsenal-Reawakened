package com.deadlyhunter.bloodarsenalreawakened.item.heart;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HeartItem extends Item {

    public enum Type {
        CRIMSON("crimson"),
        SANGUINE("sanguine"),
        ASCENSION("ascension");

        public final String id;
        Type(String id) { this.id = id; }
    }

    private final Type type;
    private static final FoodProperties FOOD = new FoodProperties.Builder()
            .nutrition(0).saturationMod(0.0f).alwaysEat().build();

    public static final int MAX_USES = 10;

    public HeartItem(Properties props, Type type) {
        super(props.food(FOOD).stacksTo(16));
        this.type = type;
    }

    public Type getHeartType() { return type; }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            int used = HeartUtils.getUses(player, type);
            if (used >= MAX_USES) {

                player.hurt(player.damageSources().magic(), 10.0F);
                player.displayClientMessage(Component.literal("Don’t be too greedy! You can only eat this heart 10 times."), true);
                return InteractionResultHolder.fail(stack);
            }
        }

        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            int used = HeartUtils.getUses(player, type);
            if (used < MAX_USES) {
                HeartUtils.setUses(player, type, used + 1);
                HeartUtils.applyAllHeartBonuses(player);

                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0));
                player.displayClientMessage(Component.literal(
                        prettyName() + " consumed (" + (used + 1) + "/" + MAX_USES + ")"), true);
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    private String prettyName() {
        return switch (type) {
            case CRIMSON -> "Crimson Heart";
            case SANGUINE -> "Sanguine Heart";
            case ASCENSION -> "Heart of Ascension";
        };
    }
}
