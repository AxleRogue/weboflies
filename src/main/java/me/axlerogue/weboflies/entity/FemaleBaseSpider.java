package me.axlerogue.weboflies.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import me.axlerogue.weboflies.entity.client.ModEntities;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public abstract class FemaleBaseSpider extends BaseSpiderEntity {
    protected final net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(1);

    public FemaleBaseSpider(EntityType<? extends FemaleBaseSpider> type, Level level) {
        super(type, level);
    }

    public net.minecraft.world.SimpleContainer getInventory() {
        return this.inventory;
    }

    public void updateEquipment() {
        if (!this.level().isClientSide) {
            this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, this.inventory.getItem(0));
            this.setDropChance(net.minecraft.world.entity.EquipmentSlot.MAINHAND, 1.0F);
        }
    }

    @Override
    public Genders getGender() {
        return Genders.FEMALE;
    }

    @Override
    public void setNestPos(BlockPos pos) {
        this.nestPos = pos;
    }

    @Override
    public BlockPos getNestPos() {
        return this.nestPos;
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (this.nestPos != null) {
            nbt.putLong("NestPos", this.nestPos.asLong());
        }
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("NestPos")) {
            this.nestPos = BlockPos.of(nbt.getLong("NestPos"));
        }
    }

    public static class SpiderLayEggGoal extends Goal {
        private final FemaleBaseSpider spider;
        private int layTicks;

        public SpiderLayEggGoal(FemaleBaseSpider spider) {
            this.spider = spider;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.spider.getInLove() > 600 && this.spider.getMatingCooldown() <= 0;
        }

        @Override
        public void start() {
            this.layTicks = 0;
        }

        @Override
        public void tick() {
            this.layTicks++;
            if (this.layTicks >= 100) {
                layEgg();
            }
        }

        protected void layEgg() {
            if (this.spider.level() instanceof ServerLevel serverLevel) {
                BlockPos pos = this.spider.blockPosition();
                for (BlockPos p : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 0, 1))) {
                    if (serverLevel.isEmptyBlock(p) && serverLevel.getBlockState(p.below()).isSolid()) {
                        serverLevel.setBlock(p, Blocks.COBWEB.defaultBlockState(), 3);
                    }
                }
                
                int count = 1 + this.spider.getRandom().nextInt(3);
                for (int i = 0; i < count; i++) {
                    SpiderEgg egg = ModEntities.SPIDER_EGG.get().create(serverLevel);
                    if (egg != null) {
                        egg.setParentType(EntityType.getKey(this.spider.getType()).toString());
                        double offsetX = (this.spider.getRandom().nextDouble() - 0.5D) * 0.5D;
                        double offsetZ = (this.spider.getRandom().nextDouble() - 0.5D) * 0.5D;
                        egg.moveTo(this.spider.getX() + offsetX, this.spider.getY(), this.spider.getZ() + offsetZ, 0.0F, 0.0F);
                        serverLevel.addFreshEntity(egg);
                    }
                }
                
                this.spider.setNestPos(pos);
                this.spider.setInLove(0);
                this.spider.setMatingCooldown(12000);
                serverLevel.broadcastEntityEvent(this.spider, (byte) 18);
            }
        }
    }

    public static class SpiderGuardNestGoal extends Goal {
        private final FemaleBaseSpider spider;

        public SpiderGuardNestGoal(FemaleBaseSpider spider) {
            this.spider = spider;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.spider.getNestPos() != null && this.spider.getTarget() == null;
        }

        @Override
        public void tick() {
            BlockPos nest = this.spider.getNestPos();
            if (this.spider.distanceToSqr(nest.getX(), nest.getY(), nest.getZ()) > 25.0D) {
                this.spider.getNavigation().moveTo(nest.getX(), nest.getY(), nest.getZ(), 1.0D);
            } else if (this.spider.getRandom().nextInt(40) == 0) {
                double x = nest.getX() + (this.spider.getRandom().nextDouble() - 0.5D) * 6.0D;
                double y = nest.getY();
                double z = nest.getZ() + (this.spider.getRandom().nextDouble() - 0.5D) * 6.0D;
                this.spider.getNavigation().moveTo(x, y, z, 0.8D);
            }
        }
    }

    public static class SpiderDefendNestGoal extends NearestAttackableTargetGoal<Player> {
        private final FemaleBaseSpider spider;

        public SpiderDefendNestGoal(FemaleBaseSpider spider) {
            super(spider, Player.class, true);
            this.spider = spider;
        }

        @Override
        public boolean canUse() {
            return this.spider.getNestPos() != null && super.canUse();
        }

        @Override
        protected double getFollowDistance() {
            return 8.0D;
        }
    }
}
