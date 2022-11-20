package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.blocks.CobwebCornerBlock;
import io.github.mortuusars.evident.config.CommonConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }

    @SubscribeEvent
    public static void blockRegistryOverrides(RegistryEvent.Register<Block> event) {
//        if (CommonConfig.CHANGE_DEFAULT_COBWEB_SOUND.get()) {
//            event.getRegistry().register(new WebBlock(BlockBehaviour.Properties.of(Material.WEB)
//                    .sound(SoundType.AZALEA_LEAVES)
//                    .noCollission()
//                    .requiresCorrectToolForDrops()
//                    .strength(4.0F))
//                    .setRegistryName("minecraft:cobweb"));
//        }
    }
}
