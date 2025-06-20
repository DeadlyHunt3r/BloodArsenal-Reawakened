package deadlyhunter.bloodarsenalreawakened.common.item.totem;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class UsedTotemOfBloodShieldingItem extends Item {

    public UsedTotemOfBloodShieldingItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, net.minecraft.entity.Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (entity instanceof PlayerEntity && selected && !world.isRemote) {
            PlayerEntity player = (PlayerEntity) entity;
            player.sendStatusMessage(new StringTextComponent("This Totem of Blood Shielding is consumed. Recharge it at the Blood Altar!"), true);
        }
    }
}