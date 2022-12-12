package io.github.mortuusars.evident.data;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.data.provider.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Evident.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();

        if (event.includeServer()){
            BlockTags blockTagsProvider = new BlockTags(generator, event.getExistingFileHelper());
            generator.addProvider(blockTagsProvider);
            generator.addProvider(new ItemTags(generator, blockTagsProvider, event.getExistingFileHelper()));
            generator.addProvider(new Recipes(generator));
            generator.addProvider(new ModLootTablesProvider(generator));
        }

        if (event.includeClient()){
            generator.addProvider(new BlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(new ItemModels(generator, event.getExistingFileHelper()));
//            generator.addProvider(new ModLanguageProvider(generator, "en_us"));
//            generator.addProvider(new ModLanguageProvider(generator, "uk_ua"));
        }
    }
}