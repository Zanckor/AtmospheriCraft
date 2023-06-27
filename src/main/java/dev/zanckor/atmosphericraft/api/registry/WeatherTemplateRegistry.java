package dev.zanckor.atmosphericraft.api.registry;

import dev.zanckor.atmosphericraft.api.enuminterface.AbstractWeatherEvent;
import dev.zanckor.atmosphericraft.api.enuminterface.EnumWeatherEvent;

import java.util.HashMap;

/**
 * Registry for Weather handlers.
 * Weather handler allows dev to execute code for player and level depending on the weather event is running.
 */

public class WeatherTemplateRegistry {
    private static HashMap<EnumWeatherEvent, AbstractWeatherEvent> weather_event = new HashMap<>();

    public static void registerWeatherEvent(EnumWeatherEvent key) {
        weather_event.put(key, key.getWeatherEvent());
    }

    public static AbstractWeatherEvent getWeatherEvent(EnumWeatherEvent key) {
        try {
            return weather_event.get(key);
        } catch (NullPointerException e){
            throw new RuntimeException("Incorrect weather event key: " + key);
        }
    }

    public static HashMap<EnumWeatherEvent, AbstractWeatherEvent> getAllWeatherEvent(){
        return weather_event;
    }
}
