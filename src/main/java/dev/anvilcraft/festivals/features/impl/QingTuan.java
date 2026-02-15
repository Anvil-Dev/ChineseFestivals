package dev.anvilcraft.festivals.features.impl;

import dev.anvilcraft.festivals.ChineseFestivals;
import dev.anvilcraft.festivals.features.Feature;
import dev.anvilcraft.festivals.festivals.Festivals;
import dev.anvilcraft.festivals.festivals.IFestival;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class QingTuan extends Feature {
    public QingTuan(String id, IFestival @NotNull ... enableTimes) {
        super(id, Festivals.QING_MING);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
        this.registerItemModel(ChineseFestivals.of("qing_tuan"));
    }

    @Override
    public Map<Item, Supplier<ResourceLocation>> getItemReplace() {
        Map<Item, Supplier<ResourceLocation>> map = Collections.synchronizedMap(new HashMap<>());
        map.put(Items.BAKED_POTATO, () -> ChineseFestivals.of("qing_tuan"));
        return map;
    }

    @Override
    public Map<String, Supplier<String>> getTranslationReplace() {
        Map<String, Supplier<String>> map = Collections.synchronizedMap(new HashMap<>());
        map.put("item.minecraft.baked_potato", () -> "item.chinese_festivals.qing_tuan");
        return map;
    }
}
