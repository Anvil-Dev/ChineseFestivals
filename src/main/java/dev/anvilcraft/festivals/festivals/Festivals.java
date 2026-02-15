package dev.anvilcraft.festivals.festivals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Festivals {
    public static boolean hasChanged = false;
    // 春节
    public static final IFestival CHINESE_SPRING_FESTIVAL = new LunarFestival("spring", 12, 23, 1, 8);
    // 元宵节
    public static final IFestival LANTERN_FESTIVAL = new LunarFestival("lantern", 1, 15);
    // 端午节
    public static final IFestival LOONG_BOAT_FESTIVAL = new LunarFestival("loong_boat", 5, 5);
    // 七夕节
    public static final IFestival QIXI_FESTIVAL = new LunarFestival("qixi", 7, 7);
    // 中秋节
    public static final IFestival MOON_FESTIVAL = new LunarFestival("moon", 8, 15);
    // 重阳节
    public static final IFestival DOUBLE_NINTH_FESTIVAL = new LunarFestival("double_ninth", 9, 9);
    // 重阳节
    public static final IFestival DONG_ZHI_FESTIVAL = new SolarTermFestival("dong_zhi", SolarTermFestival.SolarTerm.DONG_ZHI);
    // 腊八节
    public static final IFestival LABA_FESTIVAL = new LunarFestival("laba", 12, 8);
    // 腊八节
    public static final IFestival QING_MING = new SolarTermFestival("qing_ming", SolarTermFestival.SolarTerm.QING_MING);

    public static final List<IFestival> FESTIVALS;

    static {
        ArrayList<IFestival> list = new ArrayList<>();
        list.add(CHINESE_SPRING_FESTIVAL);
        list.add(LANTERN_FESTIVAL);
        list.add(LOONG_BOAT_FESTIVAL);
        list.add(QIXI_FESTIVAL);
        list.add(MOON_FESTIVAL);
        list.add(DOUBLE_NINTH_FESTIVAL);
        list.add(DONG_ZHI_FESTIVAL);
        list.add(LABA_FESTIVAL);
        list.add(QING_MING);
        FESTIVALS = Collections.synchronizedList(list);
    }

    public static void refresh() {
        FESTIVALS.stream().parallel().forEach(IFestival::refresh);
    }
}
