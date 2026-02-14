package dev.anvilcraft.festivals.features.impl;

import dev.anvilcraft.festivals.features.Feature;
import dev.anvilcraft.festivals.festivals.Festivals;
import dev.anvilcraft.festivals.festivals.IFestival;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LoongBoat extends Feature {
    public LoongBoat(String id, IFestival @NotNull ... enableTimes) {
        super(id, Festivals.LOONG_BOAT_FESTIVAL);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
    }

    @Override
    public Map<String, Supplier<String>> getTranslationReplace() {
        Map<String, Supplier<String>> map = Collections.synchronizedMap(new HashMap<>());
        map.put("entity.minecraft.boat", () -> "entity.chinese_festivals.loong_boat");
        return map;
    }
}
