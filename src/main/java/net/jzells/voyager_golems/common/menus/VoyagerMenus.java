package net.jzells.voyager_golems.common.menus;

import net.jzells.voyager_golems.VoyagerGolems;
import net.jzells.voyager_golems.common.screens.GolemItemGUI;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VoyagerMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(
                    ForgeRegistries.MENU_TYPES,
                    VoyagerGolems.MODID
            );

    public static final RegistryObject<MenuType<GolemDenMenu>>
            GOLEM_DEN_MENU = MENUS.register(
            "golem_den",
            () -> IForgeMenuType.create(GolemDenMenu::new)
    );

    public static final RegistryObject<MenuType<GolemItemMenu>> GOLEM_ITEM_MENU =
            MENUS.register("golem_item_menu",
                    () -> IForgeMenuType.create(
                            (windowId, inv, data) ->
                                    new GolemItemMenu(windowId, inv)
                    ));

    public static final RegistryObject<MenuType<GolemItemUpgradeMenu>> GOLEM_ITEM_UPGRADE_MENU =
            MENUS.register("golem_item_upgrade_menu",
                    () -> IForgeMenuType.create(
                            (windowId, inv, data) ->
                                    new GolemItemUpgradeMenu(windowId, inv)
                    ));

    public static final RegistryObject<MenuType<GolemItemStatsMenu>> GOLEM_ITEM_STATS_MENU =
            MENUS.register("golem_item_stats_menu",
                    () -> IForgeMenuType.create(
                            (windowId, inv, data) ->
                                    new GolemItemStatsMenu(windowId, inv)
                    ));

    public static final RegistryObject<MenuType<GolemItemRecipesMenu>> GOLEM_ITEM_RECIPES_MENU =
            MENUS.register("golem_item_recipes_menu",
                    () -> IForgeMenuType.create(
                            (windowId, inv, data) ->
                                    new GolemItemRecipesMenu(windowId, inv)
                    ));


    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
