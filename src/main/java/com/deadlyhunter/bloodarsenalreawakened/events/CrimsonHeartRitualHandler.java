package com.deadlyhunter.bloodarsenalreawakened.events;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import com.deadlyhunter.bloodarsenalreawakened.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BARMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CrimsonHeartRitualHandler {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (player.level().isClientSide || !player.isShiftKeyDown()) return;

        ServerLevel level = (ServerLevel) player.level();

        if (stack.is(Items.NETHER_STAR)) {
            consumeItem(player, stack);
            spawnEffects(level, player, ParticleTypes.DAMAGE_INDICATOR, SoundEvents.WITHER_SPAWN);
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 3600, 1));
            giveHeart(player, new ItemStack(ModItems.CRIMSON_HEART.get()));
            
            player.displayClientMessage(Component.literal("The star devours your essence... A Crimson Heart is born!")
                    .withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD), true);
            event.setCanceled(true);
        }

        else if (stack.is(ModItems.BLEEDING_DRAGON.get())) {
            if (level.isThundering()) {
                consumeItem(player, stack);
                
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                if (lightning != null) {
                    lightning.moveTo(player.position());
                    level.addFreshEntity(lightning);
                }

                level.sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 1, player.getZ(), 40, 0.5, 1.0, 0.5, 0.1);
                level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0f, 1.5f);
                
                giveHeart(player, new ItemStack(ModItems.HEART_OF_ASCENSION.get()));

                player.displayClientMessage(Component.literal("The heavens roar as the Heart of Ascension is forged!")
                        .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD, ChatFormatting.ITALIC), true);
            } else {
                player.displayClientMessage(Component.literal("The sky remains silent. A storm is required...")
                        .withStyle(ChatFormatting.GRAY), true);
            }
            event.setCanceled(true);
        }
    }
	
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (!player.level().isClientSide && stack.is(ModItems.DRAGON_CLAW.get()) && player.isShiftKeyDown()) {
            if (event.getTarget() instanceof Animal animal) {
                ServerLevel level = (ServerLevel) player.level();

                consumeItem(player, stack);
                level.sendParticles(ParticleTypes.HEART, animal.getX(), animal.getY() + 0.5, animal.getZ(), 30, 0.3, 0.5, 0.3, 0.1);
                level.playSound(null, animal.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0f, 0.5f);
                animal.kill();

                player.getFoodData().setFoodLevel(0);
                player.getFoodData().setSaturation(0);

                giveHeart(player, new ItemStack(ModItems.SANGUINE_HEART.get()));

                player.displayClientMessage(Component.literal("A life for a heart. The blood sacrifice is complete!")
                        .withStyle(ChatFormatting.RED, ChatFormatting.BOLD), true);

                event.setCanceled(true);
            }
        }
    }

    private static void consumeItem(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) stack.shrink(1);
    }

    private static void giveHeart(Player player, ItemStack heart) {
        if (!player.getInventory().add(heart)) {
            player.drop(heart, false);
        }
    }

    private static void spawnEffects(ServerLevel level, Player player, net.minecraft.core.particles.ParticleOptions particle, net.minecraft.sounds.SoundEvent sound) {
        level.sendParticles(particle, player.getX(), player.getY() + 1.1, player.getZ(), 30, 0.3, 0.5, 0.3, 0.1);
        level.playSound(null, player.blockPosition(), sound, SoundSource.PLAYERS, 0.7f, 0.5f);
    }
}