package dev.zanckor.atmosphericraft.common.util;

import dev.zanckor.atmosphericraft.api.database.LocateHash;
import dev.zanckor.atmosphericraft.api.chunkmanager.FakeChunk;
import dev.zanckor.atmosphericraft.api.chunkmanager.enumdata.BiomeTemperature;
import dev.zanckor.atmosphericraft.core.recipe.FreezeRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.*;
import java.util.stream.StreamSupport;

import static dev.zanckor.atmosphericraft.api.chunkmanager.enumdata.BiomeTemperature.TEMPERED;
import static dev.zanckor.atmosphericraft.common.util.Mathematic.intBetween;

public class MCUtil {
    public static List<Class> biomeTemperatureClasses = Arrays.asList(BiomeTemperature.FrozenBiome.class, BiomeTemperature.ColdBiome.class, BiomeTemperature.TemperedBiome.class, BiomeTemperature.WarmBiome.class, BiomeTemperature.HotBiome.class);

    public static String getChunkBiomeName(Level level, BlockPos pos) {
        Holder<Biome> biomeHolder = level.getBiome(pos);

        return biomeHolder.unwrap().map((resourceKey) -> {
            return resourceKey.location().toString();
        }, (p_205367_) -> {
            return "[unregistered " + p_205367_ + "]";
        });
    }

    public static String getChunkBiomeName(Holder<Biome> biomeHolder) {
        return biomeHolder.unwrap().map((resourceKey) -> {
            return resourceKey.location().toString();
        }, (p_205367_) -> {
            return "[unregistered " + p_205367_ + "]";
        });
    }

    public static int getBiomeTemperature(ServerLevel level, BlockPos blockPos) {
        final Enum BIOME_ENUM = getBiomeEnum(level, blockPos);
        final BiomeTemperature BIOME_TYPE = getBiomeType(BIOME_ENUM);

        int biomeTemperature = (BIOME_TYPE.getMaxTemperature() + BIOME_TYPE.getMinTemperature()) / 2;

        if (level.isNight() && BIOME_TYPE.equals(BiomeTemperature.HOT)) biomeTemperature -= 20;

        return biomeTemperature;
    }

    public static BiomeTemperature getBiomeType(Enum BiomeEnum) {
        for (BiomeTemperature biomeTemperatureEnum : BiomeTemperature.values()) {
            final List<Enum> BIOME_TEMPERATURE_BIOMES = Arrays.asList(biomeTemperatureEnum.getBiomes());

            if (BIOME_TEMPERATURE_BIOMES.contains(BiomeEnum)) {
                return biomeTemperatureEnum;
            }
        }

        return TEMPERED;
    }

    public static Enum getEnum(String enumString, List<Class> enumRegistry) {
        for (Class enumClass : enumRegistry) {
            Object[] enumValues = enumClass.getEnumConstants();

            for (Object enumValue : enumValues) {
                if (enumValue.toString().equalsIgnoreCase(enumString)) {
                    return Enum.valueOf(enumClass, enumString.toUpperCase());
                }
            }
        }

        return null;
    }

    public static Enum getBiomeEnum(Level level, BlockPos blockPos) {
        String biomeName = MCUtil.getChunkBiomeName(level, blockPos);
        Enum biomeTemperature = getEnum(biomeName.substring(10), biomeTemperatureClasses);
        return biomeTemperature != null ? biomeTemperature : TEMPERED;
    }

    public static BiomeTemperature getBiomeTypeViaTemperature(int temperature) {
        for (BiomeTemperature biomeTemperature : BiomeTemperature.values()) {
            if (intBetween(biomeTemperature.getMinTemperature(), biomeTemperature.getMaxTemperature(), temperature))
                return biomeTemperature;
        }

        return TEMPERED;
    }

    public static ItemStack getFrozenFoodOf(ItemStack itemStack, Level level) {
        RecipeManager recipeManager = level.getRecipeManager();
        Optional<FreezeRecipe> freezingRecipe = recipeManager.getRecipeFor(FreezeRecipe.Type.INSTANCE, new SimpleContainer(itemStack), level);

        ItemStack frozenItemStack = freezingRecipe.map(recipe -> recipe.getResultItem(level.registryAccess())).orElse(itemStack);

        frozenItemStack.setCount(1);

        return frozenItemStack;
    }

    public static ItemStack getCookedFoodOf(ItemStack itemStack, Level level) {
        RecipeManager recipeManager = level.getRecipeManager();
        Optional<SmeltingRecipe> smeltingRecipe = recipeManager.getRecipeFor(RecipeType.SMELTING, new SimpleContainer(itemStack), level);

        //TODO: Si devuelve null (pq no se puede cocinar) devolver el antiguo
        ItemStack cookedItemStack = smeltingRecipe.map(recipe -> recipe.getResultItem(level.registryAccess())).orElse(itemStack);
        cookedItemStack.setCount(1);

        return cookedItemStack;
    }

    public static Entity getEntityByUUID(ServerLevel level, UUID uuid) {
        for (Entity entity : level.getAllEntities()) {
            if (entity.getUUID().equals(uuid)) return entity;
        }

        return null;
    }

    public static ItemEntity getItemEntityByUUID(ServerLevel level, UUID uuid) {
        final List<Entity> ITEM_ENTITIES = StreamSupport.stream(level.getAllEntities().spliterator(), false).filter(entity -> entity instanceof ItemEntity).toList();

        for (Entity entity : ITEM_ENTITIES) {
            if (entity.getUUID().equals(uuid)) return (ItemEntity) entity;
        }

        return null;
    }

    public static List<Entity> getAllItemEntities(ServerLevel level) {
        return StreamSupport.stream(level.getAllEntities().spliterator(), false).filter(entity -> entity instanceof ItemEntity).toList();
    }

    public static Entity getNearestEntity(Entity entity, List<Entity> entities) {
        float distanceToItem = Float.MAX_VALUE;
        Entity nearestEntity = null;

        for (Entity item : entities) {
            if (item.getUUID().equals(entity.getUUID())) continue;

            if (entity.distanceTo(item) < distanceToItem) {
                distanceToItem = entity.distanceTo(entity);

                nearestEntity = item;
            }
        }

        return nearestEntity;
    }

    public static List<FakeChunk> getSurroundingFakeChunk(ChunkPos chunkPos) {
        final FakeChunk FAKE_CHUNK = LocateHash.getFakeChunk(chunkPos);
        int xChunkCenter = FAKE_CHUNK.getBlockPos().getX();
        int zChunkCenter = FAKE_CHUNK.getBlockPos().getZ();

        List<FakeChunk> fakeChunkList = new ArrayList<>();

        for (int xChunk = xChunkCenter - 16; xChunk <= xChunkCenter + 16; xChunk += 16) {
            for (int zChunk = zChunkCenter - 16; zChunk <= zChunkCenter + 16; zChunk += 16) {
                fakeChunkList.add(LocateHash.getFakeChunk(new ChunkPos(new BlockPos(xChunk, 100, zChunk))));
            }
        }

        return fakeChunkList;
    }


    public static void summonItemEntity(ServerLevel level, ItemEntity itemEntity, ItemStack itemStack) {
        level.addFreshEntity(new ItemEntity(level,
                itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), itemStack,
                0, 0, 0));
    }

    public static void summonItemEntity(ServerLevel level, ItemEntity itemEntity, ItemStack itemStack, float xDelta, float yDelta, float zDelta) {
        level.addFreshEntity(new ItemEntity(level,
                itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), itemStack,
                xDelta, yDelta, zDelta));
    }
}
