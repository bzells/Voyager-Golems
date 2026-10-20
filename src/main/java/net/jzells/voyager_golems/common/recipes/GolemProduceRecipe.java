package net.jzells.voyager_golems.common.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
// Note: a lot of this code was copy pasted and then slightly changed. So if it seems wack, I stole it.
public class GolemProduceRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final Item golemItem;
    private final int golem_level;
    private final int productionPoints;
    private final List<ItemStack> outputs;
    private final Block recipe_block;

    public GolemProduceRecipe(
            ResourceLocation id,
            Item golemItem,
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

    public Item getGolemItem() {
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
    public boolean matches(Container pContainer, Level pLevel) {
        return !pContainer.getItem(0).isEmpty() && pContainer.getItem(0).is(golemItem); // not sure if i need the empty part... but better safe than sorry
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0).copy(); // uses first item as "result"
    }

    public List<ItemStack> getOutputs() {
        return outputs.stream()
                .map(ItemStack::copy)
                .toList();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return null;
    }

    @Override
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

    public static class Serializer
            implements RecipeSerializer<GolemProduceRecipe> {

        @Override
        public GolemProduceRecipe fromJson(
                ResourceLocation recipeId,
                JsonObject json
        ) {
            ResourceLocation golemId = ResourceLocation.parse(
                    GsonHelper.getAsString(json, "golem")
            );

            var golemItem = ForgeRegistries.ITEMS.getValue(golemId);

            if (golemItem == null || golemItem == net.minecraft.world.item.Items.AIR) {
                throw new IllegalArgumentException(
                        "Unknown golem item: " + golemId
                );
            }


            ResourceLocation block_id = ResourceLocation.parse(
                    GsonHelper.getAsString(json, "block")
            );
            Block recipe_block = ForgeRegistries.BLOCKS.getValue(block_id);


            if(recipe_block == null || recipe_block == Blocks.AIR)
            {

                throw new IllegalArgumentException(
                        "Unknown block: " + recipe_block
                );

            }



            int level = GsonHelper.getAsInt(json, "golem_level", 1);
            int duration = GsonHelper.getAsInt(json, "production_points", 200);


            JsonArray outputsJson = GsonHelper.getAsJsonArray(json, "outputs");
            List<ItemStack> outputs = new ArrayList<>();

            for (JsonElement element : outputsJson) {
                JsonObject outputJson = element.getAsJsonObject();
                ItemStack output = ShapedRecipe.itemStackFromJson(outputJson);
                outputs.add(output);
            }

            return new GolemProduceRecipe(
                    recipeId,
                    golemItem,
                    level,
                    duration,
                    outputs,
                    recipe_block
            );
        }

        @Override
        public GolemProduceRecipe fromNetwork(
                ResourceLocation recipeId,
                FriendlyByteBuf buffer
        ) {
            var golemItem = ForgeRegistries.ITEMS.getValue(buffer.readResourceLocation());
            var golemBlock = ForgeRegistries.BLOCKS.getValue(buffer.readResourceLocation());
            int level = buffer.readVarInt();
            int duration = buffer.readVarInt();

            int outputCount = buffer.readVarInt();
            List<ItemStack> outputs = new ArrayList<>();

            for (int i = 0; i < outputCount; i++) {
                outputs.add(buffer.readItem());
            }

            return new GolemProduceRecipe(
                    recipeId, golemItem, level, duration, outputs, golemBlock
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, GolemProduceRecipe recipe) {
            buffer.writeResourceLocation(
                    ForgeRegistries.ITEMS.getKey(recipe.getGolemItem())
            );
            buffer.writeResourceLocation(
                    ForgeRegistries.BLOCKS.getKey(recipe.getGolemBlock())
            );
            buffer.writeVarInt(recipe.getGolem_level());
            buffer.writeVarInt(recipe.getProductionPoints());

            buffer.writeVarInt(recipe.getOutputs().size());
            for (ItemStack output : recipe.getOutputs()) {
                buffer.writeItem(output);
            }
        }
    }
}
