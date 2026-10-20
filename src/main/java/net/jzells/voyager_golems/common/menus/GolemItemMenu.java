package net.jzells.voyager_golems.common.menus;

import mezz.jei.forge.platform.RecipeHelper;
import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.jzells.voyager_golems.common.recipes.GolemProduceRecipe;
import net.jzells.voyager_golems.common.recipes.VoyagerRecipes;
import net.jzells.voyager_golems.util.gui.DummySlot;
import net.jzells.voyager_golems.util.gui.GolemGUISlot;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class GolemItemMenu extends AbstractContainerMenu {

    public final ItemStack GOLEM_STACK;
    public final Inventory PLAYER_INVENTORY;

    private final Container output_inventory;

    private final int GOLEM_SLOT;

    public ItemStack getGolemStack() {
        return PLAYER_INVENTORY.getItem(GOLEM_SLOT);
    }

    public GolemItemMenu(int containerId, Inventory playerInventory) {
        super(VoyagerMenus.GOLEM_ITEM_MENU.get(), containerId);

        this.PLAYER_INVENTORY = playerInventory;
        this.GOLEM_STACK = playerInventory.getSelected(); // dont want to use copy here
        this.output_inventory = new SimpleContainer(12);
        this.GOLEM_SLOT = playerInventory.selected;

        // add instance of paired screen (really could have coded this better, but we are too deep)
        // on ANY button press, update the screen with .update

        List<ItemStack> recipe_outputs = List.of();
        boolean has_recipe_selected = false;


        // output slots (for show only)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                int index = col + row * 4;
                this.addSlot(new DummySlot(
                        output_inventory,
                        index,
                        98 + col * 18,
                        16 + row * 18
                ));
            }
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

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }


    public List<GolemProduceRecipe> getAvailableRecipes(
            Level level,
            ItemStack golemStack
    ) {
        if (!(golemStack.getItem() instanceof GolemItem golemItem)) {
            return List.of();
        }

        int golemLevel = golemItem.getLevel(golemStack);

        return level.getRecipeManager()
                .getAllRecipesFor(VoyagerRecipes.GOLEM_PRODUCTION_TYPE)
                .stream()
                .filter(recipe -> recipe.getGolemItem() == golemStack.getItem())
                .filter(recipe -> recipe.getGolem_level() <= golemLevel)
                .toList();
    }

    public List<GolemProduceRecipe> getAllAvailableRecipes(
            Level level,
            ItemStack golemStack
    ) {
        if (!(golemStack.getItem() instanceof GolemItem golemItem)) {
            return List.of();
        }



        return level.getRecipeManager()
                .getAllRecipesFor(VoyagerRecipes.GOLEM_PRODUCTION_TYPE)
                .stream()
                .filter(recipe ->
                        ForgeRegistries.ITEMS.getKey(recipe.getGolemItem())
                                .equals(ForgeRegistries.ITEMS.getKey(golemStack.getItem()))
                )
                .toList();
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

    public void onOpen()
    {

        if(this.GOLEM_STACK.getItem() instanceof GolemItem golem_item)
        {
            String recipe_id = golem_item.getRecipe(this.GOLEM_STACK);

            if(recipe_id.equals(GolemItem.NO_RECIPE)) return;

            ResourceLocation recipe_location_id = ResourceLocation.parse(golem_item.getRecipe(GOLEM_STACK));

            PLAYER_INVENTORY.player.level()
                    .getRecipeManager()
                    .byKey(recipe_location_id)
                    .ifPresent(recipe -> {
                        if (recipe instanceof GolemProduceRecipe golem_recipe) {
                            List<ItemStack> output_items = golem_recipe.getOutputs();

                            for (int i = 0; i < Math.min(
                                    output_items.size(),
                                    output_inventory.getContainerSize()); i++) {
                                output_inventory.setItem(i, output_items.get(i).copy());
                            }
                        }
                    });

        }

    }
}