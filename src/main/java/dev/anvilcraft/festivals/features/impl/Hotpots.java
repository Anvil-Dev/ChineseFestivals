package dev.anvilcraft.festivals.features.impl;

import dev.anvilcraft.festivals.ChineseFestivals;
import dev.anvilcraft.festivals.features.Feature;
import dev.anvilcraft.festivals.festivals.Festivals;
import dev.anvilcraft.festivals.festivals.IFestival;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public class Hotpots extends Feature {
    public Hotpots(String id, IFestival... enableTimes) {
        super(id, Festivals.DONG_ZHI_FESTIVAL);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
        this.registerItemModel(ChineseFestivals.of("hotpot_n"));
        this.registerItemModel(ChineseFestivals.of("hotpot_s"));
        this.registerBlockModel(ChineseFestivals.of("hotpot_n"));
        this.registerBlockModel(ChineseFestivals.of("hotpot_s"));
    }

    @Override
    public @Nullable ResourceLocation getBlockReplace(BlockState blockState) {
        if (blockState.is(Blocks.CAMPFIRE)) {
            if (blockState.getValue(CampfireBlock.LIT)) {
                return ChineseFestivals.of("hotpot_s");
            }
        } else if (blockState.is(Blocks.SOUL_CAMPFIRE)) {
            if (blockState.getValue(CampfireBlock.LIT)) {
                return ChineseFestivals.of("hotpot_n");
            }
        }
        return null;
    }

    @Override
    public Map<Item, Supplier<ResourceLocation>> getItemReplace() {
        Map<Item, Supplier<ResourceLocation>> map = Collections.synchronizedMap(new HashMap<>());
        map.put(Items.CAMPFIRE, () -> ChineseFestivals.of("hotpot_s"));
        map.put(Items.SOUL_CAMPFIRE, () -> ChineseFestivals.of("hotpot_n"));
        return map;
    }

    @Override
    public Map<String, Supplier<String>> getTranslationReplace() {
        Map<String, Supplier<String>> map = Collections.synchronizedMap(new HashMap<>());
        map.put("item.minecraft.campfire", () -> "item.chinese_festivals.hotpot_s");
        map.put("item.minecraft.soul_campfire", () -> "item.chinese_festivals.hotpot_n");
        map.put("item.minecraft.rabbit_stew", () -> "item.chinese_festivals.dumplings");
        map.put("block.minecraft.campfire", () -> "block.chinese_festivals.hotpot_s");
        map.put("block.minecraft.soul_campfire", () -> "block.chinese_festivals.hotpot_n");
        return map;
    }
}
