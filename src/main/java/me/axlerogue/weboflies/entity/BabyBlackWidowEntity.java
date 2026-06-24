package me.axlerogue.weboflies.entity;

import me.axlerogue.weboflies.entity.client.ModEntities;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public class BabyBlackWidowEntity extends BlackWidowEntity {
    private int ageTicks = 0;
    private static final int MAX_AGE = 72000; // 3 Minecraft days (24000 * 3)
    private String adultType = "weboflies:black_widow";

    public BabyBlackWidowEntity(EntityType<? extends BabyBlackWidowEntity> type, Level level) {
        super(type, level);
    }

    public void setAdultType(String type) {
        this.adultType = type;
    }

    public String getAdultType() {
        return this.adultType;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return BlackWidowEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.2D, 1.5D));
        this.goalSelector.addGoal(2, new BlackWidowHarvestBerriesGoal(this));
        this.goalSelector.addGoal(3, new BabyBlackWidowCallForHelpGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
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
            EntityType<?> type = EntityType.byString(this.adultType).orElse(ModEntities.BLACK_WIDOW.get());
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

    private static class BabyBlackWidowCallForHelpGoal extends Goal {
        private final BabyBlackWidowEntity baby;

        public BabyBlackWidowCallForHelpGoal(BabyBlackWidowEntity baby) {
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
                List<BlackWidowEntity> list = baby.level().getEntitiesOfClass(BlackWidowEntity.class, baby.getBoundingBox().inflate(16.0D));
                for (BlackWidowEntity spider : list) {
                    if (spider != baby && spider.getTarget() == null) {
                        spider.setTarget(attacker);
                    }
                }
            }
        }
    }
}
