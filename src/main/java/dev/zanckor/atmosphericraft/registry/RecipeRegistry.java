package dev.zanckor.atmosphericraft.registry;

import dev.zanckor.atmosphericraft.common.recipe.FreezeRecipe;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
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
