package com.deadlyhunter.bloodarsenalreawakened.item.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.core.particles.DustParticleOptions;
import org.joml.Vector3f;
import net.minecraft.core.particles.ParticleTypes;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

public class BloodBoundExecutionerItem extends SwordItem {

    private static final int LP_COST_SPEAR = 1500;
    private static final int LP_GAIN_EXECUTION = 500;

    public BloodBoundExecutionerItem(Tier tier, int attackDamage, float attackSpeed, Properties props) {
        super(tier, attackDamage, attackSpeed, props);
    }

@Override
public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    if (!attacker.level().isClientSide && attacker instanceof Player player) {

        float damageDealt = (float) player.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE);
        float predictedHealth = target.getHealth() - damageDealt;
        float healthRatio = predictedHealth / target.getMaxHealth();

        if ((healthRatio < 0.20F || target.getHealth() / target.getMaxHealth() < 0.20F) && target.isAlive()) {

            target.setHealth(0);
            target.die(player.damageSources().genericKill());

            SoulNetwork network = NetworkHelper.getSoulNetwork(player);
            network.setCurrentEssence(network.getCurrentEssence() + LP_GAIN_EXECUTION);

            player.displayClientMessage(Component.literal("Executed! +500 LP")
                    .withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD), true);

            if (player.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.sendParticles(
                    net.minecraft.core.particles.ParticleTypes.DAMAGE_INDICATOR,
                    target.getX(), target.getY() + 1, target.getZ(),
                    15, 0.2, 0.2, 0.2, 0.1
                );
            }
            return true;
        }
    }
    return super.hurtEnemy(stack, target, attacker);
}

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.isShiftKeyDown()) {
                SoulNetwork network = NetworkHelper.getSoulNetwork(serverPlayer);
                if (network.getCurrentEssence() < LP_COST_SPEAR) {
                    serverPlayer.displayClientMessage(Component.literal("Not enough blood for a Spear!").withStyle(ChatFormatting.RED), true);
                    return InteractionResultHolder.fail(stack);
                }
                network.syphon(SoulTicket.item(stack, LP_COST_SPEAR));
                fireBloodSpear(serverPlayer);
                serverPlayer.getCooldowns().addCooldown(this, 30);
                return InteractionResultHolder.sidedSuccess(stack, false);
            }
        }
        return InteractionResultHolder.pass(stack);
    }
	
	@Override
		public boolean isBarVisible(ItemStack stack) {
			return false;
	}
	
	@Override
		public boolean canBeDepleted() {
			return false;
	}

	@Override
		public boolean isEnchantable(ItemStack stack) {
			return true; 
	}

	private void fireBloodSpear(ServerPlayer player) {
		double range = 20.0;

		net.minecraft.world.phys.Vec3 start = player.getEyePosition(1.0F);

		net.minecraft.world.phys.Vec3 look = player.getViewVector(1.0F);
    
		for (double i = 0; i < range; i += 0.5) {
        net.minecraft.world.phys.Vec3 particlePos = start.add(look.scale(i));
        
        player.serverLevel().sendParticles(
            new net.minecraft.core.particles.DustParticleOptions(new org.joml.Vector3f(0.8F, 0.0F, 0.0F), 1.5F),
            particlePos.x, particlePos.y, particlePos.z, 
            2,
            0.05, 0.05, 0.05,
            0.0
        );
    }

    net.minecraft.world.phys.EntityHitResult hit = getEntityLookTarget(player, range);
    if (hit != null && hit.getEntity() instanceof LivingEntity target) {
        target.hurt(player.damageSources().magic(), 15.0F);
        target.setSecondsOnFire(3);

        player.serverLevel().sendParticles(
            net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE,
            target.getX(), target.getY() + 1, target.getZ(),
            10, 0.2, 0.2, 0.2, 0.05
        );
    }
}

    private net.minecraft.world.phys.EntityHitResult getEntityLookTarget(Player player, double range) {
        net.minecraft.world.phys.Vec3 start = player.getEyePosition(1.0F);
        net.minecraft.world.phys.Vec3 dir = player.getViewVector(1.0F).scale(range);
        net.minecraft.world.phys.Vec3 end = start.add(dir);
        net.minecraft.world.phys.AABB aabb = player.getBoundingBox().expandTowards(dir).inflate(1.0D);
        return net.minecraft.world.entity.projectile.ProjectileUtil.getEntityHitResult(
                player.level(), player, start, end, aabb, (e) -> e instanceof LivingEntity && e != player);
    }
}