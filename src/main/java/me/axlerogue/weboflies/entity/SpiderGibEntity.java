package me.axlerogue.weboflies.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class SpiderGibEntity extends Entity {
    private String partType = "head";
    private float entityScale = 1.0f;
    private int lifeTime = 0;

    public SpiderGibEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public void setPartType(String type) {
        this.partType = type;
    }

    public String getPartType() {
        return this.partType;
    }

    public void setEntityScale(float scale) {
        this.entityScale = scale;
    }

    public float getEntityScale() {
        return this.entityScale;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();
        this.lifeTime++;
        if (this.lifeTime > 200) { // 10 seconds
            this.discard();
        }
        
        // Simple gravity and movement
        if (!this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.04D, 0));
        }
        this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("PartType")) {
            this.partType = nbt.getString("PartType");
        }
        if (nbt.contains("EntityScale")) {
            this.entityScale = nbt.getFloat("EntityScale");
        }
        this.lifeTime = nbt.getInt("LifeTime");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putString("PartType", this.partType);
        nbt.putFloat("EntityScale", this.entityScale);
        nbt.putInt("LifeTime", this.lifeTime);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
