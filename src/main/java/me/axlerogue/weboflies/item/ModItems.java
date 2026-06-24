package me.axlerogue.weboflies.item;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.block.ModBlocks;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WebOfLies.MODID);

    public static final RegistryObject<Item> THREAD_OF_FATE = ITEMS.register("thread_of_fate",
            () -> new ThreadOfFateItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> GOOSE_BERRY = ITEMS.register("goose_berry",
            () -> new GooseBerryItem(ModBlocks.GOOSE_BERRY_BUSH.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.4f).build())));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
