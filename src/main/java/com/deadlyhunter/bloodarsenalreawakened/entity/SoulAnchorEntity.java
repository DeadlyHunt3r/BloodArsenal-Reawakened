package com.deadlyhunter.bloodarsenalreawakened.entity;

import com.deadlyhunter.bloodarsenalreawakened.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SoulAnchorEntity extends LivingEntity {

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(SoulAnchorEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private int ritualTimer = 0;
    private static final int RITUAL_DURATION = 100;

    public SoulAnchorEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UUID, Optional.empty());
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(0, Math.sin(this.tickCount * 0.1) * 0.015, 0);

        if (!this.level().isClientSide) {
            checkRitualConditions();
        } else if (ritualTimer > 0) {
            spawnRitualParticles();
        }
    }

    private void checkRitualConditions() {
        Level level = this.level();
        List<ItemEntity> nearbyItems = level.getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(3.0));
        
        ItemEntity ingredient = null;
        ItemEntity baseWeapon = null;
        ItemStack reward = ItemStack.EMPTY;

        for (ItemEntity itemEnt : nearbyItems) {
            ItemStack stack = itemEnt.getItem();
            ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
            if (registryName == null) continue;

            if (stack.is(ModItems.ESSENCE_OF_DECAY.get())) {
                for (ItemEntity other : nearbyItems) {
                    ResourceLocation otherName = ForgeRegistries.ITEMS.getKey(other.getItem().getItem());
                    if (otherName != null && (otherName.toString().contains("soulscythe") || otherName.toString().contains("sentient_scythe"))) {
                        ingredient = itemEnt;
                        baseWeapon = other;
                        reward = new ItemStack(ModItems.SANGUINE_SCYTHE.get());
                    }
                }
            }

            if (stack.is(ModItems.BLEEDING_DRAGON.get())) {
                for (ItemEntity other : nearbyItems) {
                    ResourceLocation otherName = ForgeRegistries.ITEMS.getKey(other.getItem().getItem());
                    if (otherName != null && (otherName.toString().contains("soulsword") || otherName.toString().contains("sentient_sword"))) {
                        ingredient = itemEnt;
                        baseWeapon = other;
                        reward = new ItemStack(ModItems.BLOOD_BOUND_EXECUTIONER.get());
                    }
                }
            }
        }

        if (ingredient != null && baseWeapon != null && !reward.isEmpty()) {
            ritualTimer++;
            pullItemToCenter(ingredient);
            pullItemToCenter(baseWeapon);

            if (ritualTimer % 20 == 0) {
                level.playSound(null, this.blockPosition(), SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 1.0f, 0.5f + (ritualTimer / 100f));
            }

            if (ritualTimer >= RITUAL_DURATION) {
                performRitualFinal(ingredient, baseWeapon, reward);
                ritualTimer = 0;
            }
        } else {
            ritualTimer = 0;
        }
    }

    private void pullItemToCenter(ItemEntity item) {
        Vec3 center = this.position().add(0, 0.5, 0);
        Vec3 motion = center.subtract(item.position()).normalize().scale(0.12);
        item.setDeltaMovement(motion);
    }

    private void spawnRitualParticles() {
        double angle = this.tickCount * 0.4;
        double x = Math.cos(angle) * 1.2;
        double z = Math.sin(angle) * 1.2;
        this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX() + x, this.getY() + 0.5, this.getZ() + z, 0, 0.08, 0);
        if (this.tickCount % 2 == 0) {
            this.level().addParticle(ParticleTypes.WITCH, this.getX() - x, this.getY() + 0.5, this.getZ() - z, 0, 0.1, 0);
        }
    }

    private void performRitualFinal(ItemEntity ing, ItemEntity base, ItemStack rewardStack) {
        ServerLevel serverLevel = (ServerLevel) this.level();
        ing.getItem().shrink(1);
        base.getItem().shrink(1);

        serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY() + 0.5, this.getZ(), 1, 0, 0, 0, 0);
        serverLevel.sendParticles(ParticleTypes.SOUL, this.getX(), this.getY() + 0.5, this.getZ(), 150, 0.4, 0.4, 0.4, 0.15);
        
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
        if (lightning != null) {
            lightning.moveTo(this.position());
            lightning.setVisualOnly(true);
            serverLevel.addFreshEntity(lightning);
        }

        serverLevel.playSound(null, this.blockPosition(), SoundEvents.WITHER_DEATH, SoundSource.BLOCKS, 1.2f, 0.5f);

        ItemEntity reward = new ItemEntity(serverLevel, this.getX(), this.getY() + 0.5, this.getZ(), rewardStack);
        reward.setNoPickUpDelay();
        serverLevel.addFreshEntity(reward);
        this.discard();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hasBeenHurt = super.hurt(source, amount);

        if (hasBeenHurt && !this.level().isClientSide) {
            UUID ownerUUID = this.getOwnerUUID();
            if (ownerUUID != null) {
                Player owner = this.level().getServer().getPlayerList().getPlayer(ownerUUID);
                if (owner != null) {

                    owner.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                    owner.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
                    
                    owner.sendSystemMessage(Component.literal("Your Soul Anchor is under attack! You feel your essence shattering...")
                            .withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));
                    
                    owner.playNotifySound(SoundEvents.WITHER_HURT, SoundSource.PLAYERS, 0.5f, 0.5f);
                }
            }
        }
        return hasBeenHurt;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!this.level().isClientSide && isOwner(player)) {
            this.spawnAtLocation(ModItems.SOUL_ANCHOR.get());
            this.discard();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    public void setOwner(UUID uuid) { this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid)); }
    public UUID getOwnerUUID() { return this.entityData.get(OWNER_UUID).orElse(null); }
    public boolean isOwner(Player player) { return player.getUUID().equals(getOwnerUUID()); }

    @Override public void addAdditionalSaveData(CompoundTag nbt) { super.addAdditionalSaveData(nbt); if (getOwnerUUID() != null) nbt.putUUID("OwnerUUID", getOwnerUUID()); }
    @Override public void readAdditionalSaveData(CompoundTag nbt) { super.readAdditionalSaveData(nbt); if (nbt.hasUUID("OwnerUUID")) setOwner(nbt.getUUID("OwnerUUID")); }
    @Override public Iterable<ItemStack> getArmorSlots() { return Collections.emptyList(); }
    @Override public ItemStack getItemBySlot(EquipmentSlot slot) { return ItemStack.EMPTY; }
    @Override public void setItemSlot(EquipmentSlot slot, ItemStack stack) {}
    @Override public HumanoidArm getMainArm() { return HumanoidArm.RIGHT; }
}