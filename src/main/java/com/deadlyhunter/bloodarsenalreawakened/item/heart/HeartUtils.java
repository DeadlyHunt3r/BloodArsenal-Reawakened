package com.deadlyhunter.bloodarsenalreawakened.item.heart;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public final class HeartUtils {
    private HeartUtils() {}

    // PersistentData keys
    private static final String NBT_ROOT = BARMain.MOD_ID;
    private static final String NBT_CRIMSON  = "heart_crimson_uses";
    private static final String NBT_SANGUINE = "heart_sanguine_uses";
    private static final String NBT_ASCEND   = "heart_ascension_uses";

    private static final UUID UUID_CRIMSON  = UUID.fromString("3b5b5b33-2a7c-4c55-9b7f-9e0c2b8c1001");
    private static final UUID UUID_SANGUINE = UUID.fromString("3b5b5b33-2a7c-4c55-9b7f-9e0c2b8c1002");
    private static final UUID UUID_ASCEND   = UUID.fromString("3b5b5b33-2a7c-4c55-9b7f-9e0c2b8c1003");

    public static int getUses(Player p, HeartItem.Type type) {
        CompoundTag root = p.getPersistentData().getCompound(NBT_ROOT);
        return switch (type) {
            case CRIMSON -> root.getInt(NBT_CRIMSON);
            case SANGUINE -> root.getInt(NBT_SANGUINE);
            case ASCENSION -> root.getInt(NBT_ASCEND);
        };
    }

    public static void setUses(Player p, HeartItem.Type type, int value) {
        CompoundTag root = p.getPersistentData().getCompound(NBT_ROOT);
        switch (type) {
            case CRIMSON -> root.putInt(NBT_CRIMSON, value);
            case SANGUINE -> root.putInt(NBT_SANGUINE, value);
            case ASCENSION -> root.putInt(NBT_ASCEND, value);
        }
        p.getPersistentData().put(NBT_ROOT, root);
    }

    /** wendet alle drei Herz-Boni als Attribute +2.0 pro Use an */
    public static void applyAllHeartBonuses(Player p) {
        AttributeInstance maxHealth = p.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth == null) return;

        removeModifier(maxHealth, UUID_CRIMSON);
        removeModifier(maxHealth, UUID_SANGUINE);
        removeModifier(maxHealth, UUID_ASCEND);

        // Neu hinzufügen
        addOrUpdate(maxHealth, UUID_CRIMSON,  getUses(p, HeartItem.Type.CRIMSON)  * 2.0D, "bar_crimson_hearts");
        addOrUpdate(maxHealth, UUID_SANGUINE, getUses(p, HeartItem.Type.SANGUINE) * 2.0D, "bar_sanguine_hearts");
        addOrUpdate(maxHealth, UUID_ASCEND,   getUses(p, HeartItem.Type.ASCENSION)* 2.0D, "bar_ascension_hearts");

        // Falls aktuelle HP über neuem Max liegen → clamp
        if (p.getHealth() > p.getMaxHealth()) {
            p.setHealth(p.getMaxHealth());
        }
    }

    private static void addOrUpdate(AttributeInstance inst, UUID id, double amount, String name) {
        if (amount == 0) return;
        AttributeModifier mod = new AttributeModifier(id, name, amount, AttributeModifier.Operation.ADDITION);
        inst.addPermanentModifier(mod);
    }

    private static void removeModifier(AttributeInstance inst, UUID id) {
        AttributeModifier old = inst.getModifier(id);
        if (old != null) inst.removePermanentModifier(id);
    }
}
