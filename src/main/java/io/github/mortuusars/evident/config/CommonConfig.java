package io.github.mortuusars.evident.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue BURNABLE_ENABLED;
    public static final ForgeConfigSpec.BooleanValue BURNABLE_CONSUME_ITEM;
    public static final ForgeConfigSpec.BooleanValue BURNABLE_DAMAGE_ITEM;

    public static final ForgeConfigSpec.BooleanValue SHOOTING_TORCHES_ENABLED;
    public static final ForgeConfigSpec.IntValue SHOOTING_TORCHES_DURABILITY_COST;
    public static final ForgeConfigSpec.IntValue SHOOTING_TORCHES_INGITE_SECONDS;

    public static final ForgeConfigSpec.BooleanValue CHANGE_DEFAULT_COBWEB_SOUND;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Burnables");
        BURNABLE_ENABLED = builder
                .comment("Items with tag \"evident:igniter\" can burn blocks with tag \"evident:burnable\".\n\"" +
                        "Enable/disable burnable mechanic:\"")
                .define("Enabled", true);
        BURNABLE_CONSUME_ITEM = builder
                .comment("Whether Igniter item (without durability) should be consumed when burning a block.")
                .define("ConsumeIgniter", false);
        BURNABLE_DAMAGE_ITEM = builder
                .comment("Whether Igniter items with durability (such as Flint and Steel) should be damaged when burning a block.")
                .define("IgniterDurabilityCost", true);
        builder.pop();

        builder.push("ShootingTorches");
        SHOOTING_TORCHES_ENABLED = builder
                .comment("Torches can be used as ammo for bow/crossbow. Torch will be placed at a target location (if possible).",
                        "Enable/disable torch shooting mechanic:\"")
                .define("Enabled", true);
        SHOOTING_TORCHES_DURABILITY_COST = builder
                .comment("Amount of durability loss per hit")
                .defineInRange("DurabilityCost", 2, 1, Integer.MAX_VALUE);
        SHOOTING_TORCHES_INGITE_SECONDS = builder
                .comment("Duration of entity ignite on hit in seconds. Set to 0 to disable.")
                .defineInRange("IgniteDuration", 4, 0, Integer.MAX_VALUE);
        builder.pop();

        CHANGE_DEFAULT_COBWEB_SOUND = builder
                .comment("Change Cobweb sound to something that's not stone ¯\\_(ツ)_/¯")
                .define("ChangeCobwebSound", true);


        SPEC = builder.build();
    }
}
