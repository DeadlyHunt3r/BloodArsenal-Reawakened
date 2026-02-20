package com.deadlyhunter.bloodarsenalreawakened.item.sigil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.core.data.SoulTicket;

import java.util.List;

public class LightningSigilItem extends SigilBaseItem.InstantUse {
    public static final String TAG_LEVEL = "LightningLevel";
    private static final int MAX_LEVEL = 5;
    private static final int MIN_LEVEL = 1;
    private static final double RANGE = 64.0D;
    
    // Basis-Kosten pro Blitz
    private static final int BASE_LP_COST = 500;

    public LightningSigilItem(Item.Properties props) {
        super(props.stacksTo(1));
    }

    public static int getLevel(ItemStack stack) {
        if (!stack.hasTag()) return MIN_LEVEL;
        int lvl = stack.getOrCreateTag().getInt(TAG_LEVEL);
        return Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, lvl == 0 ? 1 : lvl));
    }

    public static void setLevel(ItemStack stack, int level) {
        int clamped = Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, level));
        stack.getOrCreateTag().putInt(TAG_LEVEL, clamped);
    }

    public int getLpCost(int level) {
        return BASE_LP_COST * (level * level);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            int next = getLevel(stack) + 1;
            if (next > MAX_LEVEL) next = MIN_LEVEL;
            setLevel(stack, next);
            
            if (!level.isClientSide) {
                player.displayClientMessage(Component.literal("Lightning Sigil Level: " + next)
                        .withStyle(ChatFormatting.AQUA), true);
            }
            player.getCooldowns().addCooldown(this, 6);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            int currentLevel = getLevel(stack);
            int totalCost = getLpCost(currentLevel);
            SoulNetwork network = NetworkHelper.getSoulNetwork(serverPlayer);

            if (network.getCurrentEssence() < totalCost) {
                player.displayClientMessage(Component.literal("Insufficient LP! Need " + totalCost + " LP")
                        .withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(stack);
            }

            network.syphon(SoulTicket.item(stack, totalCost));

            BlockHitResult hit = raycast(level, player, RANGE);
            Vec3 pos = hit != null ? hit.getLocation() : endVec(player, RANGE);

            for (int i = 0; i < currentLevel; i++) {
                double ox = (currentLevel == 1) ? 0 : (level.random.nextDouble() - 0.5) * 2.0;
                double oz = (currentLevel == 1) ? 0 : (level.random.nextDouble() - 0.5) * 2.0;
                spawnLightning((ServerLevel) level, pos.x + ox, pos.y, pos.z + oz, serverPlayer);
            }

            player.getCooldowns().addCooldown(this, 10 + currentLevel * 5);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int lvl = getLevel(stack);
        tooltip.add(Component.literal("Current Level: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(String.valueOf(lvl)).withStyle(ChatFormatting.AQUA)));
        tooltip.add(Component.literal("Cost: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(getLpCost(lvl) + " LP").withStyle(ChatFormatting.RED)));
        
        super.appendHoverText(stack, level, tooltip, flag);
    }

    private static BlockHitResult raycast(Level level, Player player, double range) {
        Vec3 start = player.getEyePosition(1.0F);
        Vec3 end = endVec(player, range);
        ClipContext ctx = new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player);
        return level.clip(ctx);
    }

    private static Vec3 endVec(Player player, double range) {
        Vec3 start = player.getEyePosition(1.0F);
        Vec3 dir = player.getViewVector(1.0F);
        return start.add(dir.scale(range));
    }

    private static void spawnLightning(ServerLevel level, double x, double y, double z, ServerPlayer cause) {
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt == null) return;
        bolt.moveTo(x, y, z);
        bolt.setCause(cause);
        level.addFreshEntity(bolt);
    }
}