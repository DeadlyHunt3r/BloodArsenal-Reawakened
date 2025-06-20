package deadlyhunter.bloodarsenalreawakened.common.item.sigil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

import java.util.*;

public class SigilOfBloodlustItem extends SigilBaseItem {
    private static final int LP_COST = 20_000;
    private static final int BUFF_DURATION_TICKS = 5 * 60 * 20;
    private static final int DEBUFF_DURATION_TICKS = 1 * 60 * 20;
    private static final int COOLDOWN_TICKS = 10 * 60 * 20;

    private static final Map<UUID, Integer> buffTimers = new HashMap<>();
    private static final Map<UUID, Integer> debuffTimers = new HashMap<>();
    private static final Map<UUID, Integer> cooldownTimers = new HashMap<>();

    private static final Set<UUID> buffedPlayers = new HashSet<>();
    private static final Set<UUID> debuffedPlayers = new HashSet<>();

    public SigilOfBloodlustItem(Properties properties) {
        super(properties, LP_COST);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        UUID playerId = player.getUniqueID();

        if (cooldownTimers.containsKey(playerId) && cooldownTimers.get(playerId) > 0) {
            if (!world.isRemote) {
                player.sendStatusMessage(new StringTextComponent("The bloodlust sigil needs a moment to recharge."), true);
            }
            return ActionResult.resultFail(stack);
        }

        if (player.isCreative()) {
            if (!world.isRemote) {
                activateBuff(player);
                cooldownTimers.put(playerId, COOLDOWN_TICKS);
            }
            return ActionResult.resultSuccess(stack);
        }

        if (getBinding(stack) == null) {
            return ActionResult.resultFail(stack);
        }

        if (!world.isRemote && NetworkHelper.getSoulNetwork(getBinding(stack))
                .syphonAndDamage(player, SoulTicket.item(stack, world, player, LP_COST)).isSuccess()) {
            activateBuff(player);
            cooldownTimers.put(playerId, COOLDOWN_TICKS);
            return ActionResult.resultSuccess(stack);
        } else {
            if (!world.isRemote) {
                player.sendStatusMessage(new StringTextComponent(""), true);
            }
            return ActionResult.resultFail(stack);
        }
    }

    private void activateBuff(PlayerEntity player) {
        UUID playerId = player.getUniqueID();

        buffTimers.put(playerId, BUFF_DURATION_TICKS);
        buffedPlayers.add(playerId);

        debuffTimers.remove(playerId);
        debuffedPlayers.remove(playerId);

        applyBuffEffects(player);

        player.sendStatusMessage(new StringTextComponent("Bloodrage unleashed!"), true);
    }

    public static void tickEffects(PlayerEntity player) {
        UUID playerId = player.getUniqueID();

        if (buffTimers.containsKey(playerId)) {
            int timeLeft = buffTimers.get(playerId) - 1;
            if (timeLeft <= 0) {
                buffTimers.remove(playerId);
                buffedPlayers.remove(playerId);

                debuffTimers.put(playerId, DEBUFF_DURATION_TICKS);
                debuffedPlayers.add(playerId);

                player.sendStatusMessage(new StringTextComponent("Bloodlust fades, weakness takes hold..."), true);
            } else {
                buffTimers.put(playerId, timeLeft);
            }
        }

        else if (debuffTimers.containsKey(playerId)) {
            int timeLeft = debuffTimers.get(playerId) - 1;
            if (timeLeft <= 0) {
                debuffTimers.remove(playerId);
                debuffedPlayers.remove(playerId);
                player.sendStatusMessage(new StringTextComponent("The weakness is gone."), true);
            } else {
                debuffTimers.put(playerId, timeLeft);
                applyDebuffEffects(player);
            }
        }

        if (cooldownTimers.containsKey(playerId)) {
            int ticks = cooldownTimers.get(playerId) - 1;
            if (ticks <= 0) {
                cooldownTimers.remove(playerId);
            } else {
                cooldownTimers.put(playerId, ticks);
            }
        }
    }

    private static void applyBuffEffects(PlayerEntity player) {
        UUID playerId = player.getUniqueID();
        if (!buffedPlayers.contains(playerId))
            return;

        player.addPotionEffect(new EffectInstance(Effects.STRENGTH, BUFF_DURATION_TICKS, 3, false, true, true));
        player.addPotionEffect(new EffectInstance(Effects.REGENERATION, BUFF_DURATION_TICKS, 3, false, true, true));
        player.addPotionEffect(new EffectInstance(Effects.RESISTANCE, BUFF_DURATION_TICKS, 2, false, true, true));
        player.addPotionEffect(new EffectInstance(Effects.SPEED, BUFF_DURATION_TICKS, 1, false, true, true));
        player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, BUFF_DURATION_TICKS, 1, false, true, true));
        player.addPotionEffect(new EffectInstance(Effects.HASTE, BUFF_DURATION_TICKS, 3, false, true, true));
    }

	private static void applyDebuffEffects(PlayerEntity player) {
		UUID playerId = player.getUniqueID();
		if (!debuffedPlayers.contains(playerId))
			return;

		int duration = 40;

		player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, duration, 2, true, true));
		player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, duration, 1, true, true));
		player.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, duration, 2, true, true));
	}
}
