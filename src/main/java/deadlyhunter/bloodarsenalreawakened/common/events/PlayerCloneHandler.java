package deadlyhunter.bloodarsenalreawakened.common.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerCloneHandler {

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        PlayerEntity original = event.getOriginal();
        PlayerEntity clone = event.getPlayer();

        clone.getPersistentData().putInt("heart1_count", original.getPersistentData().getInt("heart1_count"));
        clone.getPersistentData().putInt("heart2_count", original.getPersistentData().getInt("heart2_count"));
        clone.getPersistentData().putInt("heart3_count", original.getPersistentData().getInt("heart3_count"));
    }
}
