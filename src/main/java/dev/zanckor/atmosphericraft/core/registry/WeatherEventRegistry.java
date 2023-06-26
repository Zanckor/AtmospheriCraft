package dev.zanckor.atmosphericraft.core.registry;

import dev.zanckor.atmosphericraft.api.enuminterface.EnumWeatherEvent;
import dev.zanckor.atmosphericraft.api.registry.WeatherTemplateRegistry;

import java.util.Arrays;

public class WeatherEventRegistry {
    public static void register(){
        Arrays.stream(EnumWeatherEvent.values()).forEach(WeatherTemplateRegistry::registerWeatherEvent);
    }
}