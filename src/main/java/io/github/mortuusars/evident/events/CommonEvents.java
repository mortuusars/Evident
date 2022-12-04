package io.github.mortuusars.evident.events;

import io.github.mortuusars.evident.content.Burnable;
import io.github.mortuusars.evident.setup.built_in_resourcepacks.ResourcePackLoader;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvents {

    @SubscribeEvent
    public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
        Burnable.onBlockActivated(event);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void addPackFinders(AddPackFindersEvent event) {
            ResourcePackLoader.addPackFinders(event);
        }
    }
}
