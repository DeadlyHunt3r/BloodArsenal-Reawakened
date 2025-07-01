package deadlyhunter.bloodarsenalreawakened.common.item.tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ItemTier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class BloodstormCleaverItem extends SwordItem {
    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("5c53e446-054e-4f4f-b8b9-9eaf4e2efee4");

    public BloodstormCleaverItem(Properties properties) {
        super(ItemTier.NETHERITE, 0, -2.4F, properties);
    }

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hitEntity(stack, target, attacker);
    
		if (!attacker.world.isRemote && attacker instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) attacker;
			CompoundNBT tag = stack.getOrCreateTag();

			// BINDING
			if (!tag.contains("OwnerUUID")) {
				tag.putString("OwnerUUID", player.getUniqueID().toString());
				player.sendStatusMessage(new StringTextComponent(
						TextFormatting.GREEN + "You are now the owner of the Bloodstorm Cleaver."), true);
				stack.setTag(tag);
			}

			// LEVEL-UP SYSTEM
			if (!target.isAlive()) {
				int level = tag.getInt("Level");
				if (level < 100) {
					int kills = tag.getInt("KillCount") + 1;
					int required = getRequiredKills(level + 1);
					if (kills >= required) {
						level++;
						kills = 0;
						player.sendMessage(new StringTextComponent(TextFormatting.DARK_RED +
								"Bloodstorm Cleaver leveled up to Level " + level + "!"), player.getUniqueID());
					}
					tag.putInt("KillCount", kills);
				}
				tag.putInt("Level", level);
				stack.setTag(tag);
			}
		}
		return result;
	}

    private int getRequiredKills(int level) {
        return level * 10;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> base = super.getAttributeModifiers(slot, stack);
        if (slot == EquipmentSlotType.MAINHAND) {
            CompoundNBT tag = stack.getOrCreateTag();
            int level = tag.getInt("Level");
            double damage = 7.0 + (level / 100.0) * 93.0;
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(base);
            builder.put(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(ATTACK_DAMAGE_UUID, "Attack Damage", damage, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return base;
    }


    public static void onPlayerHoldingTick(PlayerEntity player, ItemStack stack) {
        if (player.world.isRemote) return;
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains("OwnerUUID") && !player.getUniqueID().toString().equals(tag.getString("OwnerUUID"))) {
            player.attackEntityFrom(DamageSource.MAGIC.setDamageBypassesArmor(), 2.0F);
            if (player.world.getGameTime() % 20 == 0) {
                player.sendStatusMessage(new StringTextComponent(
                        TextFormatting.RED + "You are not the owner! The Cleaver burns your hand!"), true);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (selected && entity instanceof PlayerEntity) {
            onPlayerHoldingTick((PlayerEntity) entity, stack);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        CompoundNBT tag = stack.getOrCreateTag();
        int level = tag.getInt("Level");
        tooltip.add(new StringTextComponent(TextFormatting.DARK_RED + "Level: " + level));
        if (level < 100) {
            int kills = tag.getInt("KillCount");
            int next = getRequiredKills(level + 1);
            tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Kills: " + kills + " / " + next));
        } else {
            tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Max Level reached"));
        }
        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }
}
