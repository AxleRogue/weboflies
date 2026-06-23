package me.axlerogue.weboflies.event;

import me.axlerogue.weboflies.WebOfLies;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = WebOfLies.MODID)
public class PortalEvents {
    private static final ResourceKey<Level> DARK_FOREST_KEY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(WebOfLies.MODID, "dark_forest"));
    private static final Random RANDOM = new Random();
    private static final String[] STORY_LINES = {
            "The web tightens around your soul...",
            "The King of Lies watches from the shadows.",
            "This dimension was forgotten for a reason.",
            "Every thread is a lie woven into reality.",
            "Can you hear the skittering in the dark?"
    };

    @SubscribeEvent
    public static void onRightClickCobweb(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (!level.isClientSide && level.getBlockState(event.getPos()).is(Blocks.COBWEB)) {
            Player player = event.getEntity();
            if (level.dimension() != DARK_FOREST_KEY) {
                if (RANDOM.nextInt(10) == 0) {
                    player.sendSystemMessage(Component.literal(STORY_LINES[RANDOM.nextInt(STORY_LINES.length)]));
                }
                
                ServerLevel darkForest = ((ServerLevel) level).getServer().getLevel(DARK_FOREST_KEY);
                if (darkForest != null) {
                    // Teleport to 0, 0 as requested
                    BlockPos targetPos = new BlockPos(0, 64, 0);
                    BlockPos safePos = darkForest.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, targetPos);
                    BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(safePos.getX(), safePos.getY(), safePos.getZ());
                    
                    boolean foundSurface = false;
                    while (mutablePos.getY() > darkForest.getMinBuildHeight()) {
                        if (darkForest.getBlockState(mutablePos).is(Blocks.GRASS_BLOCK) || darkForest.getBlockState(mutablePos).is(Blocks.PODZOL)) {
                            foundSurface = true;
                            break;
                        }
                        if (darkForest.getBlockState(mutablePos).isSolidRender(darkForest, mutablePos) && darkForest.getBlockState(mutablePos.above()).isAir()) {
                            foundSurface = true;
                            break;
                        }
                        mutablePos.move(0, -1, 0);
                    }
                    
                    if (!foundSurface) {
                        mutablePos.set(0, 63, 0);
                        if (darkForest.getBlockState(mutablePos).isAir() || darkForest.getBlockState(mutablePos).is(Blocks.BEDROCK)) {
                            darkForest.setBlockAndUpdate(mutablePos, Blocks.GRASS_BLOCK.defaultBlockState());
                        }
                    }

                    BlockPos finalPos = mutablePos.immutable().above();
                    player.teleportTo(darkForest, finalPos.getX() + 0.5, finalPos.getY(), finalPos.getZ() + 0.5, java.util.Collections.emptySet(), player.getYRot(), player.getXRot());
                }
            }
        }
    }
}
