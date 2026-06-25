package me.axlerogue.weboflies.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class MaleBaseSpider extends BaseSpiderEntity {
    public MaleBaseSpider(EntityType<? extends MaleBaseSpider> type, Level level) {
        super(type, level);
    }

    @Override
    public Genders getGender() {
        return Genders.MALE;
    }

    @Override
    public void setNestPos(BlockPos pos) {
        // Males don't nest by default?
    }

    @Override
    public BlockPos getNestPos() {
        return null;
    }
}
