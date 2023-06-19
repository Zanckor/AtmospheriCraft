package dev.zanckor.atmosphericraft.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import static dev.zanckor.atmosphericraft.AtmospheriCraft.MOD_ID;

public class FreezeRecipe implements Recipe<SimpleContainer> {
    protected final RecipeType<?> type;
    protected final ResourceLocation id;
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final float experience;
    protected final int cookingTime;

    public FreezeRecipe(RecipeType<?> p_250197_, ResourceLocation p_249379_, Ingredient p_251354_, ItemStack p_252185_, float p_252165_, int p_250256_) {
        this.type = p_250197_;
        this.id = p_249379_;
        this.ingredient = p_251354_;
        this.result = p_252185_;
        this.experience = p_252165_;
        this.cookingTime = p_250256_;
    }

    public boolean matches(SimpleContainer p_43748_, Level p_43749_) {
        return this.ingredient.test(p_43748_.getItem(0));
    }

    public ItemStack assemble(SimpleContainer p_43746_, RegistryAccess p_267063_) {
        return this.result.copy();
    }

    public boolean canCraftInDimensions(int p_43743_, int p_43744_) {
        return true;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    public float getExperience() {
        return this.experience;
    }

    public ItemStack getResultItem(RegistryAccess p_266851_) {
        return this.result;
    }

    public ItemStack getResultItem() {
        return this.result;
    }


    public String getGroup() {
        return "";
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public RecipeType<?> getType() {
        return this.type;
    }

    public static class Type implements RecipeType<FreezeRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
        public static final String ID = "freeze_recipe";
    }


    public static class Serializer implements RecipeSerializer<FreezeRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(MOD_ID, "freeze_recipe");

        @Override
        public FreezeRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new FreezeRecipe(Type.INSTANCE, pRecipeId, inputs.get(0), output, 0, 0);
        }

        @Override
        public @Nullable FreezeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new FreezeRecipe(Type.INSTANCE, id, inputs.get(0), output, 0, 0);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FreezeRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);
        }
    }
}