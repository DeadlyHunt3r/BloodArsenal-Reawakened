package com.deadlyhunter.bloodarsenalreawakened.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class AddItemModifier extends LootModifier {
    public static final Codec<AddItemModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst)
        .and(ResourceLocation.CODEC.fieldOf("item").forGetter(m -> ForgeRegistries.ITEMS.getKey(m.item)))
        .and(Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance))
        .apply(inst, AddItemModifier::fromCodec));

    private final Item item;
    private final float chance;

    public AddItemModifier(LootItemCondition[] conditions, Item item, float chance) {
        super(conditions);
        this.item = item;
        this.chance = chance;
    }

    private static AddItemModifier fromCodec(LootItemCondition[] conds, ResourceLocation itemId, float chance) {
        Item it = ForgeRegistries.ITEMS.getValue(itemId);
        return new AddItemModifier(conds, it, chance);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext ctx) {
        if (item != null && ctx.getRandom().nextFloat() < chance) {
            generatedLoot.add(new ItemStack(item));
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() { return CODEC; }
}
