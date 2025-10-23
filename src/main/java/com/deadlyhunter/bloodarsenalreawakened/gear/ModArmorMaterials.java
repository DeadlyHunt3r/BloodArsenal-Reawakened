package com.deadlyhunter.bloodarsenalreawakened.gear;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    BLOOD_TRANSCENDENCE(
            "blood_transcendence",
            37,                         // Haltbarkeit-Basis (irrelevant)
            new int[]{3, 8, 6, 3},      // [boots, leggings, chest, helmet]
            18,                         // Enchantability
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            2.0F,                       // Toughness
            0.05F,                      // Knockback-Resist
            () -> Ingredient.EMPTY 
    );

    private final String name;
    private final int durabilityMultiplier;
    private final int[] defenseForType;
    private final int enchantValue;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};

    ModArmorMaterials(String name, int durabilityMultiplier, int[] defenseForType,
                      int enchantValue, SoundEvent equipSound, float toughness, float knockbackResistance,
                      Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.defenseForType = defenseForType;
        this.enchantValue = enchantValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override public int getDurabilityForType(ArmorItem.Type type) { return HEALTH_PER_SLOT[type.getSlot().getIndex()] * durabilityMultiplier; }
    @Override public int getDefenseForType(ArmorItem.Type type)    { return defenseForType[type.getSlot().getIndex()]; }
    @Override public int getEnchantmentValue()                      { return enchantValue; }
    @Override public SoundEvent getEquipSound()                     { return equipSound; }
    @Override public Ingredient getRepairIngredient()               { return repairIngredient.get(); }
    @Override public String getName()                               { return BARMain.MOD_ID + ":" + name; }
    @Override public float getToughness()                           { return toughness; }
    @Override public float getKnockbackResistance()                 { return knockbackResistance; }
}
