package me.axlerogue.weboflies.entity;

import me.axlerogue.weboflies.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
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
import java.util.List;

public class BlackWidowEntity extends Spider {
    private int inLove;
    private int matingCooldown;

    public BlackWidowEntity(EntityType<? extends Spider> type, Level level) {
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
                this.spawnEgg(mate);
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
        if (itemstack.is(Items.FERMENTED_SPIDER_EYE)) {
            if (!this.level().isClientSide) {
                if (this.matingCooldown <= 0 && this.inLove <= 0) {
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    this.inLove = 600;
                    this.level().broadcastEntityEvent(this, (byte) 18);
                    return InteractionResult.SUCCESS;
                }
            } else {
                return InteractionResult.CONSUME;
            }
        }
        return super.mobInteract(player, hand);
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
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SpiderPlaceWebGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (entity) -> this.level().isNight()));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public boolean canBeRiddenUnderFluidType(net.minecraftforge.fluids.FluidType type, net.minecraft.world.entity.Entity rider) {
        return false;
    }

    @Override
    protected boolean canRide(net.minecraft.world.entity.Entity rider) {
        return false;
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
        return Spider.createAttributes()
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
