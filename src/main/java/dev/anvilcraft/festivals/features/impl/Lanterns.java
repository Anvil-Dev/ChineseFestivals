package dev.anvilcraft.festivals.features.impl;

import dev.anvilcraft.festivals.ChineseFestivals;
import dev.anvilcraft.festivals.features.Feature;
import dev.anvilcraft.festivals.festivals.Festivals;
import dev.anvilcraft.festivals.festivals.IFestival;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import javax.annotation.Nullable;

public class Lanterns extends Feature {
    public Lanterns(String id, IFestival... enableTimes) {
        super(id, Festivals.CHINESE_SPRING_FESTIVAL, Festivals.LANTERN_FESTIVAL);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
        this.registerBlockModel(ChineseFestivals.of("lantern_hanging"));
        this.registerBlockModel(ChineseFestivals.of("tall_lantern_hanging"));
    }

    @Override
    public @Nullable ResourceLocation getBlockReplace(BlockState blockState) {
        if (blockState.is(Blocks.LANTERN) && blockState.getValue(LanternBlock.HANGING)) {
            return ChineseFestivals.of("lantern_hanging");
        }
        if (blockState.is(Blocks.SOUL_LANTERN) && blockState.getValue(LanternBlock.HANGING)) {
            return ChineseFestivals.of("tall_lantern_hanging");
        }
        return null;
    }

    @Override
    public @Nullable String getBlockTranslateReplace(Block block) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
        if (block instanceof LanternBlock && "minecraft".equals(key.getNamespace())) {
            switch (key.getPath()) {
                case "lantern":
                    return "block.chinese_festivals.lantern";
                case "soul_lantern":
                    return "block.chinese_festivals.tall_lantern";
            }
        }
        return null;
    }
}
