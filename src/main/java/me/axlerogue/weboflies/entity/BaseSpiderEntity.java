package me.axlerogue.weboflies.entity;

import me.axlerogue.weboflies.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.DifficultyInstance;

public abstract class BaseSpiderEntity extends Animal implements Enemy, ISpider {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(BaseSpiderEntity.class, EntityDataSerializers.BYTE);
    protected int inLove;
    protected int matingCooldown;
    protected BlockPos nestPos;

    public BaseSpiderEntity(EntityType<? extends BaseSpiderEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void setNestPos(BlockPos pos) {
        this.nestPos = pos;
    }

    @Override
    public BlockPos getNestPos() {
        return this.nestPos;
    }

    public int getInLove() {
        return this.inLove;
    }

    public void setInLove(int inLove) {
        this.inLove = inLove;
        if (inLove > 0) {
            this.level().broadcastEntityEvent(this, (byte) 18);
        }
    }

    public int getMatingCooldown() {
        return this.matingCooldown;
    }

    public void setMatingCooldown(int matingCooldown) {
        this.matingCooldown = matingCooldown;
    }

    public abstract Genders getGender();

    @Override
    public boolean isBaby() {
        return super.isBaby();
    }

    @Override
    public boolean canMate(Animal pOtherAnimal) {
        if (pOtherAnimal == this) return false;
        if (!(pOtherAnimal instanceof ISpider other)) return false;
        if (this.isFemale() && other.isMale()) return true;
        if (this.isMale() && other.isFemale()) return true;
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new WallClimberNavigation(this, pLevel);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean pClimbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (pClimbing) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    @Override
    public void makeStuckInBlock(BlockState pState, net.minecraft.world.phys.Vec3 pStickiness) {
        if (!pState.is(net.minecraft.world.level.block.Blocks.COBWEB)) {
            super.makeStuckInBlock(pState, pStickiness);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("InLove", this.inLove);
        nbt.putInt("MatingCooldown", this.matingCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.inLove = nbt.getInt("InLove");
        this.matingCooldown = nbt.getInt("MatingCooldown");
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag nbt) {
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData, nbt);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null; // We handle breeding differently via SpiderEgg
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(ModItems.GOOSE_BERRY.get()) || pStack.is(net.minecraft.world.item.Items.FERMENTED_SPIDER_EYE);
    }
    
    // Day behavior is aggressive by default in Spider, but we can customize AI here
    
    @Override
    public boolean canBeRiddenUnderFluidType(net.minecraftforge.fluids.FluidType type, net.minecraft.world.entity.Entity rider) {
        return false;
    }

    @Override
    protected boolean canRide(net.minecraft.world.entity.Entity rider) {
        return false;
    }
}
