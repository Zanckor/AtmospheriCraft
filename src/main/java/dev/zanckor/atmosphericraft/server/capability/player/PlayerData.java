package dev.zanckor.atmosphericraft.server.capability.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class PlayerData implements INBTSerializable<CompoundTag> {
    private float suffocation = 0;
    private float freezing = 0;

    public float getSuffocation() {
        return suffocation;
    }

    public void setSuffocation(float suffocation) {
        this.suffocation = suffocation;
    }

    public void addSuffocation(float suffocation) {
        this.suffocation += suffocation;
    }

    public float getFreezing() {
        return freezing;
    }

    public void setFreezing(float freezing) {
        this.freezing = freezing;
    }

    public void addFreezing(float freezing) {
        this.freezing += freezing;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("suffocation", suffocation);
        nbt.putFloat("freezing", freezing);

        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        suffocation = nbt.getFloat("suffocation");
        freezing = nbt.getFloat("freezing");
    }

    public void copyForRespawn(PlayerData oldStore){
        suffocation = oldStore.suffocation;
        freezing = oldStore.freezing;
    }
}
