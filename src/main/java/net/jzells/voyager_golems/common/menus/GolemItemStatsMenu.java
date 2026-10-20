package net.jzells.voyager_golems.common.menus;

import net.jzells.voyager_golems.util.gui.GolemGUISlot;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class GolemItemStatsMenu  extends AbstractContainerMenu {

    private ItemStack menu_stack;
    private final int GOLEM_SLOT;

    public ItemStack getGolemStack() {
        return PLAYER_INVENTORY.getItem(GOLEM_SLOT);
    }

    protected GolemItemStatsMenu(int containerId, Inventory playerInventory) {
        super(VoyagerMenus.GOLEM_ITEM_STATS_MENU.get(), containerId);


        this.GOLEM_SLOT = playerInventory.selected;
        this.PLAYER_INVENTORY = playerInventory;
        this.GOLEM_STACK = playerInventory.getSelected();

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(
                        playerInventory,
                        col + row * 9 + 9,
                        8 + col * 18,
                        92 + row * 18
                ));
            }
        }

        for (int col = 0; col < 9; col++) {
            if(playerInventory.getItem(col).is(this.GOLEM_STACK.getItem()))
            {
                this.addSlot(new GolemGUISlot(
                        playerInventory,
                        col,
                        8 + col * 18,
                        150
                ));
            }
            else
            {
                this.addSlot(new Slot(
                        playerInventory,
                        col,
                        8 + col * 18,
                        150
                ));
            }

        }
    }

    public ItemStack GOLEM_STACK;
    public final Inventory PLAYER_INVENTORY;

    public void onOpen(){}

    public void setUpdated()
    {
        this.GOLEM_STACK = PLAYER_INVENTORY.getSelected();
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public boolean clickMenuButton(Player pPlayer, int pId) {

        if(pPlayer instanceof ServerPlayer server_player)
        {
            if(pId == 0)
            {
                openUpgradeScreen(server_player, this.GOLEM_STACK);
            }
            if(pId == 1)
            {
                openStatsScreen(server_player, this.GOLEM_STACK);
            }
            if(pId == 2)
            {
                openRecipesScreen(server_player, this.GOLEM_STACK);
            }
            if(pId == 3)
            {
                openHomeScreen(server_player, this.GOLEM_STACK);
            }
            return true;
        }

        return false;
    }

    private void openRecipesScreen(ServerPlayer player, ItemStack golem_stack) {
        NetworkHooks.openScreen(
                player,
                new SimpleMenuProvider(
                        (containerId, playerInventory, p) -> {
                            GolemItemRecipesMenu menu = new GolemItemRecipesMenu(
                                    containerId,
                                    playerInventory
                            );

                            menu.onOpen();
                            return menu;
                        },
                        Component.literal(this.GOLEM_STACK.getItem().getName(golem_stack).getString())
                )
        );
    }

    private void openUpgradeScreen(ServerPlayer player, ItemStack golem_stack) {
        NetworkHooks.openScreen(
                player,
                new SimpleMenuProvider(
                        (containerId, playerInventory, p) -> {
                            GolemItemUpgradeMenu menu = new GolemItemUpgradeMenu(
                                    containerId,
                                    playerInventory
                            );

                            menu.onOpen();
                            return menu;
                        },
                        Component.literal(this.GOLEM_STACK.getItem().getName(golem_stack).getString())
                )
        );
    }
    private void openStatsScreen(ServerPlayer player, ItemStack golem_stack) {
        NetworkHooks.openScreen(
                player,
                new SimpleMenuProvider(
                        (containerId, playerInventory, p) -> {
                            GolemItemStatsMenu menu = new GolemItemStatsMenu(
                                    containerId,
                                    playerInventory
                            );

                            menu.onOpen();
                            return menu;
                        },
                        Component.literal(this.GOLEM_STACK.getItem().getName(golem_stack).getString())
                )
        );
    }

    private void openHomeScreen(ServerPlayer player, ItemStack golem_stack) {
        NetworkHooks.openScreen(
                player,
                new SimpleMenuProvider(
                        (containerId, playerInventory, p) -> {
                            GolemItemMenu menu = new GolemItemMenu(
                                    containerId,
                                    playerInventory
                            );

                            menu.onOpen();
                            return menu;
                        },
                        Component.literal(this.GOLEM_STACK.getItem().getName(golem_stack).getString())
                )
        );
    }


}
