package com.deadlyhunter.bloodarsenalreawakened.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class TooltipItem extends Item {
    private final String tooltipKey;

    public TooltipItem(Properties props, String tooltipKey) {
        super(props);
        this.tooltipKey = tooltipKey;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component
                .translatable("tooltip.blood_arsenal_reawakened." + tooltipKey + ".desc")
        );
    }
}
