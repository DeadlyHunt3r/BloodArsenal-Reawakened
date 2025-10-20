package com.deadlyhunter.bloodarsenalreawakened.item.sigil;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DivinitySigilItem extends SigilBaseItem.Toggleable {
    public DivinitySigilItem(Item.Properties props) {
        super(props.stacksTo(1));
    }

    public static boolean isActiveOn(Player player) {

        for (ItemStack stack : new ItemStack[]{player.getMainHandItem(), player.getOffhandItem()}) {
            if (stack.getItem() instanceof DivinitySigilItem && SigilBaseItem.Toggleable.isActive(stack)) return true;
        }

        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof DivinitySigilItem && SigilBaseItem.Toggleable.isActive(stack)) return true;
        }
        return false;
    }
}
