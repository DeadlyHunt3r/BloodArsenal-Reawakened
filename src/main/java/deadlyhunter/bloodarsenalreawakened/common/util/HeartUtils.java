package deadlyhunter.bloodarsenalreawakened.common.util;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class HeartUtils {
    private static final UUID HEART_MODIFIER_UUID = UUID.fromString("e7d3f9b4-0cc1-11ec-82a8-0242ac130003");

    public static void updatePlayerHearts(PlayerEntity player, int totalHearts) {
        double healthIncrease = totalHearts * 2.0;

        if (player.getAttribute(Attributes.MAX_HEALTH).getModifier(HEART_MODIFIER_UUID) != null) {
            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(HEART_MODIFIER_UUID);
        }

        AttributeModifier modifier = new AttributeModifier(HEART_MODIFIER_UUID, "Permanent heart bonus", healthIncrease, AttributeModifier.Operation.ADDITION);
        player.getAttribute(Attributes.MAX_HEALTH).applyPersistentModifier(modifier);

        player.setHealth(player.getMaxHealth());
    }
}
