package ca.lizardwizard.redstoneadditions;

import ca.lizardwizard.redstoneadditions.block.ModBlocks;
import ca.lizardwizard.redstoneadditions.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("redstoneadditions")
public class Redstoneadditions {



    // Define mod id in a common place for everything to reference
    public static final String MODID = "redstoneadditions";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Redstoneadditions(FMLJavaModLoadingContext context) {

        var modBusGroup = context.getModBusGroup();

        // Register the commonSetup method for modloading
        FMLCommonSetupEvent.getBus(modBusGroup).addListener(this::commonSetup);


        LOGGER.info("RA: Loading Redstone Additions Mod!");
        ModItems.register(modBusGroup);
        ModBlocks.register(modBusGroup);

        LOGGER.info("RA: Registered Mod Items and Blocks!");

        LOGGER.info("RA: Adding blocks to Creative Tabs!");
        // Register the item to a creative tab
        BuildCreativeModeTabContentsEvent.BUS.addListener(Redstoneadditions::addCreative);
        LOGGER.info("RA: Registered Creative Tab Mod Items and Blocks!");

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code


        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("Redstone Additions ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(ModBlocks.REDSTONE_CLOCK);
            event.accept(ModBlocks.REDSTONE_NOT_GATE);
            event.accept(ModBlocks.REDSTONE_OR_GATE);
            event.accept(ModBlocks.REDSTONE_AND_GATE);

        }
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
