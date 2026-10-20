package net.jzells.voyager_golems.common.items.golems;

import net.jzells.voyager_golems.common.menus.GolemItemMenu;
import net.jzells.voyager_golems.common.recipes.GolemProduceRecipe;
import net.jzells.voyager_golems.util.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class GolemItem extends Item {

    public final String GROUPING;
    public final String TYPE;
    public static final String GOLEM_TAG = "golem_info";
    public final int TIER;
    public static final String LEVEL_TAG = "level";
    public static final String CURRENT_XP_TAG = "current_xp";
    public static final String XP_TO_LEVEL_TAG = "level_xp";
    public static final String SIZE_TAG = "size";
    public static final String POTENCY_TAG = "potency";
    public static final String RECIPE_TAG = "selected_recipe";
    public static final String NO_RECIPE = "none";



    public static final String POTENCY_UPGRADE_COUNT_TAG = "potency_upgrade_count";
    public static final String SIZE_UPGRADE_COUNT_TAG = "size_upgrade_count";
    public static final String PRESTIGE_UPGRADE_COUNT_TAG = "prestige_upgrade_count";
    public static final String PRESTIGE_XP_MULTIPLIER_TAG = "prestige_xp_multiplier";

    public GolemItem(Properties pProperties, String grouping, String type, int tier) {
        super(pProperties);
        GROUPING = grouping;
        TYPE = type;
        TIER = tier;
    }

    public void initializeNBT(ItemStack stack)
    {
        CompoundTag tag = stack.getOrCreateTagElement(GOLEM_TAG);

        if (!tag.contains(LEVEL_TAG))
            tag.putInt(LEVEL_TAG, 1);

        if (!tag.contains(SIZE_TAG))
            tag.putFloat(SIZE_TAG, 1.0f);

        if (!tag.contains(POTENCY_TAG))
            tag.putFloat(POTENCY_TAG, 0.8f);

        if (!tag.contains(CURRENT_XP_TAG))
            tag.putInt(CURRENT_XP_TAG, 0);

        if (!tag.contains(XP_TO_LEVEL_TAG))
            tag.putInt(XP_TO_LEVEL_TAG, 10);

        if(!tag.contains(RECIPE_TAG))
            tag.putString(RECIPE_TAG, "none");

        if(!tag.contains(POTENCY_UPGRADE_COUNT_TAG))
            tag.putInt(POTENCY_UPGRADE_COUNT_TAG, 0);

        if(!tag.contains(SIZE_UPGRADE_COUNT_TAG))
            tag.putInt(SIZE_UPGRADE_COUNT_TAG, 0);

        if(!tag.contains(PRESTIGE_UPGRADE_COUNT_TAG))
            tag.putInt(PRESTIGE_UPGRADE_COUNT_TAG, 0);

        if(!tag.contains(PRESTIGE_XP_MULTIPLIER_TAG))
            tag.putFloat(PRESTIGE_XP_MULTIPLIER_TAG, 1f);
    }

    public void initializePrestigedNBT(ItemStack stack)
    {
        CompoundTag tag = stack.getOrCreateTagElement(GOLEM_TAG);

            int old_level = this.getLevel(stack);
            int total_upgrades = this.getPotencyUpgradeTally(stack) + this.getSizeUpgradeTally(stack);

            tag.putInt(LEVEL_TAG, 10);
            tag.putFloat(SIZE_TAG, 1f);
            tag.putFloat(POTENCY_TAG, 0.8f);
            tag.putInt(CURRENT_XP_TAG, 0);
            tag.putInt(XP_TO_LEVEL_TAG, 10);
            tag.putString(RECIPE_TAG, "none");
            tag.putInt(POTENCY_UPGRADE_COUNT_TAG, 0);
            tag.putInt(SIZE_UPGRADE_COUNT_TAG, 0);
            tag.putInt(PRESTIGE_XP_MULTIPLIER_TAG, (old_level + total_upgrades) * this.TIER);
    }

    private CompoundTag getGolemTag(ItemStack stack)
    {
        initializeNBT(stack);
        return stack.getOrCreateTagElement(GOLEM_TAG);
    }

    public int getLevel(ItemStack stack)
    {
        return this.getGolemTag(stack).getInt(LEVEL_TAG);
    }
    public float getSize(ItemStack stack)
    {
        return this.getGolemTag(stack).getFloat(SIZE_TAG);
    }
    public float getPotency(ItemStack stack)
    {
        return this.getGolemTag(stack).getFloat(POTENCY_TAG);
    }
    public long getCurrentXPValue(ItemStack stack)
    {
        return this.getGolemTag(stack).getLong(CURRENT_XP_TAG);
    }
    public long getXPToLevelValue(ItemStack stack)
    {
        return this.getGolemTag(stack).getLong(XP_TO_LEVEL_TAG);
    }
    public void setLevel(ItemStack stack, int level)
    {
        this.getGolemTag(stack).putInt(LEVEL_TAG, level);
    }
    public void incrementPotencyUpgradeTally(ItemStack stack)
    {
        int old_tally = this.getGolemTag(stack).getInt(POTENCY_UPGRADE_COUNT_TAG);
        this.getGolemTag(stack).putInt(POTENCY_UPGRADE_COUNT_TAG, old_tally + 1);
    }
    public void incrementSizeUpgradeTally(ItemStack stack)
    {
        int old_tally = this.getGolemTag(stack).getInt(SIZE_UPGRADE_COUNT_TAG);
        this.getGolemTag(stack).putInt(SIZE_UPGRADE_COUNT_TAG, old_tally + 1);
    }
    public int getPotencyUpgradeTally(ItemStack stack)
    {
        return this.getGolemTag(stack).getInt(POTENCY_UPGRADE_COUNT_TAG);
    }
    public int getSizeUpgradeTally(ItemStack stack)
    {
        return this.getGolemTag(stack).getInt(SIZE_UPGRADE_COUNT_TAG);
    }

    public void setRecipe(ItemStack stack, String recipe_id)
    {

        this.getGolemTag(stack).putString(RECIPE_TAG, recipe_id);
    }

    public String getRecipe(ItemStack stack)
    {

        return this.getGolemTag(stack).getString(RECIPE_TAG);
    }

    public int getAvailableUpgrades(ItemStack stack)
    {
        int total_upgrades = this.getPotencyUpgradeTally(stack) + this.getSizeUpgradeTally(stack);
        return this.getLevel(stack) - total_upgrades - (this.hasPrestiged(stack) ? 0 : this.getPrestigeCount(stack));
    }
    public int getPrestigeCount(ItemStack stack)
    {
        return this.getGolemTag(stack).getInt(PRESTIGE_UPGRADE_COUNT_TAG);
    }

    public void incrementPrestige(ItemStack stack)
    {
        if(hasAscended(stack))
        {
            prestige(stack);
        }
        int old_presige = this.getPrestigeCount(stack);
        this.getGolemTag(stack).putInt(PRESTIGE_UPGRADE_COUNT_TAG, old_presige + 1);
        if(old_presige + 1 == 4) {ascend(stack);}

    }

    public int getXpPrestigeMult(ItemStack stack)
    {
        return  this.getGolemTag(stack).getInt(PRESTIGE_XP_MULTIPLIER_TAG);
    }

    public long xpPrestige(ItemStack stack)
    {
        return (long) (this.getPrestigeCount(stack) >= 1 ? 2 : 1) * this.getXpPrestigeMult(stack);
    }
    public float sizePrestige(ItemStack stack)
    {
        return this.getPrestigeCount(stack) >= 2 ? 2f : 1f;
    }
    public float potencyPrestige(ItemStack stack)
    {
        return this.getPrestigeCount(stack) >= 3 ? 2f : 1f;
    }
    public boolean hasAscended(ItemStack stack)
    {
        return this.getPrestigeCount(stack) >= 4;
    }
    public boolean hasPrestiged(ItemStack stack)
    {
        return this.getPrestigeCount(stack) >= 5;
    }
    public void ascend(ItemStack stack)
    {
        float old_size = this.getSize(stack);
        float old_potency = this.getPotency(stack);
        int old_level = this.getLevel(stack);

        this.setSize(stack, old_size * 4);
        this.setPotency(stack, old_potency * 4);

        this.setLevel(stack, old_level + 10);
    }
    public void prestige(ItemStack stack)
    {
        this.initializePrestigedNBT(stack);
    }

    public void setSize(ItemStack stack, float size)
    {
        this.getGolemTag(stack).putFloat(SIZE_TAG, size);
    }

    public void setPotency(ItemStack stack, float potency)
    {
        this.getGolemTag(stack).putFloat(POTENCY_TAG, potency);
    }

    public void setCurrentXPValue(ItemStack stack, long xp)
    {
        this.getGolemTag(stack).putLong(CURRENT_XP_TAG, xp);
    }

    public void setXPToLevelValue(ItemStack stack, long xp)
    {
        this.getGolemTag(stack).putLong(XP_TO_LEVEL_TAG, xp);
    }

    public boolean canUseRecipe(ItemStack stack, GolemProduceRecipe recipe)
    {
        int recipe_level = recipe.getGolem_level();
        return this.getLevel(stack) >= recipe_level;
    }

    public void doGolemLevelWork(ItemStack stack, boolean xp_boosted, float den_mult, Level pLevel)
    {
        long curr_xp = getCurrentXPValue(stack);
        long xp_to_level = getXPToLevelValue(stack);
        int level = getLevel(stack);
        int tier = this.TIER;
        int output_item_count = pLevel.getRecipeManager()
                .byKey(ResourceLocation.parse(this.getRecipe(stack)))
                .filter(recipe -> recipe instanceof GolemProduceRecipe)
                .map(recipe -> (GolemProduceRecipe) recipe)
                .map(recipe -> recipe.getOutputs().size())
                .orElse(0);

        curr_xp += Constants.GOLEM_XP_FORMULA(xp_boosted, den_mult, this.xpPrestige(stack), output_item_count);
        while(curr_xp >= xp_to_level)
        {
            curr_xp -= xp_to_level;
            level++;

            xp_to_level = (long) (xp_to_level * Constants.GOLEM_LEVEL_XP_MULTIPLIER_FORMULA(tier, level));
        }
        setLevel(stack, level);
        setCurrentXPValue(stack, curr_xp);
        setXPToLevelValue(stack, xp_to_level);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
//        doGolemLevelWork(itemstack, false, 1);
        return super.onBlockStartBreak(itemstack, pos, player);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level pLevel,
            Player pPlayer,
            InteractionHand pUsedHand
    ) {
        ItemStack golem_stack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer && pPlayer.isShiftKeyDown()) {
            openScreen(serverPlayer, golem_stack);
            return InteractionResultHolder.sidedSuccess(
                    golem_stack,
                    pLevel.isClientSide()
            );
        }

        return InteractionResultHolder.fail(
                golem_stack
        );
    }

    private void openScreen(ServerPlayer player, ItemStack golem_stack) {
        NetworkHooks.openScreen(
                player,
                new SimpleMenuProvider(
                        (containerId, playerInventory, p) -> {
                            GolemItemMenu menu = new GolemItemMenu(
                                    containerId,
                                    playerInventory
                            );

                            menu.onOpen();
                            return menu;
                        },
                        Component.literal(this.asItem().getName(golem_stack).getString())
                )
        );
    }
}
