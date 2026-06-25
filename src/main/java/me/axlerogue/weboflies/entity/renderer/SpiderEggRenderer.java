package me.axlerogue.weboflies.entity.renderer;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.entity.SpiderEgg;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SpiderEggRenderer extends LivingEntityRenderer<SpiderEgg, SpiderEggModel<SpiderEgg>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WebOfLies.MODID, "textures/entity/spider_egg/spider_egg.png");

    public SpiderEggRenderer(EntityRendererProvider.Context context) {
        super(context, new SpiderEggModel<>(context.bakeLayer(SpiderEggModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    protected void renderNameTag(SpiderEgg pEntity, Component pDisplayName, com.mojang.blaze3d.vertex.PoseStack pPoseStack, net.minecraft.client.renderer.MultiBufferSource pBuffer, int pPackedLight) {
        super.renderNameTag(pEntity, pDisplayName, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    protected boolean shouldShowName(SpiderEgg pEntity) {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation(SpiderEgg entity) {
        return TEXTURE;
    }
}
