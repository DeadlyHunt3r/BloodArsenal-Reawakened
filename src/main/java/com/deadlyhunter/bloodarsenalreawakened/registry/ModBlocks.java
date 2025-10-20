package com.deadlyhunter.bloodarsenalreawakened.registry;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> REGISTER =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BARMain.MOD_ID);

    public static RegistryObject<Item> registerBlockItem(RegistryObject<Block> block) {
        return ModItems.REGISTER.register(block.getId().getPath(),
                () -> new BlockItem(block.get(), new Item.Properties()));
    }

}
