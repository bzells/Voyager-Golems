package net.jzells.voyager_golems.common.data;

import net.jzells.voyager_golems.util.Constants;
import net.minecraft.resources.ResourceLocation;

public record GolemDenDefinition(
        Constants.GOLEM_GROUPS group,
        ResourceLocation texture,
        float output_boost,
        float speed_boost,
        float xp_boost,
        int tier
) {
}
