package me.axlerogue.weboflies.entity;

import net.minecraft.core.BlockPos;

public interface ISpider {
    Genders getGender();
    
    default boolean isFemale() {
        return getGender() == Genders.FEMALE;
    }
    
    default boolean isMale() {
        return getGender() == Genders.MALE;
    }

    default boolean isBaby() { return getGender() == Genders.BABY; }

    void setNestPos(BlockPos pos);
    BlockPos getNestPos();
}
