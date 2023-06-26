package dev.zanckor.atmosphericraft.core.registry;

import dev.zanckor.atmosphericraft.core.recipe.FreezeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static dev.zanckor.atmosphericraft.AtmospheriCraft.MOD_ID;

public class RecipeRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);

    public static final RegistryObject<RecipeSerializer<FreezeRecipe>> FREEZE_RECIPE_SERIALIZER =
            RECIPE_SERIALIZER.register("freeze_recipe", () -> FreezeRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);
    }
}
