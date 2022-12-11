package io.github.mortuusars.evident.advancement;

import com.google.gson.JsonObject;
import io.github.mortuusars.evident.Evident;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ChoppingBlockTrigger extends SimpleCriterionTrigger<ChoppingBlockTrigger.TriggerInstance> {

    private static final ResourceLocation ID = Evident.resource("use_chopping_block");

    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext context) {
        return new TriggerInstance(player);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, TriggerInstance::test);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(EntityPredicate.Composite player) {
            super(ChoppingBlockTrigger.ID, player);
        }

        public boolean test() {
            return true;
        }
    }
}
