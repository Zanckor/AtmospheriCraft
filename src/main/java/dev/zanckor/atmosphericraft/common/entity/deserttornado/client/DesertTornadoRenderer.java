package dev.zanckor.atmosphericraft.common.entity.deserttornado.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.zanckor.atmosphericraft.common.entity.deserttornado.server.DesertTornadoEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import static dev.zanckor.atmosphericraft.AtmospheriCraft.MOD_ID;

public class DesertTornadoRenderer extends GeoEntityRenderer<DesertTornadoEntity> {

    public DesertTornadoRenderer(EntityRendererProvider.Context context) {
        super(context, new DesertTornadoModel());
    }


    //Increase size
    @Override
    public void render(DesertTornadoEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        scaleModelForRender(3, 3, poseStack, animatable, model.getBakedModel(new ResourceLocation(MOD_ID, "geo/entity/deserttornado.geo.json")), false, partialTick, packedLight, getPackedOverlay(animatable, 0));
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }


    //Rotates eyes instead of full body
    @Override
    public void renderRecursively(PoseStack poseStack, DesertTornadoEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if(bone.getName().equals("eyes")){
            bone.setRotY((-animatable.getViewYRot(0) + 180) * ((float) Math.PI / 180F));
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    //Make texture translucent
    @Override
    public RenderType getRenderType(DesertTornadoEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

    //Deletes rotation of full body
    @Override
    protected void applyRotations(DesertTornadoEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
    }
}
