package net.jzells.voyager_golems.common.blocks.blockentity;

import net.jzells.voyager_golems.common.blocks.GolemDenBlock;
import net.jzells.voyager_golems.common.data.GolemDenDefinition;
import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.jzells.voyager_golems.common.menus.GolemDenMenu;
import net.jzells.voyager_golems.common.recipes.GolemProduceRecipe;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class GolemDenBlockEntity extends BlockEntity implements MenuProvider {

    public static final int GOLEM_SLOT = 0;
    public static final int OUTPUT_START = 1;
    public static final int OUTPUT_SLOTS = 15;
    private int progress;

    private Optional<IItemHandler> outputHandler = Optional.empty();

    private IItemHandler createOutputHandler() {
        return new RangedWrapper(
                this.inventory,
                OUTPUT_START,
                OUTPUT_START + OUTPUT_SLOTS // can only pull from output slots
        ) {
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return super.extractItem(slot, amount, simulate);
            }
        };
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

//    @Override
//    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
//        return saveWithoutMetadata(registries);
//    }

    public Optional<IItemHandler> getCapability() {
        if (outputHandler.isEmpty()) {
            outputHandler = Optional.of(this.createOutputHandler());
        }
        return outputHandler;
    }

//    @Override
//    public void invalidateCapabilities() {
//        super.invalidateCapabilities();
//    }

//    @Override
//    public void invalidateCaps() {
//        super.invalidateCaps();
//        outputHandler.invalidate(); // get rid of output handler when invalidate needs to happen
//    }

    public GolemItem getGolemItem()
    {
        if(this.getGolemStack().getItem() instanceof GolemItem golemItem)
        {
            return golemItem;
        }
        return null;
    }

    public int getRecipeProgress()
    {
        return this.progress;
    }

    public GolemProduceRecipe getCurrentRunningRecipe()
    {
        if(this.getGolemItem() == null)
        {
            return null;
        }
        ResourceLocation recipe_key = ResourceLocation.parse(this.getGolemItem().getRecipe(this.getGolemStack()));

        //        GolemProduceRecipe golemProduceRecipe = (GolemProduceRecipe) level.getRecipeManager()
//                .byKey(recipe_key)
//                .filter(recipe -> recipe instanceof GolemProduceRecipe).orElse(null);

        return (GolemProduceRecipe) level.getRecipeManager()
                .byKey(recipe_key)
                .map(RecipeHolder::value)
                .filter(r -> r instanceof GolemProduceRecipe)
                .orElse(null);

    }

    public static void tick(Level level,
                            BlockPos pos,
                            BlockState state,
                            GolemDenBlockEntity den)
    {
        if(level.isClientSide) return;

        ItemStack golem_stack = den.inventory.getStackInSlot(GOLEM_SLOT);

        if(golem_stack.isEmpty())
        {
            den.progress = 0;
            return;
        }

        SimpleContainer input = new SimpleContainer(1);
        input.setItem(GOLEM_SLOT, golem_stack);
        ResourceLocation recipe_key = ResourceLocation.parse(den.getGolemItem().getRecipe(den.getGolemStack()));

        GolemProduceRecipe golemProduceRecipe = (GolemProduceRecipe) level.getRecipeManager()
                .byKey(recipe_key)
                .map(RecipeHolder::value)
                .filter(r -> r instanceof GolemProduceRecipe)
                .orElse(null);

//        GolemProduceRecipe golemProduceRecipe = (GolemProduceRecipe) level.getRecipeManager()
//                .byKey(recipe_key)
//                .filter(recipe -> recipe instanceof GolemProduceRecipe).orElse(null);



        if(golemProduceRecipe == null)
        {
            den.progress = 0;
            return;
        }

        golemProduceRecipe = den.golemRecipeModify(golemProduceRecipe);

        den.progress++; // this will change ofc to new logic. just getting something down so i can learn how it all works

        if (den.progress >= golemProduceRecipe.getProductionPoints() && den.golemRecipeLogic(golemProduceRecipe)) {
            float golem_xp_mult = getGolemXpMult(den, golemProduceRecipe, golem_stack);

            den.progress = 0;
            den.doGolemLevelWork(golem_stack, golemProduceRecipe, golem_xp_mult);
            den.setChanged();

        }
    }

    private static float getGolemXpMult(GolemDenBlockEntity den, GolemProduceRecipe golemProduceRecipe, ItemStack golem_stack) {
        List<ItemStack> outputs = golemProduceRecipe.getOutputs();

        outputs.forEach(output -> {
            ItemStack remaining = output.copy();

            for (int slot = OUTPUT_START; slot <= OUTPUT_SLOTS && !remaining.isEmpty(); slot++) {
                remaining = den.inventory.insertItem(slot, remaining, false);
            }

            if (!remaining.isEmpty()) {

            }
        });

        GolemItem golem_item = (GolemItem) golem_stack.getItem();

        float golem_xp_mult = outputs.size() * golem_item.TIER;
        return golem_xp_mult;
    }

    private boolean golemRecipeLogic(GolemProduceRecipe golemProduceRecipe)
    {
        ItemStack golem_stack = getGolemStack();
        if(!golem_stack.isEmpty())
        {
            int recipe_level = golemProduceRecipe.getGolem_level();
            if(golem_stack.getItem() instanceof GolemItem golem_item)
            {
                boolean level_check = recipe_level <= golem_item.getLevel(golem_stack);
                boolean den_check = this.getDefinition().tier() >= golem_item.TIER;

                return den_check && level_check && isRequiredBlockPresent(golemProduceRecipe);
            }
        }
        return false;
    }

    public boolean isRequiredBlockPresent(GolemProduceRecipe golemProduceRecipe)
    {
        if(golemProduceRecipe == null)
        {
            return false;
        }
        Block golem_req_block = golemProduceRecipe.getGolemBlock();

        for(Direction direction : Direction.values())
        {
            if(!this.level.isClientSide())
            {
                if (this.level.getBlockState(this.getBlockPos().relative(direction)).is(golem_req_block))
                {

                    return true;
                }
            }
        }
        return false;
    }

    private GolemProduceRecipe golemRecipeModify(GolemProduceRecipe golemProduceRecipe)
    {
        ItemStack golem_stack = getGolemStack();
        if(golem_stack.getItem() instanceof GolemItem golem_item)
        {
            float golem_size = golem_item.getSize(golem_stack);
            float golem_potency = golem_item.getPotency(golem_stack);

            float den_output_boost = this.getDefinition().output_boost();
            float den_speed_boost = this.getDefinition().speed_boost();

            int new_duration = (int) Math.max(1, golemProduceRecipe.getProductionPoints() / (golem_potency * den_speed_boost));
            float new_output_mod = golem_size * den_output_boost;

            List<ItemStack> new_outputs = golemProduceRecipe.getOutputs().stream().map( // .map wizardry lowkey
                    output ->
                    {
                        ItemStack item_copy = output.copy();
                        item_copy.setCount(Math.max(
                                1,
                                (int) (item_copy.getCount() * new_output_mod)));
                        return item_copy;
                    }
            ).toList();

            return new GolemProduceRecipe(
                    golemProduceRecipe.getId(),
                    golemProduceRecipe.getGolemItem(),
                    golemProduceRecipe.getGolem_level(),
                    new_duration,
                    new_outputs,
                    golemProduceRecipe.getGolemBlock()
                    );
        }
        return golemProduceRecipe;
    }

    private void doGolemLevelWork(ItemStack golem_stack, GolemProduceRecipe golemProduceRecipe, float golem_mult)
    {
        GolemItem golemItem = (GolemItem) golem_stack.getItem();
        assert this.level != null;
        golemItem.doGolemLevelWork(golem_stack, isXPBoosted(), this.getDefinition().xp_boost(), this.level);
    }

    private boolean isXPBoosted()
    {
        return true;
    }

    public ItemStack getGolemStack()
    {
        return this.inventory.getStackInSlot(GOLEM_SLOT);
    }

    private final ItemStackHandler inventory = new ItemStackHandler(OUTPUT_SLOTS + 1) {
        @Override
        protected void onContentsChanged(int slot) {
            GolemDenBlockEntity.this.setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == GOLEM_SLOT) {
                return stack.getItem() instanceof GolemItem;
            }

            return slot >= OUTPUT_START && slot <= OUTPUT_SLOTS;
        }
    };

    public GolemDenBlockEntity(BlockPos pos, BlockState state) {
        super(VoyagerBlockEntities.GOLEM_DEN_BLOCK_ENTITY.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public GolemDenDefinition getDefinition() {
        return ((GolemDenBlock) getBlockState().getBlock())
                .getDefinition();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("Progress", this.progress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.load(tag, registries);
    }

    public void load(CompoundTag tag, HolderLookup.Provider registries) {
//        super.load(tag);

        if (tag.contains("Inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        }
        if(tag.contains("Progress"))
        {
            this.progress = tag.getInt("Progress");
        }
    }



    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new GolemDenMenu(pContainerId, pPlayerInventory, this);
    }

    private boolean canInsertIntoSlot(int slot, ItemStack stack) {
        ItemStack existing = inventory.getStackInSlot(slot);

        if (existing.isEmpty()) {
            return stack.getCount() <= Math.min(
                    stack.getMaxStackSize(),
                    inventory.getSlotLimit(slot)
            );
        }

        if (!ItemStack.isSameItemSameComponents(existing, stack)) {
            return false;
        }

        int maxStackSize = Math.min(
                existing.getMaxStackSize(),
                inventory.getSlotLimit(slot)
        );

        return existing.getCount() + stack.getCount() <= maxStackSize;
    }
}