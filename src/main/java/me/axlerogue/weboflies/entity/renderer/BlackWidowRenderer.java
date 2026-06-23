package me.axlerogue.weboflies.entity.renderer;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.entity.BlackWidowEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.SpiderModel;

import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class BlackWidowRenderer extends SpiderRenderer<BlackWidowEntity> {
    public static final ResourceLocation BLACK_WIDOW_LOCATION = new ResourceLocation(WebOfLies.MODID, "textures/entity/spiders/black_widow.png");
    public static final ResourceLocation BLACK_WIDOW_NIGHT_HUNT = new ResourceLocation(WebOfLies.MODID, "textures/entity/spiders/black_widow_night_hunt.png");
    public static final ResourceLocation GLOW_TINT = new ResourceLocation(WebOfLies.MODID, "textures/entity/glow_tint.png");

    public BlackWidowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new BlackWidowNightHuntLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(BlackWidowEntity entity) {
        return BLACK_WIDOW_LOCATION;
    }

    private static class BlackWidowNightHuntLayer extends RenderLayer<BlackWidowEntity, SpiderModel<BlackWidowEntity>> {
        public BlackWidowNightHuntLayer(BlackWidowRenderer renderer) {
            super(renderer);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, BlackWidowEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.level().isNight()) {
                VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.eyes(BLACK_WIDOW_NIGHT_HUNT));
                this.getParentModel().renderToBuffer(poseStack, vertexconsumer, 15728640, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

}
