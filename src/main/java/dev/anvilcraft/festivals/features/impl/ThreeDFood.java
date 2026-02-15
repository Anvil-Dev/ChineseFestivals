package dev.anvilcraft.festivals.features.impl;

import dev.anvilcraft.festivals.ChineseFestivals;
import dev.anvilcraft.festivals.features.Feature;
import dev.anvilcraft.festivals.festivals.Festivals;
import dev.anvilcraft.festivals.festivals.IFestival;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ThreeDFood extends Feature {
    public static final TagKey<Item> HAS_PLATE = TagKey.create(Registries.ITEM, ChineseFestivals.of("has_plate"));

    public ThreeDFood(String id, IFestival @NotNull ... enableTimes) {
        super(id, Festivals.QING_MING, Festivals.CHINESE_SPRING_FESTIVAL);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
        this.registerItemModel(ChineseFestivals.of("apple_3d"));
        this.registerItemModel(ChineseFestivals.of("cookie_3d"));
    }

    @Override
    public Map<Item, Supplier<ResourceLocation>> get3DFoodReplace() {
        Map<Item, Supplier<ResourceLocation>> map = Collections.synchronizedMap(new HashMap<>());
        map.put(Items.APPLE, () -> ChineseFestivals.of("apple_3d"));
        map.put(Items.COOKIE, () -> ChineseFestivals.of("cookie_3d"));
        return map;
    }
}
