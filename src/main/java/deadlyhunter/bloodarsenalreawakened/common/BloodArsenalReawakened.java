package deadlyhunter.bloodarsenalreawakened.common;

import deadlyhunter.bloodarsenalreawakened.client.ClientProxy;
import deadlyhunter.bloodarsenalreawakened.common.block.ModBlocks;
import deadlyhunter.bloodarsenalreawakened.common.core.IProxy;
import deadlyhunter.bloodarsenalreawakened.common.item.ModItems;
import deadlyhunter.bloodarsenalreawakened.common.potion.ModEffects;
import deadlyhunter.bloodarsenalreawakened.data.DataGenerators;
import deadlyhunter.bloodarsenalreawakened.common.integration.BloodMagicIntegration;
import deadlyhunter.bloodarsenalreawakened.common.events.DeathEventHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BloodArsenalReawakened.MOD_ID)
public class BloodArsenalReawakened
{
    public static final String MOD_ID = "bloodarsenalreawakened";

    public static final Logger LOGGER = LogManager.getLogger();

    public static IProxy proxy = new IProxy() {};

    public static boolean curiosLoaded = false;

    public BloodArsenalReawakened()
    {
        DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> proxy = new ClientProxy());
        proxy.registerHandlers();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(DataGenerators::gatherData);
        ModEffects.EFFECTS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);

        modBus.addListener(this::commonSetup);


        MinecraftForge.EVENT_BUS.register(new DeathEventHandler());
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        BloodMagicIntegration.integrate();


    }

    public static ResourceLocation rl(String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }
}
