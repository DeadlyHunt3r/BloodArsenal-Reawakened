package deadlyhunter.bloodarsenalreawakened.common.events;

import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber
public class LightningAlchemyHandler {

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        World world = event.world;
        if (world.isRemote) return;

        List<LightningBoltEntity> bolts = world.getEntitiesWithinAABB(
                LightningBoltEntity.class,
                new AxisAlignedBB(-1000, -1000, -1000, 1000, 1000, 1000)
        );

        for (LightningBoltEntity bolt : bolts) {
            if (bolt.ticksExisted != 1) continue; 

            BlockPos strikePos = bolt.getPosition();


            AxisAlignedBB area = new AxisAlignedBB(
                    strikePos.add(-2, -2, -2),
                    strikePos.add(3, 3, 3)
            );

            List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, area);

            int essenceCount = 0;
            int appleCount = 0;
            int eyeCount = 0;
            int dustCount = 0;
			int bottleCount= 0;


            for (ItemEntity itemEntity : items) {
                BlockPos pos = itemEntity.getPosition();
                if (world.getBlockState(pos).getBlock() != Blocks.WATER) continue;

                ItemStack stack = itemEntity.getItem();
                String id = stack.getItem().getRegistryName().toString();

                switch (id) {
                    case "bloodarsenalreawakened:essence_of_decay":
                        essenceCount += stack.getCount();
                        break;
                    case "minecraft:enchanted_golden_apple":
                        appleCount += stack.getCount();
                        break;
                    case "bloodarsenalreawakened:bleeding_dragons_eye":
                        eyeCount += stack.getCount();
                        break;
                    case "bloodmagic:corrupted_dust":
                        dustCount += stack.getCount();
                        break;
					case "minecraft:glass_bottle":
						bottleCount += stack.getCount();
						break;
                }
            }


            if (essenceCount >= 16 && appleCount >= 16 && eyeCount >= 8 && dustCount >= 64 && bottleCount >= 1) {

                for (ItemEntity itemEntity : items) {
                    BlockPos pos = itemEntity.getPosition();
                    if (world.getBlockState(pos).getBlock() == Blocks.WATER) {
                        ItemStack stack = itemEntity.getItem();
                        String id = stack.getItem().getRegistryName().toString();

                        if (id.equals("bloodarsenalreawakened:essence_of_decay") ||
                            id.equals("minecraft:enchanted_golden_apple") ||
                            id.equals("bloodarsenalreawakened:bleeding_dragons_eye") ||
                            id.equals("bloodmagic:corrupted_dust") ||
							id.equals("minecraft:glass_bottle")) {
                            itemEntity.remove();
                        }
                    }
                }


                ItemStack output = new ItemStack(
                    ForgeRegistries.ITEMS.getValue(new ResourceLocation("bloodarsenalreawakened", "reagent_divinity_unawakened")),
                    1);

                ItemEntity outputEntity = new ItemEntity(world,
                        strikePos.getX() + 0.5,
                        strikePos.getY() + 1,
                        strikePos.getZ() + 0.5,
                        output);

                world.addEntity(outputEntity);
            }
        }
    }
}
