package me.axlerogue.weboflies.block.entity;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, WebOfLies.MODID);

    public static final RegistryObject<BlockEntityType<HomewardCobwebBlockEntity>> HOMEWARD_COBWEB =
            BLOCK_ENTITIES.register("homeward_cobweb", () ->
                    BlockEntityType.Builder.of(HomewardCobwebBlockEntity::new,
                            ModBlocks.HOMEWARD_COBWEB.get()).build(null));
}
