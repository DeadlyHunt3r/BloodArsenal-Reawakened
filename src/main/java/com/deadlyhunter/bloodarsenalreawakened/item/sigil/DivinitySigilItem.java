package com.deadlyhunter.bloodarsenalreawakened.item.sigil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

public class DivinitySigilItem extends SigilBaseItem.Toggleable {

    private static final int LP_COST_PER_TICK = 20;

    public DivinitySigilItem(Item.Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            SoulNetwork network = NetworkHelper.getSoulNetwork(player);
            if (!SigilBaseItem.Toggleable.isActive(stack) && network.getCurrentEssence() < 400) {
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
            handleDivinityLogic(stack, player);
        } else {
            if (!isActiveOn(player)) {
                stopDivinityEffects(player);
            }
        }
    }

    private void handleDivinityLogic(ItemStack stack, ServerPlayer player) {
        SoulNetwork network = NetworkHelper.getSoulNetwork(player);

        if (network.getCurrentEssence() >= LP_COST_PER_TICK * 20) {
            applyEffects(player);

            if (player.tickCount % 20 == 0) {
                network.syphon(SoulTicket.item(stack, LP_COST_PER_TICK * 20));
            }
        } else {
            SigilBaseItem.Toggleable.setActive(stack, false);
            player.displayClientMessage(Component.literal("Your divine protection fades! (Insufficient LP)")
                    .withStyle(ChatFormatting.RED), true);
            
            if (!isActiveOn(player)) {
                stopDivinityEffects(player);
            }
        }
    }

    private void applyEffects(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 30, 4, false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 30, 0, false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 30, 1, false, false, true));
        
        if (!player.isInvulnerable()) {
            player.setInvulnerable(true);
        }
    }

    private void stopDivinityEffects(Player player) {
        if (!player.isCreative() && !player.isSpectator()) {
            player.removeEffect(MobEffects.DAMAGE_RESISTANCE);
            player.removeEffect(MobEffects.SATURATION);
            player.removeEffect(MobEffects.REGENERATION);
            player.setInvulnerable(false);
        }
    }

    public static boolean isActiveOn(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof DivinitySigilItem && SigilBaseItem.Toggleable.isActive(stack)) return true;
        }
        return false;
    }
}