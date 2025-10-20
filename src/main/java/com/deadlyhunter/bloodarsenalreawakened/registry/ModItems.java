package com.deadlyhunter.bloodarsenalreawakened.registry;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.deadlyhunter.bloodarsenalreawakened.item.sigil.DivinitySigilItem;
import com.deadlyhunter.bloodarsenalreawakened.item.sigil.FlightSigilItem;
import com.deadlyhunter.bloodarsenalreawakened.item.sigil.LightningSigilItem;
import com.deadlyhunter.bloodarsenalreawakened.item.sigil.SigilOfDecayedBloodItem;
import com.deadlyhunter.bloodarsenalreawakened.item.sigil.EnderSigilItem;
import com.deadlyhunter.bloodarsenalreawakened.item.TotemOfBloodShieldingItem;
import com.deadlyhunter.bloodarsenalreawakened.item.UsedTotemOfBloodShieldingItem;
import com.deadlyhunter.bloodarsenalreawakened.item.heart.HeartItem;


public class ModItems {
    public static final DeferredRegister<Item> REGISTER =
            DeferredRegister.create(ForgeRegistries.ITEMS, BARMain.MOD_ID);
			
	// ITEMS
	
	public static final RegistryObject<Item> TOTEM_OF_BLOOD_SHIELDING = REGISTER.register("totem_of_blood_shielding", () -> new TotemOfBloodShieldingItem(new Item.Properties().stacksTo(1).rarity(net.minecraft.world.item.Rarity.RARE)));
		
	public static final RegistryObject<Item> USED_TOTEM_OF_BLOOD_SHIELDING = REGISTER.register("used_totem_of_blood_shielding", () -> new UsedTotemOfBloodShieldingItem(new Item.Properties().stacksTo(1).rarity(net.minecraft.world.item.Rarity.UNCOMMON)));	
			
			
	// SIGIL
			
	public static final RegistryObject<Item> DIVINITY_SIGIL = REGISTER.register("divinity_sigil", () -> new DivinitySigilItem(new Item.Properties()));
		
	public static final RegistryObject<Item> FLIGHT_SIGIL = REGISTER.register("flight_sigil", () -> new FlightSigilItem(new Item.Properties()));
	
	public static final RegistryObject<Item> LIGHTNING_SIGIL = REGISTER.register("lightning_sigil", () -> new LightningSigilItem(new Item.Properties()));
	
	public static final RegistryObject<Item> SIGIL_OF_DECAYED_BLOOD = REGISTER.register("sigil_of_decayed_blood", () -> new SigilOfDecayedBloodItem(new Item.Properties()));
	
	public static final RegistryObject<Item> ENDER_SIGIL = REGISTER.register("ender_sigil", () -> new EnderSigilItem(new Item.Properties()));
	
	// HEART
	
	public static final RegistryObject<Item> CRIMSON_HEART = REGISTER.register("crimson_heart", () -> new HeartItem(new Item.Properties().rarity(Rarity.UNCOMMON), HeartItem.Type.CRIMSON));

	public static final RegistryObject<Item> SANGUINE_HEART = REGISTER.register("sanguine_heart", () -> new HeartItem(new Item.Properties().rarity(Rarity.RARE), HeartItem.Type.SANGUINE));

	public static final RegistryObject<Item> HEART_OF_ASCENSION = REGISTER.register("heart_of_ascension", () -> new HeartItem(new Item.Properties().rarity(Rarity.EPIC), HeartItem.Type.ASCENSION));
}
