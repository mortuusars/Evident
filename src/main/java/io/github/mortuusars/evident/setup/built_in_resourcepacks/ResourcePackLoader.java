package io.github.mortuusars.evident.setup.built_in_resourcepacks;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.evident.Evident;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;

// Credits: Creators-of-Create/Create
public class ResourcePackLoader {

    // Loads resourcepacks located in resources/resourcepacks
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            IModFileInfo modFileInfo = ModList.get().getModFileById(Evident.ID);
            if (modFileInfo == null) {
                LogUtils.getLogger().error("Could not find Evident mod file info; built-in resource packs will be missing!");
                return;
            }
            IModFile modFile = modFileInfo.getFile();
            event.addRepositorySource((consumer, constructor) -> {
                consumer.accept(Pack.create(Evident.resource("default_cobweb").toString(),
                        false, () -> new ModFilePackResources("Default Cobwebs", modFile, "resourcepacks/default_cobweb"), constructor, Pack.Position.TOP, PackSource.DEFAULT));
            });
        }
    }
}