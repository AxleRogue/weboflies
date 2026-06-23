package me.axlerogue.weboflies.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HomewardCobwebBlockEntity extends BlockEntity {
    private String cobwebName = "Unnamed Web";

    public HomewardCobwebBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HOMEWARD_COBWEB.get(), pos, state);
    }

    public String getCobwebName() {
        return cobwebName;
    }

    public void setCobwebName(String name) {
        this.cobwebName = name;
        setChanged();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("CobwebName")) {
            this.cobwebName = tag.getString("CobwebName");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("CobwebName", this.cobwebName);
    }
}
