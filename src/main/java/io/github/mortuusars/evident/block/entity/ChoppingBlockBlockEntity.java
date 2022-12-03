package io.github.mortuusars.evident.block.entity;

import io.github.mortuusars.evident.block.ChoppingBlockBlock;
import io.github.mortuusars.evident.recipe.ChoppingBlockRecipe;
import io.github.mortuusars.evident.setup.ForgeTags;
import io.github.mortuusars.evident.setup.ModBlockEntityTypes;
import io.github.mortuusars.evident.setup.ModRecipeTypes;
import io.github.mortuusars.evident.setup.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.IllegalFormatWidthException;
import java.util.List;
import java.util.Optional;

public class ChoppingBlockBlockEntity extends BlockEntity {

    private final ItemStackHandler inventory;
    private final LazyOptional<IItemHandler> inputHandler;
    private boolean isStoringTool;
    private double itemAngle;

    private ResourceLocation lastRecipeID;

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

        Optional<ChoppingBlockRecipe> matchingRecipe = getMatchingRecipe(new RecipeWrapper(inventory), usedStack, player);

        matchingRecipe.ifPresent(recipe -> {
            List<ItemStack> results = recipe.rollResults(level.random);
            for (ItemStack resultStack : results) {
                Containers.dropItemStack(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1, worldPosition.getZ() + 0.5, resultStack.copy());
            }
            if (player != null) {
                usedStack.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            } else {
                if (usedStack.hurt(1, level.random, null)) {
                    usedStack.setCount(0);
                }
            }
            level.playSound(null, getBlockPos(), SoundEvents.TRIDENT_HIT, SoundSource.BLOCKS, 1F, level.random.nextFloat() * 0.2F + 0.5F);
            removeItem();
//            if (player instanceof ServerPlayer) {
//                ModAdvancements.CUTTING_BOARD.trigger((ServerPlayer) player);
//            }
        });

        return matchingRecipe.isPresent();
    }

    private Optional<ChoppingBlockRecipe> getMatchingRecipe(RecipeWrapper recipeWrapper, ItemStack usedStack, Player player) {
        if (level == null) return Optional.empty();

//        if (lastRecipeID != null) {
//            level.getRecipeManager()
//                    .
//            Recipe<RecipeWrapper> recipe = ((RecipeManagerAccessor) level.getRecipeManager())
//                    .getRecipeMap(ModRecipeTypes.CUTTING.get())
//                    .get(lastRecipeID);
//            if (recipe instanceof CuttingBoardRecipe && recipe.matches(recipeWrapper, level) && ((CuttingBoardRecipe) recipe).getTool().test(toolStack)) {
//                return Optional.of((CuttingBoardRecipe) recipe);
//            }
//        }

        ITag<Item> tag = ForgeRegistries.ITEMS.tags().getTag(ForgeTags.TOOLS_AXES);

        List<ChoppingBlockRecipe> recipeList = level.getRecipeManager().getRecipesFor(ModRecipeTypes.CHOPPING.get(), recipeWrapper, level);
        if (recipeList.isEmpty()) {
            if (player != null)
                player.displayClientMessage(new TranslatableComponent("block.chopping_block.invalid_item"), true);
            return Optional.empty();
        }
        Optional<ChoppingBlockRecipe> recipe = recipeList.stream().filter(cuttingRecipe -> cuttingRecipe.getTool().test(usedStack)).findFirst();
        if (!recipe.isPresent()) {
            if (player != null)
                player.displayClientMessage(new TranslatableComponent("block.cutting_board.invalid_tool"), true);
            return Optional.empty();
        }
        lastRecipeID = recipe.get().getId();
        return recipe;
    }

    public boolean placeItem(ItemStack itemStack, Player player) {
        if (isEmpty() && !itemStack.isEmpty()) {
            isStoringTool = itemStack.is(ModTags.CHOPPING_BLOCK_WEDGEABLE);

            if (isStoringTool)
                level.playSound(null, getBlockPos(), SoundEvents.TRIDENT_HIT_GROUND, SoundSource.BLOCKS, 0.7F, level.random.nextFloat() * 0.1F + 0.8F);
            else
                level.playSound(null, getBlockPos(), SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 0.8F, level.random.nextFloat() * 0.2F + 0.9F);

            inventory.setStackInSlot(0, itemStack.split(1));
            itemAngle = player.yRotO + level.random.nextFloat() * 0.2F - 0.1F;
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
