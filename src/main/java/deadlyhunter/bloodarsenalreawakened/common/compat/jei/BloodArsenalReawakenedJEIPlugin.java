package deadlyhunter.bloodarsenalreawakened.common.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.JeiPlugin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import deadlyhunter.bloodarsenalreawakened.common.item.ModItems;

@JeiPlugin
public class BloodArsenalReawakenedJEIPlugin implements IModPlugin {

    private static final ResourceLocation ID = new ResourceLocation("bloodarsenalreawakened", "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addIngredientInfo(
            new ItemStack(ModItems.REAGENT_DIVINITY_UNAWAKENED.get()),
            VanillaTypes.ITEM,
            "Crafting Info:\n" +
            "- Throw into water:\n" +
            "  8x Essence of Decay\n" +
            "  8x Enchanted Golden Apple\n" +
            "  8x Bleeding Dragon's Eye\n" +
            "  64x Corrupted Dust\n" +
            "  1x Glass Bottle\n" +
            "\n" +
            "When struck by lightning,\n" +
            "you receive the Dormant Pact Essence."
        );

        registration.addIngredientInfo(
            new ItemStack(ModItems.ESSENCE_OF_DECAY.get()),
            VanillaTypes.ITEM,
            "Drop chance: 10%\nDropped by: Wither"
        );

        registration.addIngredientInfo(
            new ItemStack(ModItems.DRAGON_CLAW.get()),
            VanillaTypes.ITEM,
            "Drop chance: 100%\nDropped by: Ender Dragon"
        );

        registration.addIngredientInfo(
            new ItemStack(ModItems.ETHEREAL_DRAGON_HEART.get()),
            VanillaTypes.ITEM,
            "Drop chance: 10%\nDropped by: Ender Dragon"
        );
    }
}
