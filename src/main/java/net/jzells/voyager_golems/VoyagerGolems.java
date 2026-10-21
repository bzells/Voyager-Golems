package net.jzells.voyager_golems;

import com.mojang.logging.LogUtils;
import net.jzells.voyager_golems.common.blocks.VoyagerBlocks;
import net.jzells.voyager_golems.common.blocks.blockentity.VoyagerBlockEntities;
import net.jzells.voyager_golems.common.items.VoyagerItems;
import net.jzells.voyager_golems.common.items.components.VoyagerDataComponents;
import net.jzells.voyager_golems.common.menus.VoyagerMenus;
import net.jzells.voyager_golems.common.recipes.VoyagerRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
//import net.minecraftforge.event.server.ServerStartingEvent;
//import net.minecraftforge.eventbus.api.IEventBus;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.config.ModConfig;
//import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
//import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.RegistryObject;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(VoyagerGolems.MODID)
public class VoyagerGolems
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "voyager_golems";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(VoyagerGolems.MODID);

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(VoyagerGolems.MODID);

    public static final DeferredRegister.Items BLOCK_ITEMS =
            DeferredRegister.createItems(VoyagerGolems.MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(
                    BuiltInRegistries.MENU,
                    VoyagerGolems.MODID
            );




    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final Supplier<CreativeModeTab> GOLEMS_TAB = CREATIVE_MODE_TABS.register("voyager_golems_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> new ItemStack(VoyagerItems.SPRING_GOLEM.get()))
            .displayItems((parameters, output) -> {
                for (var items : ITEMS.getEntries()) {
                    output.accept(items.get());
                }
                for (var block_items : BLOCK_ITEMS.getEntries()) {
                    output.accept(block_items.get());
                }
//                output.accept(VoyagerItems.SPRING_GOLEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());



    public VoyagerGolems(IEventBus modEventBus, ModContainer modContainer)
    {
//        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered

        // Register the Deferred Register to the mod event bus so items get registered
        VoyagerBlocks.register(modEventBus);
        VoyagerItems.register(modEventBus);
        VoyagerBlockEntities.register(modEventBus);
        VoyagerMenus.register(modEventBus);
        VoyagerRecipes.register(modEventBus);
        VoyagerDataComponents.register(modEventBus);

        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::registerCapabilities);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
//        if (event.getTab() == GOLEMS_TAB.get()) {
//            event.accept(VoyagerItems.ELIXIR_GEL.get());
//        }
//        if (event.getTab() == GOLEMS_TAB.get()){
//            for(DeferredHolder<Item, ? extends Item> item : ITEMS.getEntries())
//            {
//                event.accept(item.get());
//                LOGGER.debug(item.get().toString());
//            }
//            for(var block_item : BLOCK_ITEMS.getEntries())
//            {
//                event.accept(block_item.get());
//                LOGGER.debug(block_item.get().toString());
//            }
//        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

//    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                VoyagerBlockEntities.GOLEM_DEN_BLOCK_ENTITY.get(),
                (BE,S) -> BE.getCapability().orElse(null)
                );
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
//    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    @EventBusSubscriber(modid = MODID)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
