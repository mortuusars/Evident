package io.github.mortuusars.evident;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.evident.behaviour.Burnable;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Evident.ID)
public class Evident
{
    public static final String ID = "evident";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Evident()
    {
        MinecraftForge.EVENT_BUS.addListener(Burnable::onBlockActivated);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation resource(String path) {
        return new ResourceLocation(ID, path);
    }
}
