package dev.zanckor.atmosphericraft.common.entity.deserttornado.client;

import dev.zanckor.atmosphericraft.common.entity.deserttornado.server.DesertTornadoEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.DefaultedGeoModel;
import software.bernie.geckolib.model.GeoModel;

import static dev.zanckor.atmosphericraft.AtmospheriCraft.MOD_ID;

public class DesertTornadoModel extends DefaultedEntityGeoModel<DesertTornadoEntity> {

    public DesertTornadoModel() {
        super(new ResourceLocation(MOD_ID, "deserttornado"));
    }
}
