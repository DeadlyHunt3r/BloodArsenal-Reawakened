package com.deadlyhunter.bloodarsenalreawakened;

import com.deadlyhunter.bloodarsenalreawakened.registry.*;
import com.deadlyhunter.bloodarsenalreawakened.entity.SoulAnchorEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BARMain.MOD_ID)
public class BARMain {
    public static final String MOD_ID = "blood_arsenal_reawakened";

    public BARMain() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.REGISTER.register(modBus);
        ModBlocks.REGISTER.register(modBus);
        ModEntities.ENTITIES.register(modBus);
        ModCreativeTabs.REGISTER.register(modBus);
    }

    @Mod.EventBusSubscriber(modid = BARMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void entityAttributes(EntityAttributeCreationEvent event) {
            event.put(ModEntities.SOUL_ANCHOR.get(), SoulAnchorEntity.createAttributes().build());
        }
    }

    @Mod.EventBusSubscriber(modid = BARMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.SOUL_ANCHOR.get(), SoulAnchorRender::new);
        }
    }

    private static class SoulAnchorRender extends EntityRenderer<SoulAnchorEntity> {
        protected SoulAnchorRender(EntityRendererProvider.Context context) { super(context); }

        @Override
        public void render(SoulAnchorEntity entity, float yaw, float partialTicks, PoseStack pose, MultiBufferSource buffer, int light) {
            pose.pushPose();
            pose.mulPose(Axis.YP.rotationDegrees(yaw));
            ItemStack stack = new ItemStack(ModItems.SOUL_ANCHOR.get());
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, pose, buffer, entity.level(), entity.getId());
            pose.popPose();
            super.render(entity, yaw, partialTicks, pose, buffer, light);
        }

        @Override
        public ResourceLocation getTextureLocation(SoulAnchorEntity entity) {
            return InventoryMenu.BLOCK_ATLAS;
        }
    }
}