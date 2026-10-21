package net.jzells.voyager_golems.common.menus;

import net.jzells.voyager_golems.VoyagerGolems;
import net.jzells.voyager_golems.common.screens.GolemItemGUI;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
//import net.minecraftforge.common.extensions.IForgeMenuType;
//import net.minecraftforge.eventbus.api.IEventBus;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static net.jzells.voyager_golems.VoyagerGolems.MENUS;

public class VoyagerMenus {


    public static final Supplier<MenuType<GolemDenMenu>>
            GOLEM_DEN_MENU = MENUS.register(
            "golem_den",
            () -> IMenuTypeExtension.create(GolemDenMenu::new)
    );

    public static final Supplier<MenuType<GolemItemMenu>> GOLEM_ITEM_MENU =
            MENUS.register("golem_item_menu",
                    () -> new MenuType<>(GolemItemMenu::new, FeatureFlags.DEFAULT_FLAGS)
//                    IForgeMenuType.create(
//                            (windowId, inv, data) ->
//                                    new GolemItemMenu(windowId, inv))
                    );

    public static final Supplier<MenuType<GolemItemUpgradeMenu>> GOLEM_ITEM_UPGRADE_MENU =
            MENUS.register("golem_item_upgrade_menu",
                    () -> new MenuType<>(GolemItemUpgradeMenu::new, FeatureFlags.DEFAULT_FLAGS)
//                            IForgeMenuType.create(
//                            (windowId, inv, data) ->
//                                    new GolemItemUpgradeMenu(windowId, inv)
//                    )
            );

    public static final Supplier<MenuType<GolemItemStatsMenu>> GOLEM_ITEM_STATS_MENU =
            MENUS.register("golem_item_stats_menu",
                    () -> new MenuType<>(GolemItemStatsMenu::new,FeatureFlags.DEFAULT_FLAGS)
//                            IForgeMenuType.create(
//                            (windowId, inv, data) ->
//                                    new GolemItemStatsMenu(windowId, inv)
//                    )
            );

    public static final Supplier<MenuType<GolemItemRecipesMenu>> GOLEM_ITEM_RECIPES_MENU =
            MENUS.register("golem_item_recipes_menu",
                    () -> new MenuType<>(GolemItemRecipesMenu::new, FeatureFlags.DEFAULT_FLAGS)
//                            IForgeMenuType.create(
//                            (windowId, inv, data) ->
//                                    new GolemItemRecipesMenu(windowId, inv)
//                    )
            );


    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
