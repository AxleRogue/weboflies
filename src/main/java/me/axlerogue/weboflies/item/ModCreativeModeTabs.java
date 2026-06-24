package me.axlerogue.weboflies.item;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import net.minecraft.world.item.Items;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WebOfLies.MODID);

    public static final RegistryObject<CreativeModeTab> WEB_OF_LIES_BLOCKS = CREATIVE_MODE_TABS.register("web_of_lies_blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.HOMEWARD_COBWEB.get()))
                    .title(Component.translatable("creativetab.weboflies.blocks"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.HOMEWARD_COBWEB.get());
                        pOutput.accept(ModBlocks.HAUNTED_COBWEB.get());
                        pOutput.accept(ModBlocks.SPIDERWEB.get());
                        pOutput.accept(ModBlocks.SPIDER_ROOT_LOG.get());
                        pOutput.accept(ModBlocks.SPIDER_ROOT_PLANKS.get());
                        pOutput.accept(ModBlocks.SPIDER_ROOT_LEAVES.get());
                        pOutput.accept(ModBlocks.SPIDER_ROOT_SAPLING.get());
                        pOutput.accept(ModBlocks.GOOSE_BERRY_BUSH.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> WEB_OF_LIES_ITEMS = CREATIVE_MODE_TABS.register("web_of_lies_items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.THREAD_OF_FATE.get()))
                    .title(Component.translatable("creativetab.weboflies.items"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.THREAD_OF_FATE.get());
                        pOutput.accept(ModItems.GOOSE_BERRY.get());
                        pOutput.accept(Items.SPIDER_EYE);
                        pOutput.accept(Items.FERMENTED_SPIDER_EYE);
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
