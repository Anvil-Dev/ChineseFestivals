package dev.anvilcraft.festivals.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.anvilcraft.festivals.features.Features;
import dev.anvilcraft.festivals.features.IFeature;
import dev.anvilcraft.festivals.features.impl.ThreeDFood;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(ItemFrameRenderer.class)
abstract class ItemFrameRenderMixin {
    @Inject(
        method = "getFrameModelResourceLoc",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getRenderModel(ItemFrame itemFrame, ItemStack itemStack, CallbackInfoReturnable<ModelResourceLocation> cir) {
        for (Supplier<IFeature> feature : Features.FEATURES) {
            if (feature.get().isNow()) {
                ModelResourceLocation replace = feature.get().getItemFrameReplace(itemFrame, itemStack);
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
            target = "Lnet/minecraft/world/entity/decoration/ItemFrame;getItem()Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack render3DFood(ItemFrame instance, Operation<ItemStack> original) {
        ItemStack itemStack = original.call(instance);
        if (!Features.PLATES.get().isNow() || instance.getXRot() != -90.0) return itemStack;
        for (Supplier<IFeature> feature : Features.FEATURES) {
            if (!feature.get().isNow()) continue;
            Map<Item, Supplier<Item>> itemSupplierMap = feature.get().get3DFoodReplace();
            for (Map.Entry<Item, Supplier<Item>> entry : itemSupplierMap.entrySet()) {
                if (!itemStack.is(entry.getKey())) continue;
                itemStack = new ItemStack(Holder.direct(entry.getValue().get()), itemStack.getCount(), itemStack.getComponentsPatch());
            }
        }
        return itemStack;
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
