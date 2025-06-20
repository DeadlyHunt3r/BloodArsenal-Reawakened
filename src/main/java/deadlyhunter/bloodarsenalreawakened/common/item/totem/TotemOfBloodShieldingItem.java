package deadlyhunter.bloodarsenalreawakened.common.item.totem;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import deadlyhunter.bloodarsenalreawakened.common.item.ModItems;

public class TotemOfBloodShieldingItem extends Item {

    public TotemOfBloodShieldingItem(Properties properties) {
        super(properties);
    }

    public ItemStack onPlayerDeathReplace(ItemStack stack, PlayerEntity player) {
        player.sendStatusMessage(new StringTextComponent("Your Totem of Blood Shielding has been consumed!"), true);
        return new ItemStack(ModItems.USED_TOTEM_OF_BLOOD_SHIELDING.get());
    }
}