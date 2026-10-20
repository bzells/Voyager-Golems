package net.jzells.voyager_golems.common.data;

import net.jzells.voyager_golems.util.Constants;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class GolemDenDefinitionRegistry {
    private static final Map<String, GolemDenDefinition> DEFINITIONS =
            new HashMap<>();

    public static final GolemDenDefinition FLORA_T1 = register( // should have no played around with trying to use records lol, i hate the way I have this... but if it works...
            new GolemDenDefinition(
                    Constants.GOLEM_GROUPS.FLORA,
                    ResourceLocation.fromNamespaceAndPath(
                            "voyager_golems",
                            "textures/gui/flora_den.png"
                    ),
                    1.0f, 0.8f, 1, 1
            )
    );

    public static final GolemDenDefinition CAVERN_T1 = createGolemDenDefinition(Constants.GOLEM_GROUPS.CAVERN,
            "cavern_den", 1.0f, 0.8f, 1f, 1);
    public static final GolemDenDefinition SHINY_T1 = createGolemDenDefinition(Constants.GOLEM_GROUPS.SHINY,
            "shiny_den", 1.0f, 0.8f, 1f, 1);
    public static final GolemDenDefinition NETHER_T1 = createGolemDenDefinition(Constants.GOLEM_GROUPS.NETHER,
            "nether_den", 1.0f, 1.2f, 2f, 1);
    public static final GolemDenDefinition ENDER_T1 = createGolemDenDefinition(Constants.GOLEM_GROUPS.ENDER,
            "ender_den", 1.0f, 0.8f, 1f, 1);
    public static final GolemDenDefinition DARK_T1 = createGolemDenDefinition(Constants.GOLEM_GROUPS.DARK,
            "cavern_den", 1.0f, 0.8f, 1f, 1);
    public static final GolemDenDefinition SLUDGE_T1 = createGolemDenDefinition(Constants.GOLEM_GROUPS.DARK,
            "cavern_den", 1.0f, 1f, 2f, 1);

    public static final GolemDenDefinition DARK_T2 = createGolemDenDefinition(Constants.GOLEM_GROUPS.DARK,
            "cavern_den", 1.1f, 1.1f, 2f, 2);

    private GolemDenDefinitionRegistry() {}

    private static GolemDenDefinition register(
            GolemDenDefinition definition
    ) {
        DEFINITIONS.put(definition.group().getId(), definition);
        return definition;
    }

    public static GolemDenDefinition get(String groupId) {
        GolemDenDefinition definition = DEFINITIONS.get(groupId);

        if (definition == null) {
            throw new IllegalArgumentException(
                    "Unknown Golem Den group: " + groupId
            );
        }

        return definition;
    }

    private static GolemDenDefinition createGolemDenDefinition(Constants.GOLEM_GROUPS group, String gui_texture, float output_boost, float speed_boost, float xp_boost, int tier)
    {
        return new GolemDenDefinition(
                group,
                ResourceLocation.fromNamespaceAndPath(
                        "voyager_golems",
                        "textures/gui/" + gui_texture + ".png"
                ),
                output_boost, speed_boost, xp_boost, tier
        );
    }
}
