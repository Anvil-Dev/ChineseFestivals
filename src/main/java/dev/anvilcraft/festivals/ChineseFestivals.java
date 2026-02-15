package dev.anvilcraft.festivals;

import com.mojang.logging.LogUtils;
import dev.anvilcraft.festivals.commands.DebugCommands;
import dev.anvilcraft.festivals.features.Features;
import dev.anvilcraft.festivals.features.IFeature;
import dev.anvilcraft.festivals.festivals.Festivals;
import dev.anvilcraft.festivals.festivals.IFestival;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nullable;

@Mod(value = ChineseFestivals.MOD_ID, dist = Dist.CLIENT)
public class ChineseFestivals {
    public static final String MOD_ID = "chinese_festivals";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ChineseFestivalsContext context = new ChineseFestivalsContext();
    public static final String FEATURES_CONFIG = "features";
    public static @Nullable IFestival debugFestival = null;

    public ChineseFestivals(IEventBus modEventBus, ModContainer modContainer) {
        ChineseFestivals.ChineseFestivalsContext context = new ChineseFestivals.ChineseFestivalsContext();
        context.configPath = FMLPaths.CONFIGDIR.get();
        ChineseFestivals.init(context);
        NeoForge.EVENT_BUS.register(this);
    }

    public static void init(ChineseFestivalsContext context) {
        ChineseFestivals.context = context;
        Festivals.refresh();
        LOGGER.info("ChineseFestivals initialized");
    }

    @SubscribeEvent
    public void registerCommand(RegisterClientCommandsEvent event) {
        DebugCommands.register(event.getDispatcher());
    }

    public static ResourceLocation of(String id) {
        return ResourceLocation.fromNamespaceAndPath(ChineseFestivals.MOD_ID, id);
    }

    public static IFeature getOrCreateFeature(String id, BiFunction<String, IFestival[], IFeature> featureGenerator) {
        File featureFile = getFeatureFile(id);
        if (!featureFile.isFile()) {
            IFeature feature = featureGenerator.apply(id, new IFestival[]{});
            try {
                if (!featureFile.createNewFile()) {
                    throw new IOException();
                }
                try (FileWriter writer = new FileWriter(featureFile)) {
                    Features.GSON.toJson(feature, IFeature.class, writer);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to create feature file", e);
            }
            return feature;
        }
        try (FileReader reader = new FileReader(featureFile)) {
            return Features.GSON.fromJson(reader, IFeature.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read feature file", e);
        }
    }

    private static File getFeatureFile(String id) {
        if (ChineseFestivals.context.configPath == null) {
            throw new RuntimeException("ChineseFestivals context not initialized");
        }
        Path featurePath = ChineseFestivals.context.configPath.resolve(MOD_ID).resolve(ChineseFestivals.FEATURES_CONFIG);
        File featurePathFile = featurePath.toFile();
        if (!featurePathFile.isDirectory() && !featurePath.toFile().mkdirs()) {
            throw new RuntimeException("Failed to create feature directory");
        }
        return featurePath.resolve("%s.json".formatted(id)).toFile();
    }

    public static class ChineseFestivalsContext {
        public @Nullable Path configPath = new File(".").toPath().resolve("config");
    }

    @EventBusSubscriber(modid = ChineseFestivals.MOD_ID)
    public static class ModEvents {
        @SubscribeEvent
        public static void register(RegisterEvent event) {
            for (Map.Entry<ResourceLocation, Supplier<PaintingVariant>> entry : IFeature.PAINTING_REGISTER.entrySet()) {
                event.register(Registries.PAINTING_VARIANT, entry.getKey(), entry.getValue());
            }
        }

        @SubscribeEvent
        public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
            for (ModelResourceLocation location : IFeature.ITEM_MODEL_REGISTER.values()) {
                event.register(location);
            }
            for (ModelResourceLocation location : IFeature.BLOCK_MODEL_REGISTER.values()) {
                event.register(location);
            }
        }
    }
}
