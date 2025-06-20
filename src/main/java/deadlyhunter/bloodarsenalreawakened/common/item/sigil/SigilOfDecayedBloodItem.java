package deadlyhunter.bloodarsenalreawakened.common.item.sigil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

public class SigilOfDecayedBloodItem extends SigilBaseItem {
    private static final int LP_COST_TELEPORT = 10_000;
    private static final int LP_COST_SAVE = 10_000;
    private static final float DAMAGE_ON_SAVE = 9.0F; 

    public SigilOfDecayedBloodItem(Properties properties) {
        super(properties, LP_COST_TELEPORT);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (player.isSneaking()) {

            if (!world.isRemote) {

                if (player.getHealth() > DAMAGE_ON_SAVE) {
                    if (NetworkHelper.getSoulNetwork(getBinding(stack))
                            .syphonAndDamage(player, SoulTicket.item(stack, world, player, LP_COST_SAVE)).isSuccess()) {

                        BlockPos pos = player.getPosition();
                        stack.getOrCreateTag().putInt("SavedX", pos.getX());
                        stack.getOrCreateTag().putInt("SavedY", pos.getY());
                        stack.getOrCreateTag().putInt("SavedZ", pos.getZ());

                        player.attackEntityFrom(DamageSource.MAGIC, DAMAGE_ON_SAVE);

                        player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Location saved!"), true);
                    } else {
                        player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Not enough LP to save location!"), true);
                    }
                } else {
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "You do not have enough health to save a location!"), true);
                }
            }
            return ActionResult.resultSuccess(stack);
        } else {

            if (!world.isRemote) {
                if (!stack.hasTag() || !stack.getTag().contains("SavedX")) {
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "No location saved!"), true);
                    return ActionResult.resultFail(stack);
                }

                if (NetworkHelper.getSoulNetwork(getBinding(stack))
                        .syphonAndDamage(player, SoulTicket.item(stack, world, player, LP_COST_TELEPORT)).isSuccess()) {

                    int x = stack.getTag().getInt("SavedX");
                    int y = stack.getTag().getInt("SavedY");
                    int z = stack.getTag().getInt("SavedZ");
                    BlockPos savedPos = new BlockPos(x, y, z);

                    // Teleportation
                    player.setPositionAndUpdate(savedPos.getX() + 0.5, savedPos.getY(), savedPos.getZ() + 0.5);

                    player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "Teleported to saved location!"), true);
                } else {
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Not enough LP to teleport!"), true);
                }
            }
            return ActionResult.resultSuccess(stack);
        }
    }
}
