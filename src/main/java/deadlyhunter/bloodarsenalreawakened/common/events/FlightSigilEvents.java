package deadlyhunter.bloodarsenalreawakened.common.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import deadlyhunter.bloodarsenalreawakened.common.item.sigil.FlightSigilItem;
import net.minecraft.item.ItemStack;

@Mod.EventBusSubscriber
public class FlightSigilEvents {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        PlayerEntity player = event.player;

        if (player == null || player.world.isRemote) return;

        boolean hasActiveFlightSigil = false;

        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.getItem() instanceof FlightSigilItem) {
                if (((FlightSigilItem) stack.getItem()).getActivated(stack)) {
                    hasActiveFlightSigil = true;
                    break;
                }
            }
        }

        if (hasActiveFlightSigil) {
            if (!player.abilities.allowFlying) {
                player.abilities.allowFlying = true;
                player.sendPlayerAbilities();
            }
        } else {


            if (player.getPersistentData().getBoolean("BloodArsenalReawakened_FlightEnabled")) {
                player.abilities.allowFlying = false;
                player.abilities.isFlying = false;
                player.stopFallFlying();
                player.sendPlayerAbilities();
                player.getPersistentData().remove("BloodArsenalReawakened_FlightEnabled");
            }
        }


        player.getPersistentData().putBoolean("BloodArsenalReawakened_FlightEnabled", hasActiveFlightSigil);
    }
}

