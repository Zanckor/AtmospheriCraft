package dev.zanckor.atmosphericraft.registry;

import dev.zanckor.atmosphericraft.common.item.food.FrozenFood;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static dev.zanckor.atmosphericraft.AtmospheriCraft.MOD_ID;
import static dev.zanckor.atmosphericraft.registry.ItemRegistry.FoodRegistry.FOOD_ITEMS;
import static dev.zanckor.atmosphericraft.registry.ItemRegistry.MineralRegistry.MINERAL_ITEMS;

public class ItemRegistry {
    public static class FoodRegistry {
        public static final DeferredRegister<Item> FOOD_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

        public static final RegistryObject<Item> FROZEN_APPLE = FOOD_ITEMS.register("frozen_apple",
                () -> new FrozenFood(new Item.Properties().food(new FoodProperties.Builder().nutrition(getNutritionOfFrozenFood(Items.APPLE)).build())));
        public static final RegistryObject<Item> FROZEN_BEEF = FOOD_ITEMS.register("frozen_beef",
                () -> new FrozenFood(new Item.Properties().food(new FoodProperties.Builder().nutrition(getNutritionOfFrozenFood(Items.BEEF)).build())));
        public static final RegistryObject<Item> FROZEN_CARROT = FOOD_ITEMS.register("frozen_carrot",
                () -> new FrozenFood(new Item.Properties().food(new FoodProperties.Builder().nutrition(getNutritionOfFrozenFood(Items.CARROT)).build())));
        public static final RegistryObject<Item> FROZEN_CHICKEN = FOOD_ITEMS.register("frozen_chicken",
                () -> new FrozenFood(new Item.Properties().food(new FoodProperties.Builder().nutrition(getNutritionOfFrozenFood(Items.CHICKEN)).build())));
        public static final RegistryObject<Item> FROZEN_COD = FOOD_ITEMS.register("frozen_cod",
                () -> new FrozenFood(new Item.Properties().food(new FoodProperties.Builder().nutrition(getNutritionOfFrozenFood(Items.COD)).build())));
        public static final RegistryObject<Item> FROZEN_MUTTON = FOOD_ITEMS.register("frozen_mutton",
                () -> new FrozenFood(new Item.Properties().food(new FoodProperties.Builder().nutrition(getNutritionOfFrozenFood(Items.MUTTON)).build())));
        public static final RegistryObject<Item> FROZEN_PORKCHOP = FOOD_ITEMS.register("frozen_porkchop",
                () -> new FrozenFood(new Item.Properties().food(new FoodProperties.Builder().nutrition(getNutritionOfFrozenFood(Items.PORKCHOP)).build())));
        public static final RegistryObject<Item> FROZEN_POTATO = FOOD_ITEMS.register("frozen_potato",
                () -> new FrozenFood(new Item.Properties().food(new FoodProperties.Builder().nutrition(getNutritionOfFrozenFood(Items.POTATO)).build())));
        public static final RegistryObject<Item> FROZEN_RABBIT = FOOD_ITEMS.register("frozen_rabbit",
                () -> new FrozenFood(new Item.Properties().food(new FoodProperties.Builder().nutrition(getNutritionOfFrozenFood(Items.RABBIT)).build())));
        public static final RegistryObject<Item> FROZEN_ROTTEN_FLESH = FOOD_ITEMS.register("frozen_rotten_flesh",
                () -> new FrozenFood(new Item.Properties().food(new FoodProperties.Builder().nutrition(getNutritionOfFrozenFood(Items.ROTTEN_FLESH)).build())));
        public static final RegistryObject<Item> FROZEN_SALMON = FOOD_ITEMS.register("frozen_salmon",
                () -> new FrozenFood(new Item.Properties().food(new FoodProperties.Builder().nutrition(getNutritionOfFrozenFood(Items.SALMON)).build())));
        public static final RegistryObject<Item> FROZEN_SWEET_BERRIES = FOOD_ITEMS.register("frozen_sweet_berries",
                () -> new FrozenFood(new Item.Properties().food(new FoodProperties.Builder().nutrition(getNutritionOfFrozenFood(Items.SWEET_BERRIES)).build())));
        public static final RegistryObject<Item> FROZEN_TROPICAL_FISH = FOOD_ITEMS.register("frozen_tropical_fish",
                () -> new FrozenFood(new Item.Properties().food(new FoodProperties.Builder().nutrition(getNutritionOfFrozenFood(Items.TROPICAL_FISH)).build())));
        public static final RegistryObject<Item> FROZEN_WATERMELON = FOOD_ITEMS.register("frozen_watermelon",
                () -> new FrozenFood(new Item.Properties().food(new FoodProperties.Builder().nutrition(getNutritionOfFrozenFood(Items.MELON_SLICE)).build())));


        public static int getNutritionOfFrozenFood(Item item){
            return item.getFoodProperties().getNutrition() / 2;
        }
    }

    public static class MineralRegistry {
        public static final DeferredRegister<Item> MINERAL_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

        public static final RegistryObject<Item> PIECE_OF_ICE = MINERAL_ITEMS.register("ice",
                () -> new Item(new Item.Properties()));
    }

    public static void addItemsToTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.FOOD_AND_DRINKS)) {
            FOOD_ITEMS.getEntries().forEach(event::accept);
        }

        if(event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)){
            MINERAL_ITEMS.getEntries().forEach(event::accept);
        }
    }

    public static void register(IEventBus eventBus) {
        FOOD_ITEMS.register(eventBus);
        MINERAL_ITEMS.register(eventBus);
    }
}
