package me.axlerogue.weboflies.datagen;

import com.google.gson.JsonObject;
import me.axlerogue.weboflies.WebOfLies;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * Custom data provider to generate the dimension effects JSON for the Dark Forest.
 * This ensures that the dimension is properly recognized on the client with the correct effects.
 */
public class ModDimensionEffectsProvider implements DataProvider {
    private final PackOutput output;

    public ModDimensionEffectsProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Path path = this.output.getOutputFolder().resolve("assets/" + WebOfLies.MODID + "/dimension_effects/dark_forest.json");
        
        JsonObject json = new JsonObject();
        // Standard dimension effect settings
        json.addProperty("sky_type", "normal");
        json.addProperty("fog_type", "normal");
        json.addProperty("has_sky_light", true);
        json.addProperty("has_ceiling", false);
        json.addProperty("ambient_light", 0.0f);
        
        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public String getName() {
        return "Dimension Effects: " + WebOfLies.MODID;
    }
}
