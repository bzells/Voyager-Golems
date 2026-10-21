package net.jzells.voyager_golems.common.blocks;

import net.jzells.voyager_golems.VoyagerGolems;
import net.jzells.voyager_golems.common.blocks.blockentity.GolemDenBlockEntity;
import net.jzells.voyager_golems.common.data.GolemDenDefinition;
import net.jzells.voyager_golems.common.data.GolemDenDefinitionRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
//import net.minecraftforge.eventbus.api.IEventBus;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.DeferredItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static net.jzells.voyager_golems.VoyagerGolems.*;

public class VoyagerBlocks {
    public static final Map<String,DeferredBlock<GolemDenBlock>> DEN_BLOCKS = new HashMap<>();

    public static final Pair<DeferredBlock<GolemDenBlock>, DeferredItem<Item>> FLORA_DEN_T1 = createGolemDenBlock("flora_den", GolemDenDefinitionRegistry.FLORA_T1, Blocks.OAK_PLANKS);
    public static final Pair<DeferredBlock<GolemDenBlock>, DeferredItem<Item>> CAVERN_DEN_T1 = createGolemDenBlock("cavern_den", GolemDenDefinitionRegistry.CAVERN_T1, Blocks.STONE);
    public static final Pair<DeferredBlock<GolemDenBlock>, DeferredItem<Item>> NETHER_DEN_T1 = createGolemDenBlock("nether_den", GolemDenDefinitionRegistry.NETHER_T1, Blocks.NETHERRACK);
    public static final Pair<DeferredBlock<GolemDenBlock>, DeferredItem<Item>> ENDER_DEN_T1 = createGolemDenBlock("ender_den", GolemDenDefinitionRegistry.ENDER_T1, Blocks.END_STONE);
    public static final Pair<DeferredBlock<GolemDenBlock>, DeferredItem<Item>> SHINY_DEN_T1 = createGolemDenBlock("shiny_den", GolemDenDefinitionRegistry.SHINY_T1, Blocks.IRON_BLOCK);
    public static final Pair<DeferredBlock<GolemDenBlock>, DeferredItem<Item>> DARK_DEN_T1 = createGolemDenBlock("dark_den", GolemDenDefinitionRegistry.DARK_T1, Blocks.BASALT);
    public static final Pair<DeferredBlock<GolemDenBlock>, DeferredItem<Item>> SLUDGE_DEN_T1 = createGolemDenBlock("sludge_den", GolemDenDefinitionRegistry.SLUDGE_T1, Blocks.MUD);

    public static final Pair<DeferredBlock<GolemDenBlock>, DeferredItem<Item>> DARK_DEN_T2 = createGolemDenBlock("dark_den_ii", GolemDenDefinitionRegistry.DARK_T2, Blocks.BASALT);


    private static Pair<DeferredBlock<GolemDenBlock>, DeferredItem<Item>> createGolemDenBlock(String id, GolemDenDefinition definition, Block blockBehaviour)
    {
        DeferredBlock<GolemDenBlock> return_block = BLOCKS.register(id,
                () ->
                new GolemDenBlock(BlockBehaviour.Properties.ofFullCopy(blockBehaviour), definition));

        DeferredItem<Item> return_item = BLOCK_ITEMS.register(id,
                () ->
                new BlockItem(return_block.get(), new Item.Properties()));

        DEN_BLOCKS.put(id, return_block);

        return new Pair<>(return_block, return_item);
    }

    private static Supplier<BlockEntityType<GolemDenBlockEntity>> registerGolemDenBlockEntity(
            String id,
            DeferredBlock<Block> block
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
        BLOCK_ITEMS.register(bus);
        BLOCKS.register(bus);
//        BLOCK_ENTITIES.register(bus);
    }


    private VoyagerBlocks() {}
}
