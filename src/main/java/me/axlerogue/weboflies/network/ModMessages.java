package me.axlerogue.weboflies.network;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.world.HomewardCobwebSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() { return packetId++; }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(WebOfLies.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE.messageBuilder(ServerboundTeleportPacket.class, id())
                .decoder(ServerboundTeleportPacket::new)
                .encoder(ServerboundTeleportPacket::toBytes)
                .consumerMainThread(ServerboundTeleportPacket::handle)
                .add();

        INSTANCE.messageBuilder(ClientboundCobwebListPacket.class, id())
                .decoder(ClientboundCobwebListPacket::new)
                .encoder(ClientboundCobwebListPacket::toBytes)
                .consumerMainThread(ClientboundCobwebListPacket::handle)
                .add();

        INSTANCE.messageBuilder(ClientboundBiomeMessagePacket.class, id())
                .decoder(ClientboundBiomeMessagePacket::new)
                .encoder(ClientboundBiomeMessagePacket::toBytes)
                .consumerMainThread(ClientboundBiomeMessagePacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> player), message);
    }

    // Packet for teleporting
    public static class ServerboundTeleportPacket {
        private final BlockPos pos;
        private final ResourceKey<Level> dimension;

        public ServerboundTeleportPacket(BlockPos pos, ResourceKey<Level> dimension) {
            this.pos = pos;
            this.dimension = dimension;
        }

        public ServerboundTeleportPacket(FriendlyByteBuf buf) {
            this.pos = buf.readBlockPos();
            this.dimension = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
            buf.writeResourceLocation(dimension.location());
        }

        public boolean handle(Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player != null) {
                    ServerLevel targetLevel = player.getServer().getLevel(dimension);
                    if (targetLevel != null) {
                        player.teleportTo(targetLevel, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, player.getYRot(), player.getXRot());
                    }
                }
            });
            return true;
        }
    }

    // Packet for sending cobweb list to client
    public static class ClientboundCobwebListPacket {
        private final List<HomewardCobwebSavedData.CobwebEntry> cobwebs;

        public ClientboundCobwebListPacket(List<HomewardCobwebSavedData.CobwebEntry> cobwebs) {
            this.cobwebs = cobwebs;
        }

        public ClientboundCobwebListPacket(FriendlyByteBuf buf) {
            int size = buf.readInt();
            cobwebs = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                String name = buf.readUtf();
                BlockPos pos = buf.readBlockPos();
                ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
                cobwebs.add(new HomewardCobwebSavedData.CobwebEntry(name, pos, dim));
            }
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeInt(cobwebs.size());
            for (HomewardCobwebSavedData.CobwebEntry entry : cobwebs) {
                buf.writeUtf(entry.name());
                buf.writeBlockPos(entry.pos());
                buf.writeResourceLocation(entry.dimension().location());
            }
        }

        public boolean handle(Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(() -> {
                // Handle on client side
                me.axlerogue.weboflies.client.ClientPacketHandler.handleCobwebList(cobwebs);
            });
            return true;
        }
    }

    public static class ClientboundBiomeMessagePacket {
        private final Component message;

        public ClientboundBiomeMessagePacket(Component message) {
            this.message = message;
        }

        public ClientboundBiomeMessagePacket(FriendlyByteBuf buf) {
            this.message = buf.readComponent();
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeComponent(message);
        }

        public boolean handle(Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(() -> {
                // Handle on client side
                me.axlerogue.weboflies.client.ClientPacketHandler.handleBiomeMessage(message);
            });
            return true;
        }
    }
}
