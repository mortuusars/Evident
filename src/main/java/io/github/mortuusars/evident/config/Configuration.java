package io.github.mortuusars.evident.config;

import net.minecraftforge.common.ForgeConfigSpec;

@SuppressWarnings("TextBlockMigration")
public class Configuration {
    public static final ForgeConfigSpec COMMON;

    // Burnable
    public static final ForgeConfigSpec.BooleanValue BURNABLE_ENABLED;
    public static final ForgeConfigSpec.BooleanValue BURNABLE_CONSUME_ITEM;
    public static final ForgeConfigSpec.BooleanValue BURNABLE_DAMAGE_ITEM;

    // Shooting Torches
    public static final ForgeConfigSpec.BooleanValue SHOOTING_TORCHES_ENABLED;
    public static final ForgeConfigSpec.BooleanValue SHOOTING_TORCHES_DISPENSER;
    public static final ForgeConfigSpec.DoubleValue SHOOTING_TORCHES_DAMAGE;
    public static final ForgeConfigSpec.IntValue SHOOTING_TORCHES_DURABILITY_COST;
    public static final ForgeConfigSpec.IntValue SHOOTING_TORCHES_IGNITE_SECONDS;
    public static final ForgeConfigSpec.IntValue SHOOTING_TORCHES_WITHER_SECONDS;
    public static final ForgeConfigSpec.IntValue SHOOTING_TORCHES_SLOWNESS_SECONDS;
    public static final ForgeConfigSpec.BooleanValue SHOOTING_TORCHES_IGNORE_HOTBAR;

    // Chopping Block
    public static final ForgeConfigSpec.DoubleValue CHOPPING_BLOCK_DAMAGE_CHANCE;

    // Structures
    public static final ForgeConfigSpec.BooleanValue SPAWN_CHOPPING_CAMP_HOUSES;
    public static final ForgeConfigSpec.BooleanValue SPAWN_PLAINS_CHOPPING_CAMP;
    public static final ForgeConfigSpec.BooleanValue SPAWN_TAIGA_CHOPPING_CAMP;
    public static final ForgeConfigSpec.BooleanValue SPAWN_SAVANNA_CHOPPING_CAMP;
    public static final ForgeConfigSpec.BooleanValue SPAWN_SNOWY_CHOPPING_CAMP;

    // Misc
    public static final ForgeConfigSpec.BooleanValue CHANGE_DEFAULT_COBWEB_SOUND;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Burnables");
        BURNABLE_ENABLED = builder
                .comment("Items with tag \"evident:igniters\" can burn blocks with tag \"evident:burnables\".\n\"",
                        "Enable/disable burnable mechanic\"")
                .define("Enabled", true);
        BURNABLE_CONSUME_ITEM = builder
                .comment("Igniter item (without durability) will be consumed when burning a block.")
                .define("ConsumeIgniter", false);
        BURNABLE_DAMAGE_ITEM = builder
                .comment("Igniter items with durability (such as Flint and Steel) will be damaged when burning a block.")
                .define("IgniterDurabilityCost", true);
        builder.pop();

        builder.push("ShootingTorches");
        SHOOTING_TORCHES_ENABLED = builder
                .comment("Torches (regular, soul and redstone) can be used as ammo for bow/crossbow. Torch will be placed at a target location (if possible)." +
                        "\nYou can add custom torches to shoot with by adding them to one of the item tags:" +
                        "\n'forge:torches', 'forge:soul_torches', 'forge:redstone_torches'." +
                        "\nYou can also disable shooting with particular torch by removing it from the tag. ('minecraft:soul_torch' from 'forge:soul_torches' for example)",
                        "\nEnable/disable torch shooting\"")
                .define("Enabled", true);
        SHOOTING_TORCHES_DISPENSER = builder
                .comment("Shoot torches from a dispenser")
                .define("DispenserShooting", true);
        SHOOTING_TORCHES_DAMAGE = builder
                .comment("Base Damage of the projectile")
                .defineInRange("Damage", 0.5D, 0.01D, 99D);
        SHOOTING_TORCHES_DURABILITY_COST = builder
                .comment("Amount of durability loss per shot")
                .defineInRange("DurabilityCost", 2, 1, Integer.MAX_VALUE);
        SHOOTING_TORCHES_IGNITE_SECONDS = builder
                .comment("How long entity that has been hit by torch will burn. In Seconds. Set to 0 to disable.")
                .defineInRange("IgniteDuration", 6, 0, Integer.MAX_VALUE);
        SHOOTING_TORCHES_WITHER_SECONDS = builder
                .comment("How long entity that has been hit by soul torch will have the wither debuff. In Seconds. Set to 0 to disable.")
                .defineInRange("WitherDuration", 10, 0, Integer.MAX_VALUE);
        SHOOTING_TORCHES_SLOWNESS_SECONDS = builder
                .comment("How long entity that has been hit by redstone torch will have the slowness debuff. In Seconds. Set to 0 to disable.")
                .defineInRange("SlownessDuration", 20, 0, Integer.MAX_VALUE);
        SHOOTING_TORCHES_IGNORE_HOTBAR = builder
                .comment("Torches in hotbar will have lower priority when choosing a projectile.",
                        "Torch from hotbar would still be shot if player has no other arrows in inventory.",
                        "*Projectiles in offhand (or main hand when shooting from offhand) will always take priority.")
                .define("IgnoreHotbar", false);
        builder.pop();


        builder.push("ChoppingBlock");

        CHOPPING_BLOCK_DAMAGE_CHANCE = builder
                .comment("Chance of the chopping block being damaged while chopping.", "Set to 0 to disable damage.")
                .defineInRange("DamageChance", 0.01D, 0.0D, 1.0D);

        builder.pop();

        builder.push("Structures");

        SPAWN_CHOPPING_CAMP_HOUSES = builder
                .comment("Chopping Camp buildings will spawn in villages.")
                .define("SpawnChoppingCampBuildings", true);

        builder.push("Individual");

        SPAWN_PLAINS_CHOPPING_CAMP = builder.define("SpawnPlainsChoppingCamp", true);
        SPAWN_TAIGA_CHOPPING_CAMP = builder.define("SpawnTaigaChoppingCamp", true);
        SPAWN_SAVANNA_CHOPPING_CAMP = builder.define("SpawnSavannaChoppingCamp", true);
        SPAWN_SNOWY_CHOPPING_CAMP = builder.define("SpawnSnowyChoppingCamp", true);

        builder.pop();
        builder.pop();

        builder.push("Miscellaneous");

        CHANGE_DEFAULT_COBWEB_SOUND = builder
                .comment("Change Cobweb sound to something that's not stone ??\\_(???)_/??")
                .define("ChangeCobwebSound", true);

        builder.pop();

        COMMON = builder.build();
    }
}
