package deadlyhunter.bloodarsenalreawakened.common.integration;

import deadlyhunter.bloodarsenalreawakened.common.block.ModBlocks;
import wayoftime.bloodmagic.api.IBloodMagicAPI;

public class BloodMagicIntegration
{
    public static void integrate()
    {
        IBloodMagicAPI api = IBloodMagicAPI.INSTANCE.getValue();
        api.registerAltarComponent(ModBlocks.BLOOD_INFUSED_GLOWSTONE.get().getDefaultState(), "glowstone");
    }
}
