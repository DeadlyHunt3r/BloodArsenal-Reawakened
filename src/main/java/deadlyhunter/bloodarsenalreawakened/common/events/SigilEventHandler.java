import deadlyhunter.bloodarsenalreawakened.common.item.sigil.SigilOfBloodlustItem;
import deadlyhunter.bloodarsenalreawakened.common.item.sigil.DivinitySigilItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SigilEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.world.isRemote) {
            PlayerEntity player = event.player;

            SigilOfBloodlustItem.tickEffects(player);

            boolean hasDivinitySigil = false;
            for (ItemStack stack : player.inventory.mainInventory) {
                if (stack.getItem() instanceof DivinitySigilItem) {
                    hasDivinitySigil = true;
                    break;
                }
            }

            if (hasDivinitySigil) {
                FoodStats food = player.getFoodStats();
                food.setFoodLevel(20);
            }
        }
    }
}
