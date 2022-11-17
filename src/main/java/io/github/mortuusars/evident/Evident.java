package io.github.mortuusars.evident;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.evident.behaviour.Burnable;
import io.github.mortuusars.evident.config.CommonConfig;
import io.github.mortuusars.evident.setup.ClientSetup;
import io.github.mortuusars.evident.setup.ModBlocks;
import io.github.mortuusars.evident.setup.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Evident.ID)
public class Evident
{
    public static final String ID = "evident";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Evident()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.register(bus);
        ModItems.register(bus);
//        bus.addListener(Evident::blockRegistryOverrides);

        bus.addListener(ClientSetup::onClientSetupEvent);


        MinecraftForge.EVENT_BUS.addListener(Burnable::onBlockActivated);
        MinecraftForge.EVENT_BUS.register(this);
    }



    public static ResourceLocation resource(String path) {
        return new ResourceLocation(ID, path);
    }
}
