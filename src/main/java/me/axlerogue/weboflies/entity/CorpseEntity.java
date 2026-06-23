package me.axlerogue.weboflies.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class CorpseEntity extends Entity {
    private String modelType = "black_widow";
    private float entityScale = 1.0f;

    public CorpseEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public void setModelType(String type) {
        this.modelType = type;
    }

    public String getModelType() {
        return this.modelType;
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
    protected void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("ModelType")) {
            this.modelType = nbt.getString("ModelType");
        }
        if (nbt.contains("EntityScale")) {
            this.entityScale = nbt.getFloat("EntityScale");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putString("ModelType", this.modelType);
        nbt.putFloat("EntityScale", this.entityScale);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
