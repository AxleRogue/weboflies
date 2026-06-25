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

public class BlackWidowEntity extends FemaleBaseSpider {
    public static final int PREGNANT_TIME = 1200;

    public BlackWidowEntity(EntityType<? extends BlackWidowEntity> type, Level level) {
        super(type, level);
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
        List<BaseSpiderEntity> potentialMates = this.level().getEntitiesOfClass(BaseSpiderEntity.class, this.getBoundingBox().inflate(8.0D));
        for (BaseSpiderEntity mate : potentialMates) {
            if (mate != this && this.canMate(mate) && mate.getInLove() > 0 && mate.getMatingCooldown() <= 0) {
                this.setInLove(1200); // 1200 ticks to show they are "pregnant" / looking for a place to lay egg
                mate.setInLove(0);
                mate.setMatingCooldown(12000);
                this.level().broadcastEntityEvent(this, (byte) 18);
                this.level().broadcastEntityEvent(mate, (byte) 18);
                break;
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

    @Override
    public void setInLove(int inLove) {
        super.setInLove(inLove);
    }

    public net.minecraft.world.SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.put("Inventory", this.inventory.createTag());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.inventory.fromTag(nbt.getList("Inventory", 10));
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
        this.goalSelector.addGoal(3, new FemaleBaseSpider.SpiderLayEggGoal(this));
        this.goalSelector.addGoal(4, new FemaleBaseSpider.SpiderGuardNestGoal(this));
        this.goalSelector.addGoal(5, new SpiderPlaceWebGoal(this));
        this.goalSelector.addGoal(6, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(7, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new FemaleBaseSpider.SpiderDefendNestGoal(this));
        // Aggressive at night, neutral at day
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, 
                (entity) -> this.level().isNight()));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, 10, true, false,
                (entity) -> this.level().isNight()));
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
        private final FemaleBaseSpider spider;

        public BlackWidowHarvestBerriesGoal(FemaleBaseSpider spider) {
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
            
            if (this.spider.getMatingCooldown() <= 0 && this.spider.getInLove() <= 0) {
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
