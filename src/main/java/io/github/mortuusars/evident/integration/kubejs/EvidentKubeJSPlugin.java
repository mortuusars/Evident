package io.github.mortuusars.evident.integration.kubejs;

import com.mojang.logging.LogUtils;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;
import io.github.mortuusars.evident.Evident;

public class EvidentKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void addRecipes(RegisterRecipeHandlersEvent event) {
        event.register(Evident.resource("chopping"), ChoppingRecipeJS::new);
        LogUtils.getLogger().info("Loaded KubeJS integration.");
    }
}
