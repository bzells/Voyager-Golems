package net.jzells.voyager_golems.datagen;

import net.jzells.voyager_golems.VoyagerGolems;
import net.jzells.voyager_golems.common.blocks.GolemDenBlock;
import net.jzells.voyager_golems.common.blocks.VoyagerBlocks;
import net.jzells.voyager_golems.common.items.VoyagerItems;
import net.jzells.voyager_golems.util.VoyagerWhyIsThisNotBuiltInUtil;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            if (item.get() instanceof BlockItem blockItem
                    && blockItem.getBlock() instanceof GolemDenBlock) {
                String name = item.getId().getPath()
                        .replace('_', ' ');

                Matcher matcher = Pattern.compile("(\\d+)$").matcher(name);  // what the hell does this do lol

                if (matcher.find()) {
                    int number = Integer.parseInt(matcher.group(1));

                    name = name.substring(0, matcher.start())
                            + VoyagerWhyIsThisNotBuiltInUtil.toRoman(number);
                }

                name = toTitleCase(name);

                add(item.get(), name);
            }
            else
            {
                String name = item.getId().getPath()
                        .replace('_', ' ');

                name = toTitleCase(name);

                add(item.get(), name);
            }


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
