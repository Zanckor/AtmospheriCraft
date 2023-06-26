package dev.zanckor.atmosphericraft.client.event;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sun.jna.platform.win32.OpenGL32;
import dev.zanckor.atmosphericraft.api.chunkmanager.FakeChunk;
import dev.zanckor.atmosphericraft.api.chunkmanager.Fog;
import dev.zanckor.atmosphericraft.api.chunkmanager.SkyColor;
import dev.zanckor.atmosphericraft.api.database.LocateHash;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static dev.zanckor.atmosphericraft.AtmospheriCraft.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class VFXEvent {
    static SkyColor previousSkyColor = new SkyColor(0.7F, 0.7F, 1.0F);
    static Fog previousFog = new Fog(0, 30);

    @SubscribeEvent
    public static void fog(ViewportEvent.RenderFog e) {
        final FakeChunk FAKE_CHUNK = LocateHash.getFakeChunk(e.getCamera().getEntity().chunkPosition());

        if (FAKE_CHUNK != null && previousFog.density() > 0) {
            final SkyColor SKY_COLOR = (FAKE_CHUNK.getWeatherEvent() != null) ? FAKE_CHUNK.getWeatherEvent().getSkyColor() : previousSkyColor;
            final float farPlaneDistance = FAKE_CHUNK.getWeatherEvent() != null ? FAKE_CHUNK.getWeatherEvent().getFog().distance() : 0;

            e.setNearPlaneDistance(-10);
            e.setFarPlaneDistance(farPlaneDistance);

            RenderSystem.setShaderFogStart(-10);
            RenderSystem.setShaderFogEnd(farPlaneDistance);
            RenderSystem.setShaderFogColor(previousSkyColor.red(), previousSkyColor.green(), previousSkyColor.blue(), previousFog.density());

            e.setFogShape(FogShape.SPHERE);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
        }
    }

    @SubscribeEvent
    public static void changeSkyColor(ViewportEvent.ComputeFogColor e) {
        final FakeChunk FAKE_CHUNK = LocateHash.getFakeChunk(e.getCamera().getEntity().chunkPosition());

        //TODO: Opcion para quitar lag, si está activo cambiar al instante, no smooth

        if (FAKE_CHUNK != null) {
            final SkyColor SKY_COLOR = (FAKE_CHUNK.getWeatherEvent() != null) ? FAKE_CHUNK.getWeatherEvent().getSkyColor() : new SkyColor(e.getRed(), e.getGreen(), e.getBlue());
            final Fog FOG = (FAKE_CHUNK.getWeatherEvent() != null) ? FAKE_CHUNK.getWeatherEvent().getFog() : new Fog(Integer.MAX_VALUE, Integer.MAX_VALUE);

            float redDifference = Math.abs(previousSkyColor.red() - SKY_COLOR.red());
            float greenDifference = Math.abs(previousSkyColor.green() - SKY_COLOR.green());
            float blueDifference = Math.abs(previousSkyColor.blue() - SKY_COLOR.blue());

            float redChange = redDifference > 0.1f ? Math.max(SKY_COLOR.red() / ((previousSkyColor.red()) * 10) / 25, 0.01f) : 0;
            float greenChange = greenDifference > 0.1f ? Math.max(SKY_COLOR.green() / (previousSkyColor.green() * 10) / 25, 0.01f) : 0;
            float blueChange = blueDifference > 0.1f ? Math.max(SKY_COLOR.blue() / (previousSkyColor.blue() * 10) / 25, 0.01f) : 0;

            float red = (previousSkyColor.red() < SKY_COLOR.red() ? previousSkyColor.red() + redChange : previousSkyColor.red() > SKY_COLOR.red() ? previousSkyColor.red() - redChange : previousSkyColor.red());
            float green = (previousSkyColor.green() < SKY_COLOR.green() ? previousSkyColor.green() + greenChange : previousSkyColor.green() > SKY_COLOR.green() ? previousSkyColor.green() - greenChange : previousSkyColor.green());
            float blue = (previousSkyColor.blue() < SKY_COLOR.blue() ? previousSkyColor.blue() + blueChange : previousSkyColor.blue() > SKY_COLOR.blue() ? previousSkyColor.blue() - blueChange : previousSkyColor.blue());

            float fogDensity = FAKE_CHUNK.getWeatherEvent() != null ? Math.min(previousFog.density() + 0.001f, 1) : Math.max(previousFog.density() - 0.001f, 0);
            float fogDistance = FAKE_CHUNK.getWeatherEvent() != null ? Math.max(previousFog.distance() - 0.01f, FOG.distance()) : Math.min(previousFog.distance() + 0.01f, Minecraft.getInstance().options.renderDistance().get());

            e.setRed(red);
            e.setGreen(green);
            e.setBlue(blue);

            previousFog = new Fog(fogDensity, fogDistance);
            previousSkyColor = new SkyColor(red, green, blue);
        }
    }
}