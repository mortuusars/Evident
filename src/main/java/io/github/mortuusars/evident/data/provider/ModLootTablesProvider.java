package io.github.mortuusars.evident.data.provider;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.setup.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

@SuppressWarnings("DataFlowIssue")
public class ModLootTablesProvider extends BaseLootTableProvider {

    public ModLootTablesProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void addTables() {
        blockLootTables.put(ModBlocks.CHOPPING_BLOCK.get(),
                createSimpleTable(ModBlocks.CHOPPING_BLOCK.get().getRegistryName()
                        .getPath(), ModBlocks.CHOPPING_BLOCK.get()));
        blockLootTables.put(ModBlocks.CHIPPED_CHOPPING_BLOCK.get(),
                createSimpleTable(ModBlocks.CHIPPED_CHOPPING_BLOCK.get().getRegistryName()
                        .getPath(), ModBlocks.CHIPPED_CHOPPING_BLOCK.get()));
        blockLootTables.put(ModBlocks.DAMAGED_CHOPPING_BLOCK.get(),
                createSimpleTable(ModBlocks.DAMAGED_CHOPPING_BLOCK.get().getRegistryName()
                        .getPath(), ModBlocks.DAMAGED_CHOPPING_BLOCK.get()));


        villageChests();
    }

    private void villageChests() {
        customLootTables.put(ChestLootTables.PLAINS_CHOPPING_SITE, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 6.0F))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(LootItem.lootTableItem(Items.IRON_AXE).setWeight(2))
                        .add(LootItem.lootTableItem(Items.OAK_LOG).setWeight(4)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 16.0F))))
                        .add(LootItem.lootTableItem(Items.STRIPPED_OAK_LOG).setWeight(6)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 12.0F))))
                        .add(LootItem.lootTableItem(Items.OAK_PLANKS).setWeight(6)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 12.0F))))
                        .add(LootItem.lootTableItem(Items.OAK_SAPLING).setWeight(3)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                        .add(LootItem.lootTableItem(Items.BREAD).setWeight(2)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))))
                .build());

        customLootTables.put(ChestLootTables.TAIGA_CHOPPING_SITE, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 6.0F))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(LootItem.lootTableItem(Items.IRON_AXE).setWeight(2))
                        .add(LootItem.lootTableItem(Items.SPRUCE_LOG).setWeight(4)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 16.0F))))
                        .add(LootItem.lootTableItem(Items.STRIPPED_SPRUCE_LOG).setWeight(6)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 12.0F))))
                        .add(LootItem.lootTableItem(Items.SPRUCE_PLANKS).setWeight(6)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 12.0F))))
                        .add(LootItem.lootTableItem(Items.SPRUCE_SAPLING).setWeight(3)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                        .add(LootItem.lootTableItem(Items.SWEET_BERRIES).setWeight(4)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                        .add(LootItem.lootTableItem(Items.BREAD).setWeight(2)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))))
                .build());

        customLootTables.put(ChestLootTables.SAVANNA_CHOPPING_SITE, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 6.0F))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(LootItem.lootTableItem(Items.IRON_AXE).setWeight(2))
                        .add(LootItem.lootTableItem(Items.ACACIA_LOG).setWeight(4)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 16.0F))))
                        .add(LootItem.lootTableItem(Items.STRIPPED_ACACIA_LOG).setWeight(6)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 12.0F))))
                        .add(LootItem.lootTableItem(Items.ACACIA_PLANKS).setWeight(6)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 12.0F))))
                        .add(LootItem.lootTableItem(Items.ACACIA_SAPLING).setWeight(3)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                        .add(LootItem.lootTableItem(Items.BREAD).setWeight(2)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))))
                .build());

        customLootTables.put(ChestLootTables.SNOWY_CHOPPING_SITE, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 6.0F))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(LootItem.lootTableItem(Items.IRON_AXE).setWeight(2))
                        .add(LootItem.lootTableItem(Items.SPRUCE_LOG).setWeight(4)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 16.0F))))
                        .add(LootItem.lootTableItem(Items.STRIPPED_SPRUCE_LOG).setWeight(6)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 12.0F))))
                        .add(LootItem.lootTableItem(Items.SPRUCE_PLANKS).setWeight(6)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 12.0F))))
                        .add(LootItem.lootTableItem(Items.SNOWBALL).setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                        .add(LootItem.lootTableItem(Items.SNOW_BLOCK).setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                        .add(LootItem.lootTableItem(Items.BREAD).setWeight(2)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))))
                .build());
    }

    private static class ChestLootTables {
        public static final ResourceLocation PLAINS_CHOPPING_SITE = Evident.resource("chests/village/village_plains_chopping_camp");
        public static final ResourceLocation TAIGA_CHOPPING_SITE = Evident.resource("chests/village/village_taiga_chopping_camp");
        public static final ResourceLocation SAVANNA_CHOPPING_SITE = Evident.resource("chests/village/village_savanna_chopping_camp");
        public static final ResourceLocation SNOWY_CHOPPING_SITE = Evident.resource("chests/village/village_snowy_chopping_camp");
    }
}
