package dev.zanckor.atmosphericraft.common.customevent;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.entity.player.PlayerEvent;


/**
 * This event runs when player changes chunk.
 * It is Dist.DEDICATED_SERVER, won't run in client side
 */
public class PlayerChangeChunkEvent extends PlayerEvent {
    private final LevelChunk levelChunk;

    public LevelChunk getLevelChunk() {
        return levelChunk;
    }

    public PlayerChangeChunkEvent(Player player) {
        super(player);
        BlockPos playerPos = player.getOnPos();
        this.levelChunk = player.level().getChunkAt(playerPos);
    }
}
