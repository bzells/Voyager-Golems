package net.jzells.voyager_golems.datagen;

import net.jzells.voyager_golems.VoyagerGolems;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
//import net.minecraftforge.data.event.GatherDataEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;

@EventBusSubscriber(modid = VoyagerGolems.MODID)
public class VoyagerDataGenRegistry {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();

        generator.addProvider(
                event.includeClient(),
                new GolemItemModelProvider(
                        output,
                        event.getExistingFileHelper()
                )
        );

        generator.addProvider(
                event.includeClient(),
                new GolemDenBlockStateProvider(
                        output,
                        event.getExistingFileHelper()
                )
        );

        generator.addProvider(
                event.includeClient(),
                new GolemLanguageProvider(output)
        );
    }
}
