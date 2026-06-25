package me.axlerogue.weboflies.entity.renderer;
import me.axlerogue.weboflies.entity.client.BaseSpiderModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Mob;

public abstract class BaseSpiderRenderer<T extends Mob> extends MobRenderer<T, SpiderModel<T>> {
    public BaseSpiderRenderer(EntityRendererProvider.Context context) {
        super(context, new BaseSpiderModel<>(context.bakeLayer(ModelLayers.SPIDER)), 0.8F);
    }

    @Override
    protected void renderNameTag(T pEntity, Component pDisplayName, com.mojang.blaze3d.vertex.PoseStack pPoseStack, net.minecraft.client.renderer.MultiBufferSource pBuffer, int pPackedLight) {
        super.renderNameTag(pEntity, pDisplayName, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    protected boolean shouldShowName(T pEntity) {
        return true;
    }
}
