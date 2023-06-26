package dev.zanckor.atmosphericraft.common.event;

import dev.zanckor.atmosphericraft.api.chunkmanager.FakeChunk;
import dev.zanckor.atmosphericraft.api.chunkmanager.WindSpeed;
import dev.zanckor.atmosphericraft.api.chunkmanager.enumdata.BiomeTemperature;
import dev.zanckor.atmosphericraft.api.database.LocateHash;
import dev.zanckor.atmosphericraft.common.customevent.PlayerChangeChunkEvent;
import dev.zanckor.atmosphericraft.common.util.MCUtil;
import dev.zanckor.atmosphericraft.common.util.Mathematic;
import dev.zanckor.atmosphericraft.common.util.Timer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static dev.zanckor.atmosphericraft.AtmospheriCraft.MOD_ID;
import static dev.zanckor.atmosphericraft.api.chunkmanager.enumdata.BiomeTemperature.FROZEN;
import static dev.zanckor.atmosphericraft.api.chunkmanager.enumdata.BiomeTemperature.HOT;
import static dev.zanckor.atmosphericraft.common.util.Timer.updateCooldown;

public class ChunkEventHandler {
    static ConcurrentHashMap<ChunkPos, Integer> weatherEventLifeTime = new ConcurrentHashMap<>();
    static final RandomSource RANDOM_SOURCE = RandomSource.create();


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public class weatherController {

        @SubscribeEvent
        public static void startWeatherEventOnChangeChunk(PlayerChangeChunkEvent e) {
            startWeatherEvent(e.getLevelChunk().getLevel(), e.getEntity().chunkPosition().getWorldPosition(), e.getEntity().chunkPosition(), 1, 3, null);
        }

        public static void startWeatherEvent(Level level, BlockPos chunkBlockPos, ChunkPos chunkPos, int successPercent, int successMultiplier, FakeChunk originalFakeChunk) {
            FakeChunk fakeChunk = LocateHash.getFakeChunk(chunkPos);
            if (fakeChunk != null && !fakeChunk.isAbleToGenerateWeatherEvent()) {

                final Enum BIOME_ENUM = MCUtil.getBiomeEnum(level, chunkBlockPos);
                final List<FakeChunk> SURROUNDING_CHUNKS = MCUtil.getSurroundingFakeChunk(chunkPos);
                List<FakeChunk> surroundingChunksAbleToStartWeatherEvent = addSurroundingChunksAbleToStartWeatherEvent(SURROUNDING_CHUNKS, level, BIOME_ENUM);

                //If 5 surrounding chunks are able to generate weather event and same biome, it will 100% be able, else, it has 1%
                //Add chunk to weatherEventLifeTime HashMap, so it will stop the weather event after X ticks
                successPercent = surroundingChunksAbleToStartWeatherEvent.size() >= 5 ? 100 : successPercent;

                if (Mathematic.randomBoolean(successPercent * successMultiplier)) {
                    //Adds data to fakeChunk as if able to generate weather event, adds to HashMap, and adds additional data
                    fakeChunk.setAbleToGenerateWeatherEvent(true);
                    weatherEventLifeTime.put(chunkPos, (int) Mth.randomBetween(RANDOM_SOURCE, 60 * 20, 180 * 20));
                    addAdditionalDataToChunk(originalFakeChunk, fakeChunk);

                    //Start weather event and add custom data depending on original chunk data, as wind speed
                    fakeChunk.setWeatherEvent();
                    LocateHash.putChunkHashMap(new ChunkPos(fakeChunk.getBlockPos()), fakeChunk);
                    fakeChunk.getWeatherEvent().chunkHandler(level, chunkPos);

                    //On chunk turn to be able to spawn weather event, give a % to surrounding chunks to turn them too
                    SURROUNDING_CHUNKS.forEach(chunk -> {
                        if (chunk != null) {
                            startWeatherEvent(level, chunk.getBlockPos(), new ChunkPos(chunk.getBlockPos()), 20, 1, fakeChunk);
                        }
                    });

                    //level.setBlockAndUpdate(new BlockPos(fakeChunk.getBlockPos().getX(), 100, fakeChunk.getBlockPos().getZ()), Blocks.RED_CONCRETE.defaultBlockState());
                }
            }
        }

        public static List<FakeChunk> addSurroundingChunksAbleToStartWeatherEvent(List<FakeChunk> surroundingChunks, Level level, Enum biomeEnum) {
            //Checks each surrounding chunk, and if it's able to generate weather event, and same biome of current chunk,
            //adds to final list.
            List<FakeChunk> surroundingChunksAbleToStartWeatherEvent = new ArrayList<>();

            surroundingChunks.forEach(chunk -> {
                if (chunk != null) {
                    final Enum CHUNK_BIOME_ENUM = MCUtil.getBiomeEnum(level, chunk.getBlockPos());

                    if (chunk.isAbleToGenerateWeatherEvent() && CHUNK_BIOME_ENUM.equals(biomeEnum)) {
                        surroundingChunksAbleToStartWeatherEvent.add(chunk);
                    }
                }
            });

            return surroundingChunksAbleToStartWeatherEvent;
        }

        public static void addAdditionalDataToChunk(FakeChunk originalFakeChunk, FakeChunk fakeChunk) {
            if (originalFakeChunk != null) {
                fakeChunk.windSpeed = originalFakeChunk.windSpeed;
            } else {
                fakeChunk.windSpeed = new WindSpeed(Mathematic.randomFloat(-0.005f, 0.005f), Mathematic.randomFloat(-0.002f, 0.002f), Mathematic.randomFloat(-0.005f, 0.005f));
            }
        }


        @SubscribeEvent
        public static void stopWeatherEvent(TickEvent.LevelTickEvent e) {
            if (weatherEventLifeTime != null && weatherEventLifeTime.size() > 0) {
                for (ChunkPos chunkPos : weatherEventLifeTime.keySet()) {
                    if (weatherEventLifeTime.get(chunkPos) != null) {

                        //Reduce timer for each chunk, and if it is 0 stops weather event.
                        int remainingTime = weatherEventLifeTime.get(chunkPos);

                        weatherEventLifeTime.replace(chunkPos, remainingTime - 1);

                        if (remainingTime - 1 <= 0) {
                            FakeChunk fakeChunk = LocateHash.getFakeChunk(chunkPos);

                            fakeChunk.setAbleToGenerateWeatherEvent(false);

                            LocateHash.putChunkHashMap(new ChunkPos(fakeChunk.getBlockPos()), fakeChunk);
                            weatherEventLifeTime.remove(chunkPos);
                            fakeChunk.setWeatherEvent(null);

                            //e.level.setBlock(new BlockPos(fakeChunk.getBlockPos().getX(), 100, fakeChunk.getBlockPos().getZ()), Blocks.BLUE_CONCRETE.defaultBlockState(), 1);
                        }
                    }
                }
            }
        }

        /*

        @SubscribeEvent
        public static void applyWeatherEventEffects(TickEvent.LevelTickEvent e) {
            if (weatherEventLifeTime != null && !weatherEventLifeTime.isEmpty()) {


                weatherEventLifeTime.keySet().forEach(chunkPos -> {
                    final FakeChunk FAKE_CHUNK = LocateHash.getFakeChunk(chunkPos);

                    if (FAKE_CHUNK != null && FAKE_CHUNK.getWeatherEvent() != null) {
                    }
                });
            }
        }

         */
    }


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public class chunkController {

        /**
         * <p style="text-align:center; font-size: 15px;"> Fake chunk creation </p> <hr>
         */

        private static FakeChunk createFakeChunk(ChunkPos chunkPos, ServerLevel level) {
            if (LocateHash.chunkHashMapContainsKey(chunkPos)) return LocateHash.getFakeChunk(chunkPos);

            //Copy necessary data from Chunk to FakeChunk to get faster access only if it's new
            FakeChunk fakeChunk = new FakeChunk(chunkPos.getWorldPosition(), level);
            LocateHash.putChunkHashMap(chunkPos, fakeChunk);

            return fakeChunk;
        }


        /**
         * <p style="text-align:center; font-size: 10px;"> On Load Chunk </p> <hr>
         */

        @SubscribeEvent
        public static void createFakeChunkOnLoad(ChunkEvent.Load e) {
            if (e.getLevel().getServer() != null) {
                createFakeChunk(e.getChunk().getPos(), e.getLevel().getServer().overworld());
            }
        }


        /**
         * <p style="text-align:center; font-size: 10px;"> On Update Chunk If null </p> <hr>
         */

        @SubscribeEvent
        public static void updateChunkData(TickEvent.LevelTickEvent e) {
            if (e.side.isClient()) return;
            final String TIMER_ID = "UPDATE_CHUNK_TEMPERATURE";
            final UUID TIMER_UUID = UUID.nameUUIDFromBytes(TIMER_ID.getBytes());

            //Each 10 seconds, all chunk data are updated
            if (Timer.canUseWithCooldown(TIMER_UUID, TIMER_ID, 10)) {
                updateCooldown(TIMER_UUID, TIMER_ID, 10);

                LocateHash.getChunkHashMap().entrySet().forEach(chunkEntry -> {
                    FakeChunk fakeChunk = LocateHash.getChunkHashMap().get(chunkEntry);
                    if (fakeChunk == null) {
                        fakeChunk = createFakeChunk(chunkEntry.getKey(), (ServerLevel) e.level);
                    }

                    //Update temperature based on chunk, biome, and time
                    fakeChunk.setTemperature(MCUtil.getBiomeTemperature((ServerLevel) e.level, fakeChunk.getBlockPos()));
                    chunkEntry.setValue(fakeChunk);
                });
            }
        }
    }


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public class cookItemController {

        /**
         * <p style="text-align:center; font-size: 15px;"> Cook/Froze Item </p> <hr>
         */

        @SubscribeEvent
        public static void cookItemOnBiome(TickEvent.LevelTickEvent e) {
            if (e.side.isClient() || e.level == null) return;
            final List<Entity> ITEM_ENTITY_LIST = MCUtil.getAllItemEntities((ServerLevel) e.level);
            final List<BiomeTemperature> ENUM_CONDITIONS = Arrays.asList(FROZEN, HOT);
            final ServerLevel SERVER_LEVEL = (ServerLevel) e.level;

            final String TIMER_ID = "COOK_FROZE_ITEMS";
            final UUID TIMER_UUID = UUID.nameUUIDFromBytes(TIMER_ID.getBytes());

            if (Timer.canUseWithCooldown(TIMER_UUID, TIMER_ID, 5)) {
                updateCooldown(TIMER_UUID, TIMER_ID, 5);

                //Filter by EntityType (ItemEntity), if it is added to world && age is lower than 400,
                //Then cook or froze item if age is greater than 200, and it is in correct temperature
                ITEM_ENTITY_LIST.stream().filter(entity -> {
                    ItemEntity itemEntity = (ItemEntity) entity;
                    return itemEntity.isAddedToWorld() && itemEntity.getAge() <= 400;
                }).forEach(entity -> {
                    final ItemEntity ITEM_ENTITY = (ItemEntity) entity;
                    final int BIOME_TEMPERATURE = LocateHash.getFakeChunk(ITEM_ENTITY.chunkPosition()).getTemperature();
                    final BiomeTemperature BIOME_TYPE = MCUtil.getBiomeTypeViaTemperature(BIOME_TEMPERATURE);
                    final Entity NEAREST_ITEM = MCUtil.getNearestEntity(entity, MCUtil.getAllItemEntities(SERVER_LEVEL));
                    final float DISTANCE_TO_NEAREST_ITEM = NEAREST_ITEM != null ? ITEM_ENTITY.distanceTo(NEAREST_ITEM) : 9;

                    //When 10 seconds has elapsed, turn it into his cooked/frozen item
                    //Only if nearest item is 20 blocks away
                    if (ITEM_ENTITY.getAge() > 200 && ENUM_CONDITIONS.contains(BIOME_TYPE) && DISTANCE_TO_NEAREST_ITEM > 8) {

                        ItemStack resultingItem = switch (BIOME_TYPE) {
                            case FROZEN -> MCUtil.getFrozenFoodOf(ITEM_ENTITY.getItem(), SERVER_LEVEL);
                            case HOT -> MCUtil.getCookedFoodOf(ITEM_ENTITY.getItem(), SERVER_LEVEL);
                            default -> ITEM_ENTITY.getItem();
                        };

                        //Get ItemStacks for summoning new stacks and deletes current one if it has changed
                        if (!resultingItem.getItem().equals(ITEM_ENTITY.getItem().getItem())) {
                            ItemStack stackAfterCooking = ITEM_ENTITY.getItem();
                            stackAfterCooking.setCount(ITEM_ENTITY.getItem().getCount() - 1);
                            ITEM_ENTITY.remove(Entity.RemovalReason.DISCARDED);

                            MCUtil.summonItemEntity(SERVER_LEVEL, ITEM_ENTITY, stackAfterCooking);
                            MCUtil.summonItemEntity(SERVER_LEVEL, ITEM_ENTITY, resultingItem, Mth.randomBetween(RANDOM_SOURCE, -0.2f, 0.2f), Mth.randomBetween(RANDOM_SOURCE, 0, 0.4f), Mth.randomBetween(RANDOM_SOURCE, -0.2f, 0.2f));
                        }
                    }
                });
            }
        }
    }
}