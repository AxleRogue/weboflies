package me.axlerogue.weboflies.entity.renderer;

import me.axlerogue.weboflies.entity.BabyBlackWidowEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import me.axlerogue.weboflies.entity.BlackWidowEntity;

public class BabyBlackWidowRenderer extends BlackWidowRenderer {
    public BabyBlackWidowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.25f;
    }

    @Override
    protected void scale(BlackWidowEntity entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(0.5f, 0.5f, 0.5f);
    }
}
