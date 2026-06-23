package me.axlerogue.weboflies.client;

import me.axlerogue.weboflies.world.HomewardCobwebSavedData;
import net.minecraft.client.Minecraft;

import java.util.List;

public class ClientPacketHandler {
    private static List<HomewardCobwebSavedData.CobwebEntry> lastCobwebList = List.of();

    public static void handleCobwebList(List<HomewardCobwebSavedData.CobwebEntry> cobwebs) {
        lastCobwebList = cobwebs;
        if (Minecraft.getInstance().screen instanceof HomewardCobwebScreen screen) {
            screen.updateButtons(cobwebs);
        }
    }

    public static List<HomewardCobwebSavedData.CobwebEntry> getLastCobwebList() {
        return lastCobwebList;
    }
}
