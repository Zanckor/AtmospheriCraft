package dev.zanckor.atmosphericraft.common.entity;

import dev.zanckor.atmosphericraft.common.entity.deserttornado.server.DesertTornadoEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static dev.zanckor.atmosphericraft.AtmospheriCraft.MOD_ID;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>>
            ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);


    public static final RegistryObject<EntityType<DesertTornadoEntity>> DESERT_TORNADO = ENTITY_TYPES.register("desert_tornado",
            () -> EntityType.Builder.of(DesertTornadoEntity::new, MobCategory.AMBIENT)
                    .sized(1, 0.65f)
                    .build(new ResourceLocation(MOD_ID, "desert_tornado").toString()));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
