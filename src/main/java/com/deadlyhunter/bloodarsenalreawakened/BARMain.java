package com.deadlyhunter.bloodarsenalreawakened;

import com.deadlyhunter.bloodarsenalreawakened.registry.ModBlocks;
import com.deadlyhunter.bloodarsenalreawakened.registry.ModCreativeTabs;
import com.deadlyhunter.bloodarsenalreawakened.registry.ModItems;
import com.deadlyhunter.bloodarsenalreawakened.datagen.ModDataGen;
import com.deadlyhunter.bloodarsenalreawakened.loot.ModLootModifiers;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BARMain.MOD_ID)
public class BARMain {
    public static final String MOD_ID = "blood_arsenal_reawakened";

    public BARMain() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.REGISTER.register(modBus);
        ModBlocks.REGISTER.register(modBus);
        ModCreativeTabs.REGISTER.register(modBus);
		ModLootModifiers.REGISTER.register(modBus);

        ModDataGen.register(modBus);
    }
}
