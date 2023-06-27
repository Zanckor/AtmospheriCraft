package dev.zanckor.atmosphericraft.common.event;

import dev.zanckor.atmosphericraft.api.chunkmanager.FakeChunk;
import dev.zanckor.atmosphericraft.api.database.LocateHash;
import dev.zanckor.atmosphericraft.common.customevent.CustomEventHandler;
import dev.zanckor.atmosphericraft.common.customevent.PlayerOnBiomeEvent;
import dev.zanckor.atmosphericraft.server.capability.player.PlayerData;
import dev.zanckor.atmosphericraft.server.capability.player.PlayerDataProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static dev.zanckor.atmosphericraft.AtmospheriCraft.MOD_ID;
import static dev.zanckor.atmosphericraft.api.chunkmanager.enumdata.BiomeTemperature.HOT;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerOnFrozenBiome(PlayerOnBiomeEvent.OnFrozen e) {

    }

    @SubscribeEvent
    public static void onPlayerOnHotBiome(PlayerOnBiomeEvent.OnHot e) {
        final Player PLAYER = e.player;
        final int PLAYER_TEMPERATURE = CustomEventHandler.playerTemperature.get(PLAYER.getUUID());
        final PlayerData PLAYER_DATA = PlayerDataProvider.getPlayer(PLAYER);

        //Only run if temperature is considered HOT, it may not if there is any meteorological phenomenon or its night
        if (PLAYER_TEMPERATURE > HOT.getMinTemperature() && !PLAYER.isDeadOrDying()) {
            //TODO: Apply suffocation effect just if player has no adequate equipment
            PLAYER_DATA.addSuffocation(0.1f);
            PLAYER.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20, 0));
        }
    }


    /**
     * Applies different effects via calling WeatherEvent#playerHandler
     */
    @SubscribeEvent
    public static void applyWeatherEventEffects(TickEvent.PlayerTickEvent e) {
        final Player PLAYER = e.player;
        final Level LEVEL = PLAYER.level();
        final FakeChunk FAKE_CHUNK = LocateHash.getFakeChunk(PLAYER.chunkPosition());

        if (FAKE_CHUNK != null && FAKE_CHUNK.getWeatherEvent() != null) {
            FAKE_CHUNK.getWeatherEvent().playerHandler(LEVEL, PLAYER);
        }
    }
}
