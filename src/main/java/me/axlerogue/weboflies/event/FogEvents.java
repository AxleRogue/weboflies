package me.axlerogue.weboflies.event;

import me.axlerogue.weboflies.WebOfLies;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WebOfLies.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class FogEvents {
    private static final ResourceLocation DARK_FOREST_DIM = new ResourceLocation(WebOfLies.MODID, "dark_forest");

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.dimension().location().equals(DARK_FOREST_DIM)) {
            float nightFactor = getNightFactor(mc.level);
            if (nightFactor > 0) {
                event.setRed(Mth.lerp(nightFactor, event.getRed(), 0.0F));
                event.setGreen(Mth.lerp(nightFactor, event.getGreen(), 0.0F));
                event.setBlue(Mth.lerp(nightFactor, event.getBlue(), 0.0F));
            }
        }
    }

    @SubscribeEvent
    public static void onFogRender(ViewportEvent.RenderFog event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.dimension().location().equals(DARK_FOREST_DIM)) {
            float nightFactor = getNightFactor(mc.level);
            if (nightFactor > 0) {
                float defaultNear = event.getNearPlaneDistance();
                float defaultFar = event.getFarPlaneDistance();

                // Target night fog: 0.0F near, 15.0F far
                event.setNearPlaneDistance(Mth.lerp(nightFactor, defaultNear, 0.0F));
                event.setFarPlaneDistance(Mth.lerp(nightFactor, defaultFar, 15.0F));
                event.setCanceled(true);
            }
        }
    }

    private static float getNightFactor(Level level) {
        float time = level.getTimeOfDay(1.0F); // 0.0 to 1.0. 0.5 is midnight.
        // Night starts around 0.25 (13000) and ends around 0.75 (23000)
        // Peak night is 0.5 (18000)
        
        // We want factor to be 1.0 during night and 0.0 during day, with smooth transitions.
        // Minecraft night: 13000 (0.25) to 23000 (0.75 approx)
        // Sunset transition: 12000 to 14000 (0.22 to 0.28)
        // Sunrise transition: 22000 to 24000 (0.72 to 1.0/0.0)
        
        if (time > 0.22f && time < 0.28f) {
            return (time - 0.22f) / 0.06f; // Fade in
        } else if (time >= 0.28f && time <= 0.72f) {
            return 1.0f; // Full night fog
        } else if (time > 0.72f && time < 0.78f) {
            return 1.0f - (time - 0.72f) / 0.06f; // Fade out
        }
        
        return 0.0f;
    }
}
