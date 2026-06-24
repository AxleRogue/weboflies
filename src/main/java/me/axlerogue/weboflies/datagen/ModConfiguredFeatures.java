package me.axlerogue.weboflies.datagen;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.block.ModBlocks;
import me.axlerogue.weboflies.block.GooseBerryBushBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BushFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import java.util.List;
import net.minecraft.util.valueproviders.ConstantInt;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPIDER_ROOT_KEY = registerKey("spider_root_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPIDER_ROOT_SWAMP_TREE_KEY = registerKey("spider_root_swamp_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPIDER_WEB_KEY = registerKey("spider_web");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GOOSE_BERRY_BUSH_KEY = registerKey("goose_berry_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPIDER_ROOT_BUSH_KEY = registerKey("spider_root_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GRASS_KEY = registerKey("grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_GRASS_KEY = registerKey("tall_grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LILY_PAD_KEY = registerKey("lily_pad");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        register(context, SPIDER_ROOT_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.SPIDER_ROOT_LOG.get()),
                new MegaJungleTrunkPlacer(10, 5, 0),
                BlockStateProvider.simple(ModBlocks.SPIDER_ROOT_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        ).ignoreVines().build());

        register(context, SPIDER_ROOT_SWAMP_TREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.SPIDER_ROOT_LOG.get()),
                new StraightTrunkPlacer(6, 3, 2),
                BlockStateProvider.simple(ModBlocks.SPIDER_ROOT_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        ).decorators(List.of(new TrunkVineDecorator(), new LeaveVineDecorator(0.35f))).build());

        register(context, SPIDER_WEB_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(16, 4, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.SPIDERWEB.get())))));

        register(context, GOOSE_BERRY_BUSH_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(32, 7, 3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.GOOSE_BERRY_BUSH.get().defaultBlockState().setValue(GooseBerryBushBlock.AGE, 3))))));

        register(context, SPIDER_ROOT_BUSH_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.SPIDER_ROOT_LOG.get()),
                new StraightTrunkPlacer(1, 0, 1),
                BlockStateProvider.simple(ModBlocks.SPIDER_ROOT_LEAVES.get()),
                new BushFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2),
                new TwoLayersFeatureSize(0, 0, 0)
        ).ignoreVines().build());

        register(context, GRASS_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(32, 7, 3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.GRASS)))));

        register(context, TALL_GRASS_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(32, 7, 3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.TALL_GRASS)))));

        register(context, LILY_PAD_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(10, 7, 3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.LILY_PAD)))));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(WebOfLies.MODID, name));
    }

    private static <FC extends net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                                                                           ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
