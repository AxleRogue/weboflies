package me.axlerogue.weboflies.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.axlerogue.weboflies.entity.BlackWidowEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BlackWidowBroodMotherRenderer extends BlackWidowRenderer {
    public BlackWidowBroodMotherRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius *= 3.0F;
    }

    @Override
    protected void scale(BlackWidowEntity entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(3.0F, 3.0F, 3.0F);
    }
}
