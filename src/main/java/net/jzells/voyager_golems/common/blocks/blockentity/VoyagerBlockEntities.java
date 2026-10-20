package net.jzells.voyager_golems.common.blocks.blockentity;


import net.jzells.voyager_golems.common.blocks.VoyagerBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VoyagerBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(
                    ForgeRegistries.BLOCK_ENTITY_TYPES,
                    "voyager_golems"
            );

    public static final RegistryObject<BlockEntityType<GolemDenBlockEntity>>
            GOLEM_DEN_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            "golem_den",
            () -> {
                return BlockEntityType.Builder.of(
                        GolemDenBlockEntity::new,
                        VoyagerBlocks.DEN_BLOCKS.values().stream().map(RegistryObject::get).toArray(Block[]::new)
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
