package dev.anvilcraft.festivals.mixin;

import dev.anvilcraft.festivals.features.Features;
import dev.anvilcraft.festivals.features.IFeature;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Supplier;

@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin extends HangingEntity {
    private ItemFrameMixin(EntityType<? extends HangingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected Component getTypeName() {
        for (Supplier<IFeature> feature : Features.FEATURES) {
            if (feature.get().isNow()) {
                Component component = feature.get().getItemFrameTypeReplace((ItemFrame) (Object) this);
                if (component != null) {
                    return component;
                }
            }
        }
        return super.getTypeName();
    }
}
