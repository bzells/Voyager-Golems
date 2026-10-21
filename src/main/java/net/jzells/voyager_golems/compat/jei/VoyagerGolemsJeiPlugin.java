package net.jzells.voyager_golems.compat.jei;

import com.mojang.logging.LogUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;

import net.jzells.voyager_golems.VoyagerGolems;
import net.jzells.voyager_golems.common.blocks.VoyagerBlocks;
import net.jzells.voyager_golems.common.items.VoyagerItems;
import net.jzells.voyager_golems.common.recipes.VoyagerRecipes;
import net.jzells.voyager_golems.compat.jei.GolemProduceJEICategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import net.jzells.voyager_golems.util.Constants;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

@JeiPlugin
public class VoyagerGolemsJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(
                VoyagerGolems.MODID,
                "jei_plugin"
        );
    }

    @Override
    public void registerCategories(
            IRecipeCategoryRegistration registration
    ) {
        LogUtils.getLogger().info("[JEI] Registering Golem Production category");
        registration.addRecipeCategories(
                new GolemProduceJEICategory(
                        registration.getJeiHelpers().getGuiHelper()
                )
        );
    }

    @Override
    public void registerRecipes(
            IRecipeRegistration registration
    ) {
        LogUtils.getLogger().info("[JEI] Registering Golem Production recipes");
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null) {
            return;
        }

        var recipes = minecraft.level.getRecipeManager()
                .getAllRecipesFor(
                        VoyagerRecipes.GOLEM_PRODUCTION_TYPE
                );
        LogUtils.getLogger().info("[JEI] Recipe count: {}", recipes.size());
        registration.addRecipes(
                GolemProduceJEICategory.RECIPE_TYPE,
                recipes.stream().map(RecipeHolder::value).toList()
        );
    }

    @Override
    public void registerRecipeCatalysts(
            IRecipeCatalystRegistration registration
    ) {
        registration.addRecipeCatalyst(
                new ItemStack(
                        VoyagerItems.ROCKY_GOLEM.get()
                ),
                GolemProduceJEICategory.RECIPE_TYPE
        );
    }
}