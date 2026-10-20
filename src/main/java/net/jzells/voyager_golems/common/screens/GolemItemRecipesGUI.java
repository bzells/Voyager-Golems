package net.jzells.voyager_golems.common.screens;

import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.jzells.voyager_golems.common.menus.GolemItemRecipesMenu;
import net.jzells.voyager_golems.common.recipes.GolemProduceRecipe;
import net.jzells.voyager_golems.common.recipes.VoyagerRecipes;
import net.jzells.voyager_golems.util.VoyagerGuiUtil;
import net.jzells.voyager_golems.util.gui.VoyagerGraphicsConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.List;

import static net.jzells.voyager_golems.common.screens.GolemItemGUI.*;
import static net.jzells.voyager_golems.util.VoyagerGuiUtil.playClickSound;

public class GolemItemRecipesGUI extends AbstractContainerScreen<GolemItemRecipesMenu> {





    private String selected_recipe_id;

    public GolemItemRecipesGUI(GolemItemRecipesMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.imageWidth = 176;
        this.imageHeight = 87;

        GolemItem golem_item = (GolemItem) pMenu.GOLEM_STACK.getItem();
        this.selected_recipe_id = golem_item.getRecipe(pMenu.GOLEM_STACK);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(font, title, 7, 6, 0x404040, false);
        pGuiGraphics.drawString(font, "Recipe Selection", 89, 6, 0x404040, false);
    }

    @Override
    protected void renderBg(
            GuiGraphics guiGraphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                "voyager_golems",
                "textures/gui/golem_item_recipes.png"
        );

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

//        GolemItem golem_item = (GolemItem) menu.GOLEM_STACK.getItem();
//        float prestige_percentage = (float )golem_item.getPrestigeCount(menu.GOLEM_STACK) / 5.0f;
//        renderPrestigePanel(guiGraphics, x + this.imageWidth + 1, y, mouseX, mouseY, prestige_percentage);

        renderBackground(guiGraphics);
        renderStatsPanel(guiGraphics, x - 85 - 1, y, menu.GOLEM_STACK);
        renderTooltip(guiGraphics, mouseX, mouseY);
        guiGraphics.blit(
                texture,
                x,
                y,
                0,
                0,
                imageWidth,
                imageHeight,
                imageWidth,
                imageHeight
        );



        renderHomeScreen(guiGraphics, mouseX, mouseY, x, y, 1f);
        renderRecipeHelpInfo(guiGraphics, x + imageWidth + 1, y, mouseX, mouseY);


    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics) {
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0,0,-1);
        super.renderBackground(pGuiGraphics);
        pGuiGraphics.pose().popPose();
    }

    private void renderHomeScreen(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y, float z_val)
    {
        int button_x_start = x + 7;
        int button_y_start = y + 15;
        int button_size = 18;

        int frame_x = x + 29;
        int frame_y = y + 15;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0,0,z_val);
        renderRecipes(guiGraphics, x + 44, y + 16, 7, mouseX, mouseY);
//        renderItemInFrame(frame_x, frame_y, menu.GOLEM_STACK, guiGraphics);

        if (VoyagerGuiUtil.isMouseInBounds(mouseX, mouseY,
                button_x_start, button_x_start + button_size,
                button_y_start, button_y_start + button_size)) {
            renderOptionsTextOverlay(guiGraphics, UPGRADE_BUTTON_TEXT, mouseX, mouseY);
        }

        if (VoyagerGuiUtil.isMouseInBounds(mouseX, mouseY,
                button_x_start, button_x_start + button_size,
                button_y_start + button_size, button_y_start + button_size * 2)) {
            renderOptionsTextOverlay(guiGraphics, STATS_BUTTON_TEXT, mouseX, mouseY);
        }

        if (VoyagerGuiUtil.isMouseInBounds(mouseX, mouseY,
                button_x_start, button_x_start + button_size,
                button_y_start + button_size * 2, button_y_start + button_size * 3)) {
            renderOptionsTextOverlay(guiGraphics, RECIPES_BUTTON_TEXT, mouseX, mouseY);
        }
        if (VoyagerGuiUtil.isMouseInBounds(mouseX, mouseY,
                x + HOME_BUTTON_X, x + HOME_BUTTON_X + 17,
                y + HOME_BUTTON_Y, y + HOME_BUTTON_Y + 6)) {
            renderOptionsTextOverlay(guiGraphics, RETURN_BUTTON_TEXT, mouseX, mouseY);
        }
        if (VoyagerGuiUtil.isMouseInBounds(mouseX, mouseY,
                x + RESET_RECIPE_BUTTON_X, x + RESET_RECIPE_BUTTON_X + 123,
                y + RESET_RECIPE_BUTTON_Y, y + RESET_RECIPE_BUTTON_Y + 6)) {
            renderOptionsTextOverlay(guiGraphics, RESET_RECIPE_BUTTON_TEXT, mouseX, mouseY);
        }
    }

    private void renderRecipes(GuiGraphics guiGraphics, int x, int y, int cols, int mx, int my)
    {
        GolemItem golem_item = (GolemItem) menu.GOLEM_STACK.getItem();
        List<GolemProduceRecipe> golem_recipes = getDisplayedRecipes();

        for (int i = 0; i < golem_recipes.size(); i++) {
            int col = i % cols;
            int row = i / cols;

            int recipeX = x + (18 * col);
            int recipeY = y + (18 * row);

            ItemStack showStack = golem_recipes.get(i).getOutputs().get(0);

            renderRecipeButtons(guiGraphics, golem_item, golem_recipes, i, recipeX, recipeY);

            guiGraphics.renderItem(showStack, recipeX, recipeY);
            guiGraphics.renderItemDecorations(font, showStack, recipeX, recipeY);

            if (VoyagerGuiUtil.isMouseInBounds(
                    mx, my,
                    recipeX, recipeX + 16,
                    recipeY, recipeY + 16
            )) {
                renderRecipeOutputs(
                        guiGraphics,
                        golem_recipes.get(i),
                        recipeX,
                        recipeY
                );
            }
        }
    }

    private void renderRecipeButtons(GuiGraphics guiGraphics, GolemItem golem_item, List<GolemProduceRecipe> golem_recipes, int i, int recipeX, int recipeY) {
        if(selected_recipe_id.equals(golem_recipes.get(i).getId().toString()))
        {
            guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(
                            "voyager_golems",
                            "textures/gui/recipe_button_selected.png"
                    ), recipeX - 1 , recipeY - 1,
                    0, 0,
                    18, 18,
                    18, 18);
        }
        else
        {
            guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(
                            "voyager_golems",
                            "textures/gui/recipe_button.png"
                    ), recipeX - 1, recipeY - 1,
                    0, 0,
                    18, 18,
                    18, 18);
        }
    }

    private void renderRecipeOutputs(GuiGraphics guiGraphics, GolemProduceRecipe golemProduceRecipe, int mx, int my)
    {
        List<ItemStack> output_stacks = golemProduceRecipe.getOutputs();
        int xpos = 0;
        int ypos = 0;
        for(int i = 0; i < output_stacks.size(); i++)
        {
            int cols = 4;

            xpos = mx + (18 * (i % cols));
            ypos = my + (18 * (i / cols));

            xpos += 10;
            ypos -= 18*3 - 20;

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0,0,10);
            guiGraphics.blit(
                    ResourceLocation.fromNamespaceAndPath(
                            "voyager_golems",
                            "textures/gui/recipe_output_slot.png"
                    ),
                    xpos, ypos,
                    0, 0,
                    18, 18,
                    18, 18
            );
            guiGraphics.renderItem(output_stacks.get(i), xpos + 1, ypos + 1);
            guiGraphics.renderItemDecorations(font, output_stacks.get(i), xpos + 1, ypos + 1);

            guiGraphics.pose().popPose();
            
        }
        VoyagerGraphicsConstants.renderItemOnTopOf(guiGraphics,
                VoyagerGraphicsConstants.getVoyagerTexture(VoyagerGraphicsConstants.GREEN_BUTTON_PATH)
                , 18, 18, new ItemStack(golemProduceRecipe.getGolemBlock().asItem(), 1), xpos + 18, ypos);
    }


    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        if(VoyagerGuiUtil.isMouseInBounds((int) mx, (int) my, x + UPGRADE_BUTTON_X,  x + UPGRADE_BUTTON_X + 17, y + UPGRADE_BUTTON_Y, y + UPGRADE_BUTTON_Y + 17) && button == VoyagerGuiUtil.LEFT_CLICK)
        {
            handleButtonClick(UPGRADE_BUTTON_PID);
            playClickSound();
        }
        if(VoyagerGuiUtil.isMouseInBounds((int) mx, (int) my, x + STATS_BUTTON_X,  x + STATS_BUTTON_X + 17, y + STATS_BUTTON_Y, y + STATS_BUTTON_Y + 17) && button == VoyagerGuiUtil.LEFT_CLICK)
        {
            handleButtonClick(STATS_BUTTON_PID);
            playClickSound();
        }
        if(VoyagerGuiUtil.isMouseInBounds((int) mx, (int) my, x + RECIPES_BUTTON_X,  x + RECIPES_BUTTON_X + 17, y + RECIPES_BUTTON_Y, y + RECIPES_BUTTON_Y + 17) && button == VoyagerGuiUtil.LEFT_CLICK)
        {
//            handleButtonClick(RECIPES_BUTTON_PID);
            VoyagerGuiUtil.playErrorSound();
        }
        if(VoyagerGuiUtil.isMouseInBounds((int) mx, (int) my, x + HOME_BUTTON_X,  x + HOME_BUTTON_X + 17, y + HOME_BUTTON_Y, y + HOME_BUTTON_Y + 6) && button == VoyagerGuiUtil.LEFT_CLICK)
        {
            handleButtonClick(HOME_BUTTON_PID);
            playClickSound();
        }
        if(VoyagerGuiUtil.isMouseInBounds((int) mx, (int) my, x + RESET_RECIPE_BUTTON_X,  x + RESET_RECIPE_BUTTON_X + 123, y + RESET_RECIPE_BUTTON_Y, y + RESET_RECIPE_BUTTON_Y + 6) && button == VoyagerGuiUtil.LEFT_CLICK)
        {
            handleButtonClick(RESET_RECIPE_BUTTON_PID);
            this.selected_recipe_id = "none";
            playClickSound();
        }

        int clicked_recipe = getClickedRecipe(mx, my, x + 44, y + 16, 7);
        if(clicked_recipe >= 0 && button == VoyagerGuiUtil.LEFT_CLICK)
        {
            List<GolemProduceRecipe> golem_recipes = getDisplayedRecipes();
            this.selected_recipe_id = golem_recipes.get(clicked_recipe).getId().toString(); // for client side update shenanigans
            handleButtonClick(999 + clicked_recipe);
            playClickSound();
        }

        return super.mouseClicked(mx, my, button);
    }

    private void handleButtonClick(int i) {
        Minecraft.getInstance().gameMode.handleInventoryButtonClick(
                menu.containerId,
                i
        ); // server code is so odd

    }

    private static void playClickSound() {
        Minecraft.getInstance().level.playLocalSound(
                Minecraft.getInstance().player.getX(),
                Minecraft.getInstance().player.getY(),
                Minecraft.getInstance().player.getZ(),
                SoundEvents.UI_BUTTON_CLICK.get(),
                SoundSource.MASTER,
                1.0F,
                1.0F,
                false
        );
    }


    private void renderRecipeHelpInfo(GuiGraphics guiGraphics, int x, int y, int mx, int my)
    {
        List<Component> text = List.of(
                Component.literal(
                        "What am I looking at?"
                ),
                Component.literal(
                        "Each button you see here is a recipe:"
                ),
                Component.literal("§7Every golem has it's own set of §frecipes.")
                        .append(Component.literal("\n§7Each §frecipe§7 has a §elevel requirement§7 from the golem, and a §2block requirement."))
                        .append(Component.literal("\n§fHovering over a recipe§7 button shows more info:"))
                        .append(Component.literal("\n§7 - §aOutputs§7 are labeled in the item slots §fon hover"))
                        .append(Component.literal("\n§7 - §2Block requirement§7 is labeled in §2green §fon hover"))
                        .append(Component.literal("\n§7For a §frecipe to run§f, the §2required block§f must be placed next to the golem's den."))
                        .append(Component.literal("\n"))
                        .append(Component.literal("\n§7Only §cunlocked recipes§7 will show in this screen.")));

        VoyagerGraphicsConstants.infoButtonGreen(guiGraphics, x, y);
        VoyagerGraphicsConstants.renderInfoHover(guiGraphics, font, x, y, mx, my, text);
    }

    private void renderOptionsTextOverlay(GuiGraphics guiGraphics, String buttonText, int x, int y)
    {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 999); // so text is in front
        guiGraphics.drawString(
                this.font,
                buttonText,
                x + 10,
                y + 1,
                0xFFFFFF,
                true
        );
        guiGraphics.pose().popPose();
    }

    private int getClickedRecipe(double mouseX, double mouseY, int x, int y, int cols) {
        List<GolemProduceRecipe> recipes = getDisplayedRecipes();

        for (int i = 0; i < recipes.size(); i++) {
            int recipeX = x + (18 * (i % cols));
            int recipeY = y + (18 * (i / cols));

            if (VoyagerGuiUtil.isMouseInBounds(
                    (int) mouseX, (int) mouseY,
                    recipeX, recipeX + 16,
                    recipeY, recipeY + 16
            )) {
                return i;
            }
        }

        return -1;
    }

    private List<GolemProduceRecipe> getDisplayedRecipes() {
        GolemItem golem_item = (GolemItem) menu.GOLEM_STACK.getItem();

        return Minecraft.getInstance().level.getRecipeManager()
                .getAllRecipesFor(VoyagerRecipes.GOLEM_PRODUCTION_TYPE)
                .stream()
                .filter(recipe ->
                        recipe.getGolemItem() == golem_item
                                && golem_item.canUseRecipe(menu.GOLEM_STACK, recipe))
                .sorted(Comparator.comparing(recipe -> recipe.getId().toString()))
                .toList();
    }
}
