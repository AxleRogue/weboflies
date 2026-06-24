package me.axlerogue.weboflies.datagen;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, WebOfLies.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.SPIDER_ROOT_LOG.get(),
                     ModBlocks.SPIDER_ROOT_PLANKS.get());

        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.SPIDER_ROOT_LOG.get());
        
        this.tag(BlockTags.LEAVES)
                .add(ModBlocks.SPIDER_ROOT_LEAVES.get());

        this.tag(BlockTags.SAPLINGS)
                .add(ModBlocks.SPIDER_ROOT_SAPLING.get());

        this.tag(BlockTags.BEE_GROWABLES)
                .add(ModBlocks.GOOSE_BERRY_BUSH.get());
    }
}
