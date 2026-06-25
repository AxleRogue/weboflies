package me.axlerogue.weboflies.event;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.entity.*;
import me.axlerogue.weboflies.entity.client.ModEntities;
import me.axlerogue.weboflies.entity.renderer.BlackWidowRenderer;
import me.axlerogue.weboflies.entity.renderer.BrownWidowRenderer;
import me.axlerogue.weboflies.network.ModMessages;
import me.axlerogue.weboflies.world.ModBiomes;
import me.axlerogue.weboflies.world.ModWorldPresets;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod.EventBusSubscriber(modid = WebOfLies.MODID)
public class StoryEvents {
    private static final ResourceKey<Level> DARK_FOREST_KEY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(WebOfLies.MODID, "dark_forest"));
    private static final Random RANDOM = new Random();
    private static final Map<UUID, Integer> MUMBLE_COOLDOWNS = new HashMap<>();
    private static final Map<UUID, ResourceLocation> LAST_PLAYER_BIOME = new HashMap<>();
    private static final Queue<BroadcastMessage> MESSAGE_QUEUE = new ConcurrentLinkedQueue<>();
    private static int broadcastTimer = 0;

    private static final String[] MUMBLE_MESSAGES = {
        "Did I just see something move?",
        "The air feels thick... like webs.",
        "How long have I been in this forest?",
        "I shouldn't have come here.",
        "The King is watching. I can feel it.",
        "Every step feels like I'm being tracked.",
        "Is it just me, or is the fog getting darker?",
        "I need to get out... but the webs are everywhere.",
        "A forest of lies... and I'm just another fly.",
        "That rustling sound again..."
    };

    private static class BroadcastMessage {
        final Component message;
        public BroadcastMessage(Component message) {
            this.message = message;
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (++broadcastTimer >= 100) { // 5 seconds (20 ticks * 5)
                broadcastTimer = 0;
                BroadcastMessage nextMessage = MESSAGE_QUEUE.poll();
                if (nextMessage != null) {
                    MinecraftServer server = event.getServer();
                    server.getPlayerList().broadcastSystemMessage(nextMessage.message, false);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
            Player player = event.player;
            UUID uuid = player.getUUID();
            Level level = player.level();
            BlockPos pos = player.blockPosition();

            if (!isEffectEnabled(level, pos)) {
                return;
            }

            // Biome Change Detection
            ResourceLocation currentBiome = level.getBiome(pos).unwrapKey().map(ResourceKey::location).orElse(null);
            ResourceLocation lastBiome = LAST_PLAYER_BIOME.get(uuid);

            if (currentBiome != null && !currentBiome.equals(lastBiome)) {
                LAST_PLAYER_BIOME.put(uuid, currentBiome);
                if (currentBiome.equals(new ResourceLocation(WebOfLies.MODID, "spider_root_forest"))) {
                    ModMessages.sendToPlayer(new ModMessages.ClientboundBiomeMessagePacket(
                            Component.literal("Spider Root Forest").withStyle(ChatFormatting.RED, ChatFormatting.BOLD)), (ServerPlayer) player);
                } else if (currentBiome.equals(new ResourceLocation(WebOfLies.MODID, "poison_fang_swamp"))) {
                    ModMessages.sendToPlayer(new ModMessages.ClientboundBiomeMessagePacket(
                            Component.literal("Poison Fang Swamp").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)), (ServerPlayer) player);
                }
            }

            int cooldown = MUMBLE_COOLDOWNS.getOrDefault(uuid, 0);
            if (cooldown > 0) {
                MUMBLE_COOLDOWNS.put(uuid, cooldown - 1);
            } else {
                if (RANDOM.nextInt(12000) == 0) { // Average once every 10 mins at 20tps
                    player.sendSystemMessage(Component.literal(MUMBLE_MESSAGES[RANDOM.nextInt(MUMBLE_MESSAGES.length)]).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
                    MUMBLE_COOLDOWNS.put(uuid, 6000); // 5 min minimum cooldown
                }
            }
        }
    }

    private static void queueKillMessage(Player player, String message) {
        Component broadcast = Component.literal("[" + player.getName().getString() + "] ").withStyle(ChatFormatting.GOLD)
                .append(Component.literal(message).withStyle(ChatFormatting.WHITE));
        MESSAGE_QUEUE.add(new BroadcastMessage(broadcast));
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        Level level = event.getEntity().level();
        if (level.isClientSide) return;
        
        BlockPos deathPos = event.getEntity().blockPosition();
        boolean effectEnabled = isEffectEnabled(level, deathPos);

        if (event.getSource().getEntity() instanceof Player player && effectEnabled) {
            if (event.getEntity() instanceof BlackWidowBroodMotherEntity) {
                player.sendSystemMessage(Component.literal("The Matriarch falls, but the web remains..."));
                queueKillMessage(player, "has slain the Black Widow Brood Mother!");
            } else if (event.getEntity() instanceof BlackWidowEntity && !(event.getEntity() instanceof BabyBlackWidowEntity)) {
                if (RANDOM.nextInt(10) == 0) {
                    player.sendSystemMessage(Component.literal("Another thread snapped."));
                }
                queueKillMessage(player, "has terminated a Black Widow.");
            } else if (event.getEntity() instanceof BrownWidowEntity && !(event.getEntity() instanceof BabyBrownWidowEntity)) {
                if (RANDOM.nextInt(10) == 0) {
                    player.sendSystemMessage(Component.literal("A male falls, but the colony persists."));
                }
                queueKillMessage(player, "has terminated a Brown Widow.");
            } else if (event.getEntity() instanceof SpiderEgg) {
                player.sendSystemMessage(Component.literal("A future nightmare extinguished."));
                queueKillMessage(player, "has crushed a Spider Egg.");
            } else if (event.getEntity() instanceof BabyBlackWidowEntity || event.getEntity() instanceof BabyBrownWidowEntity) {
                player.sendSystemMessage(Component.literal("Cruel... but necessary."));
                queueKillMessage(player, "has squashed a baby spider.");
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!event.getLevel().isClientSide()) {
            Player player = event.getPlayer();
            Level level = player.level();
            BlockPos pos = event.getPos();
            if (isEffectEnabled(level, pos)) {
                if (event.getState().is(Blocks.DARK_OAK_LOG) || event.getState().is(Blocks.DARK_OAK_WOOD)) {
                    if (RANDOM.nextInt(50) == 0) {
                        player.sendSystemMessage(Component.literal("The wood here screams of ancient lies."));
                    }
                } else if (event.getState().is(Blocks.COBWEB)) {
                    if (RANDOM.nextInt(20) == 0) {
                        player.sendSystemMessage(Component.literal("You tear the web, but the King still sees."));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof Player player) {
            Level level = player.level();
            BlockPos pos = event.getPos();
            if (isEffectEnabled(level, pos)) {
                if (RANDOM.nextInt(100) == 0) {
                    player.sendSystemMessage(Component.literal("Building a home in a graveyard of lies?"));
                }
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
}
