package me.axlerogue.weboflies.block;

import me.axlerogue.weboflies.block.entity.HomewardCobwebBlockEntity;
import me.axlerogue.weboflies.item.ModItems;
import me.axlerogue.weboflies.client.ClientPacketHandler;
import me.axlerogue.weboflies.client.HomewardCobwebScreen;
import me.axlerogue.weboflies.network.ModMessages;
import me.axlerogue.weboflies.world.HomewardCobwebSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class HomewardCobwebBlock extends Block implements EntityBlock {
    public HomewardCobwebBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HomewardCobwebBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && placer instanceof Player) {
            String name = "Web at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
            if (level.getBlockEntity(pos) instanceof HomewardCobwebBlockEntity webEntity) {
                webEntity.setCobwebName(name);
            }
            HomewardCobwebSavedData.get(level).addCobweb(name, pos, level.dimension());
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide) {
                HomewardCobwebSavedData.get(level).removeCobweb(pos, level.dimension());
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(ModItems.THREAD_OF_FATE.get())) {
            if (!level.isClientSide) {
                CompoundTag tag = stack.getOrCreateTag();
                
                // Soul binding check
                if (!tag.contains("OwnerUUID")) {
                    tag.putUUID("OwnerUUID", player.getUUID());
                    player.sendSystemMessage(Component.literal("The Thread of Fate is now soul-bound to you.").withStyle(ChatFormatting.DARK_PURPLE));
                }

                if (!tag.getUUID("OwnerUUID").equals(player.getUUID())) {
                    player.sendSystemMessage(Component.literal("This thread belongs to another soul.").withStyle(ChatFormatting.RED));
                    return InteractionResult.FAIL;
                }

                tag.putInt("BoundX", pos.getX());
                tag.putInt("BoundY", pos.getY());
                tag.putInt("BoundZ", pos.getZ());
                tag.putString("BoundDim", level.dimension().location().toString());

                player.sendSystemMessage(Component.literal("The thread is now linked to this Homeward Cobweb.").withStyle(ChatFormatting.GREEN));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (hand == InteractionHand.MAIN_HAND) {
            if (level.isClientSide) {
                Minecraft.getInstance().setScreen(new HomewardCobwebScreen(ClientPacketHandler.getLastCobwebList()));
            } else if (player instanceof ServerPlayer serverPlayer) {
                ModMessages.sendToPlayer(new ModMessages.ClientboundCobwebListPacket(HomewardCobwebSavedData.get(level).getCobwebs()), serverPlayer);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, hit);
    }
}
