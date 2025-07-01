package deadlyhunter.bloodarsenalreawakened.common.item;

import deadlyhunter.bloodarsenalreawakened.common.BloodArsenalReawakened;
import deadlyhunter.bloodarsenalreawakened.common.block.ModBlocks;
import deadlyhunter.bloodarsenalreawakened.common.core.BloodArsenalReawakenedCreativeTab;
import deadlyhunter.bloodarsenalreawakened.common.item.sigil.DivinitySigilItem;
import deadlyhunter.bloodarsenalreawakened.common.item.sigil.EnderSigilItem;
import deadlyhunter.bloodarsenalreawakened.common.item.sigil.LightningSigilItem;
import deadlyhunter.bloodarsenalreawakened.common.item.tool.*;
import deadlyhunter.bloodarsenalreawakened.common.item.tool.iron.*;
import deadlyhunter.bloodarsenalreawakened.common.item.tool.wood.*;
import deadlyhunter.bloodarsenalreawakened.common.item.sigil.FlightSigilItem;
import deadlyhunter.bloodarsenalreawakened.common.item.tool.BloodstormCleaverItem;
import deadlyhunter.bloodarsenalreawakened.common.item.sigil.SigilOfDecayedBloodItem;
import deadlyhunter.bloodarsenalreawakened.common.item.sigil.SigilOfBloodlustItem;
import deadlyhunter.bloodarsenalreawakened.common.item.totem.TotemOfBloodShieldingItem;
import deadlyhunter.bloodarsenalreawakened.common.item.totem.UsedTotemOfBloodShieldingItem;
import net.minecraft.item.Food;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.BlockItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


import java.util.function.Supplier;

public final class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BloodArsenalReawakened.MOD_ID);

    public static final Food FOOD_BLOOD_ORANGE = new Food.Builder().hunger(4).saturation(2).effect(new EffectInstance(Effects.REGENERATION, 80), 0.5F).setAlwaysEdible().build();
	public static final Food FOOD_HEART_1 = new Food.Builder().hunger(2).saturation(0.1f).setAlwaysEdible().build();
	public static final Food FOOD_HEART_2 = new Food.Builder().hunger(2).saturation(0.1f).setAlwaysEdible().build();
	public static final Food FOOD_HEART_3 = new Food.Builder().hunger(2).saturation(0.1f).setAlwaysEdible().build();

    public static final RegistryObject<Item> GLASS_SHARD = registerItem("glass_shard", new BloodArsenalReawakenedItem(defaultBuilder(), true));
    public static final RegistryObject<Item> BLOOD_ORANGE = registerItem("blood_orange", new BloodArsenalReawakenedItem(defaultBuilder().food(FOOD_BLOOD_ORANGE)));
    public static final RegistryObject<Item> BLOOD_INFUSED_STICK = registerItem("blood_infused_stick", new BloodArsenalReawakenedItem(defaultBuilder()));
    public static final RegistryObject<Item> BLOOD_INFUSED_GLOWSTONE_DUST = registerItem("blood_infused_glowstone_dust", new BloodArsenalReawakenedItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> INERT_BLOOD_INFUSED_IRON_INGOT = registerItem("inert_blood_infused_iron_ingot", new BloodArsenalReawakenedItem(defaultBuilder()));
    public static final RegistryObject<Item> BLOOD_INFUSED_IRON_INGOT = registerItem("blood_infused_iron_ingot", new BloodArsenalReawakenedItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BLOOD_DIAMOND = registerItem("blood_diamond", new BloodArsenalReawakenedItem(defaultBuilder()));
    public static final RegistryObject<Item> INERT_BLOOD_DIAMOND = registerItem("inert_blood_diamond", new BloodArsenalReawakenedItem(defaultBuilder()));
    public static final RegistryObject<Item> INFUSED_BLOOD_DIAMOND = registerItem("infused_blood_diamond", new BloodArsenalReawakenedItem(defaultBuilder()));
    public static final RegistryObject<Item> BOUND_BLOOD_DIAMOND = registerItem("bound_blood_diamond", new BloodArsenalReawakenedItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> STASIS_PLATE = registerItem("stasis_plate", new BloodArsenalReawakenedItem(defaultBuilder()));
    public static final RegistryObject<Item> REAGENT_Flight = registerItem("reagent_flight", new BloodArsenalReawakenedItem(defaultBuilder()));
    public static final RegistryObject<Item> REAGENT_ENDER = registerItem("reagent_ender", new BloodArsenalReawakenedItem(defaultBuilder()));
    public static final RegistryObject<Item> REAGENT_LIGHTNING = registerItem("reagent_lightning", new BloodArsenalReawakenedItem(defaultBuilder().rarity(Rarity.RARE), true, true));
    public static final RegistryObject<Item> REAGENT_DIVINITY = registerItem("reagent_divinity", new BloodArsenalReawakenedItem(defaultBuilder().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> DRAGON_CLAW = registerItem("dragon_claw", new Item(new Item.Properties().group(BloodArsenalReawakenedCreativeTab.INSTANCE).maxStackSize(64)));
    public static final RegistryObject<Item> ETHEREAL_DRAGON_HEART = registerItem("ethereal_dragon_heart", new Item(new Item.Properties().group(BloodArsenalReawakenedCreativeTab.INSTANCE).maxStackSize(16)));
    public static final RegistryObject<Item> ESSENCE_OF_DECAY = registerItem("essence_of_decay",new Item(new Item.Properties().group(BloodArsenalReawakenedCreativeTab.INSTANCE).maxStackSize(16)));
	public static final RegistryObject<Item> BLEEDING_DRAGONS_EYE = registerItem("bleeding_dragons_eye",new Item(new Item.Properties().group(BloodArsenalReawakenedCreativeTab.INSTANCE).maxStackSize(1)));
	public static final RegistryObject<Item> FRAME_OF_ASCENSION = registerItem("frame_of_ascension",new Item(new Item.Properties().group(BloodArsenalReawakenedCreativeTab.INSTANCE).maxStackSize(1)));
	public static final RegistryObject<Item> REAGENT_DIVINITY_UNAWAKENED = registerItem("reagent_divinity_unawakened",new Item(new Item.Properties().group(BloodArsenalReawakenedCreativeTab.INSTANCE).maxStackSize(1)));
	public static final RegistryObject<Item> REAGENT_BLOODLUST = registerItem("reagent_bloodlust",new Item(new Item.Properties().group(BloodArsenalReawakenedCreativeTab.INSTANCE).maxStackSize(1)));
	public static final RegistryObject<Item> REAGENT_DECAY = registerItem("reagent_decay",new Item(new Item.Properties().group(BloodArsenalReawakenedCreativeTab.INSTANCE).maxStackSize(1)));
	public static final RegistryObject<Item> TOTEM_OF_BLOOD_SHIELDING = ITEMS.register("totem_of_blood_shielding",() -> new TotemOfBloodShieldingItem(new Item.Properties().maxStackSize(1).group(BloodArsenalReawakenedCreativeTab.INSTANCE)));
    public static final RegistryObject<Item> USED_TOTEM_OF_BLOOD_SHIELDING = ITEMS.register("used_totem_of_blood_shielding",() -> new UsedTotemOfBloodShieldingItem(new Item.Properties().maxStackSize(1).group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> HEART_1 = registerItem("heart_1", new BloodArsenalReawakenedItem(defaultBuilder().food(FOOD_HEART_1)));
	public static final RegistryObject<Item> HEART_2 = registerItem("heart_2", new BloodArsenalReawakenedItem(defaultBuilder().food(FOOD_HEART_2)));
	public static final RegistryObject<Item> HEART_3 = registerItem("heart_3", new BloodArsenalReawakenedItem(defaultBuilder().food(FOOD_HEART_3)));
	

    public static final RegistryObject<Item> GLASS_SACRIFICIAL_DAGGER = registerItem("glass_sacrificial_dagger", new GlassSacrificialDaggerItem(unstackable()));
    public static final RegistryObject<Item> GLASS_DAGGER_OF_SACRIFICE = registerItem("glass_dagger_of_sacrifice", new GlassDaggerOfSacrificeItem(unstackable()));
	public static final RegistryObject<Item> GLASS_SHARDS = ITEMS.register("glass_shards", () ->new BlockItem(ModBlocks.GLASS_SHARDS.get(), new Item.Properties().group(BloodArsenalReawakenedCreativeTab.INSTANCE)));


    public static final RegistryObject<Item> BLOOD_INFUSED_WOODEN_PICKAXE = registerItem("blood_infused_wooden_pickaxe", new BloodInfusedWoodenPickaxeItem(unstackable()));
    public static final RegistryObject<Item> BLOOD_INFUSED_WOODEN_SHOVEL = registerItem("blood_infused_wooden_shovel", new BloodInfusedWoodenShovelItem(unstackable()));
    public static final RegistryObject<Item> BLOOD_INFUSED_WOODEN_AXE = registerItem("blood_infused_wooden_axe", new BloodInfusedWoodenAxeItem(unstackable()));
    public static final RegistryObject<Item> BLOOD_INFUSED_WOODEN_SWORD = registerItem("blood_infused_wooden_sword", new BloodInfusedWoodenSwordItem(unstackable()));
    public static final RegistryObject<Item> BLOOD_INFUSED_IRON_PICKAXE = registerItem("blood_infused_iron_pickaxe", new BloodInfusedIronPickaxeItem(unstackable()));
    public static final RegistryObject<Item> BLOOD_INFUSED_IRON_SHOVEL = registerItem("blood_infused_iron_shovel", new BloodInfusedIronShovelItem(unstackable()));
    public static final RegistryObject<Item> BLOOD_INFUSED_IRON_AXE = registerItem("blood_infused_iron_axe", new BloodInfusedIronAxeItem(unstackable()));
    public static final RegistryObject<Item> BLOOD_INFUSED_IRON_SWORD = registerItem("blood_infused_iron_sword", new BloodInfusedIronSwordItem(unstackable()));
	public static final RegistryObject<Item> BLOODSTORM_CLEAVER = registerItem("bloodstorm_cleaver",new BloodstormCleaverItem(unstackable()));
	
	public static final RegistryObject<Item> ENDER_SIGIL = registerItem("ender_sigil", new EnderSigilItem(unstackable()));
    public static final RegistryObject<Item> LIGHTNING_SIGIL = registerItem("lightning_sigil", new LightningSigilItem(unstackable().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DIVINITY_SIGIL = registerItem("divinity_sigil", new DivinitySigilItem(unstackable()));
    public static final RegistryObject<Item> SIGIL_OF_DECAYED_BLOOD = registerItem("sigil_of_decayed_blood", new SigilOfDecayedBloodItem(unstackable()));
	public static final RegistryObject<Item> FLIGHT_SIGIL = registerItem("flight_sigil", new FlightSigilItem(unstackable()));
	public static final RegistryObject<Item> SIGIL_OF_BLOODLUST = registerItem("sigil_of_bloodlust", new SigilOfBloodlustItem(unstackable()));

    public static Item.Properties defaultBuilder()
    {
        return new Item.Properties().group(BloodArsenalReawakenedCreativeTab.INSTANCE);
    }

    private static Item.Properties unstackable()
    {
        return defaultBuilder().maxStackSize(1);
    }

    public static RegistryObject<Item> registerItem(String name, Item item)
    {
        return ITEMS.register(name, () -> item);
    }

    public enum ItemTier implements IItemTier
    {
        BLOOD_INFUSED_WOOD(180, 5.5F, 1, 1, 25, () -> ModBlocks.BLOOD_INFUSED_PLANKS.get().asItem()),
        BLOOD_INFUSED_IRON(900, 7.5F, 2.7F, 3, 30, ModItems.BLOOD_INFUSED_IRON_INGOT),
        ;

        private final int maxUses;
        private final float efficiency;
        private final float attackDamage;
        private final int harvestLevel;
        private final int enchantability;
        private final Supplier<Item> repairItem;

        ItemTier(int maxUses, float efficiency, float attackDamage, int harvestLevel, int enchantability, Supplier<Item> repairItem)
        {
            this.maxUses = maxUses;
            this.efficiency = efficiency;
            this.attackDamage = attackDamage;
            this.harvestLevel = harvestLevel;
            this.enchantability = enchantability;
            this.repairItem = repairItem;
        }

        @Override
        public int getMaxUses()
        {
            return maxUses;
        }

        @Override
        public float getEfficiency()
        {
            return efficiency;
        }

        @Override
        public float getAttackDamage()
        {
            return attackDamage;
        }

        @Override
        public int getHarvestLevel()
        {
            return harvestLevel;
        }

        @Override
        public int getEnchantability()
        {
            return enchantability;
        }

        @Override
        public Ingredient getRepairMaterial()
        {
            return Ingredient.fromItems(repairItem.get());
        }
    }
}
