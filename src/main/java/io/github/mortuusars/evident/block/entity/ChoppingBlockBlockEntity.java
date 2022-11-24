package io.github.mortuusars.evident.block.entity;

import io.github.mortuusars.evident.setup.ModBlockEntityTypes;
import io.github.mortuusars.evident.setup.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChoppingBlockBlockEntity extends BlockEntity {

    private final ItemStackHandler inventory;
    private final LazyOptional<IItemHandler> inputHandler;
    private boolean isStoringTool;
    private double itemAngle;

    public ChoppingBlockBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.CHOPPING_BLOCK.get(), pos, blockState);
        inventory = createInventoryHandler();
        inputHandler = LazyOptional.of(() -> inventory);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        isStoringTool = compound.getBoolean("IsStoringTool");
        itemAngle = compound.getDouble("ItemAngle");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("Inventory", inventory.serializeNBT());
        compound.putBoolean("IsStoringTool", isStoringTool);
        compound.putDouble("ItemAngle", itemAngle);
    }


    public boolean isStoringTool() {
        return isStoringTool;
    }
    public double getItemAngle() { return itemAngle; }

    public boolean processStoredItem(ItemStack usedStack, Player player) {
        if (level == null || isEmpty())
            return false;

        ItemStack storedItem = getStoredItem();

        if (storedItem.is(ItemTags.LOGS)) {
            Containers.dropItemStack(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1, worldPosition.getZ() + 0.5, new ItemStack(Items.OAK_PLANKS, 5));
            removeItem();
            return true;
        }

        return false;
    }

    public boolean placeItem(ItemStack itemStack, Player player) {
        if (isEmpty() && !itemStack.isEmpty()) {
            isStoringTool = itemStack.is(ModTags.CHOPPING_BLOCK_WEDGEABLE);
            inventory.setStackInSlot(0, itemStack.split(1));
            itemAngle = player.yRotO;
            inventoryChanged();
            return true;
        }
        return false;
    }

    public ItemStack removeItem() {
        if (!isEmpty()) {
            isStoringTool = false;
            ItemStack item = getStoredItem().split(1);
            inventoryChanged();
            return item;
        }
        return ItemStack.EMPTY;
    }



    private ItemStackHandler createInventoryHandler() {
        return new ItemStackHandler() {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }
        };
    }

    protected void inventoryChanged() {
        super.setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) && side != Direction.DOWN) {
            return inputHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        inputHandler.invalidate();
    }

    public ItemStack getStoredItem() {
        return inventory.getStackInSlot(0);
    }

    public boolean isEmpty() {
        return inventory.getStackInSlot(0).isEmpty();
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }
}
