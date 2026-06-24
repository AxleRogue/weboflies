package me.axlerogue.weboflies.item;

import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.block.ModBlocks;
import me.axlerogue.weboflies.world.HomewardCobwebSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ThreadOfFateItem extends Item {
    private static final Random RANDOM = new Random();
    private static final ResourceKey<Level> DARK_FOREST_KEY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(WebOfLies.MODID, "dark_forest"));

    public ThreadOfFateItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            CompoundTag tag = stack.getOrCreateTag();
            
            // Soul binding check
            if (!tag.contains("OwnerUUID")) {
                tag.putUUID("OwnerUUID", player.getUUID());
                player.sendSystemMessage(Component.literal("The Thread of Fate is now soul-bound to you.").withStyle(ChatFormatting.DARK_PURPLE));
                return InteractionResultHolder.success(stack);
            }

            if (!tag.getUUID("OwnerUUID").equals(player.getUUID())) {
                player.sendSystemMessage(Component.literal("This thread belongs to another soul.").withStyle(ChatFormatting.RED));
                return InteractionResultHolder.fail(stack);
            }

            // New logic: Teleport to a random cobweb in the current dimension
            HomewardCobwebSavedData data = HomewardCobwebSavedData.get(level);
            List<HomewardCobwebSavedData.CobwebEntry> dimensionCobwebs = data.getCobwebs().stream()
                    .filter(entry -> entry.dimension().equals(level.dimension()))
                    .collect(Collectors.toList());

            if (!dimensionCobwebs.isEmpty()) {
                HomewardCobwebSavedData.CobwebEntry randomEntry = dimensionCobwebs.get(RANDOM.nextInt(dimensionCobwebs.size()));
                BlockPos targetPos = randomEntry.pos();
                
                serverPlayer.teleportTo(serverPlayer.serverLevel(), targetPos.getX() + 0.5, targetPos.getY() + 0.1, targetPos.getZ() + 0.5, java.util.Collections.emptySet(), serverPlayer.getYRot(), serverPlayer.getXRot());
                player.sendSystemMessage(Component.literal("The threads of fate pull you to a random web...").withStyle(ChatFormatting.LIGHT_PURPLE));
                return InteractionResultHolder.consume(stack);
            } else {
                player.sendSystemMessage(Component.literal("No Homeward Cobwebs found in this dimension.").withStyle(ChatFormatting.RED));
                return InteractionResultHolder.fail(stack);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide && entity instanceof Player player) {
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.contains("BoundX")) {
                BlockPos pos = new BlockPos(tag.getInt("BoundX"), tag.getInt("BoundY"), tag.getInt("BoundZ"));
                BlockState state = level.getBlockState(player.blockPosition());
                
                // If player is standing in a Homeward Cobweb, link it if not already linked
                if (level.getBlockState(player.blockPosition().below()).is(ModBlocks.HOMEWARD_COBWEB.get()) || state.is(ModBlocks.HOMEWARD_COBWEB.get())) {
                    BlockPos webPos = state.is(ModBlocks.HOMEWARD_COBWEB.get()) ? player.blockPosition() : player.blockPosition().below();
                    if (!tag.contains("BoundX") || tag.getInt("BoundX") != webPos.getX() || tag.getInt("BoundY") != webPos.getY() || tag.getInt("BoundZ") != webPos.getZ()) {
                        // This logic is better handled by right-clicking the block, but user said "link and soul bind"
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("A mystical thread that binds souls across dimensions.").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Right-click a Homeward Cobweb to link.").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.literal("Right-click in the air to teleport to a random linked web.").withStyle(ChatFormatting.BLUE));
        
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            if (tag.contains("OwnerUUID")) {
                tooltip.add(Component.empty());
                tooltip.add(Component.literal("Bound to soul").withStyle(ChatFormatting.DARK_GRAY));
            }
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
