package me.axlerogue.weboflies;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
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
import me.axlerogue.weboflies.entity.renderer.SpiderEggModel;
import me.axlerogue.weboflies.entity.renderer.SpiderEggRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(WebOfLies.MODID)
public class WebOfLies {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "weboflies";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "weboflies" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "weboflies" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

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
        ModMessages.register();
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
        }

        @SubscribeEvent
        public static void registerBlockColors(net.minecraftforge.client.event.RegisterColorHandlersEvent.Block event) {
            ModBlocks.registerBlockColors(event);
        }
    }
}
