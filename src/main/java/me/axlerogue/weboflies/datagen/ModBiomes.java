package me.axlerogue.weboflies.datagen;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.sound.ModSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ModBiomes {
    public static final ResourceKey<Biome> POISON_FANG_SWAMP = ResourceKey.create(Registries.BIOME,
            new ResourceLocation(WebOfLies.MODID, "poison_fang_swamp"));
    public static final ResourceKey<Biome> SPIDER_ROOT_FOREST = ResourceKey.create(Registries.BIOME,
            new ResourceLocation(WebOfLies.MODID, "spider_root_forest"));

    public static void bootstrap(BootstapContext<Biome> context) {
        context.register(POISON_FANG_SWAMP, poisonFangSwamp(context));
        context.register(SPIDER_ROOT_FOREST, spiderRootForest(context));
    }

    public static Biome poisonFangSwamp(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        ModMobSpawnSettings.addMobSpawns(spawnBuilder, context);

        BiomeGenerationSettings.Builder generationBuilder = new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        generationBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(ModPlacedFeatures.SPIDER_ROOT_PLACED_KEY));
        generationBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(ModPlacedFeatures.SPIDER_ROOT_SWAMP_PLACED_KEY));
        generationBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(ModPlacedFeatures.SPIDER_WEB_PLACED_KEY));
        generationBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(ModPlacedFeatures.GOOSE_BERRY_BUSH_PLACED_KEY));
        generationBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(ModPlacedFeatures.SPIDER_ROOT_BUSH_PLACED_KEY));
        generationBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(ModPlacedFeatures.GRASS_PLACED_KEY));
        generationBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(ModPlacedFeatures.TALL_GRASS_PLACED_KEY));
        generationBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(ModPlacedFeatures.LILY_PAD_PLACED_KEY));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(0.5f)
                .generationSettings(generationBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x32CD32) // Acid Green
                        .waterFogColor(0x050533)
                        .skyColor(0x2B3D26) // Dark Swamp Green
                        .fogColor(0x000000)
                        .grassColorOverride(0x2B3D26) // Dark Swamp Green
                        .foliageColorOverride(0x2B3D26)
                        .backgroundMusic(new Music(ModSounds.POISON_FANG_SWAMP_MUSIC.getHolder().get(), 1, 0, true))
                        .build())
                .build();
    }

    public static Biome spiderRootForest(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        ModMobSpawnSettings.addMobSpawns(spawnBuilder, context);

        BiomeGenerationSettings.Builder generationBuilder = new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        // Dark Oak Forest layout usually has more dense trees
        generationBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(ModPlacedFeatures.SPIDER_ROOT_PLACED_KEY));
        generationBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(ModPlacedFeatures.SPIDER_WEB_PLACED_KEY));
        generationBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(ModPlacedFeatures.GOOSE_BERRY_BUSH_PLACED_KEY));
        generationBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(ModPlacedFeatures.SPIDER_ROOT_BUSH_PLACED_KEY));
        generationBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(ModPlacedFeatures.GRASS_PLACED_KEY));
        generationBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(ModPlacedFeatures.TALL_GRASS_PLACED_KEY));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(0.5f)
                .generationSettings(generationBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x32CD32) // Acid Green
                        .waterFogColor(0x050533)
                        .skyColor(0x2B3D26) // Dark Swamp Green
                        .fogColor(0x000000)
                        .grassColorOverride(0x2B3D26) // Dark Swamp Green
                        .foliageColorOverride(0x2B3D26)
                        .backgroundMusic(new Music(ModSounds.SPIDER_ROOT_FOREST_MUSIC.getHolder().get(), 1, 0, true))
                        .build())
                .build();
    }
}
