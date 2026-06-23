package me.axlerogue.weboflies.client;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.entity.BlackWidowEntity;
import me.axlerogue.weboflies.entity.SpiderEgg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid = WebOfLies.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientNameplateEvents {

    @SubscribeEvent
    public static void onRenderNameplate(RenderNameTagEvent event) {
        if (event.getEntity() instanceof LivingEntity living && (living instanceof BlackWidowEntity || living instanceof SpiderEgg)) {
            event.setResult(Event.Result.DENY); // Disable vanilla rendering

            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource buffer = event.getMultiBufferSource();
            Font font = Minecraft.getInstance().font;
            
            float health = living.getHealth();
            float maxHealth = living.getMaxHealth();
            String healthStr = String.format(" %.1f/%.1f", health, maxHealth);
            Component name = living.hasCustomName() ? living.getCustomName() : living.getDisplayName();
            Component fullText = Component.literal(name.getString() + healthStr);

            float f = living.getBbHeight() + 0.5F;
            poseStack.pushPose();
            poseStack.translate(0.0D, (double)f, 0.0D);
            poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
            poseStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = poseStack.last().pose();
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int j = (int)(f1 * 255.0F) << 24;
            float f2 = (float)(-font.width(fullText) / 2);
            font.drawInBatch(fullText, f2, 0, 553648127, false, matrix4f, buffer, font.width(fullText) > 0 ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, event.getPackedLight());
            font.drawInBatch(fullText, f2, 0, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, event.getPackedLight());
            
            poseStack.popPose();
        }
    }
}
