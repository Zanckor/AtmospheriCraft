package dev.zanckor.atmosphericraft.api.database;

import dev.zanckor.atmosphericraft.api.chunkmanager.FakeChunk;
import net.minecraft.world.level.ChunkPos;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Class used to store FakeChunks temporary on server to easily and fast access
 */

public class LocateHash {
    private static ConcurrentHashMap<ChunkPos, FakeChunk> chunkHashMap = new ConcurrentHashMap<>();

    public static boolean chunkHashMapContainsKey(ChunkPos chunkPos){
        return chunkHashMap.containsKey(chunkPos);
    }

    public static void putChunkHashMap(ChunkPos chunkPos, FakeChunk fakeChunk){
        chunkHashMap.put(chunkPos, fakeChunk);
    }
    public static ConcurrentHashMap<ChunkPos, FakeChunk> getChunkHashMap() {
        return chunkHashMap;
    }

    public static FakeChunk getFakeChunk(ChunkPos chunkPos){
        return chunkHashMap.get(chunkPos);
    }
}
