package me.axlerogue.weboflies.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import me.axlerogue.weboflies.entity.client.ModEntities;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.LivingEntity;
import java.util.List;

public class BabyBrownWidowEntity extends MaleBaseSpider {
    private int ageTicks = 0;
    private static final int MAX_AGE = 72000;
    private String adultType = "weboflies:brown_widow";

    public BabyBrownWidowEntity(EntityType<? extends BabyBrownWidowEntity> type, Level level) {
        super(type, level);
    }

    public void setAdultType(String type) {
        this.adultType = type;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return BrownWidowEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.2D, 1.5D));
        this.goalSelector.addGoal(2, new BabyBrownWidowCallForHelpGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            ageTicks++;
            if (ageTicks >= MAX_AGE) {
                growUp();
            }
        }
    }

    private void growUp() {
        if (this.level() instanceof ServerLevel serverLevel) {
            EntityType<?> type = EntityType.byString(this.adultType).orElse(ModEntities.BROWN_WIDOW.get());
            net.minecraft.world.entity.Entity adult = type.create(serverLevel);
            if (adult != null) {
                adult.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                if (this.hasCustomName()) {
                    adult.setCustomName(this.getCustomName());
                }
                if (adult instanceof Mob mob) {
                    mob.setPersistenceRequired();
                }
                serverLevel.addFreshEntity(adult);
                this.discard();
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("AgeTicks", this.ageTicks);
        nbt.putString("AdultType", this.adultType);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.ageTicks = nbt.getInt("AgeTicks");
        if (nbt.contains("AdultType")) {
            this.adultType = nbt.getString("AdultType");
        }
    }

    @Override
    public boolean isBaby() {
        return true;
    }

    private static class BabyBrownWidowCallForHelpGoal extends Goal {
        private final BabyBrownWidowEntity baby;

        public BabyBrownWidowCallForHelpGoal(BabyBrownWidowEntity baby) {
            this.baby = baby;
        }

        @Override
        public boolean canUse() {
            return baby.getLastHurtByMob() != null;
        }

        @Override
        public void start() {
            LivingEntity attacker = baby.getLastHurtByMob();
            if (attacker != null) {
                List<BrownWidowEntity> list = baby.level().getEntitiesOfClass(BrownWidowEntity.class, baby.getBoundingBox().inflate(16.0D));
                for (BrownWidowEntity spider : list) {
                    if ((Mob)spider != (Mob)baby && spider.getTarget() == null) {
                        spider.setTarget(attacker);
                    }
                }
            }
        }
    }
}
