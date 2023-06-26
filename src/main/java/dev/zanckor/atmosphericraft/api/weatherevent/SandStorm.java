package dev.zanckor.atmosphericraft.api.weatherevent;

import dev.zanckor.atmosphericraft.api.chunkmanager.FakeChunk;
import dev.zanckor.atmosphericraft.api.chunkmanager.Fog;
import dev.zanckor.atmosphericraft.api.chunkmanager.SkyColor;
import dev.zanckor.atmosphericraft.api.database.LocateHash;
import dev.zanckor.atmosphericraft.api.enuminterface.AbstractWeatherEvent;
import dev.zanckor.atmosphericraft.common.util.Mathematic;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import static dev.zanckor.atmosphericraft.core.registry.ParticleRegistry.SANDSTORM_DUST;

public class SandStorm extends AbstractWeatherEvent {
    final SkyColor SKY_COLOR = new SkyColor(0.71f, 0.50f, 0.24f);
    final Fog FOG = new Fog(0.8f, 15);

    @Override
    public SkyColor getSkyColor() {
        return SKY_COLOR;
    }

    @Override
    public Fog getFog() {
        return FOG;
    }

    @Override
    public void playerHandler(Level level, Player player) {
        final FakeChunk FAKE_CHUNK = LocateHash.getFakeChunk(player.chunkPosition());

        //Adds wind movement to player
        player.addDeltaMovement(FAKE_CHUNK.windSpeed.winSpeedToVec3());

        //TODO: USAR ESTO PARA CHECAR SI ESTÁ BAJO EL SUELO
        //System.out.println(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, player.blockPosition()));
    }

    @Override
    public void chunkHandler(Level level, ChunkPos chunkPos) {
        for (int rotation = 0; rotation < 360; rotation += 20) {
            final FakeChunk FAKE_CHUNK = LocateHash.getFakeChunk(chunkPos);
            int distance = (int) Mathematic.randomFloat(1, 10);

            level.addParticle(SANDSTORM_DUST.get(),
                    Mathematic.randomFromInterval(chunkPos.getMinBlockX(), chunkPos.getMinBlockX() + 15),
                    100 + 1,
                    Mathematic.randomFromInterval(chunkPos.getMinBlockZ(), chunkPos.getMinBlockZ() + 15),
                    Math.cos(rotation) * distance,
                    0,
                    Math.cos(rotation) * distance);

        }
    }
}