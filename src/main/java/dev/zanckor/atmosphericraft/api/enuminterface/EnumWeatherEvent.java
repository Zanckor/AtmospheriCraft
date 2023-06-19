package dev.zanckor.atmosphericraft.api.enuminterface;

import dev.zanckor.atmosphericraft.api.weaterevent.SandyBlizzard;
import dev.zanckor.atmosphericraft.api.chunkmanager.enumdata.BiomeTemperature.HotBiome;

import java.util.Arrays;
import java.util.List;

public enum EnumWeatherEvent {

    /*
    INTENSE_RAIN,
    RADIOACTIVE_RAIN,
    THICK_FOG,
    HAIL,
    STRONG_BLIZZARD,
    SNOWY_BLIZZARD,
    NORTHERN_LIGHTS,
    ECLIPSE,
    DROUGHT,
    VOLCANIC_STORM;
     */

    SANDY_BLIZZARD(new SandyBlizzard(),
            Arrays.asList(HotBiome.DESERT, HotBiome.ERODED_BADLANDS, HotBiome.BADLANDS));

    AbstractWeatherEvent weatherEvent;
    List<Enum> biomeTypeList;

    EnumWeatherEvent(AbstractWeatherEvent weatherEvent, List<Enum> biomeTypeList) {
        this.weatherEvent = weatherEvent;
        this.biomeTypeList = biomeTypeList;
    }

    public AbstractWeatherEvent getWeatherEvent() {
        return weatherEvent;
    }

    public List<Enum> getBiomeTypeList(){
        return biomeTypeList;
    }
}
