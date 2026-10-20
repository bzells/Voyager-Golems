package net.jzells.voyager_golems.common.menus;

import net.jzells.voyager_golems.common.screens.ISyncedScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SyncedMenu extends AbstractContainerMenu {

    protected final ISyncedScreen syncedScreen;

    protected SyncedMenu(@Nullable MenuType<?> pMenuType, int pContainerId, ISyncedScreen syncedScreen) {
        super(pMenuType, pContainerId);
        this.syncedScreen = syncedScreen;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    public void updateScreen()
    {
        syncedScreen.update(this);
    }

    @Override
    public boolean clickMenuButton(Player pPlayer, int pId) {
        updateScreen();
        return super.clickMenuButton(pPlayer, pId);
    }
}
