package io.github.mortuusars.evident.world;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.config.Configuration;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.event.server.ServerAboutToStartEvent;

import java.util.ArrayList;
import java.util.List;

public class VillageStructures {

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static void addVillageBuilding(final ServerAboutToStartEvent event) {

        if (!Configuration.SPAWN_CHOPPING_CAMP_HOUSES.get())
            return;

        Registry<StructureTemplatePool> templatePools = event.getServer().registryAccess().registry(Registry.TEMPLATE_POOL_REGISTRY).get();
        Registry<StructureProcessorList> processorLists = event.getServer().registryAccess().registry(Registry.PROCESSOR_LIST_REGISTRY).get();

        if (Configuration.SPAWN_PLAINS_CHOPPING_CAMP.get()){
            VillageStructures.addBuildingToPool(templatePools, processorLists,
                    new ResourceLocation("minecraft:village/plains/houses"),
                    Evident.ID + ":village/houses/plains_chopping_camp_1", 5);
            VillageStructures.addBuildingToPool(templatePools, processorLists,
                    new ResourceLocation("minecraft:village/plains/houses"),
                    Evident.ID + ":village/houses/plains_chopping_camp_2", 5);
        }

        if (Configuration.SPAWN_TAIGA_CHOPPING_CAMP.get()) {
            VillageStructures.addBuildingToPool(templatePools, processorLists,
                    new ResourceLocation("minecraft:village/taiga/houses"),
                    Evident.ID + ":village/houses/taiga_chopping_camp_1", 4);
            VillageStructures.addBuildingToPool(templatePools, processorLists,
                    new ResourceLocation("minecraft:village/taiga/houses"),
                    Evident.ID + ":village/houses/taiga_chopping_camp_2", 6);
        }

        if (Configuration.SPAWN_SAVANNA_CHOPPING_CAMP.get()) {
            VillageStructures.addBuildingToPool(templatePools, processorLists,
                    new ResourceLocation("minecraft:village/savanna/houses"),
                    Evident.ID + ":village/houses/savanna_chopping_camp_1", 5);
            VillageStructures.addBuildingToPool(templatePools, processorLists,
                    new ResourceLocation("minecraft:village/savanna/houses"),
                    Evident.ID + ":village/houses/savanna_chopping_camp_2", 5);
        }

        if (Configuration.SPAWN_SNOWY_CHOPPING_CAMP.get()) {
            VillageStructures.addBuildingToPool(templatePools, processorLists,
                    new ResourceLocation("minecraft:village/snowy/houses"),
                    Evident.ID + ":village/houses/snowy_chopping_camp_1", 5);
            VillageStructures.addBuildingToPool(templatePools, processorLists,
                    new ResourceLocation("minecraft:village/snowy/houses"),
                    Evident.ID + ":village/houses/snowy_chopping_camp_2", 5);
        }
    }

    public static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry,
                                         Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolLocation,
                                         String structureNbtLocation, int weight) {

        LogUtils.getLogger().info("Adding '{}' structure to pool '{}'. Weight: {}.", structureNbtLocation, poolLocation, weight);

        StructureTemplatePool pool = templatePoolRegistry.get(poolLocation);
        if (pool == null) return;

        ResourceLocation emptyProcessor = new ResourceLocation("minecraft", "empty");
        Holder<StructureProcessorList> processorHolder =
                processorListRegistry.getHolderOrThrow(ResourceKey.create(Registry.PROCESSOR_LIST_REGISTRY, emptyProcessor));

        SinglePoolElement piece = SinglePoolElement.single(structureNbtLocation, processorHolder)
                .apply(StructureTemplatePool.Projection.RIGID);

//        boolean debugMode = false;
//        if (debugMode)
//            pool.templates.clear();

        for (int i = 0; i < weight; i++) {
            pool.templates.add(piece);
        }

        List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);

//        if (debugMode)
//            listOfPieceEntries.clear();

        listOfPieceEntries.add(new Pair<>(piece, weight));
        pool.rawTemplates = listOfPieceEntries;
    }
}
