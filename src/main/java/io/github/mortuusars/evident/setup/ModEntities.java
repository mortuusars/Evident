package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.content.torch_shooting.arrow.RedstoneTorchArrow;
import io.github.mortuusars.evident.content.torch_shooting.arrow.SoulTorchArrow;
import io.github.mortuusars.evident.content.torch_shooting.arrow.TorchArrow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Evident.ID);

    public static final RegistryObject<EntityType<TorchArrow>> TORCH_ARROW = ENTITIES.register("torch_arrow",
            () -> EntityType.Builder.<TorchArrow>of(TorchArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .fireImmune()
                    .build("torch_arrow"));

    public static final RegistryObject<EntityType<SoulTorchArrow>> SOUL_TORCH_ARROW = ENTITIES.register("soul_torch_arrow",
            () -> EntityType.Builder.<SoulTorchArrow>of(SoulTorchArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .fireImmune()
                    .build("soul_torch_arrow"));

    public static final RegistryObject<EntityType<RedstoneTorchArrow>> REDSTONE_TORCH_ARROW = ENTITIES.register("redstone_torch_arrow",
            () -> EntityType.Builder.<RedstoneTorchArrow>of(RedstoneTorchArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .fireImmune()
                    .build("redstone_torch_arrow"));

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
    }
}
