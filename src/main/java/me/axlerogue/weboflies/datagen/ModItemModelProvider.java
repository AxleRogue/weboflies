package me.axlerogue.weboflies.datagen;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, WebOfLies.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.THREAD_OF_FATE);
        
        // Block items are already handled in ModBlockStateProvider for simple blocks,
        // but some might need manual registration if they are complex.
        // Based on existing files, most block items just point to the block model.
        // spider_root_sapling item model is a generated model with the sapling texture.
        saplingItem("spider_root_sapling");
        
        // Cobweb items are also generated models
        generatedItem("homeward_cobweb");
        generatedItem("haunted_cobweb");
        generatedItem("spiderweb");
        
        // Goose Berry Bush stages are blocks, and the item is GOOSE_BERRY (registered below)
        // We don't need item models for the individual stages as they are not items.
        // However, the BlockStateProvider uses these names for the block models.

        withExistingParent("glow_light", new ResourceLocation("minecraft:item/generated"));
        
        // Provide an inventory icon for the Goose Berry Bush block item using its mature stage texture
        withExistingParent("goose_berry_bush", new ResourceLocation("minecraft:item/generated"))
                .texture("layer0", new ResourceLocation(WebOfLies.MODID, "block/goose_berry_bush_stage3"));
        
        simpleItem(ModItems.GOOSE_BERRY);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("minecraft:item/generated")).texture("layer0",
                new ResourceLocation(WebOfLies.MODID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder saplingItem(String name) {
        return saplingItem(name, name);
    }

    private ItemModelBuilder saplingItem(String name, String textureName) {
        return withExistingParent(name,
                new ResourceLocation("minecraft:item/generated")).texture("layer0",
                new ResourceLocation(WebOfLies.MODID, "block/" + textureName));
    }

    private ItemModelBuilder generatedItem(String name) {
        return withExistingParent(name,
                new ResourceLocation("minecraft:item/generated")).texture("layer0",
                new ResourceLocation(WebOfLies.MODID, "block/" + name));
    }
}
