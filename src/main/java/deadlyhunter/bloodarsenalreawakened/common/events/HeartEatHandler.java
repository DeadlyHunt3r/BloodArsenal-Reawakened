package deadlyhunter.bloodarsenalreawakened.common.events;

import deadlyhunter.bloodarsenalreawakened.common.item.ModItems;
import deadlyhunter.bloodarsenalreawakened.common.util.HeartUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HeartEatHandler {

    @SubscribeEvent
    public static void onPlayerEat(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        ItemStack stack = event.getItem();

        String heartType = null;

        if (stack.getItem() == ModItems.HEART_1.get()) heartType = "heart1";
        else if (stack.getItem() == ModItems.HEART_2.get()) heartType = "heart2";
        else if (stack.getItem() == ModItems.HEART_3.get()) heartType = "heart3";

        if (heartType == null) return;

        int count = player.getPersistentData().getInt(heartType + "_count");
        if (count >= 10) {
           //player.sendMessage(new StringTextComponent("Du hast schon 10 " + heartType + ". Mehr zu essen schadet dir!"), player.getUniqueID());

            player.addPotionEffect(new EffectInstance(Effects.WITHER, 200, 1));

            int heart1 = player.getPersistentData().getInt("heart1_count");
            int heart2 = player.getPersistentData().getInt("heart2_count");
            int heart3 = player.getPersistentData().getInt("heart3_count");
            int totalHearts = heart1 + heart2 + heart3;
            HeartUtils.updatePlayerHearts(player, totalHearts);

            return;
        }

        player.getPersistentData().putInt(heartType + "_count", count + 1);

        int heart1 = player.getPersistentData().getInt("heart1_count");
        int heart2 = player.getPersistentData().getInt("heart2_count");
        int heart3 = player.getPersistentData().getInt("heart3_count");
        int totalHearts = heart1 + heart2 + heart3;

        HeartUtils.updatePlayerHearts(player, totalHearts);

       // player.sendMessage(new StringTextComponent("Du hast ein " + heartType + " gegessen und ein dauerhaftes Herz erhalten!"), player.getUniqueID());
    }
}
