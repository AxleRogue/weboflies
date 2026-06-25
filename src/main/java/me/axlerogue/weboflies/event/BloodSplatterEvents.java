package me.axlerogue.weboflies.event;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.entity.BlackWidowEntity;
import me.axlerogue.weboflies.world.ModBiomes;
import me.axlerogue.weboflies.world.ModWorldPresets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = WebOfLies.MODID)
public class BloodSplatterEvents {
    private static final ResourceKey<Level> DARK_FOREST_KEY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(WebOfLies.MODID, "dark_forest"));

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Level level = event.getEntity().level();
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            if (isEffectEnabled(level, event.getEntity().blockPosition())) {
                if (event.getEntity() instanceof BlackWidowEntity) {
                    // Green goo for spiders
                    spawnSplatter(serverLevel, event.getEntity().getX(), event.getEntity().getY() + 0.5, event.getEntity().getZ(), new Vector3f(0.0F, 1.0F, 0.0F));
                } else if (event.getEntity() instanceof Player) {
                    // Red blood for players
                    spawnSplatter(serverLevel, event.getEntity().getX(), event.getEntity().getY() + 0.5, event.getEntity().getZ(), new Vector3f(1.0F, 0.0F, 0.0F));
                }
            }
        }
    }

    private static boolean isEffectEnabled(Level level, BlockPos pos) {
        // Must be in our custom world preset
        Optional<? extends Registry<WorldPreset>> worldPresets = level.registryAccess().registry(Registries.WORLD_PRESET);
        if (worldPresets.isEmpty()) return false;
        
        Holder<WorldPreset> preset = worldPresets.get().getHolder(ModWorldPresets.WEB_OF_LIES).orElse(null);
        if (preset == null) return false;

        // Must be in our custom dimension
        if (level.dimension() != DARK_FOREST_KEY) return false;

        // Must be in our custom biomes
        if (pos != null) {
            Holder<Biome> biome = level.getBiome(pos);
            return biome.is(ModBiomes.SPIDER_ROOT_FOREST) || biome.is(ModBiomes.POISON_FANG_SWAMP);
        }

        return false;
    }

    private static void spawnSplatter(ServerLevel level, double x, double y, double z, Vector3f color) {
        DustParticleOptions dust = new DustParticleOptions(color, 1.5F);
        for (int i = 0; i < 15; ++i) {
            double dx = level.random.nextGaussian() * 0.3;
            double dy = level.random.nextGaussian() * 0.3;
            double dz = level.random.nextGaussian() * 0.3;
            level.sendParticles(dust, x + dx, y + dy, z + dz, 1, 0, 0, 0, 0.1);
        }
    }
}
