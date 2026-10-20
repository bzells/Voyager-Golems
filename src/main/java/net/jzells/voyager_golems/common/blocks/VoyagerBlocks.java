package net.jzells.voyager_golems.common.blocks;

import net.jzells.voyager_golems.common.blocks.blockentity.GolemDenBlockEntity;
import net.jzells.voyager_golems.common.data.GolemDenDefinition;
import net.jzells.voyager_golems.common.data.GolemDenDefinitionRegistry;
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

    public static final Pair<RegistryObject<Block>, RegistryObject<Item>> FLORA_DEN_T1 = createGolemDenBlock("flora_den", GolemDenDefinitionRegistry.FLORA_T1, Blocks.OAK_PLANKS);
    public static final Pair<RegistryObject<Block>, RegistryObject<Item>> CAVERN_DEN_T1 = createGolemDenBlock("cavern_den", GolemDenDefinitionRegistry.CAVERN_T1, Blocks.STONE);
    public static final Pair<RegistryObject<Block>, RegistryObject<Item>> NETHER_DEN_T1 = createGolemDenBlock("nether_den", GolemDenDefinitionRegistry.NETHER_T1, Blocks.NETHERRACK);
    public static final Pair<RegistryObject<Block>, RegistryObject<Item>> ENDER_DEN_T1 = createGolemDenBlock("ender_den", GolemDenDefinitionRegistry.ENDER_T1, Blocks.END_STONE);
    public static final Pair<RegistryObject<Block>, RegistryObject<Item>> SHINY_DEN_T1 = createGolemDenBlock("shiny_den", GolemDenDefinitionRegistry.SHINY_T1, Blocks.IRON_BLOCK);
    public static final Pair<RegistryObject<Block>, RegistryObject<Item>> DARK_DEN_T1 = createGolemDenBlock("dark_den", GolemDenDefinitionRegistry.DARK_T1, Blocks.BASALT);
    public static final Pair<RegistryObject<Block>, RegistryObject<Item>> SLUDGE_DEN_T1 = createGolemDenBlock("sludge_den", GolemDenDefinitionRegistry.SLUDGE_T1, Blocks.MUD);

    public static final Pair<RegistryObject<Block>, RegistryObject<Item>> DARK_DEN_T2 = createGolemDenBlock("dark_den_ii", GolemDenDefinitionRegistry.DARK_T2, Blocks.BASALT);


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
