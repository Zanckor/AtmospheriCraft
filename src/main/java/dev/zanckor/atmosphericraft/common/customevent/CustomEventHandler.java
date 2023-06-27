package dev.zanckor.atmosphericraft.common.customevent;

import dev.zanckor.atmosphericraft.api.chunkmanager.enumdata.BiomeTemperature;
import dev.zanckor.atmosphericraft.common.util.MCUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.UUID;

import static dev.zanckor.atmosphericraft.AtmospheriCraft.MOD_ID;

/**
 * Class made to manage custom events
 */

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CustomEventHandler {
    static HashMap<UUID, ChunkPos> playerChunkPos = new HashMap<>();
    public static HashMap<UUID, Integer> playerTemperature = new HashMap<>();


    /**
     * When player changes chunk, execute PlayerChangeChunkEvent
     */
    @SubscribeEvent
    public static void onPlayerChangeChunk(PlayerEvent e) {
        if (e.getEntity() == null || !e.getEntity().isAddedToWorld()) return;

        Player player = e.getEntity();
        UUID playerUUID = player.getUUID();
        ChunkPos currentPos = player.chunkPosition();

        if (!playerChunkPos.containsKey(playerUUID) || !playerChunkPos.get(playerUUID).equals(currentPos)) {
            if (!playerChunkPos.containsKey(playerUUID)) playerChunkPos.put(playerUUID, player.chunkPosition());

            PlayerChangeChunkEvent event = new PlayerChangeChunkEvent(player);
            playerChunkPos.replace(player.getUUID(), player.chunkPosition());
            MinecraftForge.EVENT_BUS.post(event);
        }
    }

    /**
     * Depending on what biome temperature player is in, execute his event. <p>
     * By default is Tempered biome, cz if its an error player won't receive damage.
     */
    @SubscribeEvent
    public static void playerOnBiomeEvent(TickEvent.PlayerTickEvent e) {
        if (e.player == null || playerTemperature.get(e.player.getUUID()) == null) return;
        Event biomeEvent;
        int temperature = playerTemperature.get(e.player.getUUID());
        BiomeTemperature biomeTemperature = MCUtil.getBiomeTypeViaTemperature(temperature);

        switch (biomeTemperature) {
            case FROZEN -> biomeEvent = new PlayerOnBiomeEvent.OnFrozen(e.type, e.side, e.phase, e.player);
            case COLD -> biomeEvent = new PlayerOnBiomeEvent.OnCold(e.type, e.side, e.phase, e.player);
            default -> biomeEvent = new PlayerOnBiomeEvent.OnTempered(e.type, e.side, e.phase, e.player);
            case WARM -> biomeEvent = new PlayerOnBiomeEvent.OnWarm(e.type, e.side, e.phase, e.player);
            case HOT -> biomeEvent = new PlayerOnBiomeEvent.OnHot(e.type, e.side, e.phase, e.player);
        }

        MinecraftForge.EVENT_BUS.post(biomeEvent);
    }
}