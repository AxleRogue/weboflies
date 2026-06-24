package me.axlerogue.weboflies;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import me.axlerogue.weboflies.sound.ModSounds;
import me.axlerogue.weboflies.block.entity.ModBlockEntities;
import me.axlerogue.weboflies.network.ModMessages;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import me.axlerogue.weboflies.block.ModBlocks;
import me.axlerogue.weboflies.item.ModItems;
import me.axlerogue.weboflies.item.ModCreativeModeTabs;
import me.axlerogue.weboflies.entity.BabyBlackWidowEntity;
import me.axlerogue.weboflies.entity.BlackWidowEntity;
import me.axlerogue.weboflies.entity.BlackWidowBroodMotherEntity;
import me.axlerogue.weboflies.entity.SpiderEgg;
import me.axlerogue.weboflies.entity.client.ModEntities;
import me.axlerogue.weboflies.entity.renderer.BabyBlackWidowRenderer;
import me.axlerogue.weboflies.entity.renderer.BlackWidowRenderer;
import me.axlerogue.weboflies.entity.renderer.BlackWidowBroodMotherRenderer;
import me.axlerogue.weboflies.entity.renderer.CorpseRenderer;
import me.axlerogue.weboflies.entity.renderer.SpiderGibRenderer;
import me.axlerogue.weboflies.entity.renderer.SpiderEggModel;
import me.axlerogue.weboflies.entity.renderer.SpiderEggRenderer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import org.slf4j.Logger;

@Mod(WebOfLies.MODID)
public class WebOfLies {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "weboflies";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public WebOfLies() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WebOfLiesConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();

            registerFlammable(ModBlocks.SPIDER_ROOT_LOG.get(), 5, 5);
            registerFlammable(ModBlocks.SPIDER_ROOT_PLANKS.get(), 5, 20);
            registerFlammable(ModBlocks.SPIDER_ROOT_LEAVES.get(), 30, 60);
            registerFlammable(ModBlocks.SPIDER_ROOT_SAPLING.get(), 60, 100);
        });
    }

    private void registerFlammable(Block block, int encouragement, int flammability) {
        net.minecraft.world.level.block.FireBlock fireBlock = (net.minecraft.world.level.block.FireBlock) net.minecraft.world.level.block.Blocks.FIRE;
        try {
            java.lang.reflect.Method setFlammable = net.minecraft.world.level.block.FireBlock.class.getDeclaredMethod("setFlammable", Block.class, int.class, int.class);
            setFlammable.setAccessible(true);
            setFlammable.invoke(fireBlock, block, encouragement, flammability);
        } catch (Exception e) {
            LOGGER.error("Failed to set flammable for block: " + block, e);
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(ModEntities.SPIDER_EGG.get(), SpiderEgg.createAttributes().build());
            event.put(ModEntities.BLACK_WIDOW.get(), BlackWidowEntity.createAttributes().build());
            event.put(ModEntities.BABY_BLACK_WIDOW.get(), BabyBlackWidowEntity.createAttributes().build());
            event.put(ModEntities.BLACK_WIDOW_BROOD_MOTHER.get(), BlackWidowBroodMotherEntity.createAttributes().build());
        }

        @SubscribeEvent
        public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
            event.register(ModEntities.BLACK_WIDOW.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlackWidowBroodMotherEntity::checkSpiderSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
            event.register(ModEntities.BABY_BLACK_WIDOW.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlackWidowBroodMotherEntity::checkSpiderSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
            event.register(ModEntities.BLACK_WIDOW_BROOD_MOTHER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlackWidowBroodMotherEntity::checkBroodMotherSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
            event.register(ModEntities.SPIDER_EGG.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpiderEgg::checkSpiderEggSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.HOMEWARD_COBWEB.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.HAUNTED_COBWEB.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPIDERWEB.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPIDER_ROOT_LEAVES.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPIDER_ROOT_SAPLING.get(), RenderType.cutout());
            });
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(SpiderEggModel.LAYER_LOCATION, SpiderEggModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.SPIDER_EGG.get(), SpiderEggRenderer::new);
            event.registerEntityRenderer(ModEntities.BLACK_WIDOW.get(), BlackWidowRenderer::new);
            event.registerEntityRenderer(ModEntities.BABY_BLACK_WIDOW.get(), BabyBlackWidowRenderer::new);
            event.registerEntityRenderer(ModEntities.BLACK_WIDOW_BROOD_MOTHER.get(), BlackWidowBroodMotherRenderer::new);
            event.registerEntityRenderer(ModEntities.HAUNTED_COBWEB_PROJECTILE.get(), net.minecraft.client.renderer.entity.ThrownItemRenderer::new);
            event.registerEntityRenderer(ModEntities.CORPSE.get(), CorpseRenderer::new);
            event.registerEntityRenderer(ModEntities.SPIDER_GIB.get(), SpiderGibRenderer::new);
        }

        @SubscribeEvent
        public static void registerBlockColors(net.minecraftforge.client.event.RegisterColorHandlersEvent.Block event) {
            ModBlocks.registerBlockColors(event);
        }
        @SubscribeEvent
        public static void registerDimensionSpecialEffects(RegisterDimensionSpecialEffectsEvent event) {
            event.register(new ResourceLocation(WebOfLies.MODID, "dark_forest"), new DimensionSpecialEffects(Float.NaN, false, DimensionSpecialEffects.SkyType.NORMAL, false, false) {
                @Override
                public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
                    return fogColor;
                }

                @Override
                public boolean isFoggyAt(int x, int y) {
                    return false;
                }
            });
        }
    }
}
