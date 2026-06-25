package me.axlerogue.weboflies.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.tags.BlockTags;
import me.axlerogue.weboflies.entity.client.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpiderEgg extends LivingEntity implements ISpider {
    private int hatchTicks = 0;
    private static final int HATCH_TIME = 24000; // 1 whole Minecraft day
    private String parentType = "weboflies:black_widow";

    @Override
    public Genders getGender() {
        return Genders.FEMALE;
    }

    @Override
    public boolean isBaby() {
        return true;
    }

    @Override
    public void setNestPos(BlockPos pos) {}

    @Override
    public BlockPos getNestPos() { return null; }

    public SpiderEgg(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    public static boolean checkSpiderEggSpawnRules(EntityType<? extends LivingEntity> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) || level.getBlockState(pos.below()).is(BlockTags.LEAVES);
    }

    public void setParentType(String type) {
        this.parentType = type;
    }

    public String getParentType() {
        return this.parentType;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            hatchTicks++;
            // Hatch if it has been at least HATCH_TIME and it's morning (between 0 and 1000)
            if (hatchTicks >= HATCH_TIME && this.level().getDayTime() % 24000 < 1000) {
                hatch();
            }
        }
    }

    private void hatch() {
        if (this.level() instanceof ServerLevel serverLevel) {
            int count = 4;
            boolean isBroodMother = this.parentType.equals("weboflies:black_widow_brood_mother");

            for (int i = 0; i < count; i++) {
                Mob offspring;
                if (isBroodMother) {
                    // Brood mother eggs spawn adults
                    if (this.random.nextBoolean()) {
                        offspring = ModEntities.BLACK_WIDOW.get().create(serverLevel);
                    } else {
                        offspring = ModEntities.BROWN_WIDOW.get().create(serverLevel);
                    }
                } else {
                    // Regular black widow eggs spawn babies
                    if (this.random.nextBoolean()) {
                        offspring = ModEntities.BABY_BLACK_WIDOW.get().create(serverLevel);
                        if (offspring instanceof BabyBlackWidowEntity baby) {
                            baby.setAdultType("weboflies:black_widow");
                        }
                    } else {
                        offspring = ModEntities.BABY_BROWN_WIDOW.get().create(serverLevel);
                        if (offspring instanceof BabyBrownWidowEntity baby) {
                            baby.setAdultType("weboflies:brown_widow");
                        }
                    }
                }

                if (offspring != null) {
                    double offsetX = (this.random.nextDouble() - 0.5D) * 0.5D;
                    double offsetZ = (this.random.nextDouble() - 0.5D) * 0.5D;
                    offspring.moveTo(this.getX() + offsetX, this.getY(), this.getZ() + offsetZ, this.getYRot(), this.getXRot());
                    serverLevel.addFreshEntity(offspring);
                }
            }
            this.discard();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("HatchTicks", this.hatchTicks);
        nbt.putString("ParentType", this.parentType);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.hatchTicks = nbt.getInt("HatchTicks");
        if (nbt.contains("ParentType")) {
            this.parentType = nbt.getString("ParentType");
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return java.util.Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }


    @Override
    public boolean isAlliedTo(net.minecraft.world.entity.Entity entity) {
        if (entity instanceof BlackWidowEntity || entity instanceof SpiderEgg) {
            return true;
        }
        return super.isAlliedTo(entity);
    }
}
