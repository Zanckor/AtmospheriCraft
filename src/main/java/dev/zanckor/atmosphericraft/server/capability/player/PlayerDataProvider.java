package dev.zanckor.atmosphericraft.server.capability.player;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class PlayerDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<PlayerData> PLAYER_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerData>() {});
    private PlayerData playerData = null;
    private final LazyOptional<PlayerData> optional = LazyOptional.of(this::createData);

    private PlayerData createData() {
        if (playerData == null) {
            playerData = new PlayerData();
        }

        return playerData;
    }

    public static PlayerData getPlayer(Player player) {
        LazyOptional<PlayerData> playerData = player.getCapability(PLAYER_DATA_CAPABILITY, null);
        return playerData.orElse(null);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_DATA_CAPABILITY) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return createData().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        createData().deserializeNBT(compoundTag);
    }
}
