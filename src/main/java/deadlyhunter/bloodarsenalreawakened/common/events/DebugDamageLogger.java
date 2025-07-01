package deadlyhunter.bloodarsenalreawakened.common.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DebugDamageLogger {

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        // System.out.println("[Debug] LivingHurtEvent wurde ausgelöst!");

        if (!(event.getEntityLiving() instanceof PlayerEntity))
            return;

        PlayerEntity player = (PlayerEntity) event.getEntityLiving();

        float damage = event.getAmount();
        String sourceName = event.getSource().getDamageType();
        String attacker = event.getSource().getTrueSource() != null
                ? event.getSource().getTrueSource().getName().getString()
                : "Unbekannt";

        // Debug-Ausgabe für später:
        // player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "[Debug] Schaden: " + damage + " von " + sourceName + " (" + attacker + ")"), true);
    }
}
