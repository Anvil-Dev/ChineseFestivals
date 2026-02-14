package dev.anvilcraft.festivals.features.impl;

import dev.anvilcraft.festivals.features.Feature;
import dev.anvilcraft.festivals.features.IFeature;
import dev.anvilcraft.festivals.festivals.Festivals;
import dev.anvilcraft.festivals.festivals.IFestival;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.decoration.PaintingVariants;

import java.util.List;
import javax.annotation.Nullable;

public class Couplets extends Feature {
    private static final PaintingVariant COUPLET_LEFT = IFeature.registerPainting("couplet_left", 16, 32);
    private static final PaintingVariant COUPLET_RIGHT = IFeature.registerPainting("couplet_right", 16, 32);
    private static final PaintingVariant COUPLET_TOP = IFeature.registerPainting("couplet_top", 32, 16);
    private static final PaintingVariant COUPLET_FU = IFeature.registerPainting("couplet_fu", 32, 32);
    public Couplets(String id, IFestival... enableTimes) {
        super(id, Festivals.CHINESE_SPRING_FESTIVAL);
        if (enableTimes.length > 0) {
            super.enableTimes.clear();
            super.enableTimes.addAll(List.of(enableTimes));
        }
    }

    @Override
    public @Nullable PaintingVariant getPaintingReplace(Painting painting) {
        ResourceLocation location = painting.getVariant().unwrapKey().orElse(PaintingVariants.KEBAB).location();
        if ("minecraft".equals(location.getNamespace())) {
            switch (location.getPath()) {
                case "graham":
                    return COUPLET_LEFT;
                case "wanderer":
                    return COUPLET_RIGHT;
                case "creebet":
                    return COUPLET_TOP;
                case "bust":
                    return COUPLET_FU;
            }
        }
        return null;
    }
}
