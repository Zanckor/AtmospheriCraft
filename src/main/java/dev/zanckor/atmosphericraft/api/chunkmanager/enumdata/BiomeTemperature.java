package dev.zanckor.atmosphericraft.api.chunkmanager.enumdata;

public enum BiomeTemperature {
    FROZEN(0, 20, FrozenBiome.values()),
    COLD(21, 40, ColdBiome.values()),
    TEMPERED(41, 60, TemperedBiome.values()),
    WARM(61, 80, WarmBiome.values()),
    HOT(81, 100, HotBiome.values());

    private int minTemperature, maxTemperature;
    private Enum[] biomes;

    public int getMinTemperature() {
        return minTemperature;
    }
    public int getMaxTemperature() {
        return maxTemperature;
    }
    public Enum[] getBiomes() {
        return biomes;
    }

    private BiomeTemperature(int minTemperature, int maxTemperature, Enum[] enumList) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.biomes = enumList;
    }


    public enum FrozenBiome {
        FROZEN_OCEAN, DEEP_FROZEN_OCEAN,

        JAGGED_PEAKS, FROZEN_PEAKS,

        SNOWY_SLOPES, FROZEN_RIVER, ICE_SPIKES
    }

    public enum ColdBiome {
        COLD_OCEAN, DEEP_COLD_OCEAN, DEEP_OCEAN,

        STONY_PEAKS,

        GROVE,

        TAIGA, OLD_GROWTH_PINE_TAIGA, OLD_GROWTH_SPRUCE_TAIGA, SNOWY_TAIGA,

        SWAMP, MANGROVE_SWAMP,

        SNOWY_BEACH,

        SNOWY_PLAINS
    }

    public enum TemperedBiome {
        OCEAN,
        MEADOW,

        MUSHROOM_FIELDS,

        CHERRY_GROVE,

        WINDSWEPT_HILLS, WINDSWEPT_GRAVELLY_HILLS, WINDSWEPT_FOREST, WINDSWEPT_SAVANNA,

        FOREST, FLOWER_FOREST, BIRCH_FOREST, OLD_GROWTH_BIRCH_FOREST, DARK_FOREST,

        RIVER,

        STONY_SHORE,

        PLAINS, SUNFLOWER_PLAINS
    }

    public enum WarmBiome {
        WARM_OCEAN, LUKEWARM_OCEAN, DEEP_LUKEWARM_OCEAN,

        JUNGLE, SPARSE_JUNGLE, BAMBOO_JUNGLE,

        BEACH,

        SAVANNA, SAVANNA_PLATEAU
    }

    public enum HotBiome {
        DESERT,

        BADLANDS, WOODED_BADLANDS, ERODED_BADLANDS
    }
}
