package me.axlerogue.weboflies.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import me.axlerogue.weboflies.entity.BlackWidowEntity;
import me.axlerogue.weboflies.entity.SpiderEgg;

public class HauntedCobwebBlock extends WebBlock {
    public HauntedCobwebBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        pEntity.makeStuckInBlock(pState, new Vec3(0.25D, 0.05F, 0.25D));
        if (!pLevel.isClientSide && pEntity instanceof LivingEntity living && !(pEntity instanceof BlackWidowEntity || pEntity instanceof SpiderEgg)) {
             if (pLevel.getGameTime() % 20 == 0) {
                 living.hurt(pLevel.damageSources().magic(), 1.0F);
             }
        }
    }
}
