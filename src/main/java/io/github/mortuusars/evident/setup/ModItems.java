package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.Evident;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Evident.ID);

    public static final RegistryObject<BlockItem> COBWEB_CORNER = ITEMS.register("cobweb_corner",
            () -> new BlockItem(ModBlocks.COBWEB_CORNER.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));

    public static final RegistryObject<BlockItem> CHOPPING_BLOCK = ITEMS.register("chopping_block",
            () -> new BlockItem(ModBlocks.CHOPPING_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
