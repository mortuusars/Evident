package io.github.mortuusars.evident.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue BURNABLE_ENABLED;
    public static final ForgeConfigSpec.BooleanValue BURNABLE_CONSUME_ITEM;
    public static final ForgeConfigSpec.BooleanValue BURNABLE_DAMAGE_ITEM;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Burnables");
        builder.comment("Items with tag \"evident:igniter\" can burn blocks with tag \"evident:burnable\".");

        BURNABLE_ENABLED = builder
                .comment("Items with tag \"evident:igniter\" can burn blocks with tag \"evident:burnable\".\n\"Enable/disable burnable mechanic\"")
                .define("BurnableEnabled", true);

        BURNABLE_CONSUME_ITEM = builder
                .comment("Whether Igniter item (without durability) should be consumed when burning a block.")
                .define("BurnableConsume", false);

        BURNABLE_DAMAGE_ITEM = builder
                .comment("Whether Igniter items with durability (such as Flint and Steel) should be damaged when burning a block.")
                .define("BurnableDamageDurability", true);

        builder.pop();

        SPEC = builder.build();
    }
}
