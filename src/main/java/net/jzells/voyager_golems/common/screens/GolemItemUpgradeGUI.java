package net.jzells.voyager_golems.common.screens;

import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.jzells.voyager_golems.common.menus.GolemItemUpgradeMenu;
import net.jzells.voyager_golems.util.Constants;
import net.jzells.voyager_golems.util.VoyagerGuiUtil;
import net.jzells.voyager_golems.util.gui.VoyagerGraphicsConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

import static net.jzells.voyager_golems.common.screens.GolemItemGUI.*;
import static net.jzells.voyager_golems.util.VoyagerGuiUtil.*;

public class GolemItemUpgradeGUI extends AbstractContainerScreen<GolemItemUpgradeMenu> {


    public static final int SIZE_UPGRADE_BUTTON_X = 97;
    public static final int SIZE_UPGRADE_BUTTON_Y = 49;
    public static final int SIZE_UPGRADE_BUTTON_PID = 101;

    public static final int POTENCY_UPGRADE_BUTTON_X = 129;
    public static final int POTENCY_UPGRADE_BUTTON_Y = 49;
    public static final int POTENCY_UPGRADE_BUTTON_PID = 102;

    public static final int PRESTIGE_BUTTON_X = 152;
    public static final int PRESTIGE_BUTTON_Y = 49;
    public static final int PRESTIGE_BUTTON_PID = 103;

    public static final String SIZE_UPGRADE_BUTTON_TEXT_PASS = "Size";
    public static final String SIZE_UPGRADE_BUTTON_TEXT_FAIL_LEVEL = "No more level points to spend";
    public static final String SIZE_UPGRADE_BUTTON_TEXT_FAIL_ITEM = "Not enough elixir";

    public static final String POTENCY_UPGRADE_BUTTON_TEXT_PASS = "Potency";
    public static final String POTENCY_UPGRADE_BUTTON_TEXT_FAIL_LEVEL = "No more level points to spend";
    public static final String POTENCY_UPGRADE_BUTTON_TEXT_FAIL_ITEM = "Not enough elixir";

    public static final String PRESTIGE_BUTTON_TEXT_PASS = "+1 Prestige";
    public static final String PRESTIGE_BUTTON_TEXT_FAIL_LEVEL = "No more level points to spend";

    private ItemStack GOLEM_STACK;

    // reminder to add a client side item stack for the want. ugh. so stupid.

    // OR add update() function that passes in new stack, and menu info!!!




    private GolemItem getGolemItem() {
        ItemStack stack = menu.getGolemStack();

        if (stack.getItem() instanceof GolemItem golemItem) {
            return golemItem;
        }

        return null;
    }





    private static String UPGRADE_BUTTON_TEXT_ELIXIR_WANT(ItemStack stack)
    {
        return "Spend " + stack.getCount() + " " + stack.getItem().getName(stack).getString();
    }

    public GolemItemUpgradeGUI(GolemItemUpgradeMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.imageWidth = 176;
        this.imageHeight = 174;



    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                "voyager_golems",
                "textures/gui/golem_item_upgrade.png"
        );

        renderBackground(pGuiGraphics);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);



        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        ItemStack golem_stack = menu.getGolemStack();

        renderStatsPanel(pGuiGraphics, x - 85 - 1, y, golem_stack, getGolemItem().getSize(golem_stack), getGolemItem().getPotency(golem_stack), getGolemItem().getLevel(golem_stack));

        float prestige_percentage = getGolemItem().getPrestigeCount(golem_stack) / 5.0f;



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



        renderHomeScreen(pGuiGraphics, pMouseX, pMouseY, x, y, 1f);
        renderPrestigePanel(pGuiGraphics, x + this.imageWidth + 1, y, pMouseX, pMouseY, prestige_percentage);
        renderSmallSideExtraStatsPanel(pGuiGraphics, x - 85 - 1, y + 86, menu);
    }

    private static GolemItem getGolemItem(GolemItemUpgradeMenu menu) {
        ItemStack stack = menu.getGolemStack();

        if (stack.getItem() instanceof GolemItem golemItem) {
            return golemItem;
        }

        return null;
    }

    public static void renderSmallSideExtraStatsPanel(GuiGraphics guiGraphics, int x, int y, GolemItemUpgradeMenu menu)
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
        pGuiGraphics.drawString(font, "Upgrade Golem", 97, 6, 0x404040, false);
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
        renderItemInFrame(frame_x, frame_y, menu.getGolemStack(), guiGraphics);


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
                x + SIZE_UPGRADE_BUTTON_X, x + SIZE_UPGRADE_BUTTON_X + 17,
                y + SIZE_UPGRADE_BUTTON_Y, y + SIZE_UPGRADE_BUTTON_Y + 17)) {
            if(menu.canSpendElixir(Constants.INCREASE_SIZE_GOLEM_OPERATION) && getGolemItem().getAvailableUpgrades(menu.getGolemStack()) > 0)
            {
                renderOptionsTextOverlay(guiGraphics, UPGRADE_BUTTON_TEXT_ELIXIR_WANT(menu.getElixirAskStack(Constants.INCREASE_SIZE_GOLEM_OPERATION)) + " to increase " + SIZE_UPGRADE_BUTTON_TEXT_PASS, mouseX, mouseY, Color.GREEN.getRGB());
            } else if (getGolemItem().getAvailableUpgrades(menu.getGolemStack()) <= 0) {
                renderOptionsTextOverlay(guiGraphics, SIZE_UPGRADE_BUTTON_TEXT_FAIL_LEVEL, mouseX, mouseY, Color.RED.getRGB());
            } else
            {
                renderOptionsTextOverlay(guiGraphics, SIZE_UPGRADE_BUTTON_TEXT_FAIL_ITEM, mouseX, mouseY, Color.RED.getRGB());
            }

        }
        if (VoyagerGuiUtil.isMouseInBounds(mouseX, mouseY,
                x + POTENCY_UPGRADE_BUTTON_X, x + POTENCY_UPGRADE_BUTTON_X + 17,
                y + POTENCY_UPGRADE_BUTTON_Y, y + POTENCY_UPGRADE_BUTTON_Y + 17)) {
            if(menu.canSpendElixir(Constants.INCREASE_POTENCY_GOLEM_OPERATION) && getGolemItem().getAvailableUpgrades(menu.getGolemStack()) > 0)
            {
                renderOptionsTextOverlay(guiGraphics, UPGRADE_BUTTON_TEXT_ELIXIR_WANT(menu.getElixirAskStack(Constants.INCREASE_POTENCY_GOLEM_OPERATION)) + " to increase " + POTENCY_UPGRADE_BUTTON_TEXT_PASS, mouseX, mouseY, Color.GREEN.getRGB());
            } else if (getGolemItem().getAvailableUpgrades(menu.getGolemStack()) <= 0) {
                renderOptionsTextOverlay(guiGraphics, POTENCY_UPGRADE_BUTTON_TEXT_FAIL_LEVEL, mouseX, mouseY, Color.RED.getRGB());
            } else
            {
                renderOptionsTextOverlay(guiGraphics, POTENCY_UPGRADE_BUTTON_TEXT_FAIL_ITEM, mouseX, mouseY, Color.RED.getRGB());
            }

        }
        if (VoyagerGuiUtil.isMouseInBounds(mouseX, mouseY,
                x + PRESTIGE_BUTTON_X, x + PRESTIGE_BUTTON_X + 17,
                y + PRESTIGE_BUTTON_Y, y + PRESTIGE_BUTTON_Y + 17)) {
            if(menu.canPrestige(getGolemItem().getPrestigeCount(menu.getGolemStack())) && getGolemItem().getAvailableUpgrades(menu.getGolemStack()) > 0)
            {
                renderOptionsTextOverlay(guiGraphics, UPGRADE_BUTTON_TEXT_ELIXIR_WANT(Constants.GOLEM_PRESIGE_WANTS(getGolemItem().getPrestigeCount(menu.getGolemStack()))) + " for " + PRESTIGE_BUTTON_TEXT_PASS, mouseX, mouseY, Color.GREEN.getRGB());
            } else if (getGolemItem().getAvailableUpgrades(menu.getGolemStack()) <= 0) {
                renderOptionsTextOverlay(guiGraphics, POTENCY_UPGRADE_BUTTON_TEXT_FAIL_LEVEL, mouseX, mouseY, Color.RED.getRGB());
            } else
            {
                renderOptionsTextOverlay(guiGraphics, "Prestige Item Not Found in Inventory", mouseX, mouseY, Color.RED.getRGB());
            }

        }

    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics) {
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0,0,-1);
        super.renderBackground(pGuiGraphics);
        pGuiGraphics.pose().popPose();
    }

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

    private void renderOptionsTextOverlay(GuiGraphics guiGraphics, String buttonText, int x, int y, int color)
    {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 999); // so text is in front
        guiGraphics.drawString(
                this.font,
                buttonText,
                x + 10,
                y + 1,
                color,
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
//            handleButtonClick(UPGRADE_BUTTON_PID);
//            playClickSound();
            VoyagerGuiUtil.playErrorSound();
        }
        if(VoyagerGuiUtil.isMouseInBounds((int) mx, (int) my, x + STATS_BUTTON_X,  x + STATS_BUTTON_X + 17, y + STATS_BUTTON_Y, y + STATS_BUTTON_Y + 17) && button == VoyagerGuiUtil.LEFT_CLICK)
        {
            handleButtonClick(STATS_BUTTON_PID);
            playClickSound();
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
        if(VoyagerGuiUtil.isMouseInBounds((int) mx, (int) my, x + SIZE_UPGRADE_BUTTON_X,  x + SIZE_UPGRADE_BUTTON_X + 17, y + SIZE_UPGRADE_BUTTON_Y, y + SIZE_UPGRADE_BUTTON_Y + 17) && button == VoyagerGuiUtil.LEFT_CLICK)
        {
            handleButtonClick(SIZE_UPGRADE_BUTTON_PID);
            if (menu.canSpendElixir(Constants.INCREASE_SIZE_GOLEM_OPERATION) && getGolemItem().getAvailableUpgrades(menu.getGolemStack()) > 0) {

                playLevelSound();
            } else {
                VoyagerGuiUtil.playErrorSound();
            }
        }
        if(VoyagerGuiUtil.isMouseInBounds((int) mx, (int) my, x + POTENCY_UPGRADE_BUTTON_X,  x + POTENCY_UPGRADE_BUTTON_X + 17, y + POTENCY_UPGRADE_BUTTON_Y, y + POTENCY_UPGRADE_BUTTON_Y + 17) && button == VoyagerGuiUtil.LEFT_CLICK)
        {
            handleButtonClick(POTENCY_UPGRADE_BUTTON_PID);
            if (menu.canSpendElixir(Constants.INCREASE_POTENCY_GOLEM_OPERATION) && getGolemItem().getAvailableUpgrades(menu.getGolemStack()) > 0) {


                playLevelSound();
            } else {
                VoyagerGuiUtil.playErrorSound();
            }
        }
        if(VoyagerGuiUtil.isMouseInBounds((int) mx, (int) my, x + PRESTIGE_BUTTON_X,  x + PRESTIGE_BUTTON_X + 17, y + PRESTIGE_BUTTON_Y, y + PRESTIGE_BUTTON_Y + 17) && button == VoyagerGuiUtil.LEFT_CLICK)
        {
            handleButtonClick(PRESTIGE_BUTTON_PID);
            if (menu.canPrestige(this.getGolemItem().getPrestigeCount(menu.getGolemStack())) && getGolemItem().getAvailableUpgrades(menu.getGolemStack()) > 0) {

                playPrestigeSound();
            } else {
                VoyagerGuiUtil.playErrorSound();
            }
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
