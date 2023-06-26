package dev.zanckor.atmosphericraft.api.enuminterface;

import dev.zanckor.atmosphericraft.api.chunkmanager.FakeChunk;
import dev.zanckor.atmosphericraft.api.chunkmanager.Fog;
import dev.zanckor.atmosphericraft.api.chunkmanager.SkyColor;
import dev.zanckor.atmosphericraft.api.chunkmanager.enumdata.BiomeTemperature;
import dev.zanckor.atmosphericraft.api.database.LocateHash;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static dev.zanckor.atmosphericraft.AtmospheriCraft.MOD_ID;

public abstract class AbstractWeatherEvent {


    public abstract SkyColor getSkyColor();

    public abstract Fog getFog();

    public abstract void playerHandler(Level level, Player player);
    public abstract void chunkHandler(Level level, ChunkPos chunkPos);
}