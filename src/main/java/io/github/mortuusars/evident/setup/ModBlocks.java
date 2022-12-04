package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.content.chopping_block.ChoppingBlockBlock;
import io.github.mortuusars.evident.content.cobweb.CobwebCornerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Evident.ID);

    public static final RegistryObject<CobwebCornerBlock> COBWEB_CORNER = BLOCKS.register("cobweb_corner",
            () -> new CobwebCornerBlock(BlockBehaviour.Properties.of(Material.WEB)
                    .sound(SoundType.AZALEA_LEAVES)
                    .noCollission()
                    .strength(0.25F)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<ChoppingBlockBlock> CHOPPING_BLOCK = BLOCKS.register("chopping_block",
            () -> new ChoppingBlockBlock(BlockBehaviour.Properties.of(Material.WOOD)
                    .sound(SoundType.WOOD)
                    .strength(2F)));

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }
}
