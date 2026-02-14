package dev.anvilcraft.festivals.mixin;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.sugar.Local;
import dev.anvilcraft.festivals.client.model.LoongBoatModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LayerDefinitions.class)
abstract class LayerDefinitionsMixin {
    @Unique
    private static final String DEFAULT_LAYER = "main";

    @Contract(pure = true)
    @Inject(
        method = "createRoots",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/model/BoatModel;createBodyModel()Lnet/minecraft/client/model/geom/builders/LayerDefinition;"
        )
    )
    private static void createRoots(
        CallbackInfoReturnable<Map<ModelLayerLocation, LayerDefinition>> cir,
        @Local ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> builder
    ) {
        builder.put(LoongBoatModel.LAYER_LOCATION, LoongBoatModel.createBodyModel());
    }
}
