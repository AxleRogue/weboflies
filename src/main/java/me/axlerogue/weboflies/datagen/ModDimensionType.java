package me.axlerogue.weboflies.datagen;

import me.axlerogue.weboflies.WebOfLies;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.OptionalLong;

public class ModDimensionType {
    public static final ResourceKey<DimensionType> DARK_FOREST_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(WebOfLies.MODID, "dark_forest"));

    public static void bootstrap(BootstapContext<DimensionType> context) {
        context.register(DARK_FOREST_TYPE, new DimensionType(
                OptionalLong.empty(), // fixedTime
                true, // hasSkylight
                false, // hasCeiling
                false, // ultwarm
                true, // natural
                1.0, // coordinateScale
                true, // bedWorks
                false, // respawnAnchorWorks
                -64, // minY
                384, // height
                384, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                new ResourceLocation(WebOfLies.MODID, "dark_forest"), // effectsLocation
                0.0f, // ambientLight
                new DimensionType.MonsterSettings(false, true, ConstantInt.of(0), 0)
        ));
    }
}
