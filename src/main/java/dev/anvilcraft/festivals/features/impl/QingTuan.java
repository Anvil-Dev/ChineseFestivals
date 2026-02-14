package dev.anvilcraft.festivals.features.impl;

import dev.anvilcraft.festivals.features.Feature;
import dev.anvilcraft.festivals.features.IFeature;
import dev.anvilcraft.festivals.festivals.Festivals;
import dev.anvilcraft.festivals.festivals.IFestival;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class QingTuan extends Feature {
    public static final Supplier<Item> QING_TUAN = IFeature.createItem("qing_tuan", new Item.Properties().food(Foods.BAKED_POTATO), Item::new);

    public QingTuan(String id, IFestival @NotNull ... enableTimes) {
        super(id, Festivals.QING_MING);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
    }

    @Override
    public Map<Item, Supplier<Item>> getItemReplace() {
        Map<Item, Supplier<Item>> map = Collections.synchronizedMap(new HashMap<>());
        map.put(Items.BAKED_POTATO, QING_TUAN);
        return map;
    }

    @Override
    public Map<String, Supplier<String>> getTranslationReplace() {
        Map<String, Supplier<String>> map = Collections.synchronizedMap(new HashMap<>());
        map.put("item.minecraft.baked_potato", () -> "item.chinese_festivals.qing_tuan");
        return map;
    }
}
