package dev.anvilcraft.festivals.mixin;

import dev.anvilcraft.festivals.features.Features;
import dev.anvilcraft.festivals.features.IFeature;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.RegistryAwareItemModelShaper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
@Mixin(RegistryAwareItemModelShaper.class)
public abstract class RegistryAwareItemModelShaperMixin extends ItemModelShaper {
    @Shadow(remap = false)
    @Final
    private Map<Holder.Reference<Item>, BakedModel> models;

    public RegistryAwareItemModelShaperMixin(ModelManager arg) {
        super(arg);
    }

    @Inject(method = "getItemModel", at = @At("HEAD"), cancellable = true)
    private void getItemModel(Item item, CallbackInfoReturnable<BakedModel> cir) {
        for (Supplier<IFeature> feature : Features.FEATURES) {
            if (feature.get().isNow()) {
                Supplier<Item> item1 = feature.get().getItemReplace().getOrDefault(item, null);
                if (item1 != null) {
                    Item item2 = item1.get();
                    BuiltInRegistries.ITEM.getResourceKey(item2)
                        .ifPresent(key -> cir.setReturnValue(this.models.get(BuiltInRegistries.ITEM.getHolderOrThrow(key))));
                    return;
                }
            }
        }
    }
}
