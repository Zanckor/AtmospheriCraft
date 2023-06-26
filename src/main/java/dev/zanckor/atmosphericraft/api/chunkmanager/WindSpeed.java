package dev.zanckor.atmosphericraft.api.chunkmanager;

import net.minecraft.world.phys.Vec3;

public record WindSpeed(double x, double y, double z) {
    public Vec3 winSpeedToVec3(){
        return new Vec3(x, y, z);
    }
}
