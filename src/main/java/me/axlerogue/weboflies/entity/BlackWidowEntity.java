package me.axlerogue.weboflies.entity;

import me.axlerogue.weboflies.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import me.axlerogue.weboflies.entity.client.ModEntities;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import me.axlerogue.weboflies.item.ModItems;
import me.axlerogue.weboflies.block.GooseBerryBushBlock;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.world.level.block.Blocks;

public class BlackWidowEntity extends BaseSpiderEntity {
    public static final int PREGNANT_TIME = 1200;
    private int inLove;
    private int matingCooldown;
    private final SimpleContainer inventory = new SimpleContainer(1);
    private BlockPos nestPos;

    public BlackWidowEntity(EntityType<? extends BlackWidowEntity> type, Level level) {
        super(type, level);
    }

    public void setNestPos(BlockPos pos) {
        this.nestPos = pos;
    }

    public BlockPos getNestPos() {
        return this.nestPos;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.inLove > 0) {
            --this.inLove;
            if (this.inLove % 10 == 0) {
                this.level().addParticle(net.minecraft.core.particles.ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
            }
        } else {
            // If they are holding a berry and NOT in love and cooldown is over, consume it to fall in love
            ItemStack held = this.getInventory().getItem(0);
            if (!held.isEmpty() && (held.is(ModItems.GOOSE_BERRY.get()) || held.is(Items.FERMENTED_SPIDER_EYE)) && this.matingCooldown <= 0) {
                held.shrink(1);
                this.updateEquipment();
                this.setInLove(600);
            }
        }
        if (this.matingCooldown > 0) {
            --this.matingCooldown;
        }

        if (!this.level().isClientSide && this.inLove > 0 && this.matingCooldown <= 0 && this.level().isDay()) {
            findMate();
        }
    }

    private void findMate() {
        List<BlackWidowEntity> potentialMates = this.level().getEntitiesOfClass(BlackWidowEntity.class, this.getBoundingBox().inflate(8.0D));
        for (BlackWidowEntity mate : potentialMates) {
            if (mate != this && mate.inLove > 0 && mate.matingCooldown <= 0) {
                this.setInLove(1200); // 1200 ticks to show they are "pregnant" / looking for a place to lay egg
                mate.setInLove(0);
                mate.matingCooldown = 12000;
                this.level().broadcastEntityEvent(this, (byte) 18);
                this.level().broadcastEntityEvent(mate, (byte) 18);
                break;
            }
        }
    }

    private void spawnEgg(BlackWidowEntity mate) {
        if (this.level() instanceof ServerLevel serverLevel) {
            SpiderEgg egg = ModEntities.SPIDER_EGG.get().create(serverLevel);
            if (egg != null) {
                egg.setParentType(EntityType.getKey(this.getType()).toString());
                egg.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
                serverLevel.addFreshEntity(egg);
                this.inLove = 0;
                mate.inLove = 0;
                this.matingCooldown = 6000;
                mate.matingCooldown = 6000;
                serverLevel.broadcastEntityEvent(this, (byte) 18);
                serverLevel.broadcastEntityEvent(mate, (byte) 18);
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(Items.FERMENTED_SPIDER_EYE) || itemstack.is(ModItems.GOOSE_BERRY.get())) {
            if (!this.level().isClientSide) {
                if (this.matingCooldown <= 0 && this.inLove <= 0) {
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    this.setInLove(600);
                    return InteractionResult.SUCCESS;
                }
            } else {
                return InteractionResult.CONSUME;
            }
        }
        return super.mobInteract(player, hand);
    }

    public void setInLove(int inLove) {
        this.inLove = inLove;
        if (inLove > 0) {
            this.level().broadcastEntityEvent(this, (byte) 18);
        }
    }

    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("InLove", this.inLove);
        nbt.putInt("MatingCooldown", this.matingCooldown);
        nbt.put("Inventory", this.inventory.createTag());
        if (this.nestPos != null) {
            nbt.putLong("NestPos", this.nestPos.asLong());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.inLove = nbt.getInt("InLove");
        this.matingCooldown = nbt.getInt("MatingCooldown");
        this.inventory.fromTag(nbt.getList("Inventory", 10));
        if (nbt.contains("NestPos")) {
            this.nestPos = BlockPos.of(nbt.getLong("NestPos"));
        }
        this.updateEquipment();
    }

    private void updateEquipment() {
        if (!this.level().isClientSide) {
            this.setItemSlot(EquipmentSlot.MAINHAND, this.inventory.getItem(0));
            this.setDropChance(EquipmentSlot.MAINHAND, 1.0F);
        }
    }

    @Override
    protected void dropCustomDeathLoot(net.minecraft.world.damagesource.DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                this.spawnAtLocation(itemstack);
            }
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BlackWidowHarvestBerriesGoal(this));
        this.goalSelector.addGoal(3, new BlackWidowLayEggGoal(this));
        this.goalSelector.addGoal(4, new BlackWidowGuardNestGoal(this));
        this.goalSelector.addGoal(5, new SpiderPlaceWebGoal(this));
        this.goalSelector.addGoal(6, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(7, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new BlackWidowDefendNestGoal(this));
        // Aggressive at night, neutral at day
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, 
                (entity) -> this.level().isNight()));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, 10, true, false,
                (entity) -> this.level().isNight()));
    }

    public static class BlackWidowLayEggGoal extends Goal {
        private final BlackWidowEntity spider;
        private int layTicks;

        public BlackWidowLayEggGoal(BlackWidowEntity spider) {
            this.spider = spider;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.spider.inLove > 600 && this.spider.matingCooldown <= 0;
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

        private void layEgg() {
            if (this.spider.level() instanceof ServerLevel serverLevel) {
                BlockPos pos = this.spider.blockPosition();
                // Create nest: place webs around
                for (BlockPos p : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 0, 1))) {
                    if (serverLevel.isEmptyBlock(p) && serverLevel.getBlockState(p.below()).isSolid()) {
                        serverLevel.setBlock(p, Blocks.COBWEB.defaultBlockState(), 3);
                    }
                }
                
                // Spawn a clutch of eggs (1-3)
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
                this.spider.matingCooldown = 12000; // 10 minutes
                serverLevel.broadcastEntityEvent(this.spider, (byte) 18);
            }
        }
    }

    public static class BlackWidowGuardNestGoal extends Goal {
        private final BlackWidowEntity spider;

        public BlackWidowGuardNestGoal(BlackWidowEntity spider) {
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

    public static class BlackWidowDefendNestGoal extends NearestAttackableTargetGoal<Player> {
        private final BlackWidowEntity spider;

        public BlackWidowDefendNestGoal(BlackWidowEntity spider) {
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
        
        @Override
        public void start() {
            super.start();
            // Alert other spiders nearby? Maybe later.
        }
    }

    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity entity) {
        if (super.doHurtTarget(entity)) {
            if (entity instanceof LivingEntity living) {
                living.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.POISON, 100, 0), this);
            }
            return true;
        }
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (target instanceof BlackWidowEntity || target instanceof SpiderEgg) {
            return false;
        }
        return super.canAttack(target);
    }

    @Override
    public boolean isAlliedTo(net.minecraft.world.entity.Entity entity) {
        if (entity instanceof BlackWidowEntity || entity instanceof SpiderEgg) {
            return true;
        }
        return super.isAlliedTo(entity);
    }
    public static class BlackWidowHarvestBerriesGoal extends MoveToBlockGoal {
        private final BlackWidowEntity spider;

        public BlackWidowHarvestBerriesGoal(BlackWidowEntity spider) {
            super(spider, 1.0D, 8);
            this.spider = spider;
        }

        @Override
        public boolean canUse() {
            return !this.spider.level().isNight() && this.spider.getInventory().getItem(0).isEmpty() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.spider.getInventory().getItem(0).isEmpty() && super.canContinueToUse();
        }

        @Override
        protected boolean isValidTarget(LevelReader level, BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            return state.is(ModBlocks.GOOSE_BERRY_BUSH.get()) && state.getValue(GooseBerryBushBlock.AGE) >= 2;
        }

        @Override
        public void tick() {
            super.tick();
            if (this.isReachedTarget()) {
                BlockState state = this.spider.level().getBlockState(this.blockPos);
                if (state.is(ModBlocks.GOOSE_BERRY_BUSH.get()) && state.getValue(GooseBerryBushBlock.AGE) >= 2) {
                    harvest(state);
                }
            }
        }

        private void harvest(BlockState state) {
            int age = state.getValue(GooseBerryBushBlock.AGE);
            int count = 1 + this.spider.getRandom().nextInt(2) + (age == 3 ? 1 : 0);
            ItemStack berries = new ItemStack(ModItems.GOOSE_BERRY.get(), count);
            this.spider.getInventory().setItem(0, berries);
            this.spider.updateEquipment();
            
            BlockState newState = state.setValue(GooseBerryBushBlock.AGE, 1);
            this.spider.level().setBlock(this.blockPos, newState, 2);
            this.spider.level().playSound(null, this.blockPos, net.minecraft.sounds.SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 0.8F + this.spider.getRandom().nextFloat() * 0.4F);
            
            if (this.spider.matingCooldown <= 0 && this.spider.inLove <= 0) {
                this.spider.setInLove(600);
            }
        }
    }

    public static class SpiderPlaceWebGoal extends Goal {
        private final BlackWidowEntity spider;

        public SpiderPlaceWebGoal(BlackWidowEntity spider) {
            this.spider = spider;
        }

        @Override
        public boolean canUse() {
            return this.spider.getRandom().nextInt(1000) == 0 && this.spider.level().getBlockState(this.spider.blockPosition()).isAir();
        }

        @Override
        public void start() {
            BlockPos pos = this.spider.blockPosition();
            if (this.spider.level().getBlockState(pos).isAir()) {
                this.spider.level().setBlockAndUpdate(pos, ModBlocks.SPIDERWEB.get().defaultBlockState());
            }
        }
    }

}
