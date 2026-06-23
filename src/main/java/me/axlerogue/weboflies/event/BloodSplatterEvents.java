package me.axlerogue.weboflies.event;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.entity.BlackWidowEntity;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

@Mod.EventBusSubscriber(modid = WebOfLies.MODID)
public class BloodSplatterEvents {
    private static final ResourceKey<Level> DARK_FOREST_KEY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(WebOfLies.MODID, "dark_forest"));

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Level level = event.getEntity().level();
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            if (level.dimension() == DARK_FOREST_KEY) {
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
