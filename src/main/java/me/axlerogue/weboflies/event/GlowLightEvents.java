package me.axlerogue.weboflies.event;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.block.ModBlocks;
import me.axlerogue.weboflies.entity.BabyBlackWidowEntity;
import me.axlerogue.weboflies.entity.BrownWidowEntity;
import me.axlerogue.weboflies.entity.BabyBrownWidowEntity;
import me.axlerogue.weboflies.entity.BlackWidowBroodMotherEntity;
import me.axlerogue.weboflies.entity.BlackWidowEntity;
import me.axlerogue.weboflies.entity.SpiderEgg;
import me.axlerogue.weboflies.world.ModBiomes;
import me.axlerogue.weboflies.world.ModWorldPresets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = WebOfLies.MODID)
public class GlowLightEvents {
    private static final ResourceKey<Level> DARK_FOREST_KEY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(WebOfLies.MODID, "dark_forest"));
    private static final Map<UUID, BlockPos> ENTITY_LIGHT_POS = new HashMap<>();

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.level.isClientSide) {
            Level level = event.level;
            
            // Only process our entities
            if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                // To avoid ConcurrentModificationException, we might need a safer way or just iterate carefully
                for (Entity entity : serverLevel.getAllEntities()) {
                    if (isModSpider(entity) && entity.isAlive()) {
                        if (!isEffectEnabled(level, entity.blockPosition())) {
                            // If effect is not enabled in this area, remove existing light and skip
                            BlockPos oldPos = ENTITY_LIGHT_POS.remove(entity.getUUID());
                            if (oldPos != null) {
                                removeLight(level, oldPos);
                            }
                            continue;
                        }

                        UUID uuid = entity.getUUID();
                        BlockPos currentPos = entity.blockPosition();
                        BlockPos oldPos = ENTITY_LIGHT_POS.get(uuid);

                        if (oldPos == null || !oldPos.equals(currentPos)) {
                            // Remove old light
                            if (oldPos != null) {
                                removeLight(level, oldPos);
                            }

                            // Place new light if possible (on air or replaceable blocks)
                            // Using setBlock with flag 3 (1 | 2) to cause block update and sync to client
                            ModBlocks.GlowColor color = entity instanceof SpiderEgg ? ModBlocks.GlowColor.GREEN : ModBlocks.GlowColor.RED;
                            BlockState state = ModBlocks.GLOW_LIGHT.get().defaultBlockState().setValue(ModBlocks.COLOR, color);

                            if (level.getBlockState(currentPos).isAir() || level.getBlockState(currentPos).canBeReplaced()) {
                                level.setBlock(currentPos, state, 3);
                                ENTITY_LIGHT_POS.put(uuid, currentPos);
                            } else if (level.getBlockState(currentPos.above()).isAir() || level.getBlockState(currentPos.above()).canBeReplaced()) {
                                BlockPos above = currentPos.above();
                                level.setBlock(above, state, 3);
                                ENTITY_LIGHT_POS.put(uuid, above);
                            } else {
                                ENTITY_LIGHT_POS.remove(uuid);
                            }
                        }
                    }
                }

                // Cleanup positions for entities that no longer exist in this level
                ENTITY_LIGHT_POS.entrySet().removeIf(entry -> {
                    Entity entity = serverLevel.getEntity(entry.getKey());
                    if (entity == null || !entity.isAlive() || entity.level() != level) {
                        removeLight(level, entry.getValue());
                        return true;
                    }
                    return false;
                });
            }
        }
    }

    private static boolean isEffectEnabled(Level level, BlockPos pos) {
        // Must be in our custom world preset
        Optional<? extends Registry<WorldPreset>> worldPresets = level.registryAccess().registry(Registries.WORLD_PRESET);
        if (worldPresets.isEmpty()) return false;
        
        Holder<WorldPreset> preset = worldPresets.get().getHolder(ModWorldPresets.WEB_OF_LIES).orElse(null);
        if (preset == null) return false;

        // Must be in our custom dimension
        if (level.dimension() != DARK_FOREST_KEY) return false;

        // Must be in our custom biomes
        if (pos != null) {
            Holder<Biome> biome = level.getBiome(pos);
            return biome.is(ModBiomes.SPIDER_ROOT_FOREST) || biome.is(ModBiomes.POISON_FANG_SWAMP);
        }

        return false;
    }

    @SubscribeEvent
    public static void onEntityLeave(EntityLeaveLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            UUID uuid = event.getEntity().getUUID();
            BlockPos pos = ENTITY_LIGHT_POS.remove(uuid);
            if (pos != null) {
                removeLight(event.getLevel(), pos);
            }
        }
    }

    private static void removeLight(Level level, BlockPos pos) {
        if (level.getBlockState(pos).is(ModBlocks.GLOW_LIGHT.get())) {
            level.removeBlock(pos, false);
        }
    }

    private static boolean isModSpider(Entity entity) {
        return entity instanceof BlackWidowEntity || 
               entity instanceof BabyBlackWidowEntity || 
               entity instanceof BlackWidowBroodMotherEntity || 
               entity instanceof BrownWidowEntity ||
               entity instanceof BabyBrownWidowEntity ||
               entity instanceof SpiderEgg;
    }
}
