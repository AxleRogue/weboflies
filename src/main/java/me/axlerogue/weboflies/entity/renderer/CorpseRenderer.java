package me.axlerogue.weboflies.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.axlerogue.weboflies.entity.CorpseEntity;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Axis;

import net.minecraft.client.model.geom.ModelPart;
import java.util.Random;

public class CorpseRenderer extends EntityRenderer<CorpseEntity> {
    private final SpiderModel<CorpseEntity> spiderModel;
    private final ModelPart head;
    private final ModelPart body0;
    private final ModelPart body1;
    private final ModelPart leg0;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart leg5;
    private final ModelPart leg6;
    private final ModelPart leg7;

    public CorpseRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart root = context.bakeLayer(ModelLayers.SPIDER);
        this.spiderModel = new SpiderModel<>(root);
        this.head = getPartSafe(root, "head");
        this.body0 = getPartSafe(root, "body0");
        this.body1 = getPartSafe(root, "body1");
        this.leg0 = getPartSafe(root, "right_hind_leg", "leg0");
        this.leg1 = getPartSafe(root, "left_hind_leg", "leg1");
        this.leg2 = getPartSafe(root, "right_middle_rear_leg", "leg2");
        this.leg3 = getPartSafe(root, "left_middle_rear_leg", "leg3");
        this.leg4 = getPartSafe(root, "right_middle_front_leg", "leg4");
        this.leg5 = getPartSafe(root, "left_middle_front_leg", "leg5");
        this.leg6 = getPartSafe(root, "right_front_leg", "leg6");
        this.leg7 = getPartSafe(root, "left_front_leg", "leg7");
    }

    private ModelPart getPartSafe(ModelPart root, String... names) {
        for (String name : names) {
            try {
                return root.getChild(name);
            } catch (Exception ignored) {
            }
        }
        // Return a dummy part if not found to prevent NPE
        return new ModelPart(java.util.Collections.emptyList(), java.util.Collections.emptyMap());
    }

    @Override
    public void render(CorpseEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        
        float scale = entity.getEntityScale();
        poseStack.scale(scale, scale, scale);
        
        // Lay flat on ground
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.translate(0.0D, -0.05D, 0.0D); // Slightly above ground
        
        if (!"player".equals(entity.getModelType())) {
            Random random = new Random(entity.getUUID().getLeastSignificantBits());
            
            // Render parts spread out
            if (head != null) renderPart(poseStack, buffer, packedLight, head, random, 0.2f);
            if (body0 != null) renderPart(poseStack, buffer, packedLight, body0, random, 0.3f);
            if (body1 != null) renderPart(poseStack, buffer, packedLight, body1, random, 0.4f);
            if (leg0 != null) renderPart(poseStack, buffer, packedLight, leg0, random, 0.5f);
            if (leg1 != null) renderPart(poseStack, buffer, packedLight, leg1, random, 0.5f);
            if (leg2 != null) renderPart(poseStack, buffer, packedLight, leg2, random, 0.5f);
            if (leg3 != null) renderPart(poseStack, buffer, packedLight, leg3, random, 0.5f);
            if (leg4 != null) renderPart(poseStack, buffer, packedLight, leg4, random, 0.5f);
            if (leg5 != null) renderPart(poseStack, buffer, packedLight, leg5, random, 0.5f);
            if (leg6 != null) renderPart(poseStack, buffer, packedLight, leg6, random, 0.5f);
            if (leg7 != null) renderPart(poseStack, buffer, packedLight, leg7, random, 0.5f);
        }
        
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private void renderPart(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ModelPart part, Random random, float spread) {
        poseStack.pushPose();
        
        // Random offset on the "ground" (which is XZ plane after rotation)
        float dx = (random.nextFloat() - 0.5f) * spread;
        float dy = (random.nextFloat() - 0.5f) * spread;
        poseStack.translate(dx, dy, 0.0D);
        
        // Random rotation
        poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0f));
        
        // Offset for spider model alignment
        poseStack.translate(0.0D, -1.0D, 0.0D);

        part.render(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(BlackWidowRenderer.BLACK_WIDOW_LOCATION)), packedLight, 655360, 1.0F, 1.0F, 1.0F, 1.0F);
        
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(CorpseEntity entity) {
        return BlackWidowRenderer.BLACK_WIDOW_LOCATION;
    }
}
