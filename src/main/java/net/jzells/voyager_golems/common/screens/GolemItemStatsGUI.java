package net.jzells.voyager_golems.common.screens;

import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.jzells.voyager_golems.common.menus.GolemItemMenu;
import net.jzells.voyager_golems.common.menus.GolemItemStatsMenu;
import net.jzells.voyager_golems.util.VoyagerGuiUtil;
import net.jzells.voyager_golems.util.gui.VoyagerGraphicsConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

import static net.jzells.voyager_golems.common.screens.GolemItemGUI.*;
import static net.jzells.voyager_golems.util.VoyagerGuiUtil.playClickSound;

public class GolemItemStatsGUI extends AbstractContainerScreen<GolemItemStatsMenu> {

    public GolemItemStatsGUI(GolemItemStatsMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.imageWidth = 176;
        this.imageHeight = 174;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                "voyager_golems",
                "textures/gui/golem_item_stats.png"
        );

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

//        renderBackground(pGuiGraphics,pMouseX,pMouseY,pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        pGuiGraphics.blit(
                texture,
                x,
                y,
                0,
                0,
                imageWidth,
                imageHeight,
                176,
                174
        );
        GolemItem golem_item = (GolemItem) menu.GOLEM_STACK.getItem();
        float prestige_percentage = (float )golem_item.getPrestigeCount(menu.GOLEM_STACK) / 5.0f;
        renderPrestigePanel(pGuiGraphics, x + this.imageWidth + 1, y, pMouseX, pMouseY, prestige_percentage);


        renderSmallSideExtraStatsPanel(pGuiGraphics, x - 85 - 1, y, menu);
        renderHomeScreen(pGuiGraphics, pMouseX, pMouseY, x, y, 1f);
    }

    private static GolemItem getGolemItem(GolemItemStatsMenu menu) {
        ItemStack stack = menu.getGolemStack();

        if (stack.getItem() instanceof GolemItem golemItem) {
            return golemItem;
        }

        return null;
    }

    public static void renderSmallSideExtraStatsPanel(GuiGraphics guiGraphics, int x, int y, GolemItemStatsMenu menu)
    {
        Font font = Minecraft.getInstance().font;
        int text_x = x + 6;
        int text_y = y + 10;
        VoyagerGraphicsConstants.smallSidePanel(guiGraphics, x, y);
        int total_upgrades = getGolemItem(menu).getLevel(menu.getGolemStack()) - getGolemItem(menu).getAvailableUpgrades(menu.getGolemStack());
        int upgrade_pounts = getGolemItem(menu).getAvailableUpgrades(menu.getGolemStack());
        float xp_multiplier = ((float)getGolemItem(menu).getXpPrestigeMult(menu.getGolemStack())) * (getGolemItem(menu).getPrestigeCount(menu.getGolemStack()) > 0 ? 2 : 1);
        guiGraphics.drawString(font, "Lvls Avail.: " + upgrade_pounts, text_x, text_y, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Lvls Spent: " + total_upgrades, text_x, text_y + 10, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "XP Mult: " + xp_multiplier, text_x, text_y + 20, Color.darkGray.getRGB(), false);

    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(font, playerInventoryTitle, 7, 81, 0x404040, false);
        pGuiGraphics.drawString(font, title, 7, 6, 0x404040, false);

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
        renderItemInFrame(frame_x, frame_y, menu.GOLEM_STACK, guiGraphics);
        renderStats(x + 97,y +  15, x, y, guiGraphics);

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



    }

    private void renderStats(int x, int y, int wX, int wY, GuiGraphics guiGraphics)
    {
//         0x404040
        GolemItem golem_item = (GolemItem) menu.GOLEM_STACK.getItem();
        guiGraphics.drawString(font, "Group: " + golem_item.GROUPING, x, y, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Type: " + golem_item.TYPE, x, y + 10, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Tier: " + golem_item.TIER, x, y + 20, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Size: " + String.format("%.2f", golem_item.getSize(menu.GOLEM_STACK)),
                x, y + 30, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Potency: " + String.format("%.2f", golem_item.getPotency(menu.GOLEM_STACK)),
                x, y + 40, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Level: " + golem_item.getLevel(menu.GOLEM_STACK), x, y+50, Color.darkGray.getRGB(), false);

        float curr_xp = (float) golem_item.getCurrentXPValue(menu.GOLEM_STACK);
        float level_xp = (float) golem_item.getXPToLevelValue(menu.GOLEM_STACK);
        float xp_percentage = level_xp > 0 ? /* you never know */ curr_xp / level_xp : 0f;
        xp_percentage = Mth.clamp(xp_percentage, 0.0f, 1.0f); // not sure if i need it.. but example online use it :shrug:

        int xp_bar_max_width = 72;
        int xp_bar_height = 6;
        int xp_bar_to_draw_width = (int) (xp_percentage * xp_bar_max_width);
//        xp_bar_to_draw_width = xp_bar_max_width;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0f, 0f, 10f);

        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(
                "voyager_golems",
                "textures/gui/golem_xp_bar.png"
        ), wX + 97, wY + 73, 0, 0,
                xp_bar_to_draw_width, xp_bar_height, xp_bar_max_width, xp_bar_height
                );

        guiGraphics.pose().popPose();

    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0,0,-1);
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.pose().popPose();
    }

//    @Override
//    public void renderBackground(GuiGraphics pGuiGraphics) {
//        pGuiGraphics.pose().pushPose();
//        pGuiGraphics.pose().translate(0,0,-1);
//        super.renderBackground(pGuiGraphics);
//        pGuiGraphics.pose().popPose();
//    }

    private void renderItemInFrame(int x, int y, ItemStack stack, GuiGraphics guiGraphics) {
        float scale = 3.75F;
        int frameSize = 64;
        int itemSize = 16;
        int scaledSize = (int) (itemSize * scale);

        int centeredX = x + (frameSize - scaledSize) / 2;
        int centeredY = y + (frameSize - scaledSize) / 2;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(centeredX, centeredY, 0);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.renderItem(stack, 0, 0);
        guiGraphics.pose().popPose();
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
//            handleButtonClick(STATS_BUTTON_PID);
//            playClickSound();
            VoyagerGuiUtil.playErrorSound();
        }

        if(VoyagerGuiUtil.isMouseInBounds((int) mx, (int) my, x + RECIPES_BUTTON_X,  x + RECIPES_BUTTON_X + 17, y + RECIPES_BUTTON_Y, y + RECIPES_BUTTON_Y + 17) && button == VoyagerGuiUtil.LEFT_CLICK)
        {
            handleButtonClick(RECIPES_BUTTON_PID);
            playClickSound();
        }
        if(VoyagerGuiUtil.isMouseInBounds((int) mx, (int) my, x + HOME_BUTTON_X,  x + HOME_BUTTON_X + 17, y + HOME_BUTTON_Y, y + HOME_BUTTON_Y + 6) && button == VoyagerGuiUtil.LEFT_CLICK)
        {
            handleButtonClick(HOME_BUTTON_PID);
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
}
