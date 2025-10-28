package ca.lizardwizard.redstoneadditions.block;

import ca.lizardwizard.redstoneadditions.Redstoneadditions;
import ca.lizardwizard.redstoneadditions.block.custom.NotGate;
import ca.lizardwizard.redstoneadditions.block.custom.OrGate;
import ca.lizardwizard.redstoneadditions.block.custom.RedstoneClock;
import ca.lizardwizard.redstoneadditions.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.rmi.registry.Registry;
import java.util.function.Supplier;

import static net.minecraft.world.item.Items.registerBlock;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Redstoneadditions.MODID);

    public static final RegistryObject<Block> REDSTONE_CLOCK = registerBlock("redstone-clock",
            () -> new RedstoneClock(BlockBehaviour.Properties.of().strength(2f)));
    public static final  RegistryObject<Block> REDSTONE_NOT_GATE = registerBlock("redstone-not-gate",
            () -> new NotGate(BlockBehaviour.Properties.of().strength(2f)));

    public static final RegistryObject<Block> REDSTONE_OR_GATE = registerBlock("redstone-or-gate",
            () -> new OrGate(BlockBehaviour.Properties.of().strength(2f)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {

        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
