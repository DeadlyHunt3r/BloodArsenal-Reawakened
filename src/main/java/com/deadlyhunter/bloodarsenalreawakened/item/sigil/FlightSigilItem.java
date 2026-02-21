package com.deadlyhunter.bloodarsenalreawakened.item.sigil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

public class FlightSigilItem extends SigilBaseItem.Toggleable {
    
    private static final int LP_COST_PER_TICK = 20; 

    public FlightSigilItem(Item.Properties props) { 
        super(props.stacksTo(1)); 
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            SoulNetwork network = NetworkHelper.getSoulNetwork(player);

            if (!SigilBaseItem.Toggleable.isActive(stack) && network.getCurrentEssence() < 100) {
                player.displayClientMessage(Component.literal("Insufficient LP!")
                        .withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(stack);
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {

        if (level.isClientSide || !(entity instanceof ServerPlayer player)) return;

        if (SigilBaseItem.Toggleable.isActive(stack)) {
            handleFlightLogic(stack, player);
        } else {

            if (!isActiveOn(player)) {
                stopFlight(player);
            }
        }
    }

    private void handleFlightLogic(ItemStack stack, ServerPlayer player) {
        SoulNetwork network = NetworkHelper.getSoulNetwork(player);

        if (network.getCurrentEssence() >= LP_COST_PER_TICK * 20) {
            if (!player.getAbilities().mayfly) {
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
            }
            
            if (player.tickCount % 20 == 0) {
                network.syphon(SoulTicket.item(stack, LP_COST_PER_TICK * 20)); 
            }
        } else {
            SigilBaseItem.Toggleable.setActive(stack, false);
            player.displayClientMessage(Component.literal("Insufficient LP! Flight deactivated.")
                    .withStyle(ChatFormatting.RED), true);
            
            if (!isActiveOn(player)) {
                stopFlight(player);
            }
        }
    }

    private void stopFlight(Player player) {
        if (!player.isCreative() && !player.isSpectator()) {
            if (player.getAbilities().mayfly) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }
        }
    }

    public static boolean isActiveOn(Player p) {
        for (ItemStack s : p.getInventory().items) {
            if (s.getItem() instanceof FlightSigilItem && SigilBaseItem.Toggleable.isActive(s)) return true;
        }
        return false;
    }
}