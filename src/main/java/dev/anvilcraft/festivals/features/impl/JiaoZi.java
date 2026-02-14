package dev.anvilcraft.festivals.features.impl;

import dev.anvilcraft.festivals.features.Feature;
import dev.anvilcraft.festivals.features.IFeature;
import dev.anvilcraft.festivals.festivals.Festivals;
import dev.anvilcraft.festivals.festivals.IFestival;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class JiaoZi extends Feature {
    public static final Supplier<Item> DUMPLINGS = IFeature.createItem("jiao_zi", new Item.Properties().food(Foods.RABBIT_STEW), Item::new);

    public JiaoZi(String id, IFestival... enableTimes) {
        super(id, Festivals.DONG_ZHI_FESTIVAL, Festivals.CHINESE_SPRING_FESTIVAL);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
    }

    @Override
    public Map<Item, Supplier<Item>> getItemReplace() {
        Map<Item, Supplier<Item>> map = Collections.synchronizedMap(new HashMap<>());
        map.put(Items.RABBIT_STEW, DUMPLINGS);
        return map;
    }

    @Override
    public Map<String, Supplier<String>> getTranslationReplace() {
        Map<String, Supplier<String>> map = Collections.synchronizedMap(new HashMap<>());
        map.put("item.minecraft.rabbit_stew", () -> "item.chinese_festivals.jiao_zi");
        return map;
    }
}
