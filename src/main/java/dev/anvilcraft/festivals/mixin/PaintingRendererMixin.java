package dev.anvilcraft.festivals.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.anvilcraft.festivals.features.Features;
import dev.anvilcraft.festivals.features.IFeature;
import dev.anvilcraft.festivals.client.renderer.CustomPaintingRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(PaintingRenderer.class)
abstract class PaintingRendererMixin {
    @WrapOperation(
        method = "render(Lnet/minecraft/world/entity/decoration/Painting;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/Painting;getVariant()Lnet/minecraft/core/Holder;")
    )
    private Holder<PaintingVariant> modify(Painting instance, Operation<Holder<PaintingVariant>> original) {
        for (Supplier<IFeature> feature : Features.FEATURES) {
            if (feature.get().isNow()) {
                PaintingVariant variant = feature.get().getPaintingReplace(instance);
                if (variant == null) continue;
                return Holder.direct(variant);
            }
        }
        return original.call(instance);
    }

    @WrapOperation(
        method = "render(Lnet/minecraft/world/entity/decoration/Painting;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/RenderType;entitySolid(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"
        )
    )
    private RenderType modifyRenderer(ResourceLocation location, Operation<RenderType> original) {
        return RenderType.entityCutout(location);
    }

    @Shadow
    private void vertex(
        PoseStack.Pose pose,
        VertexConsumer consumer,
        float x,
        float y,
        float u,
        float v,
        float z,
        int normalX,
        int normalY,
        int normalZ,
        int packedLight
    ) {
    }

    @Inject(method = "renderPainting", at = @At("HEAD"), cancellable = true)
    private void rewriteRenderer(
        PoseStack poseStack,
        VertexConsumer vertexConsumer,
        Painting painting,
        int i,
        int j,
        TextureAtlasSprite textureAtlasSprite,
        TextureAtlasSprite textureAtlasSprite2,
        CallbackInfo ci
    ) {
        PaintingVariant variant = IFeature.execute(it -> it.getPaintingReplace(painting));
        if (variant == null) return;
        CustomPaintingRenderer.render(poseStack, vertexConsumer, painting, i, j, textureAtlasSprite, this::vertex);
        ci.cancel();
    }
}
