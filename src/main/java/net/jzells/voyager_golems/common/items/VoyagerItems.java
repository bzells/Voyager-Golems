package net.jzells.voyager_golems.common.items;

import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.jzells.voyager_golems.util.Constants;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.jzells.voyager_golems.VoyagerGolems.MODID;

public class VoyagerItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<GolemItem> SPRING_GOLEM = createGolemItem("spring", Constants.GOLEM_GROUPS.FLORA.getId(), 1);
    public static final RegistryObject<GolemItem> GEM_GOLEM = createGolemItem("gem", Constants.GOLEM_GROUPS.SHINY.getId(), 1);
    public static final RegistryObject<GolemItem> VOIDLING_GOLEM = createGolemItem("voidling", Constants.GOLEM_GROUPS.ENDER.getId(), 1);
    public static final RegistryObject<GolemItem> HELISH_GOLEM = createGolemItem("hellish", Constants.GOLEM_GROUPS.NETHER.getId(), 1);
    public static final RegistryObject<GolemItem> ORE_GOLEM = createGolemItem("ore", Constants.GOLEM_GROUPS.SHINY.getId(), 1);
    public static final RegistryObject<GolemItem> ROCKY_GOLEM = createGolemItem("rocky", Constants.GOLEM_GROUPS.CAVERN.getId(), 1);
    public static final RegistryObject<GolemItem> PETRO_GOLEM = createGolemItem("petro", Constants.GOLEM_GROUPS.DARK.getId(), 1);
    public static final RegistryObject<GolemItem> FOREST_GOLEM = createGolemItem("forest", Constants.GOLEM_GROUPS.FLORA.getId(), 1);
    public static final RegistryObject<GolemItem> MUD_GOLEM = createGolemItem("mud", Constants.GOLEM_GROUPS.SLUDGE.getId(), 1);


    public static final RegistryObject<GolemItem> KNIGHT_GOLEM = createGolemItem("knight", Constants.GOLEM_GROUPS.DARK.getId(), 2);


    public static final RegistryObject<Item> SMALL_ELIXIR_GEL = ITEMS.register(
            "tiny_jellied_elixir", () -> new Item(new Item.Properties())
    );
    public static final RegistryObject<Item> ELIXIR_GEL = ITEMS.register(
            "jellied_elixir", () -> new Item(new Item.Properties())
    );

    public static final RegistryObject<Item> SMALL_ENRICHED_ELIXIR_GEL = ITEMS.register(
            "tiny_jellied_enriched_elixir", () -> new Item(new Item.Properties())
    );
    public static final RegistryObject<Item> ELIXIR_ENRICHED_GEL = ITEMS.register(
            "jellied_enriched_elixir", () -> new Item(new Item.Properties())
    );

    public static final RegistryObject<Item> SMALL_DARK_ELIXIR_GEL = ITEMS.register(
            "tiny_jellied_dark_elixir", () -> new Item(new Item.Properties())
    );
    public static final RegistryObject<Item> DARK_ELIXIR_GEL = ITEMS.register(
            "jellied_dark_elixir", () -> new Item(new Item.Properties())
    );

    public static final RegistryObject<Item> SMALL_GOLDEN_ELIXIR_GEL = ITEMS.register(
            "tiny_jellied_golden_elixir", () -> new Item(new Item.Properties())
    );
    public static final RegistryObject<Item> GOLDEN_ELIXIR_GEL = ITEMS.register(
            "jellied_golden_elixir", () -> new Item(new Item.Properties())
    );

    public static final RegistryObject<Item> XP_STAR = ITEMS.register(
            "xp_star", () -> new Item(new Item.Properties())
    );
    public static final RegistryObject<Item> DARK_ELIXIR_HEART = ITEMS.register(
            "dark_elixir_heart", () -> new Item(new Item.Properties())
    );
    public static final RegistryObject<Item> VOID_ORB = ITEMS.register(
            "void_orb", () -> new Item(new Item.Properties())
    );
    public static final RegistryObject<Item> STAR_OF_ASCENSION = ITEMS.register(
            "star_of_ascension", () -> new Item(new Item.Properties())
    );
    public static final RegistryObject<Item> STAR_OF_OBLIVION = ITEMS.register(
            "star_of_oblivion", () -> new Item(new Item.Properties())
    );





    private static RegistryObject<GolemItem> createGolemItem(String type, String grouping, int tier)
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
