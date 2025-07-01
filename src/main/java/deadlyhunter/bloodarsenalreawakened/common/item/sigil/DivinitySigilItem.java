package deadlyhunter.bloodarsenalreawakened.common.item.sigil;

import deadlyhunter.bloodarsenalreawakened.common.ConfigHandler;
import deadlyhunter.bloodarsenalreawakened.common.core.handler.EquipmentHandler;
import deadlyhunter.bloodarsenalreawakened.common.item.ModItems;
import deadlyhunter.bloodarsenalreawakened.common.util.helper.PlayerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.*;

public class DivinitySigilItem extends SigilBaseItem.Toggleable
{
    private static final Set<String> BLOOD_MAGIC_DAMAGE_TYPES = 
        Collections.unmodifiableSet(new HashSet<>(Arrays.asList("bloodmagic", "lp_drain", "internal")));

    public DivinitySigilItem(Properties properties)
    {
        super(properties, ConfigHandler.COMMON.divinitySigilCost.get());
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, PlayerEntity player, int itemSlot, boolean isSelected)
    {
        if (PlayerHelper.isFakePlayer(player))
            return;

        player.fallDistance = 0;
        player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 2, 4, true, false));
		player.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 2, 99, true, false, false));

    }

    public static void onPlayerAttacked(LivingAttackEvent event)
    {
        if (!(event.getEntityLiving() instanceof PlayerEntity))
            return;

        PlayerEntity player = (PlayerEntity) event.getEntityLiving();

        if (!event.isCancelable())
            return;

        ItemStack divinitySigil = EquipmentHandler.findOrEmpty(ModItems.DIVINITY_SIGIL.get(), player);
        if (divinitySigil.isEmpty())
            return;

        DivinitySigilItem divinitySigilItem = (DivinitySigilItem) divinitySigil.getItem();
        if (!divinitySigilItem.getActivated(divinitySigil))
            return;

        DamageSource source = event.getSource();
        String type = source.getDamageType().toLowerCase();

        if (!BLOOD_MAGIC_DAMAGE_TYPES.contains(type))
        {
            event.setCanceled(true);
        }
    }
}
