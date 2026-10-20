package net.jzells.voyager_golems.common.menus;


import net.jzells.voyager_golems.common.blocks.blockentity.GolemDenBlockEntity;
import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class GolemDenMenu extends AbstractContainerMenu {

    private final GolemDenBlockEntity blockEntity;

    private int client_recipe_progress;

    private final DataSlot recipeProgress = new DataSlot() {
        @Override
        public int get() {
            return blockEntity.getRecipeProgress();
        }

        @Override
        public void set(int value) {
            client_recipe_progress = value;
        }
    };

    public GolemDenMenu(
            int containerId,
            Inventory playerInventory,
            GolemDenBlockEntity blockEntity
    ) {
        super(VoyagerMenus.GOLEM_DEN_MENU.get(), containerId);

        this.blockEntity = blockEntity;

        // data slot for syncing client and server (just figured this out today(oct 17th 26), so i have to refactor a lot of code...)
        addDataSlot(recipeProgress);

        // golem slot
        addSlot(new SlotItemHandler(
                blockEntity.getInventory(),
                GolemDenBlockEntity.GOLEM_SLOT,
                30, 29
        ) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                if (!(stack.getItem() instanceof GolemItem golem)) {
                    return false;
                }

                String requiredGroup =
                        GolemDenMenu.this.blockEntity
                                .getDefinition()
                                .group()
                                .getId();

                return golem.GROUPING.equals(requiredGroup);
            }
        });

        // output
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 5; col++) {
                int slotIndex =
                        GolemDenBlockEntity.OUTPUT_START + row * 5 + col;

                addSlot(new SlotItemHandler(
                        blockEntity.getInventory(),
                        slotIndex,
                        80 + col * 18,
                        8 + row * 18
                ) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }
                });
            }
        }

        // players inv
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(
                        playerInventory,
                        col + row * 9 + 9,
                        8 + col * 18,
                        84 + row * 18
                ));
            }
        }

        // player hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(
                    playerInventory,
                    col,
                    8 + col * 18,
                    142
            ));
        }
    }

    public int getRecipeProgress() {
        return client_recipe_progress;
    }

    public GolemDenMenu(
            int containerId,
            Inventory playerInventory,
            FriendlyByteBuf data
    ) {
        this(
                containerId,
                playerInventory,
                (GolemDenBlockEntity) playerInventory.player.level()
                        .getBlockEntity(data.readBlockPos())
        );
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity != null
                && blockEntity.getBlockPos().distToCenterSqr(
                player.position()
        ) <= 64.0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (!slot.hasItem()) {
            return empty;
        }

        ItemStack original = slot.getItem();
        ItemStack copy = original.copy();

        int blockInventorySize = 1 + GolemDenBlockEntity.OUTPUT_SLOTS;
        int playerInventoryEnd = slots.size();

        if (index < blockInventorySize) {
            if (!moveItemStackTo(
                    original,
                    blockInventorySize,
                    playerInventoryEnd,
                    true
            )) {
                return empty;
            }
        } else {
            if (original.getItem() instanceof GolemItem
                    && slots.get(0).mayPlace(original)) {
                if (!moveItemStackTo(original, 0, 1, false)) {
                    return empty;
                }
            } else {
                return empty;
            }
        }

        if (original.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (original.getCount() == copy.getCount()) {
            return empty;
        }

        slot.onTake(player, original);
        return copy;
    }

    public GolemDenBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
