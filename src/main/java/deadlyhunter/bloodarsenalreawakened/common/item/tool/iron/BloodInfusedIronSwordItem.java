package deadlyhunter.bloodarsenalreawakened.common.item.tool.iron;

import deadlyhunter.bloodarsenalreawakened.common.ConfigHandler;
import deadlyhunter.bloodarsenalreawakened.common.item.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.common.item.IBindable;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

import java.util.List;

public class BloodInfusedIronSwordItem extends SwordItem
{
    public BloodInfusedIronSwordItem(Properties properties)
    {
        this(ModItems.ItemTier.BLOOD_INFUSED_IRON, properties);
    }

    public BloodInfusedIronSwordItem(IItemTier material, Properties properties)
    {
        super(material, 3, -2.4F, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected)
    {
        if (!world.isRemote && world.getGameTime() % ConfigHandler.COMMON.bloodInfusedIronToolsRepairUpdate.get() == 0)
        {
            if (player instanceof PlayerEntity && stack.getDamage() > 0)
            {
                SoulNetwork network = NetworkHelper.getSoulNetwork((PlayerEntity) player);
                if (network != null && network.syphon(SoulTicket.item(stack, world, player, ConfigHandler.COMMON.bloodInfusedIronToolsRepairCost.get())) > 0)
                {
                    stack.setDamage(stack.getDamage() - 1);
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }
}
