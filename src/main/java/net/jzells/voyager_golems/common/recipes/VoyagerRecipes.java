package net.jzells.voyager_golems.common.recipes;

import net.jzells.voyager_golems.VoyagerGolems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VoyagerRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(
                    ForgeRegistries.RECIPE_SERIALIZERS,
                    VoyagerGolems.MODID
            );

    public static final RegistryObject<RecipeSerializer<GolemProduceRecipe>>
            GOLEM_PRODUCTION_SERIALIZER =
            SERIALIZERS.register(
                    "golem_production",
                    GolemProduceRecipe.Serializer::new
            );

    public static final ResourceLocation GOLEM_PRODUCTION_ID =
            ResourceLocation.fromNamespaceAndPath(
                    VoyagerGolems.MODID,
                    "golem_production"
            );

    public static final RecipeType<GolemProduceRecipe> GOLEM_PRODUCTION_TYPE =
            RecipeType.simple(GOLEM_PRODUCTION_ID);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}