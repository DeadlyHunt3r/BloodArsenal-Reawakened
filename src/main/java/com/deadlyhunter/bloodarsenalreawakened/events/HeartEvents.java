package com.deadlyhunter.bloodarsenalreawakened.events;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import com.deadlyhunter.bloodarsenalreawakened.item.heart.HeartUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BARMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HeartEvents {

    /** Nach Respawn persistenten Fortschritt übernehmen */
    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone e) {
        if (!e.isWasDeath()) return;
        Player oldP = e.getOriginal();
        Player newP = e.getEntity();

        CompoundTag oldData = oldP.getPersistentData();
        CompoundTag newData = newP.getPersistentData();

        // kompletten BAR-NBT-Block kopieren
        if (oldData.contains(BARMain.MOD_ID)) {
            newData.put(BARMain.MOD_ID, oldData.getCompound(BARMain.MOD_ID).copy());
        }

        // Attribute-Boni wieder anwenden
        if (!newP.level().isClientSide()) {
            HeartUtils.applyAllHeartBonuses(newP);
        }
    }

    /** Beim Einloggen Boni anwenden */
    @SubscribeEvent
    public static void onLogin(PlayerLoggedInEvent e) {
        Player p = e.getEntity();
        if (!p.level().isClientSide()) {
            HeartUtils.applyAllHeartBonuses(p);
        }
    }
}
