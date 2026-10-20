package net.jzells.voyager_golems.common.screens;

import net.jzells.voyager_golems.common.menus.GolemDenMenu;
import net.jzells.voyager_golems.common.menus.SyncedMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class SyncedScreen extends AbstractContainerScreen<SyncedMenu> implements ISyncedScreen{
    public SyncedScreen(SyncedMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
    }

    @Override
    abstract public void update(AbstractContainerMenu syncedMenu);
}
