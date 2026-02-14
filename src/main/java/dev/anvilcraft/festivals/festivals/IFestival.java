package dev.anvilcraft.festivals.festivals;

public interface IFestival {
    String getId();

    boolean isNow();

    void refresh();
}
