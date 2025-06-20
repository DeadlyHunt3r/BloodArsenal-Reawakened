package deadlyhunter.bloodarsenalreawakened.common.events;

import deadlyhunter.bloodarsenalreawakened.common.item.ModItems;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class DragonDropsEvent
{
    @SubscribeEvent
    public static void onDragonDeath(LivingDropsEvent event)
    {
        if (!(event.getEntityLiving() instanceof EnderDragonEntity))
            return;

        World world = event.getEntityLiving().world;
        if (world.isRemote)
            return;

        Random random = world.getRandom();

        // 100% Drop Dragon Claw
        ItemStack dragonClaw = new ItemStack(ModItems.DRAGON_CLAW.get());
        ItemEntity clawEntity = new ItemEntity(world, event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(), dragonClaw);
        event.getDrops().add(clawEntity);

        // 10% Drop Ethereal Dragon Heart
        if (random.nextFloat() < 0.1f)
        {
            ItemStack heart = new ItemStack(ModItems.ETHEREAL_DRAGON_HEART.get());
            ItemEntity heartEntity = new ItemEntity(world, event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(), heart);
            event.getDrops().add(heartEntity);
        }
    }
}