package com.deadlyhunter.bloodarsenalreawakened.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

import javax.annotation.Nullable;
import java.util.List;

public class SanguineScytheItem extends SwordItem {
    public SanguineScytheItem(Tier tier, int attackDamage, float attackSpeed, Properties props) {

        super(tier, attackDamage, attackSpeed, props);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && !player.level().isClientSide) {

            if (!target.isAlive() || target.getHealth() <= 0) {
                SoulNetwork network = NetworkHelper.getSoulNetwork(player);
                network.addLifeEssence(100, network.getOrbTier() * 10000);
            }
        }
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                double radius = 5.0;
                List<Entity> entities = level.getEntities(player, player.getBoundingBox().inflate(radius));
                
                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity target && target != player) {
                        target.hurt(level.damageSources().playerAttack(player), 8.0f);
                        target.knockback(1.0, player.getX() - target.getX(), player.getZ() - target.getZ());
                        
                        if (!target.isAlive()) {
                            SoulNetwork network = NetworkHelper.getSoulNetwork(player);
                            network.addLifeEssence(100, network.getOrbTier() * 10000);
                        }
                    }
                }

                ServerLevel serverLevel = (ServerLevel) level;
                for (double i = 0; i < Math.PI * 2; i += Math.PI / 16) {
                    double x = Math.cos(i) * 3.5;
                    double z = Math.sin(i) * 3.5;
                    serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + x, player.getY() + 0.5, player.getZ() + z, 1, 0, 0, 0, 0);
                    serverLevel.sendParticles(ParticleTypes.DAMAGE_INDICATOR, player.getX() + x, player.getY() + 0.8, player.getZ() + z, 1, 0.1, 0.1, 0.1, 0.02);
                }
                
                player.hurt(level.damageSources().magic(), 2.0f); 
                player.getCooldowns().addCooldown(this, 100); 
            }
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.blood_arsenal_reawakened.sanguine_scythe.title").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("tooltip.blood_arsenal_reawakened.sanguine_scythe.desc").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}