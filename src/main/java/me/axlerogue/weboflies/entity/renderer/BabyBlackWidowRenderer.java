package me.axlerogue.weboflies.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.entity.BabyBlackWidowEntity;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class BabyBlackWidowRenderer extends BaseSpiderRenderer<BabyBlackWidowEntity> {
    public static final ResourceLocation BLACK_WIDOW_LOCATION = new ResourceLocation(WebOfLies.MODID, "textures/entity/spiders/black_widow.png");
    public static final ResourceLocation BLACK_WIDOW_NIGHT_HUNT = new ResourceLocation(WebOfLies.MODID, "textures/entity/spiders/black_widow_night_hunt.png");

    public BabyBlackWidowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.25f;
        this.addLayer(new BabyBlackWidowNightHuntLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(BabyBlackWidowEntity entity) {
        return BLACK_WIDOW_LOCATION;
    }

    @Override
    protected void scale(BabyBlackWidowEntity entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(0.5f, 0.5f, 0.5f);
    }

    private static class BabyBlackWidowNightHuntLayer extends RenderLayer<BabyBlackWidowEntity, SpiderModel<BabyBlackWidowEntity>> {
        public BabyBlackWidowNightHuntLayer(BabyBlackWidowRenderer renderer) {
            super(renderer);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, BabyBlackWidowEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.level().isNight()) {
                VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.eyes(BLACK_WIDOW_NIGHT_HUNT));
                this.getParentModel().renderToBuffer(poseStack, vertexconsumer, 15728640, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
