package me.axlerogue.weboflies.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.axlerogue.weboflies.entity.SpiderGibEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Axis;

public class SpiderGibRenderer extends EntityRenderer<SpiderGibEntity> {
    private final ModelPart head;
    private final ModelPart body0;
    private final ModelPart body1;
    private final ModelPart leg;

    public SpiderGibRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart root = context.bakeLayer(ModelLayers.SPIDER);
        this.head = getPartSafe(root, "head");
        this.body0 = getPartSafe(root, "body0");
        this.body1 = getPartSafe(root, "body1");
        this.leg = getPartSafe(root, "right_hind_leg", "leg0");
    }

    private ModelPart getPartSafe(ModelPart root, String... names) {
        for (String name : names) {
            try {
                return root.getChild(name);
            } catch (Exception ignored) {
            }
        }
        return new ModelPart(java.util.Collections.emptyList(), java.util.Collections.emptyMap());
    }

    @Override
    public void render(SpiderGibEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        
        float scale = entity.getEntityScale();
        poseStack.scale(scale, scale, scale);
        
        // Add some spin based on entity lifeTime
        float spin = (entity.tickCount + partialTicks) * 10.0f;
        poseStack.mulPose(Axis.XP.rotationDegrees(spin));
        poseStack.mulPose(Axis.YP.rotationDegrees(spin * 0.5f));

        ModelPart partToRender = switch (entity.getPartType()) {
            case "head" -> head;
            case "body0" -> body0;
            case "body1" -> body1;
            default -> leg;
        };

        if (partToRender != null) {
            // Adjust for model origin
            poseStack.translate(0.0D, -1.0D, 0.0D);
            partToRender.render(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity))), packedLight, 655360, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SpiderGibEntity entity) {
        return BlackWidowRenderer.BLACK_WIDOW_LOCATION;
    }
}
