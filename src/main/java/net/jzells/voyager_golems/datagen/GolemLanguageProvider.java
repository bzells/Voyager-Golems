package net.jzells.voyager_golems.datagen;

import net.jzells.voyager_golems.VoyagerGolems;
import net.jzells.voyager_golems.common.blocks.VoyagerBlocks;
import net.jzells.voyager_golems.common.items.VoyagerItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class GolemLanguageProvider extends LanguageProvider {
    public GolemLanguageProvider(PackOutput output) {
        super(output, VoyagerGolems.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        VoyagerItems.ITEMS.getEntries().forEach(item -> {

                String name = item.getId().getPath()
                        .replace('_', ' ');

                name = toTitleCase(name);

                add(item.get(), name);

        });
        VoyagerBlocks.ITEMS.getEntries().forEach(item -> {

            String name = item.getId().getPath()
                    .replace('_', ' ');

            name = toTitleCase(name);

            add(item.get(), name);

        });
    }

    private String toTitleCase(String text) { // for lang gen
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : text.toCharArray()) {
            if (c == ' ') {
                result.append(c);
                capitalizeNext = true;
            } else {
                result.append(capitalizeNext
                        ? Character.toUpperCase(c)
                        : c);
                capitalizeNext = false;
            }
        }

        return result.toString();
    }


}
