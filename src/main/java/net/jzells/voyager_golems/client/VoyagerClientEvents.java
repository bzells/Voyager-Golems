package net.jzells.voyager_golems.client;

import net.jzells.voyager_golems.VoyagerGolems;
import net.jzells.voyager_golems.common.menus.GolemDenMenu;
import net.jzells.voyager_golems.common.menus.GolemItemUpgradeMenu;
import net.jzells.voyager_golems.common.menus.VoyagerMenus;
import net.jzells.voyager_golems.common.screens.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;;
@Mod.EventBusSubscriber(
        modid = VoyagerGolems.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class VoyagerClientEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() ->
                MenuScreens.register(
                        VoyagerMenus.GOLEM_DEN_MENU.get(),
                        GolemDenScreen::new
                ));

        event.enqueueWork(() -> {
            MenuScreens.register(
                    VoyagerMenus.GOLEM_ITEM_MENU.get(),
                    GolemItemGUI::new
            );
        });

        event.enqueueWork(() -> {
            MenuScreens.register(
                    VoyagerMenus.GOLEM_ITEM_UPGRADE_MENU.get(),
                    GolemItemUpgradeGUI::new
            );
        });

        event.enqueueWork(() -> {
            MenuScreens.register(
                    VoyagerMenus.GOLEM_ITEM_STATS_MENU.get(),
                    GolemItemStatsGUI::new
            );
        });

        event.enqueueWork(() -> {
            MenuScreens.register(
                    VoyagerMenus.GOLEM_ITEM_RECIPES_MENU.get(),
                    GolemItemRecipesGUI::new
            );
        });
    }
}
