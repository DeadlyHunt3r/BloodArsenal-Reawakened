package com.deadlyhunter.bloodarsenalreawakened.item.sigil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

public class SigilOfDecayedBloodItem extends SigilBaseItem.InstantUse {
    private static final String TAG_DIM   = "TargetDim";
    private static final String TAG_X     = "TargetX";
    private static final String TAG_Y     = "TargetY";
    private static final String TAG_Z     = "TargetZ";
    private static final String TAG_YAW   = "TargetYaw";
    private static final String TAG_PITCH = "TargetPitch";

    private static final int LP_COST_SAVE = 5000;
    private static final int LP_COST_TELEPORT = 5000;
    
    private static final int COOLDOWN_TP = 20; 
    private static final double OFFSET_CENTER = 0.5d;

    public SigilOfDecayedBloodItem(Item.Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            SoulNetwork network = NetworkHelper.getSoulNetwork(serverPlayer);

            if (serverPlayer.isShiftKeyDown()) {

                if (network.getCurrentEssence() < LP_COST_SAVE) {
                    serverPlayer.displayClientMessage(Component.literal("Insufficient LP to save location! Need " + LP_COST_SAVE + " LP")
                            .withStyle(ChatFormatting.RED), true);
                    return InteractionResultHolder.fail(stack);
                }

                network.syphon(SoulTicket.item(stack, LP_COST_SAVE));
                saveHere(stack, serverPlayer);
                
                serverPlayer.displayClientMessage(Component.literal("Location stored!")
                        .withStyle(ChatFormatting.GREEN), true);
                serverPlayer.getCooldowns().addCooldown(this, 10);
                return InteractionResultHolder.sidedSuccess(stack, false);
            }

            if (!hasSaved(stack)) {
                serverPlayer.displayClientMessage(Component.literal("No location saved!")
                        .withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(stack);
            }

            if (network.getCurrentEssence() < LP_COST_TELEPORT) {
                serverPlayer.displayClientMessage(Component.literal("Insufficient LP to teleport! Need " + LP_COST_TELEPORT + " LP")
                        .withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(stack);
            }

            boolean success = teleportToSaved(stack, serverPlayer, network);
            if (success) {
                serverPlayer.getCooldowns().addCooldown(this, COOLDOWN_TP);
                return InteractionResultHolder.sidedSuccess(stack, false);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private static void saveHere(ItemStack stack, Player p) {
        var tag = stack.getOrCreateTag();
        ResourceLocation dimKey = p.level().dimension().location();
        tag.putString(TAG_DIM, dimKey.toString());
        tag.putDouble(TAG_X, p.getX());
        tag.putDouble(TAG_Y, p.getY());
        tag.putDouble(TAG_Z, p.getZ());
        tag.putFloat(TAG_YAW, p.getYRot());
        tag.putFloat(TAG_PITCH, p.getXRot());
    }

    private static boolean hasSaved(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(TAG_DIM);
    }

    private static boolean teleportToSaved(ItemStack stack, ServerPlayer sp, SoulNetwork network) {
        var tag = stack.getTag();
        if (tag == null) return false;

        String dimStr = tag.getString(TAG_DIM);
        double x = tag.getDouble(TAG_X);
        double y = tag.getDouble(TAG_Y);
        double z = tag.getDouble(TAG_Z);
        float yaw = tag.getFloat(TAG_YAW);
        float pitch = tag.getFloat(TAG_PITCH);

        ResourceLocation rl = ResourceLocation.tryParse(dimStr);
        if (rl == null) return false;

        ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, rl);
        @Nullable ServerLevel target = sp.server.getLevel(key);
        
        if (target == null) {
            sp.displayClientMessage(Component.literal("Target dimension not available.")
                    .withStyle(ChatFormatting.RED), true);
            return false;
        }

        network.syphon(SoulTicket.item(stack, LP_COST_TELEPORT));

        if (sp.isPassenger()) sp.stopRiding();
        sp.setShiftKeyDown(false);
        
        double tx = x + OFFSET_CENTER;
        double ty = Math.max(target.getMinBuildHeight() + 1, y);
        double tz = z + OFFSET_CENTER;

        sp.teleportTo(target, tx, ty, tz, yaw, pitch);
        sp.setDeltaMovement(0, 0, 0);
        sp.hurtMarked = true;
        
        return true;
    }
}