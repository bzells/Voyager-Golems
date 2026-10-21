package net.jzells.voyager_golems.common.screens;

import net.jzells.voyager_golems.common.items.VoyagerItems;
import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.jzells.voyager_golems.common.menus.GolemItemMenu;
import net.jzells.voyager_golems.util.VoyagerGuiUtil;
import net.jzells.voyager_golems.util.gui.VoyagerGraphicsConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;


import java.awt.*;
import java.util.List;

import static net.jzells.voyager_golems.util.VoyagerGuiUtil.playClickSound;

public class GolemItemGUI extends AbstractContainerScreen<GolemItemMenu> {

    public static final int UPGRADE_BUTTON_X = 8;
    public static final int UPGRADE_BUTTON_Y = 16;

    public static final int UPGRADE_BUTTON_PID = 0;

    public static final int STATS_BUTTON_X = 8;
    public static final int STATS_BUTTON_Y = 34;

    public static final int STATS_BUTTON_PID = 1;

    public static final int RECIPES_BUTTON_X = 8;
    public static final int RECIPES_BUTTON_Y = 50;
    public static final int RECIPES_BUTTON_PID = 2;

    public static final int HOME_BUTTON_X = 8;
    public static final int HOME_BUTTON_Y = 71;

    public static final int HOME_BUTTON_PID = 3;

    public static final int RESET_RECIPE_BUTTON_X = 44;
    public static final int RESET_RECIPE_BUTTON_Y = 71;

    public static final int RESET_RECIPE_BUTTON_PID = 4;

    public static final String UPGRADE_BUTTON_TEXT = "Upgrade Golem";
    public static final String STATS_BUTTON_TEXT = "View Golem Stats";
    public static final String RECIPES_BUTTON_TEXT = "Change Golem Output";
    public static final String RETURN_BUTTON_TEXT = "Return";
    public static final String RESET_RECIPE_BUTTON_TEXT = "Clear Golem Outputs";

    private static GolemItem getGolemItem(GolemItemMenu menu) {
        ItemStack stack = menu.getGolemStack();

        if (stack.getItem() instanceof GolemItem golemItem) {
            return golemItem;
        }

        return null;
    }

    public static void renderPrestigePanel(GuiGraphics guiGraphics, int x, int y, int mx, int my, float percentage)
    {
        VoyagerGraphicsConstants.goldenSidePanel(guiGraphics, x, y);
        VoyagerGuiUtil.renderBottomToTopVertialBarPercentage(guiGraphics,
                ResourceLocation.fromNamespaceAndPath(
                        VoyagerGraphicsConstants.VOYAGER_NAMESPACE,
                        VoyagerGraphicsConstants.PRESTIGE_BAR_EMPTY_PATH
                ),
                ResourceLocation.fromNamespaceAndPath(
                        VoyagerGraphicsConstants.VOYAGER_NAMESPACE,
                        VoyagerGraphicsConstants.PRESTIGE_BAR_PATH
                ), percentage, 22, 162, x + 5, y + 5);
        List<Component> componentList = List.of(
                Component.literal(
                        "Prestige:"
                ),
                Component.literal(
                        "Each prestige point gives golem a certain bonus:"
                ),
                Component.literal("§7 0: No Bonus")
                        .append(Component.literal("\n§7 1: §e2x§7 §axp bonus"))
                        .append(Component.literal("\n§7 2: §e2x§7 more §dsize §fon level up"))
                        .append(Component.literal("\n§7 3: §e2x§7 more §cpotency §fon level up"))
                        .append(Component.literal("\n§7 4: §bAscends§7: Multiply §dsize §fand §cpotency §7by §e4§f, and gain 10 levels"))
                        .append(Component.literal("\n§7 5: §cOblivion§7: §4Resets golem level to 10§7, resets stats, but §6keeps prestige points."))
                        .append(Component.literal("\n\n§cOblivion§7 applies §bAscension§7 bonuses §fon level up§7 instead of applying once "))
                        .append(Component.literal("\n§cOblivion§7 grants an §axp boost §emultiplier §f= (level + total upgrades done) * tier")));



        VoyagerGraphicsConstants.infoButton(guiGraphics, x + 33, y);
        VoyagerGraphicsConstants.renderInfoHover(guiGraphics, Minecraft.getInstance().font, x + 33, y, mx, my, componentList
                );
    }

    public static void renderSmallSideExtraStatsPanel(GuiGraphics guiGraphics, int x, int y, GolemItemMenu menu)
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

    public static void renderStatsPanel(GuiGraphics guiGraphics, int x, int y, ItemStack golem_stack)
    {
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(VoyagerGraphicsConstants.VOYAGER_NAMESPACE,
                        VoyagerGraphicsConstants.GOLEM_STATS_PANEL_PATH), x, y, 0, 0, 0, 85, 85, 85, 85
        );


        Font font = Minecraft.getInstance().font;

        GolemItem golem_item = (GolemItem) golem_stack.getItem();

        int text_x = x + 6;
        int text_y = y + 10;

        guiGraphics.drawString(font, "Group: " + golem_item.GROUPING, text_x, text_y, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Type: " + golem_item.TYPE, text_x, text_y + 10, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Tier: " + golem_item.TIER, text_x, text_y + 20, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Size: " + String.format("%.2f", golem_item.getSize(golem_stack)),
                text_x, text_y + 30, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Potency: " + String.format("%.2f", golem_item.getPotency(golem_stack)),
                text_x, text_y + 40, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Level: " + golem_item.getLevel(golem_stack), text_x, text_y+50, Color.darkGray.getRGB(), false);

        float curr_xp = (float) golem_item.getCurrentXPValue(golem_stack);
        float level_xp = (float) golem_item.getXPToLevelValue(golem_stack);
        float xp_percentage = level_xp > 0 ? /* you never know */ curr_xp / level_xp : 0f;
        xp_percentage = Mth.clamp(xp_percentage, 0.0f, 1.0f); // not sure if i need it.. but example online use it :shrug:

        int xp_bar_max_width = 72;
        int xp_bar_height = 6;
        int xp_bar_to_draw_width = (int) (xp_percentage * xp_bar_max_width);
//        xp_bar_to_draw_width = xp_bar_max_width;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0f, 0f, 10f);



        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(
                        VoyagerGraphicsConstants.VOYAGER_NAMESPACE,
                        "textures/gui/golem_xp_bar.png"
                ), x + 6, y + 73, 0, 0,
                xp_bar_to_draw_width, xp_bar_height, xp_bar_max_width, xp_bar_height
        );

        guiGraphics.pose().popPose();
    }

    public static void renderStatsPanel(GuiGraphics guiGraphics, int x, int y, ItemStack golem_stack, float manual_size, float manual_potency, int manual_level)
    {
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(VoyagerGraphicsConstants.VOYAGER_NAMESPACE,
                        VoyagerGraphicsConstants.GOLEM_STATS_PANEL_PATH), x, y, 0, 0, 0, 85, 85, 85, 85
        );


        Font font = Minecraft.getInstance().font;

        GolemItem golem_item = (GolemItem) golem_stack.getItem();

        int text_x = x + 6;
        int text_y = y + 10;

        guiGraphics.drawString(font, "Group: " + golem_item.GROUPING, text_x, text_y, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Type: " + golem_item.TYPE, text_x, text_y + 10, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Tier: " + golem_item.TIER, text_x, text_y + 20, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Size: " + String.format("%.2f", manual_size),
                text_x, text_y + 30, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Potency: " + String.format("%.2f", manual_potency),
                text_x, text_y + 40, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(font, "Level: " + manual_level, text_x, text_y+50, Color.darkGray.getRGB(), false);

        float curr_xp = (float) golem_item.getCurrentXPValue(golem_stack);
        float level_xp = (float) golem_item.getXPToLevelValue(golem_stack);
        float xp_percentage = level_xp > 0 ? /* you never know */ curr_xp / level_xp : 0f;
        xp_percentage = Mth.clamp(xp_percentage, 0.0f, 1.0f); // not sure if i need it.. but example online use it :shrug:

        int xp_bar_max_width = 72;
        int xp_bar_height = 6;
        int xp_bar_to_draw_width = (int) (xp_percentage * xp_bar_max_width);
//        xp_bar_to_draw_width = xp_bar_max_width;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0f, 0f, 10f);



        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(
                        VoyagerGraphicsConstants.VOYAGER_NAMESPACE,
                        "textures/gui/golem_xp_bar.png"
                ), x + 6, y + 73, 0, 0,
                xp_bar_to_draw_width, xp_bar_height, xp_bar_max_width, xp_bar_height
        );

        guiGraphics.pose().popPose();
    }

    public GolemItemGUI(
            GolemItemMenu menu,
            Inventory playerInventory,
            Component title
    ) {
        super(menu, playerInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 174;



    }


    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(font, playerInventoryTitle, 7, 81, 0x404040, false);
        pGuiGraphics.drawString(font, title, 7, 6, 0x404040, false);
        pGuiGraphics.drawString(font, "Current Output", 97, 6, 0x404040, false);
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
                "textures/gui/golem_item.png"
        );

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;



//        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        GolemItem golem_item = (GolemItem) menu.GOLEM_STACK.getItem();
        float prestige_percentage = (float )golem_item.getPrestigeCount(menu.GOLEM_STACK) / 5.0f;
        renderPrestigePanel(guiGraphics, x + this.imageWidth + 1, y, mouseX, mouseY, prestige_percentage);
        renderStatsPanel(guiGraphics, x - 85 - 1, y, menu.GOLEM_STACK);

        guiGraphics.blit(
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



        renderHomeScreen(guiGraphics, mouseX, mouseY, x, y, 1f);
        renderSmallSideExtraStatsPanel(guiGraphics, x - 85 - 1, y + 86, menu);
        renderTooltip(guiGraphics, mouseX, mouseY);




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
            handleButtonClick(RECIPES_BUTTON_PID);
            playClickSound();
        }
        if(VoyagerGuiUtil.isMouseInBounds((int) mx, (int) my, x + HOME_BUTTON_X,  x + HOME_BUTTON_X + 17, y + HOME_BUTTON_Y, y + HOME_BUTTON_Y + 6) && button == VoyagerGuiUtil.LEFT_CLICK)
        {
//            handleButtonClick(HOME_BUTTON_PID);
            VoyagerGuiUtil.playErrorSound();
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
                SoundEvents.UI_BUTTON_CLICK.value(),
                SoundSource.MASTER,
                1.0F,
                1.0F,
                false
        );
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
}