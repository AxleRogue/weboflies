package me.axlerogue.weboflies.client;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.world.ModBiomes;
import me.axlerogue.weboflies.world.ModWorldPresets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = WebOfLies.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientFogEvents {
    // Deprecated for the same guards in FogEvents.java to avoid conflicts and maintain consistency.
    // Keeping it here for now but with the same strict guards as requested.
    
    private static final ResourceKey<Level> DARK_FOREST_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(WebOfLies.MODID, "dark_forest"));

    @SubscribeEvent
    public static void onComputeFogColor(ViewportEvent.ComputeFogColor event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        Level level = player.level();

        // Must be in our custom world preset
        Optional<? extends Registry<WorldPreset>> worldPresets = level.registryAccess().registry(Registries.WORLD_PRESET);
        if (worldPresets.isEmpty()) return;

        Holder<WorldPreset> preset = worldPresets.get().getHolder(ModWorldPresets.WEB_OF_LIES).orElse(null);
        if (preset == null) return;

        // Must be in our custom dimension
        if (level.dimension() != DARK_FOREST_KEY) return;

        boolean isNight = !level.isDay();
        if (!isNight) return;

        Holder<Biome> biome = level.getBiome(player.blockPosition());
        if (biome.is(ModBiomes.SPIDER_ROOT_FOREST) || biome.is(ModBiomes.POISON_FANG_SWAMP)) {
            // Night-only black fog color in our custom biome/dimension
            event.setRed(0.0F);
            event.setGreen(0.0F);
            event.setBlue(0.0F);
        }
    }
}
