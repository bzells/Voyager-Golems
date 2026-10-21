package net.jzells.voyager_golems.common.blocks.blockentity;


import net.jzells.voyager_golems.common.blocks.VoyagerBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

import static net.jzells.voyager_golems.VoyagerGolems.BLOCK_ENTITIES;
//import net.minecraftforge.eventbus.api.IEventBus;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;

public class VoyagerBlockEntities {

    public static final Supplier<BlockEntityType<GolemDenBlockEntity>>
            GOLEM_DEN_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            "golem_den",
            () -> {
                return BlockEntityType.Builder.of(
                        GolemDenBlockEntity::new,
                        VoyagerBlocks.DEN_BLOCKS.values().stream().map(DeferredBlock::get).toArray(Block[]::new)
                        // add more here
                ).build(null);
            }
    );

    private VoyagerBlockEntities() {}

    public static void register(IEventBus bus)
    {
        BLOCK_ENTITIES.register(bus);
    }
}
