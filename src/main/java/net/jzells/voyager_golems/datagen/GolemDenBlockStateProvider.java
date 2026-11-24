package net.jzells.voyager_golems.datagen;

import net.jzells.voyager_golems.VoyagerGolems;
import net.jzells.voyager_golems.common.blocks.GolemDenBlock;
import net.jzells.voyager_golems.common.blocks.VoyagerBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

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
        VoyagerBlocks.BLOCKS.getEntries().forEach(
                blockRegistryObject -> {
                    if (blockRegistryObject.get() instanceof GolemDenBlock)
                    {
                        registerGolemDen(blockRegistryObject.get());
                    }
                }
        );
    }
    private void registerGolemDen(Block block) {
        String name = block.builtInRegistryHolder()
                .key()
                .location()
                .getPath();

        String type = "";
        if(name.contains("xp")) type = "xp_"; // look, if it works it works. dont want a rewire for something as simple as this icl
        if(name.contains("production")) type = "production_";


        int tier = getTierFromName(name);
        String baseName = getBaseName(name);

        ResourceLocation baseTexture =
                modLoc("block/" + baseName);

        String overlayName = getOverlayName(type, tier);

        ResourceLocation overlayTexture =
                modLoc("block/" + overlayName);

        var model = models().withExistingParent(
                        name,
                        modLoc("block/den_layered")
                )
                .texture("all", baseTexture)
                .texture("overlay", overlayTexture)
                .texture("particle", baseTexture);

        getVariantBuilder(block)
                .forAllStates(state ->
                        ConfiguredModel.builder()
                                .modelFile(model)
                                .build()
                );

        simpleBlockItem(block, model);
    }

    private int getTierFromName(String name) {
        String[] parts = name.split("_");
        return Integer.parseInt(parts[parts.length - 1]);
    }

    private String getBaseName(String name) {
        String base = name.replaceFirst("_\\d+$", "");

        if (base.indexOf('_') != base.lastIndexOf('_')) {
            return base.substring(base.indexOf('_') + 1);
        }

        return base;
    }

    private String getOverlayName(String type, int tier) {


        return type + "den_" + tier;
    }

}
