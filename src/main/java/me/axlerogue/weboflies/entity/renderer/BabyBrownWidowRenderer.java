package me.axlerogue.weboflies.entity.renderer;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.entity.BabyBrownWidowEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.SpiderModel;

public class BabyBrownWidowRenderer extends BaseSpiderRenderer<BabyBrownWidowEntity> {
    public static final ResourceLocation BROWN_WIDOW_LOCATION = new ResourceLocation(WebOfLies.MODID, "textures/entity/spiders/brown_widow.png");
    public static final ResourceLocation BROWN_WIDOW_NIGHT_HUNT = new ResourceLocation(WebOfLies.MODID, "textures/entity/spiders/brown_widow_night_hunt.png");

    public BabyBrownWidowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.4F;
        this.addLayer(new BabyBrownWidowNightHuntLayer(this));
    }

    @Override
    protected void scale(BabyBrownWidowEntity entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(0.5F, 0.5F, 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(BabyBrownWidowEntity entity) {
        return BROWN_WIDOW_LOCATION;
    }

    private static class BabyBrownWidowNightHuntLayer extends RenderLayer<BabyBrownWidowEntity, SpiderModel<BabyBrownWidowEntity>> {
        public BabyBrownWidowNightHuntLayer(BabyBrownWidowRenderer renderer) {
            super(renderer);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, BabyBrownWidowEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.level().isNight()) {
                VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.eyes(BROWN_WIDOW_NIGHT_HUNT));
                this.getParentModel().renderToBuffer(poseStack, vertexconsumer, 15728640, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
