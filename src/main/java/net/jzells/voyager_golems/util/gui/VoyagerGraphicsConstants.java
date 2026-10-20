package net.jzells.voyager_golems.util.gui;

import net.jzells.voyager_golems.util.VoyagerGuiUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class VoyagerGraphicsConstants {

    public static final String VOYAGER_NAMESPACE = "voyager_golems";
    public static final String GUI_PATH = "textures/gui/";
    public static final String RED_BUTTON_PATH = GUI_PATH + "red_button.png";
    public static final String INFO_ICON_PATH = GUI_PATH + "information_icon.png";
    public static final String GOLDEN_SIDE_PANEL_PATH = GUI_PATH + "golden_panel.png";
    public static final String PRESTIGE_BAR_EMPTY_PATH = GUI_PATH + "golem_prestige_bar_empty.png";
    public static final String PRESTIGE_BAR_PATH = GUI_PATH + "golem_prestige_bar.png";
    public static final String GOLEM_STATS_PANEL_PATH = GUI_PATH + "golem_stats_panel.png";
    public static final String VERTICAL_PROGRESS_BAR_EMPTY_PATH = GUI_PATH + "golem_progress_bar_empty.png";
    public static final String VERTICAL_PROGRESS_BAR_PATH = GUI_PATH + "golem_progress_bar.png";
    public static final String GREEN_BUTTON_PATH = GUI_PATH + "green_button.png";
    public static final ResourceLocation JEI_GOLEM_PROGRESS_BAR_EMPTY = getVoyagerTexture(GUI_PATH + "jei_golem_progress_bar_empty.png");
    public static final ResourceLocation JEI_GOLEM_PROGRESS_BAR = getVoyagerTexture(GUI_PATH + "jei_golem_progress_bar.png");
    public static final ResourceLocation SMALL_PANEL = getVoyagerTexture(GUI_PATH + "small_side_panel.png");

    public static final void infoButton(GuiGraphics guiGraphics, int pX, int pY)
    {
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(VOYAGER_NAMESPACE,
                        RED_BUTTON_PATH), pX, pY, 0, 0, 0, 18, 18, 18, 18
        );
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(VOYAGER_NAMESPACE,
                        INFO_ICON_PATH), pX, pY, 0, 0, 0, 18, 18, 18, 18
        );
    }
    public static final void infoButtonGreen(GuiGraphics guiGraphics, int pX, int pY)
    {
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(VOYAGER_NAMESPACE,
                        GREEN_BUTTON_PATH), pX, pY, 0, 0, 0, 18, 18, 18, 18
        );
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(VOYAGER_NAMESPACE,
                        INFO_ICON_PATH), pX, pY, 0, 0, 0, 18, 18, 18, 18
        );
    }

    public static void goldenSidePanel(GuiGraphics guiGraphics, int pX, int pY)
    {
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(VOYAGER_NAMESPACE,
                        GOLDEN_SIDE_PANEL_PATH), pX, pY, 0, 0, 0, 32, 174, 32, 174
        );
    }

    public static void smallSidePanel(GuiGraphics guiGraphics, int pX, int pY)
    {
        guiGraphics.blit(
                SMALL_PANEL, pX, pY, 0, 0, 0, 85, 60, 85, 60
        );
    }
    public static void renderInfoHover(GuiGraphics guiGraphics, Font font, int buttonX, int buttonY, int mx, int my, List<Component> componentText)
    {
        if(VoyagerGuiUtil.isMouseInBounds(mx, my, buttonX, buttonX + 18, buttonY, buttonY + 18))
        {
            guiGraphics.renderComponentTooltip(font, componentText, mx, my);
        }
    }
    public static void renderItemOnTopOf(GuiGraphics guiGraphics, ResourceLocation behind_texture, int tX, int tY, ItemStack stack, int x, int y)
    {
        guiGraphics.blit(behind_texture, x, y, 0, 0, 0, tX, tY, tX, tY);
        guiGraphics.renderItem(
                stack, x + 1, y + 1
        );
    }
    public static ResourceLocation getVoyagerTexture(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(
                VOYAGER_NAMESPACE, path
        );
    }
    public static void renderItemInFrame(int x, int y, int frameSize, ItemStack stack, GuiGraphics guiGraphics) {
        int itemSize = 16;
        float scale = (float) frameSize / (float) itemSize;
        int scaledSize = (int) (itemSize * scale);

        int centeredX = x + (frameSize - scaledSize) / 2;
        int centeredY = y + (frameSize - scaledSize) / 2;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(centeredX, centeredY, 0);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.renderItem(stack, 0, 0);
        guiGraphics.pose().popPose();
    }
}
