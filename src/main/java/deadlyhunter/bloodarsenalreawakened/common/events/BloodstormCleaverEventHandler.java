package deadlyhunter.bloodarsenalreawakened.common.events;

import deadlyhunter.bloodarsenalreawakened.common.item.tool.BloodstormCleaverItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BloodstormCleaverEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        PlayerEntity player = event.player;
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.getItem() instanceof BloodstormCleaverItem) {
            BloodstormCleaverItem.onPlayerHoldingTick(player, stack);
        }
    }
}
