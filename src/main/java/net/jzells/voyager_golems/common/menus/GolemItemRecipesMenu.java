package net.jzells.voyager_golems.common.menus;

import com.mojang.logging.LogUtils;
import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.jzells.voyager_golems.common.recipes.GolemProduceRecipe;
import net.jzells.voyager_golems.common.recipes.VoyagerRecipes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;


import java.util.Comparator;
import java.util.List;

public class GolemItemRecipesMenu extends AbstractContainerMenu {

    public final ItemStack GOLEM_STACK;
    public final Inventory PLAYER_INVENTORY;

    protected GolemItemRecipesMenu(int containerId, Inventory playerInventory) {
        super(VoyagerMenus.GOLEM_ITEM_RECIPES_MENU.get(), containerId);
        this.GOLEM_STACK = playerInventory.getSelected();
        this.PLAYER_INVENTORY = playerInventory;

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
            if(pId == 0) // if else if else if else lul (it's actually faster this way, dont larp)
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
            if (pId == 4)
            {
                this.removeGolemRecipe();
            }
            if(pId > 998)
            {
                if(pPlayer instanceof ServerPlayer serverPlayer)
                {
                    int recipe_index = pId - 999;

                    List<GolemProduceRecipe> golem_recipes = getDisplayedRecipes();

                    if (recipe_index < 0 || recipe_index >= golem_recipes.size()) {
                        return false;
                    }

                    this.setGolemRecipe(golem_recipes.get(recipe_index));
                }

            }
            return true;
        }

        return false;
    }

    public boolean canUseRecipe(ItemStack golem_stack, GolemProduceRecipe recipe) {
        if (!(golem_stack.getItem() instanceof GolemItem golem_item)) {
            return false;
        }

        return golem_item.getLevel(golem_stack) >= recipe.getGolem_level();
    }

    public void setGolemRecipe(GolemProduceRecipe recipe)
    {
        if(this.GOLEM_STACK.getItem() instanceof GolemItem golem_item)
        {
            golem_item.setRecipe(this.GOLEM_STACK, recipe.getId().toString());
        }
    }

    public void removeGolemRecipe()
    {
        if(this.GOLEM_STACK.getItem() instanceof GolemItem golem_item)
        {
            golem_item.setRecipe(this.GOLEM_STACK, "none");
        }
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

    private List<GolemProduceRecipe> getDisplayedRecipes() {
        if (!(GOLEM_STACK.getItem() instanceof GolemItem golem_item)) {
            return List.of();
        }

        Level level = PLAYER_INVENTORY.player.level();

        return level.getRecipeManager()
                .getAllRecipesFor(VoyagerRecipes.GOLEM_PRODUCTION_TYPE)
                .stream()
                .map(RecipeHolder::value)
                .filter(recipe ->
                        recipe.getGolemItem().getItem() == golem_item
                                && canUseRecipe(GOLEM_STACK, recipe))
                .sorted(Comparator.comparing(recipe -> recipe.getId().toString()))
                .toList();
    }

    public void onOpen(){
    }
}
