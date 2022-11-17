package io.github.mortuusars.evident.datagen;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.datagen.providers.ModBlockStatesProvider;
import io.github.mortuusars.evident.datagen.providers.ModBlockTagsProvider;
import io.github.mortuusars.evident.datagen.providers.ModItemModelsProvider;
import io.github.mortuusars.evident.datagen.providers.ModItemTagsProvider;
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
            ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(generator, event.getExistingFileHelper());
            generator.addProvider(blockTagsProvider);
            generator.addProvider(new ModItemTagsProvider(generator, blockTagsProvider, event.getExistingFileHelper()));
//            generator.addProvider(new ModLootTablesProvider(generator));
//            generator.addProvider(new ModRecipeProvider(generator));
        }

        if (event.includeClient()){
            generator.addProvider(new ModBlockStatesProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(new ModItemModelsProvider(generator, event.getExistingFileHelper()));
//            generator.addProvider(new ModLanguageProvider(generator, "en_us"));
//            generator.addProvider(new ModLanguageProvider(generator, "uk_ua"));
        }
    }
}