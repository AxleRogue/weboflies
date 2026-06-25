package me.axlerogue.weboflies.world;

import me.axlerogue.weboflies.WebOfLies;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes {
    public static final ResourceKey<Biome> POISON_FANG_SWAMP = ResourceKey.create(Registries.BIOME,
            new ResourceLocation(WebOfLies.MODID, "poison_fang_swamp"));
    public static final ResourceKey<Biome> SPIDER_ROOT_FOREST = ResourceKey.create(Registries.BIOME,
            new ResourceLocation(WebOfLies.MODID, "spider_root_forest"));
}
