package me.axlerogue.weboflies.entity.client;

import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Spider;

public class BaseSpiderModel<T extends Spider> extends SpiderModel<T> {
    public BaseSpiderModel(ModelPart root) {
        super(root);
    }
}
