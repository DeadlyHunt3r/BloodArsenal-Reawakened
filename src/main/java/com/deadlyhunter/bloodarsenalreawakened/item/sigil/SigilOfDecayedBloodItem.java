package com.deadlyhunter.bloodarsenalreawakened.item.sigil;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SigilOfDecayedBloodItem extends SigilBaseItem.InstantUse {
    // NBT-Keys
    private static final String TAG_DIM   = "TargetDim";
    private static final String TAG_X     = "TargetX";
    private static final String TAG_Y     = "TargetY";
    private static final String TAG_Z     = "TargetZ";
    private static final String TAG_YAW   = "TargetYaw";
    private static final String TAG_PITCH = "TargetPitch";

    // Teleport-Parameter
    private static final int COOLDOWN_TP = 20; // 1 Sekunde CD nach TP
    private static final double OFFSET_CENTER = 0.5d;

    public SigilOfDecayedBloodItem(Item.Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            // SPEICHERN
            if (!level.isClientSide) {
                saveHere(stack, player);
                player.displayClientMessage(Component.literal("Saved location & dimension."), true);
                player.getCooldowns().addCooldown(this, 6);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        // TELEPORT
        if (!level.isClientSide) {
            boolean ok = teleportToSaved(stack, player);
            if (!ok) {
                player.displayClientMessage(Component.literal("No saved location."), true);
            } else {
                player.getCooldowns().addCooldown(this, COOLDOWN_TP);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    /* ---------------- intern ---------------- */

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

    private static boolean teleportToSaved(ItemStack stack, Player player) {
        if (!(player instanceof ServerPlayer sp) || !hasSaved(stack)) return false;

        var tag = stack.getTag();
        String dimStr = tag.getString(TAG_DIM);
        double x = tag.getDouble(TAG_X);
        double y = tag.getDouble(TAG_Y);
        double z = tag.getDouble(TAG_Z);
        float yaw = tag.contains(TAG_YAW) ? tag.getFloat(TAG_YAW) : sp.getYRot();
        float pitch = tag.contains(TAG_PITCH) ? tag.getFloat(TAG_PITCH) : sp.getXRot();

        // Dimension holen
        ResourceLocation rl = ResourceLocation.tryParse(dimStr);
        if (rl == null) return false;

        ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, rl);
        @Nullable ServerLevel target = sp.server.getLevel(key);
        if (target == null) {
            sp.displayClientMessage(Component.literal("Target dimension not available."), true);
            return false;
        }

        // Sicherheits-Offset zur Blockmitte
        double tx = x + OFFSET_CENTER;
        double ty = y;
        double tz = z + OFFSET_CENTER;

        // Reiten beenden, um Teleport-Probleme zu vermeiden
        if (sp.isPassenger()) sp.stopRiding();

        // Spieler darf während TP nicht schlafen etc.
        sp.setShiftKeyDown(false);

        // Eigentlichen Teleport ausführen
        safeTeleport(sp, target, tx, ty, tz, yaw, pitch);
        return true;
    }

    private static void safeTeleport(ServerPlayer sp, ServerLevel dest, double x, double y, double z, float yaw, float pitch) {
        // Leicht anheben, um suffocation zu vermeiden
        double ty = Math.max(dest.getMinBuildHeight() + 1, y);
        // Teleport API
        sp.teleportTo(dest, x, ty, z, yaw, pitch);
        // Nach dem TP kurze Bewegung syncen
        sp.setDeltaMovement(sp.getDeltaMovement().multiply(0, 0, 0));
        sp.hurtMarked = true;
    }
}
