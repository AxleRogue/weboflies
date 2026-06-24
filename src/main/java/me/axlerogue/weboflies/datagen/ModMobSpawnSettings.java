package me.axlerogue.weboflies.datagen;

import me.axlerogue.weboflies.WebOfLies;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class ModMobSpawnSettings extends MobSpawnSettings.Builder {
    public static void addMobSpawns(MobSpawnSettings.Builder builder, BootstapContext<Biome> context) {
        builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(
                context.lookup(Registries.ENTITY_TYPE).getOrThrow(ResourceKey.create(Registries.ENTITY_TYPE, new ResourceLocation(WebOfLies.MODID, "black_widow"))).value(), 100, 2, 4));
        builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(
                context.lookup(Registries.ENTITY_TYPE).getOrThrow(ResourceKey.create(Registries.ENTITY_TYPE, new ResourceLocation(WebOfLies.MODID, "baby_black_widow"))).value(), 50, 1, 2));
        builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(
                context.lookup(Registries.ENTITY_TYPE).getOrThrow(ResourceKey.create(Registries.ENTITY_TYPE, new ResourceLocation(WebOfLies.MODID, "black_widow_brood_mother"))).value(), 5, 1, 1));
    }
}
