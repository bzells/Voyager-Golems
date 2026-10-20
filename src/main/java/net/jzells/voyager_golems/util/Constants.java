package net.jzells.voyager_golems.util;

import net.jzells.voyager_golems.common.items.VoyagerItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class Constants {

    public static final float ENRICHED_ELIXIR_XP_BOOST = 1.5f;

    public static final int INCREASE_SIZE_GOLEM_OPERATION = 0;
    public static final int INCREASE_POTENCY_GOLEM_OPERATION = 1;
    public static final int INCREASE_PRESTIGE_GOLEM_OPERATION = 2;

    public static final List<Item> GOLEM_SPECIAL_UPGRADE_ITEM_ORDER = List.of(VoyagerItems.XP_STAR.get(), VoyagerItems.DARK_ELIXIR_HEART.get(), VoyagerItems.VOID_ORB.get(),VoyagerItems.STAR_OF_ASCENSION.get(), VoyagerItems.STAR_OF_OBLIVION.get());

    public static float GOLEM_LEVEL_XP_MULTIPLIER_FORMULA(int tier, int level)
    {
        float tier_mult = (float) tier / 10.0f;
        float level_mult = (float) (1.0 + 0.2 * Math.exp(-level / 50.0));

        return level_mult + tier_mult;
    }
    public static long GOLEM_XP_FORMULA(boolean xp_boosted, float den_mult, float golem_mult, int unique_output_items)
    {
        float xp_mult = xp_boosted ?
                ENRICHED_ELIXIR_XP_BOOST * den_mult : den_mult;
        return (long) (golem_mult * xp_mult * unique_output_items);
    }
    public static long GOLEM_ELIXIR_COUNT_FORMULA(int operation, int operations_done)
    {
        long return_val = 1;
        if(operation == INCREASE_POTENCY_GOLEM_OPERATION)
        {
            return_val = (int) (((int) Math.pow(((double) operations_done / 2), 3) + 3) * 1.1f);
        }
        if(operation == INCREASE_SIZE_GOLEM_OPERATION)
        {
            return_val = (int) (((int) Math.pow(((double) operations_done / 2), 3) + 3) * 1.5f);
        }
        return  return_val;
    }
    public static ItemStack GOLEM_ELIXIR_ITEMSTACK_FORMULA(int operation, int operations_done)
    {
        long elixir_count = GOLEM_ELIXIR_COUNT_FORMULA(operation, operations_done);
        return getElixirStack(elixir_count, operation);
    }

    public static ItemStack GOLEM_PRESIGE_WANTS(int presige_count)
    {
        if(presige_count > 4) return new ItemStack(Items.BARRIER, 1);
        return GOLEM_SPECIAL_UPGRADE_ITEM_ORDER.get(presige_count).getDefaultInstance();
    }

    private static ItemStack getElixirStack(long count, int operation)
    {
        if (count <= 0) {
            count = 1;
        }

        List<Item> elixir_gels = List.of(
                VoyagerItems.SMALL_ELIXIR_GEL.get(),
                VoyagerItems.ELIXIR_GEL.get(),
                VoyagerItems.SMALL_ENRICHED_ELIXIR_GEL.get(),
                VoyagerItems.ELIXIR_ENRICHED_GEL.get()
        );

        List<Item> dark_elixir_gels = List.of(
                VoyagerItems.SMALL_DARK_ELIXIR_GEL.get(),
                VoyagerItems.DARK_ELIXIR_GEL.get(),
                VoyagerItems.SMALL_GOLDEN_ELIXIR_GEL.get(),
                VoyagerItems.GOLDEN_ELIXIR_GEL.get()
        );


        Item return_item = Items.AIR;

        long return_count = count;

        int i = 0;
        if(operation == INCREASE_POTENCY_GOLEM_OPERATION)
        {
            return_item = elixir_gels.get(i);
            while(return_count > 63 && i < elixir_gels.size() - 1)
            {
                return_count = (return_count + 63) / 64;
                i++;
                return_item = elixir_gels.get(i);
            }
        }
        else
        {
            return_item = dark_elixir_gels.get(i);
            while(return_count > 63 && i < dark_elixir_gels.size() - 1)
            {
                return_count = (return_count + 63) / 64;
                i++;
                return_item = dark_elixir_gels.get(i);
            }
        }


        return new ItemStack(return_item, (int)return_count);
    }

    public enum GOLEM_GROUPS {
        FLORA("flora", "§2"),
        CAVERN("cavern", "§8"),
        ENDER("ender", "§d"),
        NETHER("nether", "§4"),
        SHINY("precious", "§b"),
        DARK("dark", "§5"),
        SLUDGE("sludge", "§7"),
        MAGIC("magic", "§d"),
        MECHA("mecha", "§e");


        private final String id;
        private final String color;

        GOLEM_GROUPS(String id, String color) {
            this.id = id;
            this.color = color;
        }

        public String getId() {
            return id;
        }

        public String getColorName()
        {
            return color + id.toUpperCase();
        }
    }
}
