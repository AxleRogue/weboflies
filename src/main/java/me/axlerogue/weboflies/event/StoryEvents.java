package me.axlerogue.weboflies.event;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.entity.BlackWidowBroodMotherEntity;
import me.axlerogue.weboflies.entity.BlackWidowEntity;
import me.axlerogue.weboflies.entity.SpiderEgg;
import me.axlerogue.weboflies.entity.BabyBlackWidowEntity;
import me.axlerogue.weboflies.entity.CorpseEntity;
import me.axlerogue.weboflies.entity.SpiderGibEntity;
import me.axlerogue.weboflies.entity.client.ModEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.ChatFormatting;
import me.axlerogue.weboflies.network.ModMessages;
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

            // Biome Change Detection
            ResourceLocation currentBiome = player.level().getBiome(player.blockPosition()).unwrapKey().map(ResourceKey::location).orElse(null);
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

            if (player.level().dimension() == DARK_FOREST_KEY) {
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
    }

    private static void queueKillMessage(Player player, String message) {
        Component broadcast = Component.literal("[" + player.getName().getString() + "] ").withStyle(ChatFormatting.GOLD)
                .append(Component.literal(message).withStyle(ChatFormatting.WHITE));
        MESSAGE_QUEUE.add(new BroadcastMessage(broadcast));
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (!event.getEntity().level().isClientSide && event.getSource().getEntity() instanceof Player player) {
            if (event.getEntity() instanceof BlackWidowBroodMotherEntity) {
                player.sendSystemMessage(Component.literal("The Matriarch falls, but the web remains..."));
                queueKillMessage(player, "has slain the Black Widow Brood Mother!");
            } else if (event.getEntity() instanceof BlackWidowEntity && !(event.getEntity() instanceof BabyBlackWidowEntity)) {
                if (RANDOM.nextInt(10) == 0) {
                    player.sendSystemMessage(Component.literal("Another thread snapped."));
                }
                queueKillMessage(player, "has terminated a Black Widow.");
            } else if (event.getEntity() instanceof SpiderEgg) {
                player.sendSystemMessage(Component.literal("A future nightmare extinguished."));
                queueKillMessage(player, "has crushed a Spider Egg.");
            } else if (event.getEntity() instanceof BabyBlackWidowEntity) {
                player.sendSystemMessage(Component.literal("Cruel... but necessary."));
                queueKillMessage(player, "has squashed a Baby Black Widow.");
            }
        }

        // Corpse spawning logic
        Level level = event.getEntity().level();
        if (!level.isClientSide && level.dimension() == DARK_FOREST_KEY) {
            boolean shouldSpawnCorpse = false;
            String type = "black_widow";
            float scale = 1.0f;

            if (event.getEntity() instanceof BlackWidowBroodMotherEntity) {
                shouldSpawnCorpse = true;
                type = "brood_mother";
                scale = 3.0f;
            } else if (event.getEntity() instanceof BabyBlackWidowEntity) {
                shouldSpawnCorpse = true;
                type = "baby_black_widow";
                scale = 0.3f;
            } else if (event.getEntity() instanceof BlackWidowEntity) {
                shouldSpawnCorpse = true;
                type = "black_widow";
                scale = 1.0f;
            }

            if (shouldSpawnCorpse) {
                CorpseEntity corpse = ModEntities.CORPSE.get().create(level);
                if (corpse != null) {
                    corpse.setModelType(type);
                    corpse.setEntityScale(scale);
                    corpse.setPos(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
                    corpse.setYRot(event.getEntity().getYRot());
                    level.addFreshEntity(corpse);
                }

                // Dismemberment - spawn gibs
                spawnGibs(level, event.getEntity().getX(), event.getEntity().getY() + 0.5, event.getEntity().getZ(), scale);
            }
        }
    }

    private static void spawnGibs(Level level, double x, double y, double z, float scale) {
        String[] parts = {"head", "body0", "body1", "leg", "leg", "leg", "leg", "leg", "leg", "leg", "leg"};
        for (String part : parts) {
            SpiderGibEntity gib = ModEntities.SPIDER_GIB.get().create(level);
            if (gib != null) {
                gib.setPartType(part);
                gib.setEntityScale(scale);
                gib.setPos(x, y, z);
                
                // Random explosion-like movement
                double speed = 0.2 * scale;
                gib.setDeltaMovement(
                    (RANDOM.nextDouble() - 0.5) * speed,
                    RANDOM.nextDouble() * speed,
                    (RANDOM.nextDouble() - 0.5) * speed
                );
                
                level.addFreshEntity(gib);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!event.getLevel().isClientSide()) {
            Player player = event.getPlayer();
            if (player.level().dimension() == DARK_FOREST_KEY) {
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
            if (player.level().dimension() == DARK_FOREST_KEY) {
                if (RANDOM.nextInt(100) == 0) {
                    player.sendSystemMessage(Component.literal("Building a home in a graveyard of lies?"));
                }
            }
        }
    }
}
