package net.jzells.voyager_golems.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.jzells.voyager_golems.VoyagerGolems;
import net.jzells.voyager_golems.common.blocks.GolemDenBlock;
import net.jzells.voyager_golems.common.blocks.VoyagerBlocks;
import net.jzells.voyager_golems.common.items.VoyagerItems;
import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.jzells.voyager_golems.common.recipes.GolemProduceRecipe;
import net.jzells.voyager_golems.util.Constants;
import net.jzells.voyager_golems.util.VoyagerGuiUtil;
import net.jzells.voyager_golems.util.gui.VoyagerGraphicsConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class GolemProduceJEICategory implements IRecipeCategory<GolemProduceRecipe> {

    public static final RecipeType<GolemProduceRecipe> RECIPE_TYPE =
            new RecipeType<>(
                    ResourceLocation.fromNamespaceAndPath(
                            VoyagerGolems.MODID,
                            "golem_production"
                    ),
                    GolemProduceRecipe.class
            );

    private final IDrawable icon;
    private final IDrawable background;

    public GolemProduceJEICategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemStack(
                new ItemStack(VoyagerItems.ROCKY_GOLEM.get())
        );

        this.background = guiHelper.createDrawable(
                ResourceLocation.fromNamespaceAndPath(
                        VoyagerGraphicsConstants.VOYAGER_NAMESPACE,
                        "textures/gui/jei_golem_recipe.png"
                ), 0, 0, 150, 70
        );
    }

    @Override
    public RecipeType<GolemProduceRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }



    @Override
    public Component getTitle() {
        return Component.translatable(
                "jei.voyager_golems.golem_production"
        );
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(
            IRecipeLayoutBuilder builder,
            GolemProduceRecipe recipe,
            IFocusGroup focuses
    ) {
        builder.addSlot(
                RecipeIngredientRole.CATALYST,
                10, 16
        ).addItemStack(
                new ItemStack(recipe.getGolemItem())
        );

        builder.addSlot(
                RecipeIngredientRole.CATALYST,
                37, 38
        ).addItemStack(
                new ItemStack(recipe.getGolemBlock()).setHoverName(Component.literal("§cPlace adjacent to den"))
        );



        GolemItem golemItem = (GolemItem) recipe.getGolemItem().asItem();

        ItemStack denStack = VoyagerBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(block -> block instanceof GolemDenBlock)
                .map(block -> (GolemDenBlock) block)
                .filter(denBlock ->
                        golemItem.GROUPING.equals(
                                denBlock.getDefinition().group().getId()
                        )
                )
                .map(denBlock -> new ItemStack(denBlock.asItem()))
                .findFirst()
                .orElse(ItemStack.EMPTY);

        builder.addSlot(
                RecipeIngredientRole.CATALYST,
                37, 16
        ).addItemStack(denStack);

        int x = 71;
        int y = 18;

        for (ItemStack output : recipe.getOutputs()) {
            builder.addSlot(
                    RecipeIngredientRole.OUTPUT,
                    x, y
            ).addItemStack(output);

            x += 18;

            if (x > 125) {
                x = 90;
                y += 18;
            }
        }
    }

    @Override
    public int getWidth() {
        return 150;
    }

    @Override
    public int getHeight() {
        return 70;
    }



    @Override
    public void draw(GolemProduceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        GolemItem golemItem = (GolemItem) recipe.getGolemItem().asItem();
        background.draw(guiGraphics);
        guiGraphics.drawString(
                Minecraft.getInstance().font, "Level: " + recipe.getGolem_level(), 9, 58, 0x404040, false
        );
        guiGraphics.drawString(
                Minecraft.getInstance().font, "Time: " + ((float )recipe.getProductionPoints() / 20f) + "s", 70, 58, 0x404040, false
        );
        guiGraphics.drawString(
                Minecraft.getInstance().font, "Den Tier Required: " + golemItem.TIER, 9, 3, 0x404040, false
        );
        float percentage = (System.currentTimeMillis() % 2000L) / 2000.0F;
        VoyagerGuiUtil.renderBottomToTopVertialBarPercentage(guiGraphics, VoyagerGraphicsConstants.JEI_GOLEM_PROGRESS_BAR_EMPTY,
                VoyagerGraphicsConstants.JEI_GOLEM_PROGRESS_BAR, percentage, 6, 36, 59, 17);
//        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }
}
