package me.axlerogue.weboflies.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.core.BlockPos;
import me.axlerogue.weboflies.entity.client.ModEntities;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;

public class BlackWidowBroodMotherEntity extends BlackWidowEntity {
    private final ServerBossEvent bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

    public BlackWidowBroodMotherEntity(EntityType<? extends BlackWidowBroodMotherEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BroodMotherPounceGoal(this));
        this.goalSelector.addGoal(3, new BroodMotherShootGoal(this));
        this.goalSelector.addGoal(3, new BroodMotherSummonGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        // Brood Mother is always aggressive
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    static class BroodMotherShootGoal extends Goal {
        private final BlackWidowBroodMotherEntity mob;
        private int cooldown;

        public BroodMotherShootGoal(BlackWidowBroodMotherEntity mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.mob.getTarget();
            if (target != null && cooldown > 0) cooldown--;
            return target != null && cooldown <= 0 && this.mob.distanceToSqr(target) > 64.0D;
        }

        @Override
        public void start() {
            this.cooldown = 60;
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                HauntedCobwebProjectile projectile = new HauntedCobwebProjectile(this.mob.level(), this.mob);
                double d0 = target.getX() - this.mob.getX();
                double d1 = target.getY(0.3333333333333333D) - projectile.getY();
                double d2 = target.getZ() - this.mob.getZ();
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                projectile.shoot(d0, d1 + d3 * 0.2D, d2, 1.6F, 1.0F);
                this.mob.level().addFreshEntity(projectile);
            }
        }
    }

    static class BroodMotherSummonGoal extends Goal {
        private final BlackWidowBroodMotherEntity mob;
        private int cooldown;

        public BroodMotherSummonGoal(BlackWidowBroodMotherEntity mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.mob.getTarget();
            if (target != null && cooldown > 0) cooldown--;
            return target != null && cooldown <= 0 && this.mob.getHealth() < this.mob.getMaxHealth() * 0.75F;
        }

        @Override
        public void start() {
            this.cooldown = 400; // 20 seconds
            for (int i = 0; i < 3; i++) {
                BlockPos pos = this.mob.blockPosition().offset(-2 + this.mob.getRandom().nextInt(5), 0, -2 + this.mob.getRandom().nextInt(5));
                SpiderEgg egg = ModEntities.SPIDER_EGG.get().create(this.mob.level());
                if (egg != null) {
                    egg.setParentType(EntityType.getKey(this.mob.getType()).toString());
                    egg.moveTo(pos, 0.0F, 0.0F);
                    this.mob.level().addFreshEntity(egg);
                    
                    // Spawn regular Black Widows instead of mini-bosses
                    BlackWidowEntity minion = ModEntities.BLACK_WIDOW.get().create(this.mob.level());
                    if (minion != null) {
                        minion.moveTo(pos, 0.0F, 0.0F);
                        this.mob.level().addFreshEntity(minion);
                        egg.discard(); // Hatch instantly
                    }
                }
            }
        }
    }

    static class BroodMotherPounceGoal extends Goal {
        private final BlackWidowBroodMotherEntity mob;
        private LivingEntity target;
        private int cooldown;

        public BroodMotherPounceGoal(BlackWidowBroodMotherEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            this.target = this.mob.getTarget();
            if (this.target == null || cooldown > 0) {
                if (cooldown > 0) cooldown--;
                return false;
            }
            double d0 = this.mob.distanceToSqr(this.target);
            return d0 > 16.0D && d0 < 64.0D && this.mob.onGround();
        }

        @Override
        public void start() {
            Vec3 vec3 = this.mob.getDeltaMovement();
            Vec3 vec31 = new Vec3(this.target.getX() - this.mob.getX(), 0.0D, this.target.getZ() - this.mob.getZ());
            if (vec31.lengthSqr() > 1.0E-7D) {
                vec31 = vec31.normalize().scale(0.8D).add(vec3.scale(0.2D));
            }

            this.mob.setDeltaMovement(vec31.x, 0.5D, vec31.z);
            this.cooldown = 100; // 5 seconds cooldown
        }

        @Override
        public void tick() {
            if (this.target != null && this.mob.getBoundingBox().inflate(1.0D).intersects(this.target.getBoundingBox())) {
                this.mob.doHurtTarget(this.target);
                this.target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
            }
        }
    }

    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity entity) {
        if (super.doHurtTarget(entity)) {
            if (entity instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 1), this);
            }
            return true;
        }
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return BlackWidowEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void setCustomName(Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
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

    public static boolean checkSpiderSpawnRules(EntityType<? extends Animal> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        // Daytime-only spawning in our biome/dimension
        if (!level.getLevel().isDay()) {
            return false;
        }
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) || level.getBlockState(pos.below()).is(BlockTags.LEAVES);
    }

    public static boolean checkBroodMotherSpawnRules(EntityType<BlackWidowBroodMotherEntity> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (!checkSpiderSpawnRules(entityType, level, spawnType, pos, random)) {
            return false;
        }
        // Only one at a time in the dimension
        int broodMotherCount = 0;
        for (net.minecraft.world.entity.Entity entity : level.getLevel().getEntities().getAll()) {
            if (entity instanceof BlackWidowBroodMotherEntity && entity.isAlive()) {
                broodMotherCount++;
            }
        }
        if (broodMotherCount > 0) return false;

        return Animal.checkAnimalSpawnRules(entityType, level, spawnType, pos, random);
    }
}
