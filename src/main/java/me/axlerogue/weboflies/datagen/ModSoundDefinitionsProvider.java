package me.axlerogue.weboflies.datagen;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.sound.ModSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class ModSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public ModSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, WebOfLies.MODID, helper);
    }

    @Override
    public void registerSounds() {
        add(ModSounds.POISON_FANG_SWAMP_MUSIC, definition()
                .subtitle("subtitles.weboflies.poison_fang_swamp_music")
                .with(sound(new ResourceLocation(WebOfLies.MODID, "music/poison_fang_swamp_music")).stream()));

        add(ModSounds.SPIDER_ROOT_FOREST_MUSIC, definition()
                .subtitle("subtitles.weboflies.spider_root_forest_music")
                .with(sound(new ResourceLocation(WebOfLies.MODID, "music/spider_root_forest_music")).stream()));
    }
}
