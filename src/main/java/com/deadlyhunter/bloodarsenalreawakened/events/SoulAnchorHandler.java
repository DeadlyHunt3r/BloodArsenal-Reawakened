package com.deadlyhunter.bloodarsenalreawakened.events;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import com.deadlyhunter.bloodarsenalreawakened.entity.SoulAnchorEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

@Mod.EventBusSubscriber(modid = BARMain.MOD_ID)
public class SoulAnchorHandler {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            for (ServerLevel level : player.server.getAllLevels()) {
                for (Entity entity : level.getAllEntities()) {
                    if (entity instanceof SoulAnchorEntity anchor && anchor.isAlive()) {
                        if (player.getUUID().equals(anchor.getOwnerUUID())) {
                            
                            SoulNetwork network = NetworkHelper.getSoulNetwork(player);
                            int lpCost = 200000; // Kosten für die Rettung

                            if (network.getCurrentEssence() >= lpCost) {
                                event.setCanceled(true);
                                network.syphon(lpCost); 
                                
                                player.setHealth(player.getMaxHealth());
                                player.teleportTo(level, anchor.getX(), anchor.getY(), anchor.getZ(), player.getYRot(), player.getXRot());
                                
                                player.sendSystemMessage(Component.literal("Bound by blood, saved by the soul. Your essence returns to the Anchor."));
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}