package me.axlerogue.weboflies.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import me.axlerogue.weboflies.item.ModItems;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import java.util.List;

public class BrownWidowEntity extends MaleBaseSpider {
    public BrownWidowEntity(EntityType<? extends BrownWidowEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, 
                (entity) -> this.level().isNight()));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, 10, true, false,
                (entity) -> this.level().isNight()));
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
        List<BaseSpiderEntity> potentialMates = this.level().getEntitiesOfClass(BaseSpiderEntity.class, this.getBoundingBox().inflate(8.0D));
        for (BaseSpiderEntity mate : potentialMates) {
            if (mate != this && this.canMate(mate) && mate.getInLove() > 0 && mate.getMatingCooldown() <= 0) {
                // If the other is female, she becomes pregnant.
                if (mate instanceof FemaleBaseSpider female) {
                    female.setInLove(1200);
                    this.setInLove(0);
                    this.setMatingCooldown(12000);
                    this.level().broadcastEntityEvent(this, (byte) 18);
                    this.level().broadcastEntityEvent(female, (byte) 18);
                    break;
                }
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
}
