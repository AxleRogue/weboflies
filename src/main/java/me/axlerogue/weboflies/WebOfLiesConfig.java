package me.axlerogue.weboflies;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = WebOfLies.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WebOfLiesConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue EXAMPLE_BOOL = BUILDER
            .comment("An example boolean configuration")
            .define("exampleBool", true);

    public static final ForgeConfigSpec.ConfigValue<String> EXAMPLE_STRING = BUILDER
            .comment("An example string configuration")
            .define("exampleString", "Hello World");

    static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }
}
