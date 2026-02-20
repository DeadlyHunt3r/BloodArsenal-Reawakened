package com.deadlyhunter.bloodarsenalreawakened.item.sigil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

@Mod.EventBusSubscriber(modid = "bloodarsenalreawakened")
public class BloodLustSigilItem extends SigilBaseItem.Toggleable {

    private static final int LP_COST_PER_TICK = 15;
    private static final float SELF_DAMAGE_PER_SECOND = 1.0F;

    public BloodLustSigilItem(Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        if (level.isClientSide || !(entity instanceof ServerPlayer player)) return;

        if (SigilBaseItem.Toggleable.isActive(stack)) {
            SoulNetwork network = NetworkHelper.getSoulNetwork(player);

            if (player.tickCount % 20 == 0) {
                if (network.getCurrentEssence() < LP_COST_PER_TICK * 20) {
                    SigilBaseItem.Toggleable.setActive(stack, false);
                    player.displayClientMessage(Component.literal("Your bloodlust fades...")
                            .withStyle(ChatFormatting.RED), true);
                    return;
                }
                network.syphon(SoulTicket.item(stack, LP_COST_PER_TICK * 20));

                player.hurt(player.damageSources().magic(), SELF_DAMAGE_PER_SECOND);
            }
        }
    }

    @SubscribeEvent
    public static void onVampiricAttack(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {

            if (isActiveOn(player)) {
                float damageDealt = event.getAmount();

                float healAmount = damageDealt * 0.20F; 
                player.heal(healAmount);

                ((net.minecraft.server.level.ServerLevel)player.level()).sendParticles(
                    net.minecraft.core.particles.ParticleTypes.HEART,
                    player.getX(), player.getY() + 1.5, player.getZ(),
                    3, 0.2, 0.2, 0.2, 0.1
                );
            }
        }
    }

    public static boolean isActiveOn(ServerPlayer player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof BloodLustSigilItem && SigilBaseItem.Toggleable.isActive(stack)) {
                return true;
            }
        }
        return false;
    }
}