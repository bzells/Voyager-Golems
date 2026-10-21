package net.jzells.voyager_golems.common.items.golems;

import net.jzells.voyager_golems.common.items.components.GolemDataComponent;
import net.jzells.voyager_golems.common.menus.GolemItemMenu;
import net.jzells.voyager_golems.common.recipes.GolemProduceRecipe;
import net.jzells.voyager_golems.util.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import static net.jzells.voyager_golems.common.items.components.VoyagerDataComponents.GOLEM_DATA_COMPONENT;
//import net.minecraftforge.network.NetworkHooks;

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

    public void initializeNBT(ItemStack stack) {
        var data = stack.get(GOLEM_DATA_COMPONENT.get());
        if (data == null) {
            stack.set(GOLEM_DATA_COMPONENT.value(),new GolemDataComponent());
        }
    }

    public void initializePrestigedNBT(ItemStack stack) {
        GolemDataComponent data = this.getGolemData(stack);


        int old_level = this.getLevel(stack);
        int total_upgrades = this.getPotencyUpgradeTally(stack) + this.getSizeUpgradeTally(stack);

        data.setLevel(10);
        data.setSize(1f);
        data.setPotency(0.8f);
        data.setCurrentExp(0);
        data.setExpToLevel(10);
        data.setRecipe("none");
        data.setPotencyUpgrade(0);
        data.setSizeUpgrade(0);
        data.setPrestigeMultiplier((old_level + total_upgrades) * this.TIER);
    }

    private GolemDataComponent getGolemData(ItemStack stack) {
        initializeNBT(stack);
        return stack.get(GOLEM_DATA_COMPONENT.get());
    }

    public int getLevel(ItemStack stack) {
        return this.getGolemData(stack).getLevel();
    }

    public float getSize(ItemStack stack) {
        return this.getGolemData(stack).getSize();
    }

    public float getPotency(ItemStack stack) {
        return this.getGolemData(stack).getPotency();
    }

    public long getCurrentXPValue(ItemStack stack) {
        return this.getGolemData(stack).getCurrentExp();
    }

    public long getXPToLevelValue(ItemStack stack) {
        return this.getGolemData(stack).getExpToLevel();
    }

    public void setLevel(ItemStack stack, int level) {
        this.getGolemData(stack).setLevel(level);
    }

    public void incrementPotencyUpgradeTally(ItemStack stack) {
        int old_tally = this.getGolemData(stack).getPotencyUpgrade();
        this.getGolemData(stack).setPotencyUpgrade(old_tally+1);
    }

    public void incrementSizeUpgradeTally(ItemStack stack) {
        int old_tally = this.getGolemData(stack).getSizeUpgrade();
        this.getGolemData(stack).setSizeUpgrade(old_tally+1);
    }

    public int getPotencyUpgradeTally(ItemStack stack) {
        return this.getGolemData(stack).getPotencyUpgrade();
    }

    public int getSizeUpgradeTally(ItemStack stack) {
        return this.getGolemData(stack).getSizeUpgrade();
    }

    public void setRecipe(ItemStack stack, String recipe_id) {

        this.getGolemData(stack).setRecipe(recipe_id);
    }

    public String getRecipe(ItemStack stack) {
        return this.getGolemData(stack).getRecipe();
    }

    public int getAvailableUpgrades(ItemStack stack) {
        int total_upgrades = this.getPotencyUpgradeTally(stack) + this.getSizeUpgradeTally(stack);
        return this.getLevel(stack) - total_upgrades - (this.hasPrestiged(stack) ? 0 : this.getPrestigeCount(stack));
    }

    public int getPrestigeCount(ItemStack stack) {
        return this.getGolemData(stack).getPrestigeUpgrade();
    }

    public void incrementPrestige(ItemStack stack) {
        if (hasAscended(stack)) {
            prestige(stack);
        }
        int old_presige = this.getPrestigeCount(stack);
        this.getGolemData(stack).setPrestigeUpgrade(old_presige + 1);
        if (old_presige + 1 == 4) {
            ascend(stack);
        }

    }

    public int getXpPrestigeMult(ItemStack stack) {
        return this.getGolemData(stack).getPrestigeMultiplier();
    }

    public long xpPrestige(ItemStack stack) {
        return (long) (this.getPrestigeCount(stack) >= 1 ? 2 : 1) * this.getXpPrestigeMult(stack);
    }

    public float sizePrestige(ItemStack stack) {
        return this.getPrestigeCount(stack) >= 2 ? 2f : 1f;
    }

    public float potencyPrestige(ItemStack stack) {
        return this.getPrestigeCount(stack) >= 3 ? 2f : 1f;
    }

    public boolean hasAscended(ItemStack stack) {
        return this.getPrestigeCount(stack) >= 4;
    }

    public boolean hasPrestiged(ItemStack stack) {
        return this.getPrestigeCount(stack) >= 5;
    }

    public void ascend(ItemStack stack) {
        float old_size = this.getSize(stack);
        float old_potency = this.getPotency(stack);
        int old_level = this.getLevel(stack);

        this.setSize(stack, old_size * 4);
        this.setPotency(stack, old_potency * 4);

        this.setLevel(stack, old_level + 10);
    }

    public void prestige(ItemStack stack) {
        this.initializePrestigedNBT(stack);
    }

    public void setSize(ItemStack stack, float size) {
        this.getGolemData(stack).setSize(size);
    }

    public void setPotency(ItemStack stack, float potency) {
        this.getGolemData(stack).setPotency(potency);
    }

    public void setCurrentXPValue(ItemStack stack, long xp) {
        this.getGolemData(stack).setCurrentExp(xp);
    }

    public void setXPToLevelValue(ItemStack stack, long xp) {
        this.getGolemData(stack).setExpToLevel(xp);
    }

    public boolean canUseRecipe(ItemStack stack, GolemProduceRecipe recipe) {
        int recipe_level = recipe.getGolem_level();
        return this.getLevel(stack) >= recipe_level;
    }

    public void doGolemLevelWork(ItemStack stack, boolean xp_boosted, float den_mult, Level pLevel) {
        long curr_xp = getCurrentXPValue(stack);
        long xp_to_level = getXPToLevelValue(stack);
        int level = getLevel(stack);
        int tier = this.TIER;
        int output_item_count = pLevel.getRecipeManager()
                .byKey(ResourceLocation.parse(this.getRecipe(stack)))
                .map(RecipeHolder::value)
                .filter(recipe -> recipe instanceof GolemProduceRecipe)
                .map(recipe -> (GolemProduceRecipe) recipe)
                .map(recipe -> recipe.getOutputs().size())
                .orElse(0);

        curr_xp += Constants.GOLEM_XP_FORMULA(xp_boosted, den_mult, this.xpPrestige(stack), output_item_count);
        while (curr_xp >= xp_to_level) {
            curr_xp -= xp_to_level;
            level++;

            xp_to_level = (long) (xp_to_level * Constants.GOLEM_LEVEL_XP_MULTIPLIER_FORMULA(tier, level));
        }
        setLevel(stack, level);
        setCurrentXPValue(stack, curr_xp);
        setXPToLevelValue(stack, xp_to_level);
    }

//    @Override
//    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
//        doGolemLevelWork(itemstack, false, 1);
//
//        return super.onBlockStartBreak(itemstack, pos, player);
//    }

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

        player.openMenu(
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

//        NetworkHooks.openScreen(
//                player,
//                new SimpleMenuProvider(
//                        (containerId, playerInventory, p) -> {
//                            GolemItemMenu menu = new GolemItemMenu(
//                                    containerId,
//                                    playerInventory
//                            );
//
//                            menu.onOpen();
//                            return menu;
//                        },
//                        Component.literal(this.asItem().getName(golem_stack).getString())
//                )
//        );
    }
}
