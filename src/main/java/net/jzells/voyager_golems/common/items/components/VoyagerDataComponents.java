package net.jzells.voyager_golems.common.items.components;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.jzells.voyager_golems.VoyagerGolems.MODID;

public class VoyagerDataComponents {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister
            .createDataComponents(Registries.DATA_COMPONENT_TYPE,MODID);

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<GolemDataComponent>> GOLEM_DATA_COMPONENT =
            DATA_COMPONENTS.registerComponentType(
            "golem_data",
            builder ->
                    builder.persistent(GolemDataComponent.CODEC).networkSynchronized(GolemDataComponent.STREAM_CODEC)
    );

    public static void register(IEventBus event) {
        GolemDataComponent.init();
        DATA_COMPONENTS.register(event);
    }
}
