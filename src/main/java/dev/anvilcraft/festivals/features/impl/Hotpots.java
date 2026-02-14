package dev.anvilcraft.festivals.features.impl;

import dev.anvilcraft.festivals.data.BlockModelData;
import dev.anvilcraft.festivals.features.Feature;
import dev.anvilcraft.festivals.features.IFeature;
import dev.anvilcraft.festivals.festivals.Festivals;
import dev.anvilcraft.festivals.festivals.IFestival;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
    public static final Supplier<Item> HOTPOT_N_ITEM = IFeature.createItem("hotpot_n", new Item.Properties(), Item::new);
    public static final Supplier<Item> HOTPOT_S_ITEM = IFeature.createItem("hotpot_s", new Item.Properties(), Item::new);
    public static final ModelResourceLocation HOTPOT_N = IFeature.registerBlockModel(new BlockModelData("hotpot_n"));
    public static final ModelResourceLocation HOTPOT_S = IFeature.registerBlockModel(new BlockModelData("hotpot_s"));
    public Hotpots(String id, IFestival... enableTimes) {
        super(id, Festivals.DONG_ZHI_FESTIVAL);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
    }

    @Override
    public @Nullable ModelResourceLocation getBlockReplace(BlockState blockState) {
        if (blockState.is(Blocks.CAMPFIRE)) {
            if (blockState.getValue(CampfireBlock.LIT)) {
                return HOTPOT_S;
            }
        } else if (blockState.is(Blocks.SOUL_CAMPFIRE)) {
            if (blockState.getValue(CampfireBlock.LIT)) {
                return HOTPOT_N;
            }

        }
        return null;
    }

    @Override
    public Map<Item, Supplier<Item>> getItemReplace() {
        Map<Item, Supplier<Item>> map = Collections.synchronizedMap(new HashMap<>());
        map.put(Items.CAMPFIRE, HOTPOT_S_ITEM);
        map.put(Items.SOUL_CAMPFIRE, HOTPOT_N_ITEM);
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
