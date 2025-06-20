package deadlyhunter.bloodarsenalreawakened.common.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import deadlyhunter.bloodarsenalreawakened.common.item.totem.TotemOfBloodShieldingItem;
import deadlyhunter.bloodarsenalreawakened.common.item.totem.UsedTotemOfBloodShieldingItem;

public class DeathEventHandler {

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity)) return;

        PlayerEntity player = (PlayerEntity) event.getEntity();

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);

            if (stack.getItem() instanceof TotemOfBloodShieldingItem) {
                ItemStack usedTotem = ((TotemOfBloodShieldingItem) stack.getItem()).onPlayerDeathReplace(stack, player);
                player.inventory.setInventorySlotContents(i, usedTotem);


                event.setCanceled(true);


                player.setHealth(20.0F);


                player.clearActivePotions();


                player.getFoodStats().setFoodLevel(6);
                player.getFoodStats().addStats(6, 1.0F);
				
				player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 200, 0));


                player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);


                if (!player.world.isRemote && player.world instanceof ServerWorld) {
                    ((ServerWorld) player.world).spawnParticle(ParticleTypes.TOTEM_OF_UNDYING,
                            player.getPosX(), player.getPosY() + 1.0D, player.getPosZ(),
                            30, 0.5, 1.0, 0.5, 0.1);
                }


                player.sendStatusMessage(new StringTextComponent("The Totem of Blood Shielding saved you!"), true);

                break;
            }
        }
    }
}
