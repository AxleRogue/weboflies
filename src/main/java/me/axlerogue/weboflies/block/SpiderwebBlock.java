package me.axlerogue.weboflies.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SpiderwebBlock extends WebBlock {
    public SpiderwebBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        // Same slowing effect as vanilla cobweb
        pEntity.makeStuckInBlock(pState, new Vec3(0.25D, 0.05F, 0.25D));
    }
}
