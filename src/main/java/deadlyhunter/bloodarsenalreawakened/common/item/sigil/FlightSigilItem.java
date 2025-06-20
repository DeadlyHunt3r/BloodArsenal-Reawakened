package deadlyhunter.bloodarsenalreawakened.common.item.sigil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilToggleable;
import deadlyhunter.bloodarsenalreawakened.common.ConfigHandler;
import deadlyhunter.bloodarsenalreawakened.common.util.helper.PlayerHelper;

import javax.annotation.Nullable;
import java.util.List;

public class FlightSigilItem extends ItemSigilToggleable
{
    private static final String ACTIVATED_TAG = "enabled";

    public FlightSigilItem(Properties properties)
    {
        super(properties, ConfigHandler.COMMON.flightSigilCost.get());
    }

    public boolean getActivated(ItemStack stack)
    {
        return stack.getOrCreateTag().getBoolean(ACTIVATED_TAG);
    }

    public void setActivated(ItemStack stack, boolean activated)
    {
        stack.getOrCreateTag().putBoolean(ACTIVATED_TAG, activated);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getHeldItem(hand);

        if (!world.isRemote)
        {
            boolean active = !getActivated(stack);
            setActivated(stack, active);

            player.sendStatusMessage(new StringTextComponent("Flight Sigil " + (active ? "activated" : "deactivated")), true);
        }

        return ActionResult.resultSuccess(stack);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, PlayerEntity player, int itemSlot, boolean isSelected)
    {
        if (world.isRemote || PlayerHelper.isFakePlayer(player))
            return;

        boolean active = getActivated(stack);

        if (active)
        {
            player.abilities.allowFlying = true;

            if (!player.abilities.isFlying)
            {
                player.abilities.isFlying = true;
            }

            player.sendPlayerAbilities();
        }
        else
        {
            if (!player.isCreative() && !player.isSpectator())
            {
                player.abilities.allowFlying = false;

                if (player.abilities.isFlying)
                {
                    player.abilities.isFlying = false;
                }

                player.stopFallFlying();

                player.sendPlayerAbilities();
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        tooltip.add(new TranslationTextComponent("tooltip.bloodarsenalreawakened.flight_sigil.desc").mergeStyle(TextFormatting.GRAY));
    }
}
