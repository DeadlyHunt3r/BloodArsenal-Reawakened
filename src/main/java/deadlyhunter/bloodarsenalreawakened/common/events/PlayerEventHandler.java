package deadlyhunter.bloodarsenalreawakened.common.events;

import deadlyhunter.bloodarsenalreawakened.common.util.HeartUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();
        restoreHearts(player);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        restoreHearts(player);
    }

    private static void restoreHearts(PlayerEntity player) {
        int heart1 = player.getPersistentData().getInt("heart1_count");
        int heart2 = player.getPersistentData().getInt("heart2_count");
        int heart3 = player.getPersistentData().getInt("heart3_count");

        int totalHearts = heart1 + heart2 + heart3;
        HeartUtils.updatePlayerHearts(player, totalHearts);
    }
}
