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

public class ZongZi extends Feature {
    public ZongZi(String id, IFestival @NotNull ... enableTimes) {
        super(id, Festivals.LOONG_BOAT_FESTIVAL);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
        this.registerItemModel(ChineseFestivals.of("zong_zi"));
    }

    @Override
    public Map<Item, Supplier<ResourceLocation>> getItemReplace() {
        HashMap<Item, Supplier<ResourceLocation>> map = new HashMap<>();
        map.put(Items.PUMPKIN_PIE, () -> ChineseFestivals.of("zong_zi"));
        return map;
    }

    @Override
    public Map<String, Supplier<String>> getTranslationReplace() {
        Map<String, Supplier<String>> map = Collections.synchronizedMap(new HashMap<>());
        map.put("item.minecraft.pumpkin_pie", () -> "item.chinese_festivals.zong_zi");
        return map;
    }
}
