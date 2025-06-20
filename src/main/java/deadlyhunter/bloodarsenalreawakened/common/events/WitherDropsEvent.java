package deadlyhunter.bloodarsenalreawakened.common.events;

import deadlyhunter.bloodarsenalreawakened.common.item.ModItems;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class WitherDropsEvent
{
    @SubscribeEvent
    public static void onWitherDeath(LivingDropsEvent event)
    {
        if (!(event.getEntityLiving() instanceof WitherEntity))
            return;

        // Nur wenn ein Spieler den Wither getötet hat
        if (!(event.getSource().getTrueSource() instanceof PlayerEntity))
            return;

        World world = event.getEntityLiving().world;
        if (world.isRemote)
            return;

        Random random = world.getRandom();

        // 10% Dropchance für Essence of Decay
        if (random.nextFloat() < 0.1f)
        {
            ItemStack decayEssence = new ItemStack(ModItems.ESSENCE_OF_DECAY.get());
            ItemEntity decayDrop = new ItemEntity(world,
                    event.getEntityLiving().getPosX(),
                    event.getEntityLiving().getPosY(),
                    event.getEntityLiving().getPosZ(),
                    decayEssence);
            event.getDrops().add(decayDrop);
        }
    }
}