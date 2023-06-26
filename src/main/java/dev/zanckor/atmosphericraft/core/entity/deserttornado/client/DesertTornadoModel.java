package dev.zanckor.atmosphericraft.core.entity.deserttornado.client;

import dev.zanckor.atmosphericraft.core.entity.deserttornado.server.DesertTornadoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import static dev.zanckor.atmosphericraft.AtmospheriCraft.MOD_ID;

public class DesertTornadoModel extends DefaultedEntityGeoModel<DesertTornadoEntity> {

    public DesertTornadoModel() {
        super(new ResourceLocation(MOD_ID, "deserttornado"));
    }
}
