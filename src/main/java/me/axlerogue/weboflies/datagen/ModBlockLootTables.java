package me.axlerogue.weboflies.datagen;

import me.axlerogue.weboflies.item.ModItems;
import me.axlerogue.weboflies.block.ModBlocks;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import me.axlerogue.weboflies.block.GooseBerryBushBlock;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.HOMEWARD_COBWEB.get());
        this.dropSelf(ModBlocks.HAUNTED_COBWEB.get());
        this.dropSelf(ModBlocks.SPIDERWEB.get());
        this.dropSelf(ModBlocks.SPIDER_ROOT_LOG.get());
        this.dropSelf(ModBlocks.SPIDER_ROOT_PLANKS.get());
        this.dropSelf(ModBlocks.SPIDER_ROOT_SAPLING.get());

        this.add(ModBlocks.GOOSE_BERRY_BUSH.get(), block -> {
            LootItemCondition.Builder lootitemcondition$builder = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(GooseBerryBushBlock.AGE, 3));
            return applyExplosionDecay(block, LootTable.lootTable()
                    .withPool(net.minecraft.world.level.storage.loot.LootPool.lootPool()
                            .add(net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem(ModItems.GOOSE_BERRY.get())
                                    .apply(net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount(net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between(2.0F, 3.0F)))
                                    .when(lootitemcondition$builder)))
                    .withPool(net.minecraft.world.level.storage.loot.LootPool.lootPool()
                            .add(net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem(ModItems.GOOSE_BERRY.get())
                                    .apply(net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount(net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between(1.0F, 2.0F)))
                                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(GooseBerryBushBlock.AGE, 2))))));
        });

        this.add(ModBlocks.SPIDER_ROOT_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.SPIDER_ROOT_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(block -> !(block instanceof net.minecraft.world.level.block.AirBlock))
                ::iterator;
    }
}
