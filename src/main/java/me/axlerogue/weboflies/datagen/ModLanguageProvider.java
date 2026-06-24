package me.axlerogue.weboflies.datagen;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.block.ModBlocks;
import me.axlerogue.weboflies.item.ModItems;
import me.axlerogue.weboflies.entity.client.ModEntities;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, WebOfLies.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("creativetab.weboflies.blocks", "Web Of Lies Blocks");
        add("creativetab.weboflies.items", "Web Of Lies Items");

        add("subtitles.weboflies.poison_fang_swamp_music", "Poison Fang Swamp Music plays");
        add("subtitles.weboflies.spider_root_forest_music", "Spider Root Forest Music plays");

        addBlock(ModBlocks.HOMEWARD_COBWEB, "Homeward Cobweb");
        addBlock(ModBlocks.HAUNTED_COBWEB, "Haunted Cobweb");
        addBlock(ModBlocks.SPIDERWEB, "Spiderweb");
        addBlock(ModBlocks.SPIDER_ROOT_LOG, "Spider Root Log");
        addBlock(ModBlocks.SPIDER_ROOT_PLANKS, "Spider Root Planks");
        addBlock(ModBlocks.SPIDER_ROOT_LEAVES, "Spider Root Leaves");
        addBlock(ModBlocks.SPIDER_ROOT_SAPLING, "Spider Root Sapling");
        addBlock(ModBlocks.GLOW_LIGHT, "Glow Light");
        addBlock(ModBlocks.GOOSE_BERRY_BUSH, "Goose Berry Bush");

        addItem(ModItems.THREAD_OF_FATE, "Thread of Fate");
        addItem(ModItems.GOOSE_BERRY, "Goose Berry");
        // These are registered as block items, but we can add them as items if needed.
        // Usually addBlock covers the block item name too.
        
        addEntityType(ModEntities.SPIDER_EGG, "Spider Egg");
        addEntityType(ModEntities.BLACK_WIDOW, "Black Widow");
        addEntityType(ModEntities.BABY_BLACK_WIDOW, "Baby Black Widow");
        addEntityType(ModEntities.BLACK_WIDOW_BROOD_MOTHER, "Black Widow Brood Mother");
        addEntityType(ModEntities.HAUNTED_COBWEB_PROJECTILE, "Haunted Cobweb Projectile");
        addEntityType(ModEntities.CORPSE, "Corpse");
        addEntityType(ModEntities.SPIDER_GIB, "Spider Gib");

        add("biome.weboflies.dark_forest", "The Dark Forest");
        add("biome.weboflies.spider_root_forest", "Spider Root Forest");
        add("biome.weboflies.poison_fang_swamp", "Poison Fang Swamp");

        add("advancement.weboflies.root.title", "Web Of Lies");
        add("advancement.weboflies.root.description", "The forgotten dimension of the King of Lies.");
        add("advancement.weboflies.kill_black_widow.title", "That was easy");
        add("advancement.weboflies.kill_black_widow.description", "Slay a Black Widow.");
        add("advancement.weboflies.witness_baby_black_widow.title", "Growing Hunger");
        add("advancement.weboflies.witness_baby_black_widow.description", "Witness a Baby Black Widow");
        add("advancement.weboflies.kill_spider_egg.title", "Get squished");
        add("advancement.weboflies.kill_spider_egg.description", "Slay a Spider Egg before it hatches.");
        add("advancement.weboflies.kill_black_widow_brood_mother.title", "Kill it with fire");
        add("advancement.weboflies.kill_black_widow_brood_mother.description", "Defeat the Black Widow Brood Mother with fire.");
        add("advancement.weboflies.obtain_thread_of_fate.title", "Tied by Fate");
        add("advancement.weboflies.obtain_thread_of_fate.description", "Craft or obtain the Thread of Fate");
        add("advancement.weboflies.obtain_homeward_cobweb.title", "Home is where the Web is");
        add("advancement.weboflies.obtain_homeward_cobweb.description", "Craft or obtain a Homeward Cobweb");
        add("advancement.weboflies.blackwidow_master_slayer.title", "BlackWidow Master Slayer");
        add("advancement.weboflies.blackwidow_master_slayer.description", "You have conquered all stages of the Black Widow.");
        add("advancement.weboflies.eat_goose_berry.title", "Sweet & Sour");
        add("advancement.weboflies.eat_goose_berry.description", "Eat a Goose Berry.");
        add("advancement.weboflies.breed_black_widow.title", "Brood Expansion");
        add("advancement.weboflies.breed_black_widow.description", "Breed two Black Widows.");
    }
}
