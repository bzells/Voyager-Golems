package net.jzells.voyager_golems.common.items;

import net.jzells.voyager_golems.common.items.components.GolemDataComponent;
import net.jzells.voyager_golems.common.items.components.VoyagerDataComponents;
import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.jzells.voyager_golems.util.Constants;
import net.minecraft.world.item.Item;
//import net.minecraftforge.eventbus.api.IEventBus;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;

import static net.jzells.voyager_golems.VoyagerGolems.*;
import static net.jzells.voyager_golems.VoyagerGolems.MODID;

public class VoyagerItems {

    public static final DeferredItem<GolemItem> SPRING_GOLEM = createGolemItem("spring", Constants.GOLEM_GROUPS.FLORA.getId(), 1);
    public static final DeferredItem<GolemItem> GEM_GOLEM = createGolemItem("gem", Constants.GOLEM_GROUPS.SHINY.getId(), 1);
    public static final DeferredItem<GolemItem> VOIDLING_GOLEM = createGolemItem("voidling", Constants.GOLEM_GROUPS.ENDER.getId(), 1);
    public static final DeferredItem<GolemItem> HELISH_GOLEM = createGolemItem("hellish", Constants.GOLEM_GROUPS.NETHER.getId(), 1);
    public static final DeferredItem<GolemItem> ORE_GOLEM = createGolemItem("ore", Constants.GOLEM_GROUPS.SHINY.getId(), 1);
    public static final DeferredItem<GolemItem> ROCKY_GOLEM = createGolemItem("rocky", Constants.GOLEM_GROUPS.CAVERN.getId(), 1);
    public static final DeferredItem<GolemItem> PETRO_GOLEM = createGolemItem("petro", Constants.GOLEM_GROUPS.DARK.getId(), 1);
    public static final DeferredItem<GolemItem> FOREST_GOLEM = createGolemItem("forest", Constants.GOLEM_GROUPS.FLORA.getId(), 1);
    public static final DeferredItem<GolemItem> MUD_GOLEM = createGolemItem("mud", Constants.GOLEM_GROUPS.SLUDGE.getId(), 1);


    public static final DeferredItem<GolemItem> KNIGHT_GOLEM = createGolemItem("knight", Constants.GOLEM_GROUPS.DARK.getId(), 2);


    public static final DeferredItem<Item> SMALL_ELIXIR_GEL = ITEMS.register(
            "tiny_jellied_elixir", () -> new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> ELIXIR_GEL = ITEMS.register(
            "jellied_elixir", () -> new Item(new Item.Properties())
    );

    public static final DeferredItem<Item> SMALL_ENRICHED_ELIXIR_GEL = ITEMS.register(
            "tiny_jellied_enriched_elixir", () -> new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> ELIXIR_ENRICHED_GEL = ITEMS.register(
            "jellied_enriched_elixir", () -> new Item(new Item.Properties())
    );

    public static final DeferredItem<Item> SMALL_DARK_ELIXIR_GEL = ITEMS.register(
            "tiny_jellied_dark_elixir", () -> new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> DARK_ELIXIR_GEL = ITEMS.register(
            "jellied_dark_elixir", () -> new Item(new Item.Properties())
    );

    public static final DeferredItem<Item> SMALL_GOLDEN_ELIXIR_GEL = ITEMS.register(
            "tiny_jellied_golden_elixir", () -> new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> GOLDEN_ELIXIR_GEL = ITEMS.register(
            "jellied_golden_elixir", () -> new Item(new Item.Properties())
    );

    public static final DeferredItem<Item> XP_STAR = ITEMS.register(
            "xp_star", () -> new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> DARK_ELIXIR_HEART = ITEMS.register(
            "dark_elixir_heart", () -> new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> VOID_ORB = ITEMS.register(
            "void_orb", () -> new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> STAR_OF_ASCENSION = ITEMS.register(
            "star_of_ascension", () -> new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> STAR_OF_OBLIVION = ITEMS.register(
            "star_of_oblivion", () -> new Item(new Item.Properties())
    );





    private static DeferredItem<GolemItem> createGolemItem(String type, String grouping, int tier)
    {
        return ITEMS.register(
                type + "_golem",
                () -> new GolemItem(new Item.Properties().stacksTo(1),
                        grouping, type, tier)
        );
    }

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
