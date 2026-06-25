package me.axlerogue.weboflies.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.entity.BlackWidowBroodMotherEntity;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class BlackWidowBroodMotherRenderer extends BaseSpiderRenderer<BlackWidowBroodMotherEntity> {
    public static final ResourceLocation BLACK_WIDOW_LOCATION = new ResourceLocation(WebOfLies.MODID, "textures/entity/spiders/black_widow.png");
    public static final ResourceLocation BLACK_WIDOW_NIGHT_HUNT = new ResourceLocation(WebOfLies.MODID, "textures/entity/spiders/black_widow_night_hunt.png");

    public BlackWidowBroodMotherRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.8f * 3.0f;
        this.addLayer(new BlackWidowBroodMotherNightHuntLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(BlackWidowBroodMotherEntity entity) {
        return BLACK_WIDOW_LOCATION;
    }

    @Override
    protected void scale(BlackWidowBroodMotherEntity entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(3.0F, 3.0F, 3.0F);
    }

    private static class BlackWidowBroodMotherNightHuntLayer extends RenderLayer<BlackWidowBroodMotherEntity, SpiderModel<BlackWidowBroodMotherEntity>> {
        public BlackWidowBroodMotherNightHuntLayer(BlackWidowBroodMotherRenderer renderer) {
            super(renderer);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, BlackWidowBroodMotherEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.level().isNight()) {
                VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.eyes(BLACK_WIDOW_NIGHT_HUNT));
                this.getParentModel().renderToBuffer(poseStack, vertexconsumer, 15728640, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
