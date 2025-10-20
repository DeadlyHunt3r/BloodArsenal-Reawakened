package com.deadlyhunter.bloodarsenalreawakened.item.sigil;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FlightSigilItem extends SigilBaseItem.Toggleable {
    public FlightSigilItem(Item.Properties props) { super(props.stacksTo(1)); }

    public static boolean isActiveOn(Player p) {

        for (ItemStack s : new ItemStack[]{p.getMainHandItem(), p.getOffhandItem()}) {
            if (s.getItem() instanceof FlightSigilItem && SigilBaseItem.Toggleable.isActive(s)) return true;
        }

        for (ItemStack s : p.getInventory().items) {
            if (s.getItem() instanceof FlightSigilItem && SigilBaseItem.Toggleable.isActive(s)) return true;
        }
        return false;
    }
}
