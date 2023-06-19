package dev.zanckor.atmosphericraft.api.chunkmanager;

import dev.zanckor.atmosphericraft.api.enuminterface.AbstractWeatherEvent;
import dev.zanckor.atmosphericraft.api.enuminterface.EnumWeatherEvent;
import dev.zanckor.atmosphericraft.common.util.MCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class FakeChunk {
    private int temperature = 50;
    private boolean isAbleToGenerateWeatherEvent = false;
    private BlockPos blockPos;
    private AbstractWeatherEvent weatherEvent;
    private ServerLevel level;


    public FakeChunk(BlockPos blockPos, ServerLevel level) {
        this.blockPos = blockPos;
        this.temperature = MCUtil.getBiomeTemperature(level, blockPos);
        this.level = level;
    }


    public int getTemperature() {
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

    public AbstractWeatherEvent getWeatherEvent() {
        return weatherEvent;
    }

    public void setWeatherEvent(){
        for(EnumWeatherEvent event : EnumWeatherEvent.values()){
            if(event.getBiomeTypeList().contains(MCUtil.getBiomeEnum(level, blockPos))){
                weatherEvent = event.getWeatherEvent();
            }
        }
    }

    public void setWeatherEvent(AbstractWeatherEvent weatherEvent){
        this.weatherEvent = weatherEvent;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
