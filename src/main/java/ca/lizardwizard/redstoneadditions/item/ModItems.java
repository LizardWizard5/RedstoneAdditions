package ca.lizardwizard.redstoneadditions.item;

import ca.lizardwizard.redstoneadditions.Redstoneadditions;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Redstoneadditions.MODID);




    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
