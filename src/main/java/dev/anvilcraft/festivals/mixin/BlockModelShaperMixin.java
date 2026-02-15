package dev.anvilcraft.festivals.mixin;

import dev.anvilcraft.festivals.features.Features;
import dev.anvilcraft.festivals.features.IFeature;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(BlockModelShaper.class)
public abstract class BlockModelShaperMixin {
    @Shadow
    @Final
    private ModelManager modelManager;

    @Inject(method = "getBlockModel", at = @At("RETURN"), cancellable = true)
    private void getBlockModel(BlockState blockState, CallbackInfoReturnable<BakedModel> cir) {
        for (Supplier<IFeature> featureSupplier : Features.FEATURES) {
            IFeature feature = featureSupplier.get();
            if (feature.isNow()) {
                ResourceLocation resourceLocation = feature.getBlockReplace(blockState);
                ModelResourceLocation modelResourceLocation = IFeature.BLOCK_MODEL_REGISTER.get(resourceLocation);
                if (modelResourceLocation == null) continue;
                BakedModel bakedmodel = this.modelManager.getModel(modelResourceLocation);
                cir.setReturnValue(bakedmodel);
                return;
            }
        }
    }
}
