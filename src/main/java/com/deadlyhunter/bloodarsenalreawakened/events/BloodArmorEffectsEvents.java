package com.deadlyhunter.bloodarsenalreawakened.events;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import com.deadlyhunter.bloodarsenalreawakened.registry.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = BARMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BloodArmorEffectsEvents {

    // Feste UUID für den "Health Clamp" Modifier (Set-Bonus)
    private static final UUID SET_HEALTH_CLAMP_UUID = UUID.fromString("d8279b60-7e2d-4bcb-bc07-6dfdb6e9c0b0");

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.END || e.player.level().isClientSide()) return;
        Player p = e.player;

        boolean helm   = p.getInventory().armor.get(3).is(ModItems.HELM_OF_BLOODSIGHT.get());
        boolean chest  = p.getInventory().armor.get(2).is(ModItems.BREASTPLATE_OF_FURY.get());
        boolean legs   = p.getInventory().armor.get(1).is(ModItems.HUNTERS_GREAVES.get());
        boolean boots  = p.getInventory().armor.get(0).is(ModItems.BOOTS_OF_THE_FROSTBLOOD.get());
        int pieces = (helm?1:0) + (chest?1:0) + (legs?1:0) + (boots?1:0);

        // Einzelteil-Effekte
        if (helm)  give(p, MobEffects.NIGHT_VISION, 220, 0);
        if (chest) give(p, MobEffects.DIG_SPEED,    220, 1); // Haste II
        if (legs)  give(p, MobEffects.MOVEMENT_SPEED,220, 1); // Speed II
        if (boots) doFrostWalkerStep(p); // FrostWalker

        // Set-Bonus
        AttributeInstance maxHealth = p.getAttribute(Attributes.MAX_HEALTH);
        if (pieces == 4) {
            give(p, MobEffects.DAMAGE_BOOST, 220, 1);   // Stärke II
            give(p, MobEffects.REGENERATION, 220, 1);   // Regeneration II
            give(p, MobEffects.DAMAGE_RESISTANCE, 220, 1); // Resistenz II
            give(p, MobEffects.FIRE_RESISTANCE, 220, 0);   // Feuerresistenz

            // Max-Health auf 4.0 "clampen": additiver, transienter Modifier = 4.0
            if (maxHealth != null) {
                // alten Clamp entfernen
                AttributeModifier old = maxHealth.getModifier(SET_HEALTH_CLAMP_UUID);
                if (old != null) maxHealth.removeModifier(old);

                double current = maxHealth.getBaseValue();
                // alle nicht-transienten Modifikatoren mitrechnen
                for (AttributeModifier m : maxHealth.getModifiers()) {
                    if (m.getOperation() == AttributeModifier.Operation.ADDITION) current += m.getAmount();

                }
                double want = 4.0D;
                double delta = want - p.getMaxHealth();
                AttributeModifier clamp = new AttributeModifier(SET_HEALTH_CLAMP_UUID, "bar_set_health_clamp", delta, AttributeModifier.Operation.ADDITION);
                maxHealth.addTransientModifier(clamp);

                if (p.getHealth() > p.getMaxHealth()) p.setHealth(p.getMaxHealth());
            }
        } else {
            // Set-Effekte weg + Health-Clamp entfernen
            if (maxHealth != null) {
                AttributeModifier old = maxHealth.getModifier(SET_HEALTH_CLAMP_UUID);
                if (old != null) maxHealth.removeModifier(old);
                if (p.getHealth() > p.getMaxHealth()) p.setHealth(p.getMaxHealth());
            }
        }
    }

    private static void give(Player p, net.minecraft.world.effect.MobEffect effect, int duration, int amp) {
        // ambient=false, visible=false, showIcon=false
        MobEffectInstance inst = new MobEffectInstance(effect, duration, amp, false, false, false);
        p.addEffect(inst);
    }

    private static void doFrostWalkerStep(Player p) {
        // Frost Walker Logik auf Wasser anwenden
        BlockPos pos = p.blockPosition();
        FrostWalkerEnchantment.onEntityMoved(p, p.level(), pos, 2);
    }
}
