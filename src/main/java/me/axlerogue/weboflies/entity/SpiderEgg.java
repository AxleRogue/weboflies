package me.axlerogue.weboflies.entity;

import me.axlerogue.weboflies.entity.client.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpiderEgg extends LivingEntity {
    private int hatchTicks = 0;
    private static final int HATCH_TIME = 12000; // Half a Minecraft day
    private String parentType = "weboflies:black_widow";

    public SpiderEgg(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
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
            if (hatchTicks >= HATCH_TIME) {
                hatch();
            }
        }
    }

    private void hatch() {
        if (this.level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 4; i++) {
                BabyBlackWidowEntity baby = ModEntities.BABY_BLACK_WIDOW.get().create(serverLevel);
                if (baby != null) {
                    baby.setAdultType(this.parentType);
                    double offsetX = (this.random.nextDouble() - 0.5D) * 0.5D;
                    double offsetZ = (this.random.nextDouble() - 0.5D) * 0.5D;
                    baby.moveTo(this.getX() + offsetX, this.getY(), this.getZ() + offsetZ, this.getYRot(), this.getXRot());
                    serverLevel.addFreshEntity(baby);
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
