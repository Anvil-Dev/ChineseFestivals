package dev.anvilcraft.festivals.mixin;

import dev.anvilcraft.festivals.features.Features;
import dev.anvilcraft.festivals.features.IFeature;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.RegistryAwareItemModelShaper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
@Mixin(RegistryAwareItemModelShaper.class)
public abstract class RegistryAwareItemModelShaperMixin extends ItemModelShaper {
    public RegistryAwareItemModelShaperMixin(ModelManager arg) {
        super(arg);
    }

    @Inject(method = "getItemModel", at = @At("HEAD"), cancellable = true)
    private void getItemModel(Item item, CallbackInfoReturnable<BakedModel> cir) {
        for (Supplier<IFeature> featureSupplier : Features.FEATURES) {
            IFeature feature = featureSupplier.get();
            if (feature.isNow()) {
                Supplier<ResourceLocation> locationSupplier = feature.getItemReplace().getOrDefault(item, null);
                if(locationSupplier != null) {
                    ResourceLocation location = locationSupplier.get();
                    ModelResourceLocation modelResourceLocation = IFeature.ITEM_MODEL_REGISTER.get(location);
                    if(modelResourceLocation != null) {
                        BakedModel model = this.getModelManager().getModel(modelResourceLocation);
                        cir.setReturnValue(model);
                    }
                }
            }
        }
    }
}
