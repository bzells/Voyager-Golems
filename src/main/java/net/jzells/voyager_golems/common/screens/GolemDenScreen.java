package net.jzells.voyager_golems.common.screens;

import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.jzells.voyager_golems.common.menus.GolemDenMenu;
import net.jzells.voyager_golems.common.menus.SyncedMenu;
import net.jzells.voyager_golems.common.recipes.GolemProduceRecipe;
import net.jzells.voyager_golems.util.VoyagerGuiUtil;
import net.jzells.voyager_golems.util.gui.VoyagerGraphicsConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class GolemDenScreen extends AbstractContainerScreen<GolemDenMenu> {


    public GolemDenScreen(GolemDenMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.imageWidth = 176;
        this.imageHeight = 166; // can tweak these with Gio

    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        ResourceLocation bg_texture =
                menu.getBlockEntity().getDefinition().texture();

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit( // .blit renders stuff. need to figure out more how this works
                bg_texture,
                x,
                y,
                0,
                0,
                imageWidth,
                imageHeight,
                176,
                166
        );

        renderProgressBar(pGuiGraphics, x, y);

    }



    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        renderBackground(pGuiGraphics,pMouseX,pMouseY,pPartialTick);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0,0,100);
        int frame_size = 62;
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        VoyagerGraphicsConstants.renderItemInFrame(x + 8, y + 8, frame_size - 2, menu.getBlockEntity().getGolemStack(), pGuiGraphics);
        pGuiGraphics.pose().popPose();
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    private void renderProgressBar(GuiGraphics guiGraphics, int x, int y)
    {
        int bar_x_pos = x + 71;
        int bar_y_pos = y + 7;

        GolemProduceRecipe recipe = menu.getBlockEntity().getCurrentRunningRecipe();
        float production_points = 10;

        GolemItem golem_item = menu.getBlockEntity().getGolemItem();
        float golem_potency = 1;
        float den_speed_boost = menu.getBlockEntity().getDefinition().speed_boost();
        if(golem_item != null)
        {
            golem_potency = golem_item.getPotency(menu.getBlockEntity().getGolemStack());
        }

        if(recipe != null)
        {
            production_points = (int) Math.max(
                    1,
                    recipe.getProductionPoints()
                            / (golem_potency * den_speed_boost));
        }
        else
        {
            // this is a shitty way to detect if the item has been removed... maybe...

        }



        int current_progress = menu.getRecipeProgress();
        float percentage = (float) current_progress / production_points;


        ResourceLocation empty_bar = ResourceLocation.fromNamespaceAndPath(
                VoyagerGraphicsConstants.VOYAGER_NAMESPACE,
                VoyagerGraphicsConstants.VERTICAL_PROGRESS_BAR_EMPTY_PATH
        );
        ResourceLocation full_bar = ResourceLocation.fromNamespaceAndPath(
                VoyagerGraphicsConstants.VOYAGER_NAMESPACE,
                VoyagerGraphicsConstants.VERTICAL_PROGRESS_BAR_PATH
        );
        VoyagerGuiUtil.renderBottomToTopVertialBarPercentage(guiGraphics, empty_bar, full_bar, percentage, 6, 62, bar_x_pos, bar_y_pos
                );
    }


    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(
                font,
                title,
                8,
                73,
                0xFFFFFF,
                false
        );

        pGuiGraphics.drawString(
                font,
                playerInventoryTitle,
                79,
                73,
                0xFFFFFF,
                false
        );
    }


}
