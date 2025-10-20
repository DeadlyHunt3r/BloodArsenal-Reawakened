package com.deadlyhunter.bloodarsenalreawakened.item.sigil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class SigilBaseItem extends Item {
    public SigilBaseItem(Properties props) { super(props); }


@Override
public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
    String path = ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();
    tooltip.add(Component.translatable("tooltip.bloodarsenalreawakened." + path + ".desc")
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
}


    public static class Toggleable extends SigilBaseItem {
        public static final String TAG_ACTIVATED = "Activated";

        protected int lpCostPerUse = 0; // aktuell 0 = aus

        public Toggleable(Properties props) { super(props.stacksTo(1)); }

        public static boolean isActive(ItemStack stack) { return stack.getOrCreateTag().getBoolean(TAG_ACTIVATED); }
        public static void setActive(ItemStack stack, boolean v) { stack.getOrCreateTag().putBoolean(TAG_ACTIVATED, v); }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
            ItemStack stack = player.getItemInHand(hand);

            if (!level.isClientSide) {

                setActive(stack, !isActive(stack));
                player.displayClientMessage(Component.literal(getDescription().getString() + ": " + (isActive(stack) ? "Activated" : "Deactivated")), true);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }
    }


    public static class InstantUse extends SigilBaseItem {
        protected int lpCost = 0;
        public InstantUse(Properties props) { super(props.stacksTo(1)); }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
            ItemStack stack = player.getItemInHand(hand);
            if (!level.isClientSide) {
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }
    }
}
