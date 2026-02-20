package com.deadlyhunter.bloodarsenalreawakened.registry;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BARMain.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN = REGISTER.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + BARMain.MOD_ID))
                    .icon(() -> new ItemStack(ModItems.TOTEM_OF_BLOOD_SHIELDING.get()))
                    .displayItems((params, output) -> {
						output.accept(ModItems.DIVINITY_SIGIL.get());
						output.accept(ModItems.BLOODLUST_SIGIL.get());
						output.accept(ModItems.FLIGHT_SIGIL.get());
						output.accept(ModItems.LIGHTNING_SIGIL.get());
						output.accept(ModItems.SIGIL_OF_DECAYED_BLOOD.get());
						output.accept(ModItems.ENDER_SIGIL.get());
						output.accept(ModItems.TOTEM_OF_BLOOD_SHIELDING.get());
						output.accept(ModItems.USED_TOTEM_OF_BLOOD_SHIELDING.get());
						output.accept(ModItems.CRIMSON_HEART.get());
						output.accept(ModItems.SANGUINE_HEART.get());
						output.accept(ModItems.HEART_OF_ASCENSION.get());
						output.accept(ModItems.HELM_OF_BLOODSIGHT.get());
						output.accept(ModItems.BREASTPLATE_OF_FURY.get());
						output.accept(ModItems.HUNTERS_GREAVES.get());
						output.accept(ModItems.BOOTS_OF_THE_FROSTBLOOD.get());
						output.accept(ModItems.DRAGON_CLAW.get());
						output.accept(ModItems.BLEEDING_DRAGON.get());
						output.accept(ModItems.ESSENCE_OF_DECAY.get());
						output.accept(ModItems.SIGIL_OF_THE_BLOODMOON.get());
						output.accept(ModItems.BLOOD_BOUND_EXECUTIONER.get());
						output.accept(ModItems.GUIDE_BOOK.get());
						
						output.accept(ModItems.REAGENT_FLIGHT.get());
                        output.accept(ModItems.REAGENT_ENDER.get());
                        output.accept(ModItems.REAGENT_LIGHTNING.get());
                        output.accept(ModItems.REAGENT_DIVINITY.get());
                        output.accept(ModItems.REAGENT_DIVINITY_UNAWAKENED.get());
                        output.accept(ModItems.REAGENT_BLOODLUST.get());
                        output.accept(ModItems.REAGENT_DECAY.get());
                    })
                    .build());
}
