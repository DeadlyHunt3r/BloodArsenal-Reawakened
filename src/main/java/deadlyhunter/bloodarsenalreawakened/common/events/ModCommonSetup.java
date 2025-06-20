package deadlyhunter.bloodarsenalreawakened.common.events;

import deadlyhunter.bloodarsenalreawakened.common.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "bloodarsenalreawakened")
public class ModCommonSetup {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        PlayerEntity player = event.getPlayer();
        Hand hand = event.getHand();
        ItemStack heldItem = player.getHeldItem(hand);
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();


        if (block == ModBlocks.BLOOD_INFUSED_LOG.get()) {

            if (heldItem.getItem() instanceof AxeItem) {
                if (!world.isRemote) {

                    BlockState strippedState = ModBlocks.STRIPPED_BLOOD_INFUSED_LOG.get().getDefaultState()
                        .with(net.minecraft.block.RotatedPillarBlock.AXIS, state.get(net.minecraft.block.RotatedPillarBlock.AXIS)); 

                    world.setBlockState(pos, strippedState, 11);


                    heldItem.damageItem(1, player, (p) -> p.sendBreakAnimation(hand));

                    event.setCancellationResult(ActionResultType.SUCCESS);
                    event.setCanceled(true);
                }
            }
        }
    }
}
