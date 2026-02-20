package com.deadlyhunter.bloodarsenalreawakened.item.sigil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

public class SigilOfTheBloodMoonItem extends SigilBaseItem.InstantUse {

    private static final int LP_COST_TIME_CHANGE = 5000;
    
    private static final long TIME_MIDNIGHT = 18000L;
    private static final long TIME_DAY = 1000L;

    public SigilOfTheBloodMoonItem(Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            SoulNetwork network = NetworkHelper.getSoulNetwork(serverPlayer);

            if (network.getCurrentEssence() < LP_COST_TIME_CHANGE) {
                serverPlayer.displayClientMessage(Component.literal("Insufficient LP!")
                        .withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(stack);
            }

            ServerLevel serverLevel = (ServerLevel) level;
            
            if (serverPlayer.isShiftKeyDown()) {
                serverLevel.setDayTime(TIME_DAY);
                serverPlayer.displayClientMessage(Component.literal("The sun rises...")
                        .withStyle(ChatFormatting.YELLOW), true);
            } else {
                serverLevel.setDayTime(TIME_MIDNIGHT);
                serverPlayer.displayClientMessage(Component.literal("The Blood Moon approaches...")
                        .withStyle(ChatFormatting.DARK_RED), true);
            }

            network.syphon(SoulTicket.item(stack, LP_COST_TIME_CHANGE));
            
            serverPlayer.getCooldowns().addCooldown(this, 100);
            
            return InteractionResultHolder.sidedSuccess(stack, false);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}