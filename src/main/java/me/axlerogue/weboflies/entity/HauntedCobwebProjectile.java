package me.axlerogue.weboflies.entity;

import me.axlerogue.weboflies.block.ModBlocks;
import me.axlerogue.weboflies.entity.client.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class HauntedCobwebProjectile extends ThrowableItemProjectile {
    public HauntedCobwebProjectile(EntityType<? extends HauntedCobwebProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public HauntedCobwebProjectile(Level pLevel, LivingEntity pShooter) {
        super(ModEntities.HAUNTED_COBWEB_PROJECTILE.get(), pShooter, pLevel);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL; // Temporary item representation
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level().isClientSide) {
            pResult.getEntity().hurt(this.level().damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), 4.0F);
            BlockPos pos = pResult.getEntity().blockPosition();
            if (this.level().getBlockState(pos).isAir()) {
                this.level().setBlockAndUpdate(pos, ModBlocks.HAUNTED_COBWEB.get().defaultBlockState());
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (!this.level().isClientSide) {
            BlockPos pos = pResult.getBlockPos().relative(pResult.getDirection());
            if (this.level().getBlockState(pos).isAir()) {
                this.level().setBlockAndUpdate(pos, ModBlocks.HAUNTED_COBWEB.get().defaultBlockState());
            }
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
