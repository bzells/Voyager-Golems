package net.jzells.voyager_golems.common.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// Note: a lot of this code was copy pasted and then slightly changed. So if it seems wack, I stole it.
public class GolemProduceRecipe implements Recipe<RecipeWrapper> {

    private final ResourceLocation id;
    private final ItemStack golemItem;
    private final int golem_level;
    private final int productionPoints;
    private final List<ItemStack> outputs;
    private final Block recipe_block;

    public GolemProduceRecipe(
            ResourceLocation id,
            ItemStack golemItem,
            int golemTier,
            int productionPoints,
            List<ItemStack> outputs,
            Block recipe_block
    ) {
        this.id = id;
        this.golemItem = golemItem;
        this.golem_level = golemTier;
        this.productionPoints = productionPoints;
        this.outputs = outputs.stream()
                .map(ItemStack::copy)
                .toList();
        this.recipe_block = recipe_block;
    }

    public ItemStack getGolemItem() {
        return golemItem;
    }

    public Block getGolemBlock()
    {
        return recipe_block;
    }

    public int getGolem_level() {
        return golem_level;
    }

    public int getProductionPoints() {
        return productionPoints;
    }

    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return !pContainer.getItem(0).isEmpty() && pContainer.getItem(0).is(golemItem.getItem()); // not sure if i need the empty part... but better safe than sorry
    }

    @Override
    public ItemStack assemble(RecipeWrapper input, HolderLookup.Provider registries) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0).copy();
    }


    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0).copy(); // uses first item as "result"
    }

    public List<ItemStack> getOutputs() {
        return outputs.stream()
                .map(ItemStack::copy)
                .toList();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }


    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return VoyagerRecipes.GOLEM_PRODUCTION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return VoyagerRecipes.GOLEM_PRODUCTION_TYPE;
    }

    public static class Serializer implements RecipeSerializer<GolemProduceRecipe> {

        public static final MapCodec<GolemProduceRecipe> CODEC = RecordCodecBuilder.mapCodec(p ->
                p.group(Codec.STRING.fieldOf("id").flatXmap(rs->DataResult.success(ResourceLocation.parse(rs)),
                                        rl->DataResult.success(rl.getPath()))
                                        .forGetter(GolemProduceRecipe::getId),
                        ItemStack.CODEC.fieldOf("golem").forGetter(GolemProduceRecipe::getGolemItem),
                                Codec.INT.optionalFieldOf("golem_level",1).forGetter(GolemProduceRecipe::getGolem_level),
                                Codec.INT.optionalFieldOf("production_points",200).forGetter(GolemProduceRecipe::getProductionPoints),
                        Ingredient.CODEC_NONEMPTY
                                .listOf()
                                .fieldOf("outputs")
                                .flatXmap(ings -> {
                                    List<ItemStack> result = new ArrayList<>();
                                    ings.stream().map(Ingredient::getItems).forEach(r->result.addAll(List.of(r)));
                                    return DataResult.success(result);
                                }, itemStacks -> {
                                    List<Ingredient> result = new ArrayList<>();
                                    result.add(Ingredient.of(itemStacks.stream()));
                                    return DataResult.success(result);
                                })
                                .forGetter(GolemProduceRecipe::getOutputs),
                        Block.CODEC.fieldOf("block").forGetter(GolemProduceRecipe::getGolemBlock)
                        )
                        .apply(p,GolemProduceRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf,GolemProduceRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        ResourceLocation.STREAM_CODEC, GolemProduceRecipe::getId,
                        ItemStack.STREAM_CODEC, GolemProduceRecipe::getGolemItem,
                        ByteBufCodecs.INT, GolemProduceRecipe::getGolem_level,
                        ByteBufCodecs.INT, GolemProduceRecipe::getProductionPoints,
                        ItemStack.LIST_STREAM_CODEC, GolemProduceRecipe::getOutputs,
                        ByteBufCodecs.registry(Registries.BLOCK), GolemProduceRecipe::getGolemBlock,
                        GolemProduceRecipe::new
                );
//        @Override
//        public GolemProduceRecipe fromJson(
//                ResourceLocation recipeId,
//                JsonObject json
//        ) {
//            ResourceLocation golemId = ResourceLocation.parse(
//                    GsonHelper.getAsString(json, "golem")
//            );
//
//            var golemItem = BuiltInRegistries.ITEM.get(golemId);
//
//            if (golemItem == null || golemItem == net.minecraft.world.item.Items.AIR) {
//                throw new IllegalArgumentException(
//                        "Unknown golem item: " + golemId
//                );
//            }
//
//
//            ResourceLocation block_id = ResourceLocation.parse(
//                    GsonHelper.getAsString(json, "block")
//            );
//            Block recipe_block = BuiltInRegistries.BLOCK.get(block_id);
//
//
//            if(recipe_block == null || recipe_block == Blocks.AIR)
//            {
//
//                throw new IllegalArgumentException(
//                        "Unknown block: " + recipe_block
//                );
//
//            }
//
//
//
//            int level = GsonHelper.getAsInt(json, "golem_level", 1);
//            int duration = GsonHelper.getAsInt(json, "production_points", 200);
//
//
//            JsonArray outputsJson = GsonHelper.getAsJsonArray(json, "outputs");
//            List<ItemStack> outputs = new ArrayList<>();
//
//            for (JsonElement element : outputsJson) {
//                JsonObject outputJson = element.getAsJsonObject();
//                ItemStack output = ShapedRecipeitemStackFromJson(outputJson);
//                outputs.add(output);
//            }
//
//            return new GolemProduceRecipe(
//                    recipeId,
//                    golemItem,
//                    level,
//                    duration,
//                    outputs,
//                    recipe_block
//            );
//        }

//        @Override
//        public GolemProduceRecipe fromNetwork(
//                ResourceLocation recipeId,
//                FriendlyByteBuf buffer
//        ) {
//            var golemItem = ForgeRegistries.ITEMS.getValue(buffer.readResourceLocation());
//            var golemBlock = ForgeRegistries.BLOCKS.getValue(buffer.readResourceLocation());
//            int level = buffer.readVarInt();
//            int duration = buffer.readVarInt();
//
//            int outputCount = buffer.readVarInt();
//            List<ItemStack> outputs = new ArrayList<>();
//
//            for (int i = 0; i < outputCount; i++) {
//                outputs.add(buffer.readItem());
//            }
//
//            return new GolemProduceRecipe(
//                    recipeId, golemItem, level, duration, outputs, golemBlock
//            );
//        }
//
//        @Override
//        public void toNetwork(FriendlyByteBuf buffer, GolemProduceRecipe recipe) {
//            buffer.writeResourceLocation(
//                    ForgeRegistries.ITEMS.getKey(recipe.getGolemItem())
//            );
//            buffer.writeResourceLocation(
//                    ForgeRegistries.BLOCKS.getKey(recipe.getGolemBlock())
//            );
//            buffer.writeVarInt(recipe.getGolem_level());
//            buffer.writeVarInt(recipe.getProductionPoints());
//
//            buffer.writeVarInt(recipe.getOutputs().size());
//            for (ItemStack output : recipe.getOutputs()) {
//                buffer.writeItem(output);
//            }
//        }

        @Override
        public MapCodec<GolemProduceRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, GolemProduceRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
