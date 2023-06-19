package dev.zanckor.atmosphericraft.common.customevent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;

public class PlayerOnBiomeEvent {
    public static class OnFrozen extends TickEvent {
        public Player player;
        public LevelChunk chunkLevel;

        public OnFrozen(Type type, LogicalSide side, Phase phase, Player player) {
            super(type, side, phase);
            this.player = player;
            this.chunkLevel = player.level().getChunkAt(player.getOnPos());

        }
    }

    public static class OnCold extends TickEvent {
        public Player player;
        public LevelChunk chunkLevel;

        public OnCold(Type type, LogicalSide side, Phase phase, Player player) {
            super(type, side, phase);
            this.player = player;
            this.chunkLevel = player.level().getChunkAt(player.getOnPos());
        }
    }

    public static class OnTempered extends TickEvent {
        public Player player;
        public LevelChunk chunkLevel;

        public OnTempered(Type type, LogicalSide side, Phase phase, Player player) {
            super(type, side, phase);
            this.player = player;
            this.chunkLevel = player.level().getChunkAt(player.getOnPos());
        }
    }

    public static class OnWarm extends TickEvent {
        public Player player;
        public LevelChunk chunkLevel;

        public OnWarm(Type type, LogicalSide side, Phase phase, Player player) {
            super(type, side, phase);
            this.player = player;
            this.chunkLevel = player.level().getChunkAt(player.getOnPos());
        }
    }

    public static class OnHot extends TickEvent {
        public Player player;
        public LevelChunk chunkLevel;

        public OnHot(Type type, LogicalSide side, Phase phase, Player player) {
            super(type, side, phase);
            this.player = player;
            this.chunkLevel = player.level().getChunkAt(player.getOnPos());
        }
    }
}
