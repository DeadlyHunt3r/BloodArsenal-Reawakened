package com.deadlyhunter.bloodarsenalreawakened.item.sigil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

public class EnderSigilItem extends SigilBaseItem.InstantUse {

    private static final double MAX_RANGE = 64.0D;
    private static final double FORWARD_FALLBACK = 20.0D;
    private static final int COOLDOWN = 8;
    
    private static final int COST_TELEPORT = 250;
    private static final int COST_ENDER_CHEST = 100;

    public EnderSigilItem(Item.Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            SoulNetwork network = NetworkHelper.getSoulNetwork(serverPlayer);

            if (serverPlayer.isShiftKeyDown()) {
                if (network.getCurrentEssence() < COST_ENDER_CHEST) {

                    serverPlayer.displayClientMessage(Component.literal("Insufficient LP!").withStyle(ChatFormatting.RED), true);
                    return InteractionResultHolder.fail(stack);
                }
                
                network.syphon(SoulTicket.item(stack, COST_ENDER_CHEST));
                openEnderChest(serverPlayer);
                serverPlayer.getCooldowns().addCooldown(this, 6);
                return InteractionResultHolder.sidedSuccess(stack, false);
            }

            if (network.getCurrentEssence() < COST_TELEPORT) {

                serverPlayer.displayClientMessage(Component.literal("Insufficient LP!").withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(stack);
            }

            network.syphon(SoulTicket.item(stack, COST_TELEPORT));
            teleportWhereLooking((ServerLevel) level, serverPlayer);
            serverPlayer.getCooldowns().addCooldown(this, COOLDOWN);
            return InteractionResultHolder.sidedSuccess(stack, false);
        }
        
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private static void openEnderChest(ServerPlayer sp) {
        PlayerEnderChestContainer ender = sp.getEnderChestInventory();
        sp.openMenu(new SimpleMenuProvider((id, inv, p) -> ChestMenu.threeRows(id, inv, ender),
                Component.translatable("container.enderchest")));
    }

    private static void teleportWhereLooking(ServerLevel level, ServerPlayer sp) {
        BlockHitResult hit = raycast(level, sp, MAX_RANGE);
        Vec3 target;
        if (hit != null) {
            var bp = hit.getBlockPos();
            target = new Vec3(bp.getX() + 0.5, bp.getY() + 1.01, bp.getZ() + 0.5);
        } else {
            target = endVec(sp, FORWARD_FALLBACK);
        }

        Vec3 safe = findSafeSpot(level, target, 6);
        doTeleport(sp, safe);
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

    private static Vec3 findSafeSpot(ServerLevel level, Vec3 base, int upScan) {
        double x = base.x;
        double z = base.z;
        int y = (int)Math.floor(base.y);
        y = Math.max(level.getMinBuildHeight()+1, Math.min(level.getMaxBuildHeight()-2, y));

        for (int dy = 0; dy <= upScan; dy++) {
            int yy = y + dy;
            if (isEmpty(level, x, yy, z) && isEmpty(level, x, yy + 1, z)) {
                return new Vec3(x, yy, z);
            }
        }
        return new Vec3(x, y, z);
    }

    private static boolean isEmpty(ServerLevel level, double x, int y, double z) {
        var pos = net.minecraft.core.BlockPos.containing(x, y, z);
        BlockState st = level.getBlockState(pos);
        return st.isAir() || st.getCollisionShape(level, pos).isEmpty();
    }

    private static void doTeleport(ServerPlayer sp, Vec3 pos) {
        ServerLevel dest = sp.serverLevel();
        if (sp.isPassenger()) sp.stopRiding();
        sp.fallDistance = 0;
        sp.teleportTo(dest, pos.x, pos.y, pos.z, sp.getYRot(), sp.getXRot());
        sp.setDeltaMovement(0, 0, 0);
        sp.hurtMarked = true;
    }
}