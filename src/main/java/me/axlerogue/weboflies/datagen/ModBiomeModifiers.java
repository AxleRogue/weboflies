package me.axlerogue.weboflies.datagen;

import me.axlerogue.weboflies.WebOfLies;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_SPIDER_ROOT_TREE = registerKey("add_spider_root_tree");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        // No global biome modifiers for trees to prevent generation in the Overworld.
        // Spider Root Trees are added directly to the Spider Root Forest biome in ModBiomes.java.
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(WebOfLies.MODID, name));
    }
}
