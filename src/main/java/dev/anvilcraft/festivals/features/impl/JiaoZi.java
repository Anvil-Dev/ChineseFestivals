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

public class JiaoZi extends Feature {
    public JiaoZi(String id, IFestival... enableTimes) {
        super(id, Festivals.DONG_ZHI_FESTIVAL, Festivals.CHINESE_SPRING_FESTIVAL);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
        this.registerItemModel(ChineseFestivals.of("jiao_zi"));
    }

    @Override
    public Map<Item, Supplier<ResourceLocation>> getItemReplace() {
        Map<Item, Supplier<ResourceLocation>> map = Collections.synchronizedMap(new HashMap<>());
        map.put(Items.RABBIT_STEW, () -> ChineseFestivals.of("jiao_zi"));
        return map;
    }

    @Override
    public Map<String, Supplier<String>> getTranslationReplace() {
        Map<String, Supplier<String>> map = Collections.synchronizedMap(new HashMap<>());
        map.put("item.minecraft.rabbit_stew", () -> "item.chinese_festivals.jiao_zi");
        return map;
    }
}
