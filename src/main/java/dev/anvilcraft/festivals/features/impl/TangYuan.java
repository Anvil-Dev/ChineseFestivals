package dev.anvilcraft.festivals.features.impl;

import dev.anvilcraft.festivals.ChineseFestivals;
import dev.anvilcraft.festivals.features.Feature;
import dev.anvilcraft.festivals.festivals.Festivals;
import dev.anvilcraft.festivals.festivals.IFestival;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TangYuan extends Feature {
    public TangYuan(String id, IFestival ... enableTimes) {
        super(id, Festivals.LABA_FESTIVAL, Festivals.CHINESE_SPRING_FESTIVAL, Festivals.LANTERN_FESTIVAL);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
        this.registerItemModel(ChineseFestivals.of("tang_yuan"));
    }


    @Override
    public Map<Item, Supplier<ResourceLocation>> getItemReplace() {
        Map<Item, Supplier<ResourceLocation>> map = Collections.synchronizedMap(new HashMap<>());
        map.put(Items.BEETROOT_SOUP, () -> ChineseFestivals.of("tang_yuan"));
        return map;
    }

    @Override
    public Map<String, Supplier<String>> getTranslationReplace() {
        Map<String, Supplier<String>> map = Collections.synchronizedMap(new HashMap<>());
        map.put("item.minecraft.beetroot_soup", () -> "item.chinese_festivals.tang_yuan");
        return map;
    }
}
