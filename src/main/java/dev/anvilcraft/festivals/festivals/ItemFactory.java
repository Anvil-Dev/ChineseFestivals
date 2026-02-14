package dev.anvilcraft.festivals.festivals;

import net.minecraft.world.item.Item;

import java.util.function.Supplier;
import javax.annotation.Nullable;

public class ItemFactory<T extends Item> implements Supplier<T> {
    public @Nullable T item = null;
    public final Item.Properties properties;
    public final ItemCreator<T> creator;

    public ItemFactory(Item.Properties properties, ItemCreator<T> creator) {
        this.properties = properties;
        this.creator = creator;
    }

    public T get() {
        return item == null ? item = creator.build(properties) : item;
    }

    public interface ItemCreator<T> {
        T build(Item.Properties properties);
    }
}
