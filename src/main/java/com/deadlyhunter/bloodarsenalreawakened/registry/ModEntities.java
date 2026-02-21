package com.deadlyhunter.bloodarsenalreawakened.registry;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import com.deadlyhunter.bloodarsenalreawakened.entity.SoulAnchorEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = 
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BARMain.MOD_ID);

    public static final RegistryObject<EntityType<SoulAnchorEntity>> SOUL_ANCHOR = ENTITIES.register("soul_anchor",
        () -> EntityType.Builder.<SoulAnchorEntity>of(SoulAnchorEntity::new, MobCategory.MISC)
            .sized(0.5f, 0.5f) 
            .clientTrackingRange(10)
            .build("soul_anchor"));
}