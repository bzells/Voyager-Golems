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

        int tier = getTierFromName(name);
        String baseName = getBaseName(name);

        ResourceLocation baseTexture =
                modLoc("block/" + baseName);

        String overlayName = getOverlayName(name, tier);

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
        return name.replaceFirst("_\\d+$", "");
    }

    private String getOverlayName(String name, int tier) {
        String denName = name.replaceFirst("_\\d+$", "");

        String type = denName.substring(denName.lastIndexOf('_') + 1);

        if (type.equals("den")) {
            return "den_" + tier;
        }

        return "den_" + type + "_" + tier;
    }

}
