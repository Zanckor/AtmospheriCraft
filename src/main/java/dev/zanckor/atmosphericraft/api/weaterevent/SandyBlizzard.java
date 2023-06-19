package dev.zanckor.atmosphericraft.api.weaterevent;

import dev.zanckor.atmosphericraft.api.enuminterface.AbstractWeatherEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SandyBlizzard extends AbstractWeatherEvent {
    @Override
    public void handler(Level level, Player PLAYER) {
        System.out.println("Player is in Sandy Blizzard");
    }
}