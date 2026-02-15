package dev.anvilcraft.festivals.features;

import dev.anvilcraft.festivals.ChineseFestivals;
import dev.anvilcraft.festivals.festivals.*;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public interface IFeature {
    Map<ResourceLocation, ModelResourceLocation> ITEM_MODEL_REGISTER = Collections.synchronizedMap(new HashMap<>());
    Map<ResourceLocation, ModelResourceLocation> BLOCK_MODEL_REGISTER = Collections.synchronizedMap(new HashMap<>());
    Map<ResourceLocation, Supplier<Item>> ITEM_REGISTER = new HashMap<>();
    Map<ResourceLocation, Supplier<PaintingVariant>> PAINTING_REGISTER = new HashMap<>();

    String getId();

    boolean isNow();

    default void registerItemModel(ResourceLocation location) {
        IFeature.ITEM_MODEL_REGISTER.put(location, ModelResourceLocation.standalone(location.withPrefix("item/")));
    }

    default void registerBlockModel(ResourceLocation location) {
        IFeature.BLOCK_MODEL_REGISTER.put(location, ModelResourceLocation.standalone(location.withPrefix("block/")));
    }

    default Map<Item, Supplier<ResourceLocation>> getItemReplace() {
        return Collections.emptyMap();
    }

    default Map<Item, Supplier<Item>> get3DFoodReplaceOld() {
        return Collections.emptyMap();
    }

    default Map<Item, Supplier<ResourceLocation>> get3DFoodReplace() {
        return Collections.emptyMap();
    }

    default @Nullable ResourceLocation getBlockReplace(BlockState blockState) {
        return null;
    }

    default Map<String, Supplier<String>> getTranslationReplace() {
        return Collections.emptyMap();
    }

    default @Nullable String getBlockTranslateReplace(Block block) {
        return null;
    }

    default @Nullable ResourceLocation getItemFrameReplace(ItemFrame itemFrame, ItemStack innerItem) {
        return null;
    }

    default @Nullable Component getItemFrameTypeReplace(ItemFrame itemFrame) {
        return null;
    }

    default @Nullable PaintingVariant getPaintingReplace(Painting painting) {
        return null;
    }

    default @Nullable double[][] getFireworkParticle() {
        return null;
    }

    static Supplier<Item> createItem(String id, Item.Properties properties, ItemFactory.ItemCreator<Item> creator) {
        ItemFactory<Item> itemFactory = new ItemFactory<>(properties, creator);
        ITEM_REGISTER.put(ChineseFestivals.of(id), itemFactory);
        return itemFactory;
    }

    static PaintingVariant registerPainting(String id, int x, int y) {
        ResourceLocation location = ChineseFestivals.of(id);
        PaintingVariant variant = new PaintingVariant(x, y, location);
        IFeature.PAINTING_REGISTER.put(location, () -> variant);
        return variant;
    }

    static <T> @Nullable T execute(Function<IFeature, T> supplier) {
        for (Supplier<IFeature> feature : Features.FEATURES) {
            if (feature.get().isNow()) {
                T data = supplier.apply(feature.get());
                if (data == null) continue;
                return data;
            }
        }
        return null;
    }
}
