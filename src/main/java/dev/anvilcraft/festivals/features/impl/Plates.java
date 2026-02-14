package dev.anvilcraft.festivals.features.impl;

import dev.anvilcraft.festivals.data.BlockModelData;
import dev.anvilcraft.festivals.features.Feature;
import dev.anvilcraft.festivals.features.IFeature;
import dev.anvilcraft.festivals.festivals.Festivals;
import dev.anvilcraft.festivals.festivals.IFestival;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.List;
import javax.annotation.Nullable;

public class Plates extends Feature {
    private static final ModelResourceLocation DARK_PLATE = IFeature.registerBlockModel(new BlockModelData("plate").property(
        "dark",
        "true",
        BooleanProperty::create
    ));
    private static final ModelResourceLocation PLATE = IFeature.registerBlockModel(new BlockModelData("plate").property(
        "dark",
        "false",
        BooleanProperty::create
    ));

    public Plates(String id, IFestival... enableTimes) {
        super(id, Festivals.CHINESE_SPRING_FESTIVAL, Festivals.QING_MING);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
    }

    @Override
    public @Nullable ModelResourceLocation getItemFrameReplace(ItemFrame itemFrame, ItemStack innerItem) {
        if (itemFrame.getXRot() == -90.0 && innerItem.getItem().getFoodProperties(innerItem, null) != null) {
            return itemFrame instanceof GlowItemFrame ? PLATE : DARK_PLATE;
        }
        return null;
    }

    @Override
    public @Nullable Component getItemFrameTypeReplace(ItemFrame itemFrame) {
        ItemStack item = itemFrame.getItem();
        if (itemFrame.getXRot() == -90.0 && item.getItem().getFoodProperties(item, null) != null) {
            return Component.translatable("entity.chinese_festivals.plate" + (itemFrame instanceof GlowItemFrame ? "" : "_dark"));
        }
        return null;
    }
}
