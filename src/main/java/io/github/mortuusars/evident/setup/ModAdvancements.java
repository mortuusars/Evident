package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.advancement.ChoppingBlockTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class ModAdvancements {
    public static ChoppingBlockTrigger CHOPPING_BLOCK = new ChoppingBlockTrigger();

    public static void register() {
        CriteriaTriggers.register(CHOPPING_BLOCK);
    }
}
