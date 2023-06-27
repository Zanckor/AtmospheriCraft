package dev.zanckor.atmosphericraft.core.registry;

import dev.zanckor.atmosphericraft.api.database.TemperatureControlEnum;
import dev.zanckor.atmosphericraft.core.block.TemperatureControlBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static dev.zanckor.atmosphericraft.AtmospheriCraft.MOD_ID;
import static dev.zanckor.atmosphericraft.core.registry.ItemRegistry.FoodRegistry.FOOD_ITEMS;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCK = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static final RegistryObject<Block> IGNIS_STONE = registerBlock("ignis_stone",
            () -> new TemperatureControlBlock(BlockBehaviour.Properties.of(),

                    TemperatureControlEnum.TO_HOTTER));

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> registryObject = BLOCK.register(name, block);
        // registerBlockItem(name, block);

        return registryObject;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, Supplier<T> block) {
        return ItemRegistry.BlockRegistry.BLOCK_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCK.register(eventBus);
    }
}
