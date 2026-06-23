package me.axlerogue.weboflies.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;

public class HomewardCobwebSavedData extends SavedData {
    private final List<CobwebEntry> cobwebs = new ArrayList<>();

    public static HomewardCobwebSavedData get(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            throw new RuntimeException("Cannot access HomewardCobwebSavedData from client side!");
        }
        return serverLevel.getServer().overworld().getDataStorage().computeIfAbsent(HomewardCobwebSavedData::load, HomewardCobwebSavedData::new, "homeward_cobwebs");
    }

    public List<CobwebEntry> getCobwebs() {
        return cobwebs;
    }

    public void addCobweb(String name, BlockPos pos, ResourceKey<Level> dimension) {
        cobwebs.removeIf(entry -> entry.pos.equals(pos) && entry.dimension.equals(dimension));
        cobwebs.add(new CobwebEntry(name, pos, dimension));
        setDirty();
    }

    public void removeCobweb(BlockPos pos, ResourceKey<Level> dimension) {
        if (cobwebs.removeIf(entry -> entry.pos.equals(pos) && entry.dimension.equals(dimension))) {
            setDirty();
        }
    }

    public static HomewardCobwebSavedData load(CompoundTag tag) {
        HomewardCobwebSavedData data = new HomewardCobwebSavedData();
        ListTag list = tag.getList("Cobwebs", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entryTag = list.getCompound(i);
            String name = entryTag.getString("Name");
            BlockPos pos = BlockPos.of(entryTag.getLong("Pos"));
            ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(entryTag.getString("Dimension")));
            data.cobwebs.add(new CobwebEntry(name, pos, dim));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (CobwebEntry entry : cobwebs) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putString("Name", entry.name);
            entryTag.putLong("Pos", entry.pos.asLong());
            entryTag.putString("Dimension", entry.dimension.location().toString());
            list.add(entryTag);
        }
        tag.put("Cobwebs", list);
        return tag;
    }

    public record CobwebEntry(String name, BlockPos pos, ResourceKey<Level> dimension) {}
}
