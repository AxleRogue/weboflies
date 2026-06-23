package me.axlerogue.weboflies.entity.renderer;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.entity.SpiderEgg;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class SpiderEggModel<T extends SpiderEgg> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(WebOfLies.MODID, "spider_egg"), "main");
	private final ModelPart SpiderEgg;

	public SpiderEggModel(ModelPart root) {
		this.SpiderEgg = root.getChild("SpiderEgg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition SpiderEgg = partdefinition.addOrReplaceChild("SpiderEgg", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 31).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(17, 11).addBox(-3.0F, -10.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(17, 20).addBox(1.0F, -10.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-1.0F, -10.0F, -3.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(25, 0).addBox(-1.0F, -10.0F, 1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(26, 9).addBox(-1.0F, -6.0F, 3.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(26, 18).addBox(-5.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(26, 27).addBox(-1.0F, -6.0F, -5.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(9, 29).addBox(3.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(SpiderEgg entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		SpiderEgg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}