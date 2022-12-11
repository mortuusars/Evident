package io.github.mortuusars.evident.content.chopping_block;

import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import io.github.mortuusars.evident.setup.ModAdvancements;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ChoppingBlockBlockEntity extends BlockEntity {

    public static final float BLOCK_HEIGHT = 0.875F; // 14/16

    private final ItemStackHandler inventory;
    private final LazyOptional<IItemHandler> inputHandler;
    private boolean isStoringTool;
    private float itemAngle;

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
        itemAngle = compound.getFloat("ItemAngle");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("Inventory", inventory.serializeNBT());
        compound.putBoolean("IsStoringTool", isStoringTool);
        compound.putFloat("ItemAngle", itemAngle);
    }

    public boolean isStoringTool() {
        return isStoringTool;
    }

    public float getItemAngle() {
        return itemAngle;
    }

    public boolean processStoredItemWith(ItemStack usedStack, Player player) {
        if (level == null || isEmpty())
            return false;

        Optional<ChoppingBlockRecipe> matchingRecipe = getMatchingRecipe(new RecipeWrapper(inventory), usedStack, player);

        matchingRecipe.ifPresent(recipe -> {
            List<ItemStack> results = recipe.rollResults(level.random);

            Random random = level.getRandom();

            double relativeAngleRadians = getAngleRadiansRelativeToBlockCenter(this.getBlockPos(), player.position());
            double leftAngleRadians = relativeAngleRadians + Math.PI / 2;
            double rightAngleRadians = relativeAngleRadians - Math.PI / 2;

            Vector3d leftVector = new Vector3d((Math.cos(leftAngleRadians)), 0F, Math.sin(leftAngleRadians));
            Vector3d rightVector = new Vector3d((Math.cos(rightAngleRadians)), 0F, Math.sin(rightAngleRadians));

            boolean isLeftSide = false;

            for (ItemStack resultStack : results) {
                ItemStack stack = resultStack.copy();

                // Drop stacks on both sides from the player looking direction
                while (!stack.isEmpty()) {
                    ItemStack stackPiece = stack.split(1);

                    Vector3d currentSideVector = isLeftSide ? leftVector : rightVector;

                    double xVelocity = currentSideVector.x * 0.08 + (random.nextDouble() * 0.01);
                    double yVelocity = 0.25D + random.nextGaussian() * 0.01D;
                    double zVelocity = currentSideVector.z * 0.08 + (random.nextDouble() * 0.01);

                    double x = random.nextGaussian() * 0.05 + worldPosition.getX() + 0.5;
                    double y = random.nextDouble() * 0.1 + worldPosition.getY() + 0.8 + EntityType.ITEM.getHeight() / 2;
                    double z = random.nextGaussian() * 0.05 + worldPosition.getZ() + 0.5;

                    dropItemInWorld(stackPiece, new Vector3f((float) x, (float) y, (float) z),
                            new Vector3d(xVelocity, yVelocity, zVelocity), 12);

                    isLeftSide = !isLeftSide; // Switch side
                }
            }

            if (player != null) {
                usedStack.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            } else {
                if (usedStack.hurt(1, level.random, null)) {
                    usedStack.setCount(0);
                }
            }

            level.playSound(null, getBlockPos(), SoundEvents.TRIDENT_HIT, SoundSource.BLOCKS, 1F, level.random.nextFloat() * 0.2F + 0.5F);
            removeStoredItem();
            if (player instanceof ServerPlayer serverPlayer)
                ModAdvancements.CHOPPING_BLOCK.trigger(serverPlayer);
        });

        return matchingRecipe.isPresent();
    }


    /**
     * Calculates angle between two positions. Only X and Z, height is ignored.
     */
    private double getAngleRadiansRelativeToBlockCenter(BlockPos originPos, Vec3 relativePos) {
        double deltaX = originPos.getX() + 0.5 - relativePos.x;
        double deltaZ = originPos.getZ() + 0.5 - relativePos.z;
        return Math.atan2(deltaZ, deltaX); 
    }

    private Optional<ChoppingBlockRecipe> getMatchingRecipe(RecipeWrapper recipeWrapper, ItemStack usedStack, Player player) {
        if (level == null) return Optional.empty();

        if (lastRecipeID != null) {
            Optional<ChoppingBlockRecipe> lastRecipe = (Optional<ChoppingBlockRecipe>) level.getRecipeManager()
                    .byKey(lastRecipeID);

            if (lastRecipe.isPresent()) {
                ChoppingBlockRecipe lastChoppingBlockRecipe = lastRecipe.get();
                if (lastChoppingBlockRecipe.matches(recipeWrapper, level) && lastChoppingBlockRecipe.getTool().test(usedStack))
                    return lastRecipe;
            }
        }

        List<ChoppingBlockRecipe> recipeList = level.getRecipeManager().getRecipesFor(ModRecipeTypes.CHOPPING.get(), recipeWrapper, level);
        if (recipeList.isEmpty()) {
            if (player != null)
                player.displayClientMessage(new TranslatableComponent("block.evident.chopping_block.invalid_item"), true);
            return Optional.empty();
        }

        Optional<ChoppingBlockRecipe> recipe = recipeList.stream().filter(cuttingRecipe -> cuttingRecipe.getTool().test(usedStack)).findFirst();
        if (!recipe.isPresent()) {
            if (player != null)
                player.displayClientMessage(new TranslatableComponent("block.evident.cutting_board.invalid_tool"), true);
            return Optional.empty();
        }

        lastRecipeID = recipe.get().getId();

        return recipe;
    }

    /**
     * Places item or tool on the ChoppingBlock. Only 1 item will be placed (not entire stack).<br><br>
     * Item is not consumed if player is in creative.<br>
     * Plays placing sound.
     */
    public boolean placeItem(ItemStack itemStack, Player player) {
        if (!isEmpty() || itemStack.isEmpty())
            return false;

        isStoringTool = itemStack.is(ModTags.CHOPPING_BLOCK_WEDGEABLE);

        if (isStoringTool)
            level.playSound(null, getBlockPos(), SoundEvents.TRIDENT_HIT_GROUND, SoundSource.BLOCKS, 0.7F, level.random.nextFloat() * 0.1F + 0.8F);
        else
            level.playSound(null, getBlockPos(), SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 0.8F, level.random.nextFloat() * 0.2F + 0.9F);

        inventory.setStackInSlot(0, player.isCreative() ? itemStack.copy().split(1) : itemStack.split(1));
        double angleRadians = getAngleRadiansRelativeToBlockCenter(this.worldPosition, player.position()) - 0.1 - level.getRandom().nextDouble() * 0.2 + Math.PI / 2;
        itemAngle = (float) (angleRadians * (180 / Math.PI));
        inventoryChanged();
        return true;
    }

    public ItemStack removeStoredItem() {
        if (isEmpty())
            return ItemStack.EMPTY;

        isStoringTool = false;
        ItemStack item = getStoredItem().split(1);
        inventoryChanged();
        return item;
    }

    public ItemEntity dropStoredItem() {
        ItemStack storedStack = this.removeStoredItem();
        if (storedStack.isEmpty())
            return null;

        Random random = level.getRandom();
        Vector3f pos = new Vector3f(worldPosition.getX() + 0.5f, worldPosition.getY() + BLOCK_HEIGHT, worldPosition.getZ() + 0.5f);
        Vector3d velocity = new Vector3d(random.nextGaussian() * 0.03f, random.nextDouble() * 0.02 + 0.1, random.nextGaussian() * 0.03f);

        return dropItemInWorld(storedStack, pos, velocity, 6);
    }

    private ItemEntity dropItemInWorld(ItemStack stack, Vector3f position, Vector3d initialVelocity, int pickupDelayTicks) {
        ItemEntity itemEntity = new ItemEntity(level, position.x(), position.y(), position.z(), stack);
        itemEntity.setDeltaMovement(initialVelocity.x, initialVelocity.y, initialVelocity.z);

        if (pickupDelayTicks > 0)
            itemEntity.setPickUpDelay(pickupDelayTicks);

        level.addFreshEntity(itemEntity);
        return itemEntity;
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

    public static <E extends BlockEntity> void animateTick(Level level, BlockPos blockPos, BlockState blockState, E e) {

    }
}
