package net.jzells.voyager_golems.datagen;

import net.jzells.voyager_golems.VoyagerGolems;
import net.jzells.voyager_golems.common.blocks.VoyagerBlocks;
import net.jzells.voyager_golems.common.items.VoyagerItems;
import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class GolemItemModelProvider extends ItemModelProvider {
    public GolemItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, VoyagerGolems.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        VoyagerGolems.ITEMS.getEntries().forEach(item -> {

                basicItem(item.get());

        });

        VoyagerGolems.BLOCK_ITEMS.getEntries().forEach(item -> {
            getBuilder(item.getId().getPath())
                    .parent(new ModelFile.UncheckedModelFile(
                            modLoc("block/" + item.getId().getPath())
                    ));
        });
    }
}
