package net.jzells.voyager_golems.common.data;

import net.jzells.voyager_golems.util.Constants;
import net.minecraft.resources.ResourceLocation;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.Map;

public class GolemDenDefinitionRegistry {
    private record DenKey(String group, int tier, String type) {}

    private static final Map<DenKey, GolemDenDefinition> DEN_DEFINITIONS =
            new HashMap<>();



    private static void registerBaseTieredGolemDefinitions()
    {
        for (int i = 0; i < 8; i++) {
            float xp_mult = 1f + i;
            float output_mult = 1f;
            float speed_boost = 0.6f + ((float)i / 4);

            for(Constants.GOLEM_GROUPS group : Constants.GOLEM_GROUPS.values())
            {
                createGolemDenDefinition(group, group.getId() + "_den", output_mult, speed_boost, xp_mult, i + 1, "base");
            }
        }
    }

    private static void registerXpTieredGolemDefinitions()
    {
        for (int i = 0; i < 8; i++) {
            float xp_mult = 1f + i * 2;
            float output_mult = .25f;
            float speed_boost = 0.8f + ((float)i / 4);

            for(Constants.GOLEM_GROUPS group : Constants.GOLEM_GROUPS.values())
            {
                createGolemDenDefinition(group, group.getId() + "_den", output_mult, speed_boost, xp_mult, i + 1, "xp");
            }
        }
    }

    static {
        registerBaseTieredGolemDefinitions();
        registerXpTieredGolemDefinitions();
    }

    public static GolemDenDefinition getDenDefinition(int tier, String group, String type) {
        DenKey key = new DenKey(group, tier, type);

        return DEN_DEFINITIONS.get(key);
    }



    private static GolemDenDefinition createGolemDenDefinition(Constants.GOLEM_GROUPS group, String gui_texture, float output_boost, float speed_boost, float xp_boost, int tier, String type)
    {
        GolemDenDefinition returnDef =  new GolemDenDefinition(
                group,
                ResourceLocation.fromNamespaceAndPath(
                        "voyager_golems",
                        "textures/gui/" + gui_texture + ".png"
                ),
                output_boost, speed_boost, xp_boost, tier
        );
        DenKey key = new DenKey(group.getId(), tier, type);
        DEN_DEFINITIONS.put(key, returnDef);

        return returnDef;
    }
}
