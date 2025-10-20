package com.deadlyhunter.bloodarsenalreawakened.events;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import com.deadlyhunter.bloodarsenalreawakened.item.sigil.DivinitySigilItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BARMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DivinitySigilEvents {

    /** Cancel jeglichen Schaden (egal welche Quelle), wenn aktiv. */
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent e) {
        if (e.getEntity() instanceof Player p && DivinitySigilItem.isActiveOn(p)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onAttack(LivingAttackEvent e) {
        if (e.getEntity() instanceof Player p && DivinitySigilItem.isActiveOn(p)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent e) {
        if (e.getEntity() instanceof Player p && DivinitySigilItem.isActiveOn(p)) {
            e.setCanceled(true);
        }
    }

    /** Sättigung/Hunger permanent auffüllen, solange aktiv (Server-seitig, pro Tick). */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.END || e.player.level().isClientSide()) return;
        Player p = e.player;
        if (!DivinitySigilItem.isActiveOn(p)) return;

        FoodData food = p.getFoodData();
        if (food.getFoodLevel() < 20) {
            food.setFoodLevel(20);
        }
        if (food.getSaturationLevel() < 20.0f) {
            food.setSaturation(20.0f);
        }
    }
}
