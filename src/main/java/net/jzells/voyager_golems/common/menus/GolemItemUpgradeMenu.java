package net.jzells.voyager_golems.common.menus;

import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.jzells.voyager_golems.common.screens.GolemItemRecipesGUI;
import net.jzells.voyager_golems.common.screens.GolemItemUpgradeGUI;
import net.jzells.voyager_golems.util.Constants;
import net.jzells.voyager_golems.util.gui.DummySlot;
import net.jzells.voyager_golems.util.gui.GolemGUISlot;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class GolemItemUpgradeMenu extends AbstractContainerMenu {



    public ItemStack GOLEM_STACK;
    public final Inventory PLAYER_INVENTORY;

    private final Container INPUT_INVENTORY;

    public static final int SIZE_ELIXIR_SLOT = 0;
    public static final int POTENCY_ELIXIR_SLOT = 1;
    public static final int PRESTIGE_SLOT = 2;

    public final int GOLEM_SLOT;

    public ItemStack getElixirAskStack(int operation)
    {
        return switch (operation) {
            case Constants.INCREASE_SIZE_GOLEM_OPERATION -> this.INPUT_INVENTORY.getItem(SIZE_ELIXIR_SLOT);
            case Constants.INCREASE_POTENCY_GOLEM_OPERATION -> this.INPUT_INVENTORY.getItem(POTENCY_ELIXIR_SLOT);
            default -> ItemStack.EMPTY;
        };

    }


    public ItemStack getGolemStack() {
        return PLAYER_INVENTORY.getItem(GOLEM_SLOT);
    }



    public GolemItemUpgradeMenu(int containerId, Inventory playerInventory) {
        super(VoyagerMenus.GOLEM_ITEM_UPGRADE_MENU.get(), containerId);

        this.GOLEM_STACK = playerInventory.getSelected();
        this.PLAYER_INVENTORY = playerInventory;
        this.GOLEM_SLOT = playerInventory.selected;
        this.INPUT_INVENTORY = new SimpleContainer(3);

        // input slots
        for (int row = 0; row < 3; row++) {

                int index = row;
                this.addSlot(new DummySlot(
                        INPUT_INVENTORY,
                        index,
                        98 + row * 27,
                        16
                ));

        }


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

    public void onOpen()
    {
        askForElixir();
        askForPrestige();
    }

    public void askForElixir()
    {
        GolemItem golem_item = (GolemItem) this.GOLEM_STACK.getItem();
        this.INPUT_INVENTORY.setItem(SIZE_ELIXIR_SLOT, Constants.GOLEM_ELIXIR_ITEMSTACK_FORMULA(Constants.INCREASE_SIZE_GOLEM_OPERATION, golem_item.getSizeUpgradeTally(this.GOLEM_STACK)));
        this.INPUT_INVENTORY.setItem(POTENCY_ELIXIR_SLOT, Constants.GOLEM_ELIXIR_ITEMSTACK_FORMULA(Constants.INCREASE_POTENCY_GOLEM_OPERATION, golem_item.getPotencyUpgradeTally(this.GOLEM_STACK)));
        INPUT_INVENTORY.setChanged();
    }
    public void askForPrestige()
    {
        this.INPUT_INVENTORY.setItem(PRESTIGE_SLOT, Constants.GOLEM_PRESIGE_WANTS(this.getGolemItem().getPrestigeCount(this.GOLEM_STACK)));
        INPUT_INVENTORY.setChanged();
    }

    public boolean canSpendElixir(int operation)
    {
        GolemItem golem_item = (GolemItem) this.GOLEM_STACK.getItem();
        return switch (operation) {
            case Constants.INCREASE_SIZE_GOLEM_OPERATION -> {
                Item elixir_item = Constants.GOLEM_ELIXIR_ITEMSTACK_FORMULA(Constants.INCREASE_SIZE_GOLEM_OPERATION, golem_item.getSizeUpgradeTally(this.GOLEM_STACK)).getItem();
                yield this.PLAYER_INVENTORY.countItem(elixir_item) >= this.INPUT_INVENTORY.getItem(SIZE_ELIXIR_SLOT).getCount() && this.getGolemItem().getAvailableUpgrades(this.GOLEM_STACK) > 0;
            }
            case Constants.INCREASE_POTENCY_GOLEM_OPERATION -> {
                Item elixir_item = Constants.GOLEM_ELIXIR_ITEMSTACK_FORMULA(Constants.INCREASE_POTENCY_GOLEM_OPERATION, golem_item.getPotencyUpgradeTally(this.GOLEM_STACK)).getItem();
                yield this.PLAYER_INVENTORY.countItem(elixir_item) >= this.INPUT_INVENTORY.getItem(POTENCY_ELIXIR_SLOT).getCount() && this.getGolemItem().getAvailableUpgrades(this.GOLEM_STACK) > 0;
            }
            default -> false;
        };

    }

    public boolean canPrestige(int prestige_count)
    {
        Item prestige_item = Constants.GOLEM_PRESIGE_WANTS(prestige_count).getItem();
        return this.PLAYER_INVENTORY.countItem(prestige_item) > 0 && this.getGolemItem().getAvailableUpgrades(this.GOLEM_STACK) > 0;
    }

    public void performPrestige()
    {
        if(this.canPrestige(this.getGolemItem().getPrestigeCount(this.GOLEM_STACK)))
        {
            this.PLAYER_INVENTORY.removeItem(
                    this.PLAYER_INVENTORY.findSlotMatchingItem(
                            Constants.GOLEM_PRESIGE_WANTS(this.getGolemItem().getPrestigeCount(this.GOLEM_STACK))
                    ), 1
            );
            this.getGolemItem().incrementPrestige(this.GOLEM_STACK);

        }
        this.askForPrestige();

    }

    public void spendElixir(int operation) // good god i need to take a minute to clean this up
    {
        GolemItem golem_item = (GolemItem) this.GOLEM_STACK.getItem();
        switch (operation)
        {
            case Constants.INCREASE_POTENCY_GOLEM_OPERATION:
                this.PLAYER_INVENTORY.removeItem(this.PLAYER_INVENTORY.findSlotMatchingItem(Constants.GOLEM_ELIXIR_ITEMSTACK_FORMULA(Constants.INCREASE_POTENCY_GOLEM_OPERATION, golem_item.getPotencyUpgradeTally(this.GOLEM_STACK))), this.INPUT_INVENTORY.getItem(POTENCY_ELIXIR_SLOT).getCount());
                break;
            case Constants.INCREASE_SIZE_GOLEM_OPERATION:
                this.PLAYER_INVENTORY.removeItem(this.PLAYER_INVENTORY.findSlotMatchingItem(Constants.GOLEM_ELIXIR_ITEMSTACK_FORMULA(Constants.INCREASE_SIZE_GOLEM_OPERATION, golem_item.getSizeUpgradeTally(this.GOLEM_STACK))), this.INPUT_INVENTORY.getItem(SIZE_ELIXIR_SLOT).getCount());
                break;
            default:
                break;
        }
        this.askForElixir();

    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    public GolemItem getGolemItem()
    {
        // I WROTE THIS LATER, DONT ASK WHY I DDINT DO IT FIRST!!
        return (GolemItem) this.GOLEM_STACK.getItem();
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
            if(pId == GolemItemUpgradeGUI.SIZE_UPGRADE_BUTTON_PID)
            {
                upgradeGolemSize();
            }
            if(pId == GolemItemUpgradeGUI.POTENCY_UPGRADE_BUTTON_PID)
            {
                upgradeGolemPotency();
            }
            if(pId == GolemItemUpgradeGUI.PRESTIGE_BUTTON_PID)
            {
                performPrestige();
            }
            PLAYER_INVENTORY.setChanged();
            return true;
        }

        return false;
    }

    private void upgradeGolemPotency() {
        if (canSpendElixir(Constants.INCREASE_POTENCY_GOLEM_OPERATION))
        {

            float old_golem_potency = this.getGolemItem().getPotency(this.GOLEM_STACK);

            spendElixir(Constants.INCREASE_POTENCY_GOLEM_OPERATION);
            this.getGolemItem().incrementPotencyUpgradeTally(this.GOLEM_STACK);
            this.getGolemItem().setPotency(this.GOLEM_STACK, old_golem_potency + (0.1f * this.getGolemItem().potencyPrestige(this.GOLEM_STACK)) *
                    (this.getGolemItem().hasAscended(this.GOLEM_STACK) && this.getGolemItem().hasPrestiged(this.GOLEM_STACK) ? 4.0f : 1f)
            );
            askForElixir();
        }
    }

    private void upgradeGolemSize() {
        if (canSpendElixir(Constants.INCREASE_SIZE_GOLEM_OPERATION))
        {

            float old_golem_size = this.getGolemItem().getSize(this.GOLEM_STACK);

            spendElixir(Constants.INCREASE_SIZE_GOLEM_OPERATION);
            this.getGolemItem().incrementSizeUpgradeTally(this.GOLEM_STACK);
            this.getGolemItem().setSize(this.GOLEM_STACK, old_golem_size + (0.1f * this.getGolemItem().sizePrestige(this.GOLEM_STACK))
                    * (this.getGolemItem().hasAscended(this.GOLEM_STACK) && this.getGolemItem().hasPrestiged(this.GOLEM_STACK) ? 4.0f : 1f));
        }
        askForElixir();
    }

    private void openRecipesScreen(ServerPlayer player, ItemStack golem_stack) {
        player.openMenu(
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
//        NetworkHooks.openScreen(
//                player,
//                new SimpleMenuProvider(
//                        (containerId, playerInventory, p) -> {
//                            GolemItemRecipesMenu menu = new GolemItemRecipesMenu(
//                                    containerId,
//                                    playerInventory
//                            );
//
//                            menu.onOpen();
//                            return menu;
//                        },
//                        Component.literal(this.GOLEM_STACK.getItem().getName(golem_stack).getString())
//                )
//        );
    }

    private void openUpgradeScreen(ServerPlayer player, ItemStack golem_stack) {
        player.openMenu(
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
//        NetworkHooks.openScreen(
//                player,
//                new SimpleMenuProvider(
//                        (containerId, playerInventory, p) -> {
//                            GolemItemUpgradeMenu menu = new GolemItemUpgradeMenu(
//                                    containerId,
//                                    playerInventory
//                            );
//
//                            menu.onOpen();
//                            return menu;
//                        },
//                        Component.literal(this.GOLEM_STACK.getItem().getName(golem_stack).getString())
//                )
//        );
    }
    private void openStatsScreen(ServerPlayer player, ItemStack golem_stack) {
        player.openMenu(
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
//        NetworkHooks.openScreen(
//                player,
//                new SimpleMenuProvider(
//                        (containerId, playerInventory, p) -> {
//                            GolemItemStatsMenu menu = new GolemItemStatsMenu(
//                                    containerId,
//                                    playerInventory
//                            );
//
//                            menu.onOpen();
//                            return menu;
//                        },
//                        Component.literal(this.GOLEM_STACK.getItem().getName(golem_stack).getString())
//                )
//        );
    }

    private void openHomeScreen(ServerPlayer player, ItemStack golem_stack) {
        player.openMenu(
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
//        NetworkHooks.openScreen(
//                player,
//                new SimpleMenuProvider(
//                        (containerId, playerInventory, p) -> {
//                            GolemItemMenu menu = new GolemItemMenu(
//                                    containerId,
//                                    playerInventory
//                            );
//
//                            menu.onOpen();
//                            return menu;
//                        },
//                        Component.literal(this.GOLEM_STACK.getItem().getName(golem_stack).getString())
//                )
//        );
    }
}
