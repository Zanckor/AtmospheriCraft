package dev.zanckor.atmosphericraft.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.zanckor.atmosphericraft.common.util.Mathematic;
import dev.zanckor.atmosphericraft.core.CustomParticleRenderType;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SandstormDust extends TextureSheetParticle {
    double xDelta, yDelta, zDelta;
    float xRot, yRot, zRot;
    float maxSize;

    protected SandstormDust(ClientLevel clientLevel, double xCoord, double yCoord, double zCoord,
                            SpriteSet spriteSet, double xDeltaMovement, double yDeltaMovement, double zDeltaMovement) {
        super(clientLevel, xCoord, yCoord, zCoord, xDeltaMovement, yDeltaMovement, zDeltaMovement);

        xDelta = xDeltaMovement;
        yDelta = yDeltaMovement;
        zDelta = zDeltaMovement;

        xRot = Mathematic.randomFromInterval(0, 0.5f);
        yRot = Mathematic.randomFromInterval(0, 0.5f);
        zRot = Mathematic.randomFromInterval(0, 0.5f);

        this.setSpriteFromAge(spriteSet);
        this.maxSize = Mathematic.randomFromInterval(10, 15);
        lifetime = 1000;
    }

    @Override
    public void tick() {
        increaseSize();

        super.tick();
    }

    private void increaseSize() {
        if (quadSize < maxSize) {
            quadSize += maxSize / 100;
        }
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        renderParticle(vertexConsumer, camera, partialTicks,
                xRot, yRot, zRot);
    }


    /**
     * Custom render particle - Rotates in x and z angles, but yRotation, so will always look forward, not upward.
     */
    public void renderParticle(VertexConsumer vertexConsumer, Camera camera, float partialTicks,
                               float xRotation, float yRotation, float zRotation) {
        for (int side = 0; side < 1; side++) {
            Vec3 vec3 = camera.getPosition();
            float xPos = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
            float yPos = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
            float zPos = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
            Quaternionf quaternionf = camera.rotation();
            quaternionf.set(0, quaternionf.y, 0, quaternionf.w);

            Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
            float f3 = getQuadSize(quadSize);

            for (int i = 0; i < 4; ++i) {
                Vector3f vector3f = avector3f[i];
                vector3f.rotate(quaternionf);
                vector3f.mul(f3);
                vector3f.add(xPos, yPos, zPos);
            }

            float f6 = this.getU0();
            float f7 = this.getU1();
            float f4 = this.getV0();
            float f5 = this.getV1();
            int j = this.getLightColor(partialTicks);
            vertexConsumer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
            vertexConsumer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f7, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
            vertexConsumer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f6, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
            vertexConsumer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f6, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();

            yRotation += 180;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CustomParticleRenderType.PARTICLE_SHEET_TRANSLUCENT_ADDITIVE;
    }


    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel clientLevel,
                                       double xCoord, double yCoord, double zCoord,
                                       double xd, double yd, double zd) {

            return new SandstormDust(clientLevel, xCoord, yCoord, zCoord, this.sprite, xd, yd, zd);
        }
    }
}
