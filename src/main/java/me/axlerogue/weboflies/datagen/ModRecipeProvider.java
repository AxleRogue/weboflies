package me.axlerogue.weboflies.datagen;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.block.ModBlocks;
import me.axlerogue.weboflies.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.THREAD_OF_FATE.get())
                .pattern("SSS")
                .pattern("SGS")
                .pattern("SSS")
                .define('S', Items.STRING)
                .define('G', Items.GHAST_TEAR)
                .unlockedBy("has_string", has(Items.STRING))
                .unlockedBy("has_ghast_tear", has(Items.GHAST_TEAR))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.HOMEWARD_COBWEB.get())
                .pattern("SSS")
                .pattern("SNS")
                .pattern("SSS")
                .define('S', Items.STRING)
                .define('N', Items.NETHER_STAR)
                .unlockedBy("has_string", has(Items.STRING))
                .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                .save(pWriter);

        // Wool recipes
        woolRecipe(pWriter, Items.BLACK_DYE, Items.BLACK_WOOL, "black_wool_from_string");
        woolRecipe(pWriter, Items.BLUE_DYE, Items.BLUE_WOOL, "blue_wool_from_string");
        woolRecipe(pWriter, Items.BROWN_DYE, Items.BROWN_WOOL, "brown_wool_from_string");
        woolRecipe(pWriter, Items.CYAN_DYE, Items.CYAN_WOOL, "cyan_wool_from_string");
        woolRecipe(pWriter, Items.GRAY_DYE, Items.GRAY_WOOL, "gray_wool_from_string");
        woolRecipe(pWriter, Items.GREEN_DYE, Items.GREEN_WOOL, "green_wool_from_string");
        woolRecipe(pWriter, Items.LIGHT_BLUE_DYE, Items.LIGHT_BLUE_WOOL, "light_blue_wool_from_string");
        woolRecipe(pWriter, Items.LIGHT_GRAY_DYE, Items.LIGHT_GRAY_WOOL, "light_gray_wool_from_string");
        woolRecipe(pWriter, Items.LIME_DYE, Items.LIME_WOOL, "lime_wool_from_string");
        woolRecipe(pWriter, Items.MAGENTA_DYE, Items.MAGENTA_WOOL, "magenta_wool_from_string");
        woolRecipe(pWriter, Items.ORANGE_DYE, Items.ORANGE_WOOL, "orange_wool_from_string");
        woolRecipe(pWriter, Items.PINK_DYE, Items.PINK_WOOL, "pink_wool_from_string");
        woolRecipe(pWriter, Items.PURPLE_DYE, Items.PURPLE_WOOL, "purple_wool_from_string");
        woolRecipe(pWriter, Items.RED_DYE, Items.RED_WOOL, "red_wool_from_string");
        woolRecipe(pWriter, Items.WHITE_DYE, Items.WHITE_WOOL, "white_wool_from_string");
        woolRecipe(pWriter, Items.YELLOW_DYE, Items.YELLOW_WOOL, "yellow_wool_from_string");

        // Spider Root wood recipes
        planksFromLog(pWriter, ModBlocks.SPIDER_ROOT_PLANKS.get(), ModBlocks.SPIDER_ROOT_LOG.get());
    }

    private void planksFromLog(Consumer<FinishedRecipe> pWriter, ItemLike planks, ItemLike log) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
                .requires(log)
                .unlockedBy("has_log", has(log))
                .save(pWriter);
    }

    private void woolRecipe(Consumer<FinishedRecipe> pWriter, ItemLike dye, ItemLike wool, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wool)
                .pattern("SSS")
                .pattern("SDS")
                .pattern("SSS")
                .define('S', Items.STRING)
                .define('D', dye)
                .unlockedBy("has_string", has(Items.STRING))
                .save(pWriter, new net.minecraft.resources.ResourceLocation(WebOfLies.MODID, name));
    }
}
