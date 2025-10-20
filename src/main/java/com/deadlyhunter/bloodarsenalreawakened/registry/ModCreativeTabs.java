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
						output.accept(ModItems.FLIGHT_SIGIL.get());
						output.accept(ModItems.LIGHTNING_SIGIL.get());
						output.accept(ModItems.SIGIL_OF_DECAYED_BLOOD.get());
						output.accept(ModItems.ENDER_SIGIL.get());
						output.accept(ModItems.TOTEM_OF_BLOOD_SHIELDING.get());
						output.accept(ModItems.USED_TOTEM_OF_BLOOD_SHIELDING.get());
						output.accept(ModItems.CRIMSON_HEART.get());
						output.accept(ModItems.SANGUINE_HEART.get());
						output.accept(ModItems.HEART_OF_ASCENSION.get());
                    })
                    .build());
}
