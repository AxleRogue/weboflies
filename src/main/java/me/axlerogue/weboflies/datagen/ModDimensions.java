package me.axlerogue.weboflies.datagen;

import me.axlerogue.weboflies.WebOfLies;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.Climate;
import java.util.List;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class ModDimensions {
    public static final ResourceKey<LevelStem> DARK_FOREST_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            new ResourceLocation(WebOfLies.MODID, "dark_forest"));

    public static void bootstrap(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
        
        context.register(DARK_FOREST_KEY, new LevelStem(
                context.lookup(Registries.DIMENSION_TYPE).getOrThrow(ModDimensionType.DARK_FOREST_TYPE),
                new NoiseBasedChunkGenerator(
                        MultiNoiseBiomeSource.createFromList(
                                new Climate.ParameterList<>(List.of(
                                        Pair.of(Climate.parameters(0.3F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomeGetter.getOrThrow(ModBiomes.POISON_FANG_SWAMP)),
                                        Pair.of(Climate.parameters(-0.3F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomeGetter.getOrThrow(ModBiomes.SPIDER_ROOT_FOREST))
                                ))
                        ),
                        context.lookup(Registries.NOISE_SETTINGS).getOrThrow(NoiseGeneratorSettings.AMPLIFIED)
                )
        ));
    }
}
