package com.deadlyhunter.bloodarsenalreawakened.gear;

import com.deadlyhunter.bloodarsenalreawakened.BARMain;
import com.deadlyhunter.bloodarsenalreawakened.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BloodTranscendenceArmorItem extends ArmorItem {

    public BloodTranscendenceArmorItem(ModArmorMaterials mat, Type type, Properties props) {
        super(mat, type, props);
    }


    @Override
    public int getMaxDamage(ItemStack stack) {
        return 0; 
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int count = 0;
        Player clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer != null) {
            count = countEquipped(clientPlayer);
        }

        tooltip.add(Component.literal("Set: Blood Transcendence ")
                .withStyle(ChatFormatting.DARK_RED)
                .append(Component.literal("(" + count + "/4)").withStyle(ChatFormatting.GRAY)));
        tooltip.add(Component.translatable(
                "tooltip.bloodarsenalreawakened." + BARMain.MOD_ID + ".armor_hint")
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
    }

    public static int countEquipped(Player p) {
        int c = 0;
        ItemStack head = p.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = p.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = p.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = p.getItemBySlot(EquipmentSlot.FEET);

        if (head.is(ModItems.HELM_OF_BLOODSIGHT.get())) c++;
        if (chest.is(ModItems.BREASTPLATE_OF_FURY.get())) c++;
        if (legs.is(ModItems.HUNTERS_GREAVES.get())) c++;
        if (feet.is(ModItems.BOOTS_OF_THE_FROSTBLOOD.get())) c++;
        return c;
    }
}
