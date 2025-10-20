package com.deadlyhunter.bloodarsenalreawakened.events;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import com.deadlyhunter.bloodarsenalreawakened.registry.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BARMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BloodShieldTotemEvents {

    @SubscribeEvent
    public static void onLethalDamage(LivingHurtEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;
        if (p.level().isClientSide()) return;

        float remaining = p.getHealth() - e.getAmount();
        if (remaining > 0f) return;

        // Suche im gesamten Inventar
        int slot = findTotemSlot(p);
        if (slot < 0) return; // keins da -> sterben lassen

        // Totem triggert: Schaden canceln, Spieler retten
        e.setCanceled(true);

        p.setHealth(1.0F);
        p.removeAllEffects();

        p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 800, 1)); // 40s Reg II
        p.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));   // 5s Abs II
        p.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0)); // 40s FR
        p.setRemainingFireTicks(0);

        // Animation + Sound
        if (p instanceof ServerPlayer sp) {
            sp.level().playSound(null, sp.getX(), sp.getY(), sp.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            ((ServerLevel) sp.level()).broadcastEntityEvent(sp, (byte)35); // Totem-Animation
        }

        // Totem NICHT zerstören → in USED-Version verwandeln
        replaceSlotWithUsed(p, slot);
    }

    private static int findTotemSlot(Player p) {
        // Offhand (Index 0)
        for (int i = 0; i < p.getInventory().offhand.size(); i++) {
            ItemStack s = p.getInventory().offhand.get(i);
            if (s.is(ModItems.TOTEM_OF_BLOOD_SHIELDING.get())) return encodeSlot(SlotType.OFFHAND, i);
        }
        // Rüstung
        for (int i = 0; i < p.getInventory().armor.size(); i++) {
            ItemStack s = p.getInventory().armor.get(i);
            if (s.is(ModItems.TOTEM_OF_BLOOD_SHIELDING.get())) return encodeSlot(SlotType.ARMOR, i);
        }
        // Hauptinventar (inkl. Hotbar)
        for (int i = 0; i < p.getInventory().items.size(); i++) {
            ItemStack s = p.getInventory().items.get(i);
            if (s.is(ModItems.TOTEM_OF_BLOOD_SHIELDING.get())) return encodeSlot(SlotType.MAIN, i);
        }
        return -1;
    }

    private enum SlotType { MAIN, OFFHAND, ARMOR }
    private static int encodeSlot(SlotType t, int idx) { return (t.ordinal() << 24) | (idx & 0xFFFFFF); }
    private static SlotType slotType(int code) { return SlotType.values()[(code >>> 24) & 0xFF]; }
    private static int slotIndex(int code) { return code & 0xFFFFFF; }

    private static void replaceSlotWithUsed(Player p, int code) {
        ItemStack used = new ItemStack(ModItems.USED_TOTEM_OF_BLOOD_SHIELDING.get());
        switch (slotType(code)) {
            case OFFHAND -> p.getInventory().offhand.set(slotIndex(code), used);
            case ARMOR   -> p.getInventory().armor.set(slotIndex(code), used);
            case MAIN    -> p.getInventory().items.set(slotIndex(code), used);
        }
        p.getInventory().setChanged();
    }
}
