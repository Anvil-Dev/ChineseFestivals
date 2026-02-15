package dev.anvilcraft.festivals.features.impl;

import dev.anvilcraft.festivals.ChineseFestivals;
import dev.anvilcraft.festivals.features.Feature;
import dev.anvilcraft.festivals.festivals.Festivals;
import dev.anvilcraft.festivals.festivals.IFestival;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import javax.annotation.Nullable;

public class Plates extends Feature {
    public Plates(String id, IFestival... enableTimes) {
        super(id, Festivals.CHINESE_SPRING_FESTIVAL, Festivals.QING_MING);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
        this.registerBlockModel(ChineseFestivals.of("plate"));
        this.registerBlockModel(ChineseFestivals.of("plate_dark"));
    }

    @Override
    public @Nullable ResourceLocation getItemFrameReplace(ItemFrame itemFrame, ItemStack innerItem) {
        if (itemFrame.getXRot() == -90.0 && innerItem.getItem().getFoodProperties(innerItem, null) != null) {
            return itemFrame instanceof GlowItemFrame ? ChineseFestivals.of("plate") : ChineseFestivals.of("plate_dark");
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
