package io.github.mortuusars.evident.data.provider;

import io.github.mortuusars.evident.setup.ModBlocks;
import net.minecraft.data.DataGenerator;

public class ModLootTablesProvider extends BaseLootTableProvider {

    public ModLootTablesProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void addTables() {
        blockLootTables.put(ModBlocks.CHOPPING_BLOCK.get(), createSimpleTable(ModBlocks.CHOPPING_BLOCK.get().getRegistryName().getPath(), ModBlocks.CHOPPING_BLOCK.get()));
        blockLootTables.put(ModBlocks.CHIPPED_CHOPPING_BLOCK.get(), createSimpleTable(ModBlocks.CHIPPED_CHOPPING_BLOCK.get().getRegistryName().getPath(), ModBlocks.CHIPPED_CHOPPING_BLOCK.get()));
        blockLootTables.put(ModBlocks.DAMAGED_CHOPPING_BLOCK.get(), createSimpleTable(ModBlocks.DAMAGED_CHOPPING_BLOCK.get().getRegistryName().getPath(), ModBlocks.DAMAGED_CHOPPING_BLOCK.get()));
    }
}
