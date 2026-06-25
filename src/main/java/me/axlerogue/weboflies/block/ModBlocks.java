package me.axlerogue.weboflies.block;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WebOfLies.MODID);

    public enum GlowColor implements StringRepresentable {
        RED("red"),
        GREEN("green");

        private final String name;

        GlowColor(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    public static final EnumProperty<GlowColor> COLOR = EnumProperty.create("color", GlowColor.class);

    public static final RegistryObject<Block> GLOW_LIGHT = BLOCKS.register("glow_light",
            () -> new AirBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NONE)
                    .noCollission()
                    .noLootTable()
                    .air()
                    .lightLevel((state) -> 10)) {
                @Override
                protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
                    builder.add(COLOR);
                }

                @Override
                public RenderShape getRenderShape(BlockState state) {
                    return RenderShape.INVISIBLE;
                }
            });

    public static final RegistryObject<Block> HOMEWARD_COBWEB = registerBlock("homeward_cobweb",
            () -> new HomewardCobwebBlock(BlockBehaviour.Properties.copy(Blocks.COBWEB)
                    .mapColor(MapColor.SNOW)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.SCAFFOLDING)
                    .pushReaction(PushReaction.DESTROY)
                    .lightLevel((state) -> 7)));

    public static final RegistryObject<Block> HAUNTED_COBWEB = registerBlock("haunted_cobweb",
            () -> new HauntedCobwebBlock(BlockBehaviour.Properties.copy(Blocks.COBWEB)
                    .mapColor(MapColor.COLOR_GREEN)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.SCAFFOLDING)
                    .pushReaction(PushReaction.DESTROY)
                    .lightLevel((state) -> 7)));

    public static final RegistryObject<Block> SPIDERWEB = registerBlock("spiderweb",
            () -> new SpiderwebBlock(BlockBehaviour.Properties.copy(Blocks.COBWEB)
                    .mapColor(MapColor.SNOW)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.SCAFFOLDING)
                    .pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> SPIDER_ROOT_LOG = registerBlock("spider_root_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).mapColor(MapColor.WOOD)));

    public static final RegistryObject<Block> SPIDER_ROOT_PLANKS = registerBlock("spider_root_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).mapColor(MapColor.WOOD)));

    public static final RegistryObject<Block> SPIDER_ROOT_LEAVES = registerBlock("spider_root_leaves",
            () -> new SpiderRootLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)
                    .mapColor(MapColor.PLANT)
                    .noOcclusion()));

    public static final RegistryObject<Block> SPIDER_ROOT_SAPLING = registerBlock("spider_root_sapling",
            () -> new SaplingBlock(new AbstractTreeGrower() {
                @Nullable
                @Override
                protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pHasFlowers) {
                    return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(WebOfLies.MODID, "spider_root_tree"));
                }
            }, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> GOOSE_BERRY_BUSH = registerBlock("goose_berry_bush",
            () -> new GooseBerryBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, java.util.function.Function<RegistryObject<T>, RegistryObject<Item>> itemFactory) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        itemFactory.apply(toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            GlowColor color = state.getValue(COLOR);
            return color == GlowColor.GREEN ? 0x32CD32 : 0xFF0000; // Lime Green or Red
        }, GLOW_LIGHT.get());
    }

    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> 0xFF0000, GLOW_LIGHT.get());
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
