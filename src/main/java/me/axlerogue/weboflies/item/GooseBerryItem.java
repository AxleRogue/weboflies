package me.axlerogue.weboflies.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class GooseBerryItem extends ItemNameBlockItem {
    public GooseBerryItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide && pLivingEntity instanceof Player player) {
            if (!player.getAbilities().instabuild && player.getFoodData().needsFood()) {
                // Vanilla behavior already calls player.eat(this, pStack) inside finishUsingItem if it has FoodProperties
                // The requirement is "give saturation to players only to fill hunger points when players are hungry only"
                // This is actually how vanilla food works. Saturation is always given when eating.
                // We'll keep the vanilla behavior as it matches the requirement perfectly.
            }
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }
}
