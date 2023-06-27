package dev.zanckor.atmosphericraft.api.chunkmanager;

import dev.zanckor.atmosphericraft.api.database.TemperatureControlEnum;
import dev.zanckor.atmosphericraft.api.enuminterface.AbstractWeatherEvent;
import dev.zanckor.atmosphericraft.api.enuminterface.EnumWeatherEvent;
import dev.zanckor.atmosphericraft.common.util.MCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import static dev.zanckor.atmosphericraft.api.database.TemperatureControlEnum.NONE;

/**
 * Custom class for reduce lag. <p>
 * Instead of loading each chunk when trying to get data with Level#getChunk, copy the data useful of each chunk so when dev accessing to it, the server lag will considerable recude
 */

public class FakeChunk {
    private int temperature;
    private boolean isAbleToGenerateWeatherEvent = false;
    private BlockPos blockPos;
    private AbstractWeatherEvent weatherEvent;
    private ServerLevel level;
    public WindSpeed windSpeed;
    private TemperatureControlEnum temperatureControlEnum = NONE;


    public FakeChunk(BlockPos blockPos, ServerLevel level) {
        this.blockPos = blockPos;
        this.temperature = MCUtil.getBiomeTemperature(level, blockPos);
        this.level = level;
    }


    public int getTemperature() {
        //If there is any TemperatureControlBlock, increase or decrease temperature
        switch (temperatureControlEnum) {
            case TO_COOLER -> temperature -= 20;
            case TO_HOTTER -> temperature += 20;
        }

        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public boolean isAbleToGenerateWeatherEvent() {
        return isAbleToGenerateWeatherEvent;
    }

    public void setAbleToGenerateWeatherEvent(boolean ableToGenerateWeatherEvent) {
        isAbleToGenerateWeatherEvent = ableToGenerateWeatherEvent;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public AbstractWeatherEvent getWeatherEvent() {
        return weatherEvent;
    }


    /**
     * Apply a weather event whenever this method is called. <p>
     * This is only used for applying weather event in this chunk, not surrounding.
     */

    public void setWeatherEvent() {
        for (EnumWeatherEvent weatherEvent : EnumWeatherEvent.values()) {
            if (weatherEvent.getBiomeTypeList().contains(MCUtil.getBiomeEnum(level, blockPos))) {
                this.weatherEvent = weatherEvent.getWeatherEvent();
            }
        }
    }

    public void setWeatherEvent(AbstractWeatherEvent weatherEvent) {
        this.weatherEvent = weatherEvent;
    }

    public void changeTemperatureControlEnum(TemperatureControlEnum temperatureControlEnum){
        this.temperatureControlEnum = temperatureControlEnum;
    }
}
