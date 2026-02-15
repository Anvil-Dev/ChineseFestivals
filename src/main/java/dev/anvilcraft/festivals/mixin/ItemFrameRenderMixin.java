package dev.anvilcraft.festivals.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.anvilcraft.festivals.features.Features;
import dev.anvilcraft.festivals.features.IFeature;
import dev.anvilcraft.festivals.features.impl.ThreeDFood;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(ItemFrameRenderer.class)
abstract class ItemFrameRenderMixin {
    @Shadow
    @Final
    private ItemRenderer itemRenderer;

    @Inject(
        method = "getFrameModelResourceLoc",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getRenderModel(ItemFrame itemFrame, ItemStack itemStack, CallbackInfoReturnable<ModelResourceLocation> cir) {
        for (Supplier<IFeature> featureSupplier : Features.FEATURES) {
            IFeature feature = featureSupplier.get();
            if (feature.isNow()) {
                ResourceLocation resourceLocation = feature.getItemFrameReplace(itemFrame, itemStack);
                ModelResourceLocation replace = IFeature.BLOCK_MODEL_REGISTER.get(resourceLocation);
                if (replace != null) cir.setReturnValue(replace);
            }
        }
    }

    @WrapOperation(
        method = "render(Lnet/minecraft/world/entity/decoration/ItemFrame;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/Sheets;solidBlockSheet()Lnet/minecraft/client/renderer/RenderType;"
        )
    )
    private RenderType frameToCutout(Operation<RenderType> original) {
        return Sheets.cutoutBlockSheet();
    }

    @WrapOperation(
        method = "render(Lnet/minecraft/world/entity/decoration/ItemFrame;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderStatic(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;IILcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;I)V"
        )
    )
    private <T extends ItemFrame> void render3DFood(
        ItemRenderer instance,
        ItemStack stack,
        ItemDisplayContext displayContext,
        int combinedLight,
        int combinedOverlay,
        PoseStack poseStack,
        MultiBufferSource bufferSource,
        Level level,
        int seed,
        Operation<Void> original,
        @Local(argsOnly = true) T itemFrame,
        @Local ItemStack itemStack
    ) {
        if (!Features.PLATES.get().isNow() || itemFrame.getXRot() != -90.0 || itemStack.isEmpty()) {
            original.call(instance, stack, displayContext, combinedLight, combinedOverlay, poseStack, bufferSource, level, seed);
        }
        for (Supplier<IFeature> featureSupplier : Features.FEATURES) {
            IFeature feature = featureSupplier.get();
            if (!feature.isNow()) continue;
            Map<Item, Supplier<ResourceLocation>> foodReplace = feature.get3DFoodReplace();
            for (Map.Entry<Item, Supplier<ResourceLocation>> entry : foodReplace.entrySet()) {
                if (!itemStack.is(entry.getKey())) continue;
                Supplier<ResourceLocation> value = entry.getValue();
                if (value == null) continue;
                ResourceLocation resourceLocation = value.get();
                ModelResourceLocation modelResourceLocation = IFeature.ITEM_MODEL_REGISTER.get(resourceLocation);
                BakedModel bakedModel = this.itemRenderer.getItemModelShaper().getModelManager().getModel(modelResourceLocation);
                this.itemRenderer.render(
                    itemStack,
                    ItemDisplayContext.FIXED,
                    false,
                    poseStack,
                    bufferSource,
                    combinedLight,
                    combinedOverlay,
                    bakedModel
                );
                return;
            }
        }
        original.call(instance, stack, displayContext, combinedLight, combinedOverlay, poseStack, bufferSource, level, seed);
    }

    @WrapOperation(
        method = "render(Lnet/minecraft/world/entity/decoration/ItemFrame;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;isInvisible()Z")
    )
    private boolean noRenderPlates(ItemFrame instance, Operation<Boolean> original) {
        if (Features.PLATES.get().isNow() && instance.getXRot() == -90.0 && instance.getItem().is(ThreeDFood.HAS_PLATE)) {
            return true;
        }
        return original.call(instance);
    }
}
