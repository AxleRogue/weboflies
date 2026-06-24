package me.axlerogue.weboflies.datagen;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.block.ModBlocks;
import me.axlerogue.weboflies.block.GooseBerryBushBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, WebOfLies.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.SPIDER_ROOT_PLANKS);
        blockWithItem(ModBlocks.SPIDER_ROOT_LEAVES);

        logBlock((RotatedPillarBlock) ModBlocks.SPIDER_ROOT_LOG.get());
        simpleBlockItem(ModBlocks.SPIDER_ROOT_LOG.get(), models().withExistingParent("spider_root_log", "block/cube_column"));

        saplingBlock(ModBlocks.SPIDER_ROOT_SAPLING);
        gooseBerryBushBlock(ModBlocks.GOOSE_BERRY_BUSH);

        // Cobwebs - usually these are cross models
        crossBlock(ModBlocks.HOMEWARD_COBWEB);
        crossBlock(ModBlocks.HAUNTED_COBWEB);
        crossBlock(ModBlocks.SPIDERWEB);

        // Glow light is invisible, but we should probably give it a dummy model or skip it
        // Since it's RenderShape.INVISIBLE, vanilla doesn't look for a model, but BlockStateProvider needs one.
        simpleBlock(ModBlocks.GLOW_LIGHT.get(), models().withExistingParent("glow_light", "block/air"));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void saplingBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(blockRegistryObject.getId().getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void gooseBerryBushBlock(RegistryObject<Block> blockRegistryObject) {
        VariantBlockStateBuilder builder = getVariantBuilder(blockRegistryObject.get());
        for (int age = 0; age <= 3; age++) {
            String modelName = blockRegistryObject.getId().getPath() + "_age" + age;
            String texturePath = "block/" + blockRegistryObject.getId().getPath() + "_stage" + age;
            builder.partialState().with(GooseBerryBushBlock.AGE, age)
                .modelForState()
                .modelFile(models().cross(modelName, new ResourceLocation(WebOfLies.MODID, texturePath)).renderType("cutout"))
                .addModel();
        }
    }

    private void crossBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(blockRegistryObject.getId().getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }
}
