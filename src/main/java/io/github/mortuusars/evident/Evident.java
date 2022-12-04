package io.github.mortuusars.evident;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import io.github.mortuusars.evident.content.torch_shooting.TorchShooting;
import io.github.mortuusars.evident.config.Configuration;
import io.github.mortuusars.evident.events.ClientEvents;
import io.github.mortuusars.evident.events.CommonEvents;
import io.github.mortuusars.evident.setup.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Evident.ID)
public class Evident
{
    public static final String ID = "evident";
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final Logger LOGGER = LogUtils.getLogger();

    public Evident()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntityTypes.register(modEventBus);

        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        ModRecipeTypes.RECIPE_TYPES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.register(ClientEvents.class);

        MinecraftForge.EVENT_BUS.register(TorchShooting.class);

        MinecraftForge.EVENT_BUS.register(CommonEvents.class);
    }

    public static ResourceLocation resource(String path) {
        return new ResourceLocation(ID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            if (Configuration.CHANGE_DEFAULT_COBWEB_SOUND.get())
                Blocks.COBWEB.soundType = SoundType.AZALEA_LEAVES;

            TorchShooting.registerDispenserBehaviours();
        });
    }
}
