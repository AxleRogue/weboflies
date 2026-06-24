package me.axlerogue.weboflies.datagen;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> SPIDER_ROOT_PLACED_KEY = registerKey("spider_root_tree_placed");
    public static final ResourceKey<PlacedFeature> SPIDER_ROOT_SWAMP_PLACED_KEY = registerKey("spider_root_swamp_tree_placed");
    public static final ResourceKey<PlacedFeature> SPIDER_WEB_PLACED_KEY = registerKey("spider_web_placed");
    public static final ResourceKey<PlacedFeature> GOOSE_BERRY_BUSH_PLACED_KEY = registerKey("goose_berry_bush_placed");
    public static final ResourceKey<PlacedFeature> SPIDER_ROOT_BUSH_PLACED_KEY = registerKey("spider_root_bush_placed");
    public static final ResourceKey<PlacedFeature> GRASS_PLACED_KEY = registerKey("grass_placed");
    public static final ResourceKey<PlacedFeature> TALL_GRASS_PLACED_KEY = registerKey("tall_grass_placed");
    public static final ResourceKey<PlacedFeature> LILY_PAD_PLACED_KEY = registerKey("lily_pad_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, SPIDER_ROOT_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.SPIDER_ROOT_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2), ModBlocks.SPIDER_ROOT_SAPLING.get()));

        register(context, SPIDER_ROOT_SWAMP_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.SPIDER_ROOT_SWAMP_TREE_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(2, 0.1f, 1), ModBlocks.SPIDER_ROOT_SAPLING.get()));

        register(context, SPIDER_WEB_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.SPIDER_WEB_KEY),
                List.of(CountPlacement.of(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, GOOSE_BERRY_BUSH_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GOOSE_BERRY_BUSH_KEY),
                List.of(RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, SPIDER_ROOT_BUSH_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.SPIDER_ROOT_BUSH_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(2, 0.1f, 1), ModBlocks.SPIDER_ROOT_SAPLING.get()));

        register(context, GRASS_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GRASS_KEY),
                List.of(NoiseThresholdCountPlacement.of(-0.8D, 5, 10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, TALL_GRASS_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.TALL_GRASS_KEY),
                List.of(NoiseThresholdCountPlacement.of(-0.8D, 0, 7), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, LILY_PAD_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.LILY_PAD_KEY),
                List.of(CountPlacement.of(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome()));
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(WebOfLies.MODID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
