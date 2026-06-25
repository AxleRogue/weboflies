package me.axlerogue.weboflies.client;
import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.sound.ModSounds;
import me.axlerogue.weboflies.world.ModBiomes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WebOfLies.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientMusicEvents {

    private static SoundInstance currentBiomeMusic = null;
    private static Holder<Biome> lastBiome = null;
    private static int startCooldown = 0;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        if (startCooldown > 0) {
            startCooldown--;
        }

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) return;

        Holder<Biome> currentBiome = player.level().getBiome(player.blockPosition());

        if (lastBiome == null || !currentBiome.equals(lastBiome)) {
            handleBiomeChange(minecraft, currentBiome);
            lastBiome = currentBiome;
        }

        // Keep playing if it stopped but we are still in the biome
        if (isModBiome(currentBiome)) {
            if (startCooldown == 0 && (currentBiomeMusic == null || !minecraft.getSoundManager().isActive(currentBiomeMusic))) {
                startMusicForBiome(minecraft, currentBiome);
                startCooldown = 20; // Wait 1 second before allowing another start attempt
            }
        } else if (currentBiomeMusic != null) {
            minecraft.getSoundManager().stop(currentBiomeMusic);
            currentBiomeMusic = null;
        }
    }

    private static void handleBiomeChange(Minecraft minecraft, Holder<Biome> newBiome) {
        if (currentBiomeMusic != null) {
            minecraft.getSoundManager().stop(currentBiomeMusic);
            currentBiomeMusic = null;
        }
    }

    private static boolean isSameMusicForBiome(Holder<Biome> biome) {
        if (currentBiomeMusic == null) return false;
        SoundEvent expected = getMusicForBiome(biome);
        return expected != null && currentBiomeMusic.getLocation().equals(expected.getLocation());
    }

    private static boolean isModBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.SPIDER_ROOT_FOREST) || biome.is(ModBiomes.POISON_FANG_SWAMP);
    }

    private static void startMusicForBiome(Minecraft minecraft, Holder<Biome> biome) {
        SoundEvent music = getMusicForBiome(biome);
        if (music != null) {
            currentBiomeMusic = SimpleSoundInstance.forMusic(music);
            minecraft.getSoundManager().play(currentBiomeMusic);
        }
    }

    private static SoundEvent getMusicForBiome(Holder<Biome> biome) {
        if (biome.is(ModBiomes.SPIDER_ROOT_FOREST)) {
            return ModSounds.SPIDER_ROOT_FOREST_MUSIC.get();
        } else if (biome.is(ModBiomes.POISON_FANG_SWAMP)) {
            return ModSounds.POISON_FANG_SWAMP_MUSIC.get();
        }
        return null;
    }

    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        SoundInstance sound = event.getSound();
        if (sound == null) return;

        if (sound.getSource() == SoundSource.MUSIC) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            Holder<Biome> biomeHolder = player.level().getBiome(player.blockPosition());
            if (isModBiome(biomeHolder)) {
                // If it's NOT our mod's music, cancel it
                if (!sound.getLocation().getNamespace().equals(WebOfLies.MODID)) {
                    event.setSound(null);
                }
            }
        }
    }
}
