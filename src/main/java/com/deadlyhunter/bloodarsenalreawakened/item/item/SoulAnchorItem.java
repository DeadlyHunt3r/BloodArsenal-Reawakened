package com.deadlyhunter.bloodarsenalreawakened.item;

import com.deadlyhunter.bloodarsenalreawakened.entity.SoulAnchorEntity;
import com.deadlyhunter.bloodarsenalreawakened.registry.ModEntities;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SoulAnchorItem extends Item {
    public SoulAnchorItem(Properties props) {
        super(props);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {

        if (!entity.level().isClientSide && entity.onGround()) {
            Level level = entity.level();
            SoulAnchorEntity anchor = new SoulAnchorEntity(ModEntities.SOUL_ANCHOR.get(), level);
            anchor.setPos(entity.getX(), entity.getY() + 1.2, entity.getZ());
            
            if (entity.getOwner() != null) {
                anchor.setOwner(entity.getOwner().getUUID());
                level.addFreshEntity(anchor);
                entity.discard();
                return true;
            }
        }
        return false;
    }
}