package net.jzells.voyager_golems.common.blocks;

import net.jzells.voyager_golems.common.blocks.blockentity.GolemDenBlockEntity;
import net.jzells.voyager_golems.common.data.GolemDenDefinition;
import net.jzells.voyager_golems.common.data.GolemDenDefinitionRegistry;
import net.jzells.voyager_golems.util.Constants;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoyagerBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(
                    ForgeRegistries.BLOCKS,
                    "voyager_golems"
            );

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(
                    ForgeRegistries.ITEMS,
                    "voyager_golems"
            );

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(
                    ForgeRegistries.BLOCK_ENTITY_TYPES,
                    "voyager_golems"
            );
    public static final Map<String,RegistryObject<Block>> DEN_BLOCKS = new HashMap<>();

    private static final Map<Constants.GOLEM_GROUPS, Block> DEN_MATERIALS = Map.of(
            Constants.GOLEM_GROUPS.FLORA, Blocks.OAK_PLANKS,
            Constants.GOLEM_GROUPS.CAVERN, Blocks.STONE,
            Constants.GOLEM_GROUPS.NETHER, Blocks.NETHERRACK,
            Constants.GOLEM_GROUPS.ENDER, Blocks.END_STONE,
            Constants.GOLEM_GROUPS.SHINY, Blocks.IRON_BLOCK,
            Constants.GOLEM_GROUPS.DARK, Blocks.BASALT,
            Constants.GOLEM_GROUPS.SLUDGE, Blocks.MUD
    );

    static {
        for (int i = 0; i < 2; i++) {
            int tier = i + 1;
            for (Constants.GOLEM_GROUPS group : Constants.GOLEM_GROUPS.values()) {
                Block material = DEN_MATERIALS.get(group);

                if (material == null) continue;

                String name = group.getId() + "_den_" + tier;

                createGolemDenBlock(
                        name,
                        GolemDenDefinitionRegistry.getDenDefinition(tier, group.getId(), "base"),
                        material
                );

//                createGolemDenBlock(
//                        "xp_" + name,
//                        GolemDenDefinitionRegistry.getDenDefinition(tier, group.getId(), "xp"),
//                        material
//                );
            }
        }

    }


    private static Pair<RegistryObject<Block>, RegistryObject<Item>> createGolemDenBlock(String id, GolemDenDefinition definition, Block blockBehaviour)
    {
        RegistryObject<Block> return_block = BLOCKS.register(id,
                () ->
                new GolemDenBlock(BlockBehaviour.Properties.copy(blockBehaviour), definition));

        RegistryObject<Item> return_item = ITEMS.register(id,
                () ->
                new BlockItem(return_block.get(), new Item.Properties()));

        DEN_BLOCKS.put(id, return_block);

        return new Pair<>(return_block, return_item);
    }

    private static RegistryObject<BlockEntityType<GolemDenBlockEntity>> registerGolemDenBlockEntity(
            String id,
            RegistryObject<Block> block
    ) {
        return BLOCK_ENTITIES.register(
                id,
                () -> BlockEntityType.Builder.of(
                        GolemDenBlockEntity::new,
                        block.get()
                ).build(null)
        );
    }

    public static void register(IEventBus bus)
    {
        ITEMS.register(bus);
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }


    private VoyagerBlocks() {}
}
