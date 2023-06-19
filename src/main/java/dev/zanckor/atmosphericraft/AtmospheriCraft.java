package dev.zanckor.atmosphericraft;

import com.mojang.logging.LogUtils;
import dev.zanckor.atmosphericraft.common.entity.EntityRegistry;
import dev.zanckor.atmosphericraft.common.entity.deserttornado.client.DesertTornadoRenderer;
import dev.zanckor.atmosphericraft.common.entity.deserttornado.server.DesertTornadoEntity;
import dev.zanckor.atmosphericraft.registry.ItemRegistry;
import dev.zanckor.atmosphericraft.registry.RecipeRegistry;
import dev.zanckor.atmosphericraft.registry.WeatherEventRegistry;
import dev.zanckor.atmosphericraft.server.capability.player.PlayerDataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import static dev.zanckor.atmosphericraft.common.entity.EntityRegistry.DESERT_TORNADO;
import static dev.zanckor.atmosphericraft.server.capability.player.PlayerDataProvider.PLAYER_DATA_CAPABILITY;

@Mod(AtmospheriCraft.MOD_ID)
public class AtmospheriCraft {

    public static final String MOD_ID = "atmosphericraft";
    private static final Logger LOGGER = LogUtils.getLogger();


    public AtmospheriCraft() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        ItemRegistry.register(modEventBus);
        RecipeRegistry.register(modEventBus);
        EntityRegistry.register(modEventBus);
        WeatherEventRegistry.register();

        modEventBus.addListener(ItemRegistry::addItemsToTab);
    }


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ClientModSetup {

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(DESERT_TORNADO.get(), DesertTornadoRenderer::new);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public class CommonModSetup {
        @SubscribeEvent
        public static void assignEntityAtts(EntityAttributeCreationEvent e) {
            e.put(DESERT_TORNADO.get(), DesertTornadoEntity.setAttributes());
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventCapabilityHandler {

        @SubscribeEvent
        public static void addCapabilityPlayer(AttachCapabilitiesEvent<Entity> e) {
            PlayerDataProvider capability = new PlayerDataProvider();
            e.addCapability(new ResourceLocation(MOD_ID + "player_capability"), capability);
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone e) {
            Player player = e.getEntity();

            if (e.isWasDeath()) {
                e.getOriginal().reviveCaps();
                e.getOriginal().getCapability(PLAYER_DATA_CAPABILITY).ifPresent(oldStore -> player.getCapability(PLAYER_DATA_CAPABILITY).ifPresent(newStore -> newStore.copyForRespawn(oldStore)));

                e.getOriginal().invalidateCaps();
            }
        }
    }
}
