package net.jzells.voyager_golems.datagen;

import net.jzells.voyager_golems.VoyagerGolems;
import net.jzells.voyager_golems.common.blocks.GolemDenBlock;
import net.jzells.voyager_golems.common.blocks.VoyagerBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
//import net.minecraftforge.client.model.generators.BlockStateProvider;
//import net.minecraftforge.client.model.generators.ConfiguredModel;
//import net.minecraftforge.common.data.ExistingFileHelper;

public class GolemDenBlockStateProvider extends BlockStateProvider {

    public GolemDenBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, VoyagerGolems.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        golemDenBlockStateGen();
        // add more here
    }

    private void golemDenBlockStateGen() {
        VoyagerGolems.BLOCKS.getEntries().forEach(
                blockRegistryObject -> {
                    if (blockRegistryObject.get() instanceof GolemDenBlock)
                    {
                        registerGolemDen(blockRegistryObject.get());
                    }
                }
        );
    }

    private void registerGolemDen(Block block) {
        String name = BuiltInRegistries.BLOCK.getKey(block)
                .getPath();

        var texture = modLoc("block/" + name);

        var model = models().cubeAll(name, texture);

        getVariantBuilder(block)
                .forAllStates(state ->
                        ConfiguredModel.builder()
                                .modelFile(model)
                                .build()
                );

        simpleBlockItem(block, model);
    }
}
