package dev.anvilcraft.festivals.features;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.anvilcraft.festivals.ChineseFestivals;
import dev.anvilcraft.festivals.features.impl.*;
import dev.anvilcraft.festivals.festivals.IFestival;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public class Features {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
        .registerTypeAdapter(IFeature.class, new FeatureParser())
        .registerTypeAdapter(IFestival.class, new FeatureParser.FestivalParser())
        .create();
    public static final Map<String, BiFunction<String, IFestival[], IFeature>> FEATURE_GENERATORS = Collections.synchronizedMap(new HashMap<>());
    public static final Supplier<IFeature> COUPLETS = new FeatureGetter("couplets", Couplets::new);
    public static final Supplier<IFeature> JIAO_ZI = new FeatureGetter("jiao_zi", JiaoZi::new);
    public static final Supplier<IFeature> FIREWORKS = new FeatureGetter("fireworks", Fireworks::new);
    public static final Supplier<IFeature> FLOWER_CAKE = new FeatureGetter("flower_cake", FlowerCake::new);
    public static final Supplier<IFeature> HOTPOTS = new FeatureGetter("hotpots", Hotpots::new);
    public static final Supplier<IFeature> LABA_CONGEE = new FeatureGetter("laba_congee", LabaCongee::new);
    public static final Supplier<IFeature> LANTERNS = new FeatureGetter("lanterns", Lanterns::new);
    public static final Supplier<IFeature> MOONCAKES = new FeatureGetter("mooncakes", Mooncakes::new);
    public static final Supplier<IFeature> PLATES = new FeatureGetter("plates", Plates::new);
    public static final Supplier<IFeature> TANG_YUAN = new FeatureGetter("tang_yuan", TangYuan::new);
    public static final Supplier<IFeature> QING_TUAN = new FeatureGetter("qing_tuan", QingTuan::new);
    public static final Supplier<IFeature> THREE_D_FOOD = new FeatureGetter("3d_food", ThreeDFood::new);
    public static final Supplier<IFeature> ZONG_ZI = new FeatureGetter("zong_zi", ZongZi::new);
    public static final Supplier<IFeature> LOONG_BOAT = new FeatureGetter("loong_boat", LoongBoat::new);
    public static final List<Supplier<IFeature>> FEATURES;

    static {
        ArrayList<Supplier<IFeature>> list = new ArrayList<>();
        list.add(COUPLETS);
        list.add(JIAO_ZI);
        list.add(FIREWORKS);
        list.add(FLOWER_CAKE);
        list.add(HOTPOTS);
        list.add(LABA_CONGEE);
        list.add(LANTERNS);
        list.add(MOONCAKES);
        list.add(PLATES);
        list.add(TANG_YUAN);
        list.add(QING_TUAN);
        list.add(THREE_D_FOOD);
        list.add(ZONG_ZI);
        list.add(LOONG_BOAT);
        FEATURES = Collections.synchronizedList(list);
    }

    public static void refresh() {
        for (Supplier<IFeature> feature : FEATURES) {
            if (feature instanceof FeatureGetter getter) {
                getter.refresh();
            }
        }
    }

    public static class FeatureGetter implements Supplier<IFeature> {
        String id;
        BiFunction<String, IFestival[], IFeature> featureGenerator;
        @Nullable
        IFeature feature = null;

        public FeatureGetter(String id, BiFunction<String, IFestival[], IFeature> featureGenerator) {
            this.id = id;
            this.featureGenerator = featureGenerator;
            Features.FEATURE_GENERATORS.put(id, featureGenerator);
        }

        @Override
        public IFeature get() {
            if (feature == null) this.feature = ChineseFestivals.getOrCreateFeature(this.id, this.featureGenerator);
            return this.feature;
        }

        public void refresh() {
            this.feature = ChineseFestivals.getOrCreateFeature(this.id, this.featureGenerator);
        }
    }
}
