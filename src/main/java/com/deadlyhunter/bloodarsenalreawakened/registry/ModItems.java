package com.deadlyhunter.bloodarsenalreawakened.registry;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.deadlyhunter.bloodarsenalreawakened.item.sigil.DivinitySigilItem;
import com.deadlyhunter.bloodarsenalreawakened.item.sigil.FlightSigilItem;
import com.deadlyhunter.bloodarsenalreawakened.item.sigil.LightningSigilItem;
import com.deadlyhunter.bloodarsenalreawakened.item.sigil.SigilOfDecayedBloodItem;
import com.deadlyhunter.bloodarsenalreawakened.item.sigil.EnderSigilItem;
import com.deadlyhunter.bloodarsenalreawakened.item.sigil.SigilOfTheBloodMoonItem;
import com.deadlyhunter.bloodarsenalreawakened.item.sigil.BloodLustSigilItem;
import com.deadlyhunter.bloodarsenalreawakened.item.TotemOfBloodShieldingItem;
import com.deadlyhunter.bloodarsenalreawakened.item.UsedTotemOfBloodShieldingItem;
import com.deadlyhunter.bloodarsenalreawakened.item.BloodGuideItem;
import com.deadlyhunter.bloodarsenalreawakened.item.heart.HeartItem;
import com.deadlyhunter.bloodarsenalreawakened.gear.BloodArmorItems;
import com.deadlyhunter.bloodarsenalreawakened.gear.ModArmorMaterials;
import com.deadlyhunter.bloodarsenalreawakened.item.TooltipItem;
import com.deadlyhunter.bloodarsenalreawakened.item.item.BloodBoundExecutionerItem;
import net.minecraft.world.item.Rarity;


public class ModItems {
    public static final DeferredRegister<Item> REGISTER =
            DeferredRegister.create(ForgeRegistries.ITEMS, BARMain.MOD_ID);
			
	// ITEMS
	
	public static final RegistryObject<Item> TOTEM_OF_BLOOD_SHIELDING = REGISTER.register("totem_of_blood_shielding", () -> new TotemOfBloodShieldingItem(new Item.Properties().stacksTo(1).rarity(net.minecraft.world.item.Rarity.RARE)));
		
	public static final RegistryObject<Item> USED_TOTEM_OF_BLOOD_SHIELDING = REGISTER.register("used_totem_of_blood_shielding", () -> new UsedTotemOfBloodShieldingItem(new Item.Properties().stacksTo(1).rarity(net.minecraft.world.item.Rarity.UNCOMMON)));	
			
	public static final RegistryObject<Item> DRAGON_CLAW = REGISTER.register("dragon_claw", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.RARE)));

	public static final RegistryObject<Item> BLEEDING_DRAGON = REGISTER.register("bleeding_dragon", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.EPIC)));

	public static final RegistryObject<Item> ESSENCE_OF_DECAY = REGISTER.register("essence_of_decay", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.RARE)));

	public static final RegistryObject<Item> REAGENT_FLIGHT = REGISTER.register("reagent_flight", () -> new TooltipItem(new Item.Properties(), "reagent_flight"));
	
	public static final RegistryObject<Item> REAGENT_ENDER = REGISTER.register("reagent_ender", () -> new TooltipItem(new Item.Properties(), "reagent_ender"));
	
	public static final RegistryObject<Item> REAGENT_LIGHTNING = REGISTER.register("reagent_lightning", () -> new TooltipItem(new Item.Properties(), "reagent_lightning"));
	
	public static final RegistryObject<Item> REAGENT_DIVINITY = REGISTER.register("reagent_divinity", () -> new TooltipItem(new Item.Properties(), "reagent_divinity"));
	
	public static final RegistryObject<Item> REAGENT_DIVINITY_UNAWAKENED = REGISTER.register("reagent_divinity_unawakened", () -> new TooltipItem(new Item.Properties(), "reagent_divinity_unawakened"));
	
	public static final RegistryObject<Item> REAGENT_BLOODLUST = REGISTER.register("reagent_bloodlust", () -> new TooltipItem(new Item.Properties(), "reagent_bloodlust"));
	
	public static final RegistryObject<Item> REAGENT_DECAY = REGISTER.register("reagent_decay", () -> new TooltipItem(new Item.Properties(), "reagent_decay"));
	
	public static final RegistryObject<Item> GUIDE_BOOK = REGISTER.register("guide_book",  () -> new BloodGuideItem(new Item.Properties().stacksTo(1)));
	
	
	
	
	
	// SIGIL
			
	public static final RegistryObject<Item> DIVINITY_SIGIL = REGISTER.register("divinity_sigil", () -> new DivinitySigilItem(new Item.Properties()));
		
	public static final RegistryObject<Item> FLIGHT_SIGIL = REGISTER.register("flight_sigil", () -> new FlightSigilItem(new Item.Properties()));
	
	public static final RegistryObject<Item> LIGHTNING_SIGIL = REGISTER.register("lightning_sigil", () -> new LightningSigilItem(new Item.Properties()));
	
	public static final RegistryObject<Item> SIGIL_OF_DECAYED_BLOOD = REGISTER.register("sigil_of_decayed_blood", () -> new SigilOfDecayedBloodItem(new Item.Properties()));
	
	public static final RegistryObject<Item> ENDER_SIGIL = REGISTER.register("ender_sigil", () -> new EnderSigilItem(new Item.Properties()));
	
	public static final RegistryObject<Item> SIGIL_OF_THE_BLOODMOON = REGISTER.register("sigil_of_the_bloodmoon", () -> new SigilOfTheBloodMoonItem(new Item.Properties()));
	
	public static final RegistryObject<Item> BLOODLUST_SIGIL = REGISTER.register("bloodlust_sigil",  () -> new BloodLustSigilItem(new Item.Properties()));
	
	// HEART
	
	public static final RegistryObject<Item> CRIMSON_HEART = REGISTER.register("crimson_heart", () -> new HeartItem(new Item.Properties().rarity(Rarity.UNCOMMON), HeartItem.Type.CRIMSON));

	public static final RegistryObject<Item> SANGUINE_HEART = REGISTER.register("sanguine_heart", () -> new HeartItem(new Item.Properties().rarity(Rarity.RARE), HeartItem.Type.SANGUINE));

	public static final RegistryObject<Item> HEART_OF_ASCENSION = REGISTER.register("heart_of_ascension", () -> new HeartItem(new Item.Properties().rarity(Rarity.EPIC), HeartItem.Type.ASCENSION));
	
	// Rüstung
	
	public static final RegistryObject<Item> HELM_OF_BLOODSIGHT = REGISTER.register("helm_of_bloodsight", () -> new BloodArmorItems.HelmOfBloodsight(ModArmorMaterials.BLOOD_TRANSCENDENCE,new Item.Properties().rarity(Rarity.RARE)));
	
	public static final RegistryObject<Item> BREASTPLATE_OF_FURY = REGISTER.register("breastplate_of_fury", () -> new BloodArmorItems.BreastplateOfFury(ModArmorMaterials.BLOOD_TRANSCENDENCE,new Item.Properties().rarity(Rarity.RARE)));
	
	public static final RegistryObject<Item> HUNTERS_GREAVES = REGISTER.register("hunters_greaves", () -> new BloodArmorItems.HuntersGreaves(ModArmorMaterials.BLOOD_TRANSCENDENCE,new Item.Properties().rarity(Rarity.RARE)));
	
	public static final RegistryObject<Item> BOOTS_OF_THE_FROSTBLOOD = REGISTER.register("boots_of_the_frostblood", () -> new BloodArmorItems.BootsOfTheFrostblood(ModArmorMaterials.BLOOD_TRANSCENDENCE,new Item.Properties().rarity(Rarity.RARE)));
	
	// Waffen
	
	public static final RegistryObject<Item> BLOOD_BOUND_EXECUTIONER = REGISTER.register("blood_bound_executioner",  () -> new BloodBoundExecutionerItem(Tiers.NETHERITE, 7, -2.4F, new Item.Properties().fireResistant().stacksTo(1).rarity(Rarity.EPIC)));
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
