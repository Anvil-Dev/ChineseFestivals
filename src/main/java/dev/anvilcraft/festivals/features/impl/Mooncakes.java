package dev.anvilcraft.festivals.features.impl;

import dev.anvilcraft.festivals.ChineseFestivals;
import dev.anvilcraft.festivals.features.Feature;
import dev.anvilcraft.festivals.festivals.Festivals;
import dev.anvilcraft.festivals.festivals.IFestival;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public class Mooncakes extends Feature {
    public Mooncakes(String id, IFestival @NotNull ... enableTimes) {
        super(id, Festivals.MOON_FESTIVAL);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
        this.registerItemModel(ChineseFestivals.of("mooncakes"));
        this.registerItemModel(ChineseFestivals.of("mooncake"));
        this.registerBlockModel(ChineseFestivals.of("mooncakes"));
        this.registerBlockModel(ChineseFestivals.of("mooncakes_slice1"));
        this.registerBlockModel(ChineseFestivals.of("mooncakes_slice2"));
        this.registerBlockModel(ChineseFestivals.of("mooncakes_slice3"));
        this.registerBlockModel(ChineseFestivals.of("mooncakes_slice4"));
        this.registerBlockModel(ChineseFestivals.of("mooncakes_slice5"));
        this.registerBlockModel(ChineseFestivals.of("mooncakes_slice6"));
    }

    @Override
    public Map<Item, Supplier<ResourceLocation>> getItemReplace() {
        Map<Item, Supplier<ResourceLocation>> map = Collections.synchronizedMap(new HashMap<>());
        map.put(Items.CAKE, () -> ChineseFestivals.of("mooncakes"));
        map.put(Items.PUMPKIN_PIE, () -> ChineseFestivals.of("mooncake"));
        return map;
    }

    @Override
    public @Nullable ResourceLocation getBlockReplace(BlockState blockState) {
        if (blockState.is(Blocks.CAKE)) {
            return switch (blockState.getValue(CakeBlock.BITES)) {
                case 1 -> ChineseFestivals.of("mooncakes_slice1");
                case 2 -> ChineseFestivals.of("mooncakes_slice2");
                case 3 -> ChineseFestivals.of("mooncakes_slice3");
                case 4 -> ChineseFestivals.of("mooncakes_slice4");
                case 5 -> ChineseFestivals.of("mooncakes_slice5");
                case 6 -> ChineseFestivals.of("mooncakes_slice6");
                default -> ChineseFestivals.of("mooncakes");
            };
        }
        return null;
    }

    @Override
    public Map<String, Supplier<String>> getTranslationReplace() {
        Map<String, Supplier<String>> map = Collections.synchronizedMap(new HashMap<>());
        map.put("block.minecraft.cake", () -> "block.chinese_festivals.mooncakes");
        map.put("item.minecraft.pumpkin_pie", () -> "item.chinese_festivals.mooncake");
        return map;
    }
}
