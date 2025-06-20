package deadlyhunter.bloodarsenalreawakened.common.core;

import deadlyhunter.bloodarsenalreawakened.common.BloodArsenalReawakened;
import deadlyhunter.bloodarsenalreawakened.common.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class BloodArsenalReawakenedCreativeTab extends ItemGroup
{
    public static final BloodArsenalReawakenedCreativeTab INSTANCE = new BloodArsenalReawakenedCreativeTab();

    public BloodArsenalReawakenedCreativeTab()
    {
        super(BloodArsenalReawakened.MOD_ID);
    }

    @Nonnull
    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(ModItems.INFUSED_BLOOD_DIAMOND.get());
    }
}
