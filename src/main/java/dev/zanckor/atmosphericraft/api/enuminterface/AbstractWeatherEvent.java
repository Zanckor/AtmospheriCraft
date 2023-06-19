package dev.zanckor.atmosphericraft.api.enuminterface;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class AbstractWeatherEvent {
    public abstract void handler(Level level, Player PLAYER);
}