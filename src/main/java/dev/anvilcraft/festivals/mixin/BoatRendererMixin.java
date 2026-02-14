package dev.anvilcraft.festivals.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import dev.anvilcraft.festivals.ChineseFestivals;
import dev.anvilcraft.festivals.client.model.LoongBoatModel;
import dev.anvilcraft.festivals.features.Features;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(BoatRenderer.class)
abstract class BoatRendererMixin {
    @Unique
    private static final String DEFAULT_LAYER = "main";
    @Unique
    private static @Nullable ListModel<Boat> chineseFestivals$model = null;
    @Unique
    private boolean chineseFestivals$hasChest = false;

    @Inject(
        method = "<init>",
        at = @At("RETURN")
    )
    private void init(EntityRendererProvider.@NotNull Context context, boolean bl, CallbackInfo ci) {
        ModelLayerLocation modelLayerLocation = LoongBoatModel.LAYER_LOCATION;
        ModelPart modelPart = context.bakeLayer(modelLayerLocation);
        BoatRendererMixin.chineseFestivals$model = new LoongBoatModel(modelPart);
        this.chineseFestivals$hasChest = bl;
    }

    @WrapOperation(
        method = "render(Lnet/minecraft/world/entity/vehicle/Boat;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/BoatRenderer;getModelWithLocation(Lnet/minecraft/world/entity/vehicle/Boat;)Lcom/mojang/datafixers/util/Pair;"
        ),
        remap = false
    )
    private Pair<ResourceLocation, ListModel<Boat>> get(
        BoatRenderer instance,
        Boat boat,
        Operation<Pair<ResourceLocation, ListModel<Boat>>> original
    ) {
        if (
            !this.chineseFestivals$hasChest
            && boat.getVariant() instanceof Boat.Type type
            && type != Boat.Type.BAMBOO
            && Features.LOONG_BOAT.get().isNow()
        ) {
            return new Pair<>(ChineseFestivals.of("textures/entity/loong_boat.png"), BoatRendererMixin.chineseFestivals$model);
        }
        return original.call(instance, boat);
    }

    @Inject(
        method = "render(Lnet/minecraft/world/entity/vehicle/Boat;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V")
    )
    private void render(
        Boat boat,
        float f,
        float g,
        PoseStack poseStack,
        MultiBufferSource multiBufferSource,
        int i,
        CallbackInfo ci
    ) {
        if (!this.chineseFestivals$hasChest && boat.getVariant() != Boat.Type.BAMBOO && Features.LOONG_BOAT.get().isNow()) {
            poseStack.translate(0.0, 1.0, 0.0);
            poseStack.rotateAround(Axis.YP.rotationDegrees(90), 0.0f, 0.0f, 0.0f);
        }
    }
}
