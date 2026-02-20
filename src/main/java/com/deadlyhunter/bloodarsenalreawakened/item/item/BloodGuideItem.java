package com.deadlyhunter.bloodarsenalreawakened.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.PatchouliAPI;

public class BloodGuideItem extends Item {
    public BloodGuideItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            // Debug-Nachricht zur Bestätigung
            player.sendSystemMessage(Component.literal("Öffne Buch: blood_guide...").withStyle(ChatFormatting.YELLOW));
            
            try {
                // Versucht das Buch mit der ID 'blood_guide' zu öffnen
                PatchouliAPI.get().openBookGUI(serverPlayer, new ResourceLocation("bloodarsenalreawakened", "blood_guide"));
            } catch (Exception e) {
                player.sendSystemMessage(Component.literal("Patchouli Fehler: " + e.getMessage()).withStyle(ChatFormatting.RED));
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}