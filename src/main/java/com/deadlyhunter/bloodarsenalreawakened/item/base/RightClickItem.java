package com.deadlyhunter.bloodarsenalreawakened.item.base;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class RightClickItem extends Item {
    protected int cooldown = 0; // in Ticks (20 = 1s)
    public RightClickItem(Properties props, int cooldown) { super(props.stacksTo(1)); this.cooldown = cooldown; }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            onUseServer(level, player, stack);
            if (cooldown > 0) player.getCooldowns().addCooldown(this, cooldown);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    protected abstract void onUseServer(Level level, Player player, ItemStack stack);
}
