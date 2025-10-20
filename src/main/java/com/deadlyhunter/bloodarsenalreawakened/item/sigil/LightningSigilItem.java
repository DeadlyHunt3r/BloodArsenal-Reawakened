package com.deadlyhunter.bloodarsenalreawakened.item.sigil;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class LightningSigilItem extends SigilBaseItem.InstantUse {
    public static final String TAG_LEVEL = "LightningLevel";
    private static final int MAX_LEVEL = 5;
    private static final int MIN_LEVEL = 1;
    private static final double RANGE = 64.0D;

    public LightningSigilItem(Item.Properties props) {
        super(props.stacksTo(1));
    }

    public static int getLevel(ItemStack stack) {
        int lvl = stack.getOrCreateTag().getInt(TAG_LEVEL);
        if (lvl < MIN_LEVEL) lvl = MIN_LEVEL;
        if (lvl > MAX_LEVEL) lvl = MAX_LEVEL;
        return lvl == 0 ? 1 : lvl;
    }

    public static void setLevel(ItemStack stack, int level) {
        int clamped = Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, level));
        stack.getOrCreateTag().putInt(TAG_LEVEL, clamped);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // SHIFT + Rechtsklick => Stufe wechseln
        if (player.isShiftKeyDown()) {
            int next = getLevel(stack) + 1;
            if (next > MAX_LEVEL) next = MIN_LEVEL;
            setLevel(stack, next);
            if (!level.isClientSide) {
                player.displayClientMessage(net.minecraft.network.chat.Component.literal("Lightning Sigil Level: " + next), true);
            }
            // Kleiner Cooldown
            player.getCooldowns().addCooldown(this, 6);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }


        if (!level.isClientSide) {
            BlockHitResult hit = raycast(level, player, RANGE);
            Vec3 pos = hit != null ? hit.getLocation() : endVec(player, RANGE);
            int strikes = getLevel(stack);

            for (int i = 0; i < strikes; i++) {

                double ox = (strikes == 1) ? 0 : (level.random.nextDouble() - 0.5) * 1.5;
                double oz = (strikes == 1) ? 0 : (level.random.nextDouble() - 0.5) * 1.5;

                spawnLightning((ServerLevel) level, pos.x + ox, pos.y, pos.z + oz, player);
            }
            // Cooldown skaliert mit Stufe, um Spam zu vermeiden
            player.getCooldowns().addCooldown(this, 10 + strikes * 4);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private static BlockHitResult raycast(Level level, Player player, double range) {
        Vec3 start = player.getEyePosition(1.0F);
        Vec3 end = endVec(player, range);
        ClipContext ctx = new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player);
        BlockHitResult hit = level.clip(ctx);
        return hit.getType() != HitResult.Type.MISS ? hit : null;
    }

    private static Vec3 endVec(Player player, double range) {
        Vec3 start = player.getEyePosition(1.0F);
        Vec3 dir = player.getViewVector(1.0F);
        return start.add(dir.scale(range));
    }

    private static void spawnLightning(ServerLevel level, double x, double y, double z, Player cause) {
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt == null) return;
        bolt.moveTo(x, y, z);
        if (cause instanceof ServerPlayer sp) bolt.setCause(sp);
        level.addFreshEntity(bolt);
    }
}
