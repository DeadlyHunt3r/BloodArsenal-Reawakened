package com.deadlyhunter.bloodarsenalreawakened.events;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import com.deadlyhunter.bloodarsenalreawakened.item.sigil.FlightSigilItem;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BARMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FlightSigilEvents {
    private static final String NBT_ROOT = BARMain.MOD_ID;
    private static final String NBT_ACTIVE = "flight_sigil_active_cached";
    private static final String NBT_GRANTED = "flight_granted_by_sigil";

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.END || e.player.level().isClientSide()) return;
        Player p = e.player;

        boolean activeNow = FlightSigilItem.isActiveOn(p);

        var tag = p.getPersistentData().getCompound(NBT_ROOT);
        boolean activePrev = tag.getBoolean(NBT_ACTIVE);
        boolean grantedPrev = tag.getBoolean(NBT_GRANTED);

        if (activeNow && !activePrev) {
            if (!p.isCreative() && !p.isSpectator()) {
                p.getAbilities().mayfly = true;
                p.onUpdateAbilities();
                tag.putBoolean(NBT_GRANTED, true);
            }
            tag.putBoolean(NBT_ACTIVE, true);
            p.getPersistentData().put(NBT_ROOT, tag);
            return;
        }

        if (!activeNow && activePrev) {
            if (grantedPrev && !p.isCreative() && !p.isSpectator()) {
                p.getAbilities().flying = false;
                p.getAbilities().mayfly = false;
                p.onUpdateAbilities();
            }
            tag.putBoolean(NBT_GRANTED, false);
            tag.putBoolean(NBT_ACTIVE, false);
            p.getPersistentData().put(NBT_ROOT, tag);
        }
    }
}
