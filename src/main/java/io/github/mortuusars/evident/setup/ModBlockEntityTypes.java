package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.content.chopping_block.ChoppingBlockBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Evident.ID);

    public static final RegistryObject<BlockEntityType<ChoppingBlockBlockEntity>> CHOPPING_BLOCK = BLOCK_ENTITIES.register("chopping_block",
            () -> BlockEntityType.Builder.of(ChoppingBlockBlockEntity::new, ModBlocks.CHOPPING_BLOCK.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
