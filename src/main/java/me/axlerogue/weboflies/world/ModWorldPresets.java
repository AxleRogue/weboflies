package me.axlerogue.weboflies.world;

import me.axlerogue.weboflies.WebOfLies;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

public class ModWorldPresets {
    public static final DeferredRegister<WorldPreset> WORLD_PRESETS =
            DeferredRegister.create(Registries.WORLD_PRESET, WebOfLies.MODID);

    public static final ResourceKey<WorldPreset> WEB_OF_LIES = ResourceKey.create(Registries.WORLD_PRESET,
            new ResourceLocation(WebOfLies.MODID, "web_of_lies"));

    public static void register(IEventBus eventBus) {
        WORLD_PRESETS.register(eventBus);
    }
}
