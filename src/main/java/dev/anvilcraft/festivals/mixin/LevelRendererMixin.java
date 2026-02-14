package dev.anvilcraft.festivals.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.anvilcraft.festivals.ChineseFestivals;
import dev.anvilcraft.festivals.festivals.Festivals;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Unique
    private static final ResourceLocation chineseFestivals$MOON_LOCATION = ChineseFestivals.of("textures/environment/moon_phases.png");

    @Shadow
    private @Nullable ClientLevel level;
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "renderLevel", at = @At("RETURN"))
    private void renderLevel(
        DeltaTracker deltaTracker,
        boolean renderBlockOutline,
        Camera camera,
        GameRenderer gameRenderer,
        LightTexture lightTexture,
        Matrix4f frustumMatrix,
        Matrix4f projectionMatrix,
        CallbackInfo ci
    ) {
        if (this.level == null) return;
        if (this.level.getGameTime() % 600 == 0) {
            new Thread(Festivals::refresh).start();
        }
        if (Festivals.hasChanged) {
            Festivals.hasChanged = false;
            this.minecraft.levelRenderer.allChanged();
        }
    }

    @Inject(
        method = "renderSky",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/BufferUploader;drawWithShader(Lcom/mojang/blaze3d/vertex/MeshData;)V",
            ordinal = 2
        )
    )
    private void renderSky(
        Matrix4f frustumMatrix,
        Matrix4f projectionMatrix,
        float partialTick,
        Camera camera,
        boolean isFoggy,
        Runnable skyFogSetup,
        CallbackInfo ci,
        @Local Tesselator tesselator,
        @Local BufferBuilder bufferBuilder,
        @Local(ordinal = 2) Matrix4f matrix4f1
    ) {
        if (
            !Festivals.MOON_FESTIVAL.isNow() ||
            this.level == null
        ) {
            return;
        }
        RenderSystem.setShaderTexture(0, chineseFestivals$MOON_LOCATION);
        int moonPhase = this.level.getMoonPhase();
        int dayTime = (int) (this.level.getDayTime() % 24000);
        int col = moonPhase % 4;
        int row = moonPhase / 4 % 2;
        float x1 = (float) col / 4.0f;
        float y1 = (float) row / 2.0f;
        float x2 = (float) (col + 1) / 4.0f;
        float y2 = (float) (row + 1) / 2.0f;
        tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        float l = 25.0f;
        if (dayTime <= 18000) {
            bufferBuilder.addVertex(matrix4f1, -l, -100.0f, l).setUv(x2, y2);
            bufferBuilder.addVertex(matrix4f1, l, -100.0f, l).setUv(x1, y2);
            bufferBuilder.addVertex(matrix4f1, l, -100.0f, -l).setUv(x1, y1);
            bufferBuilder.addVertex(matrix4f1, -l, -100.0f, -l).setUv(x2, y1);
        } else {
            bufferBuilder.addVertex(matrix4f1, l, -100.0f, -l).setUv(x2, y2);
            bufferBuilder.addVertex(matrix4f1, -l, -100.0f, -l).setUv(x1, y2);
            bufferBuilder.addVertex(matrix4f1, -l, -100.0f, l).setUv(x1, y1);
            bufferBuilder.addVertex(matrix4f1, l, -100.0f, l).setUv(x2, y1);
        }
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }

    @ModifyConstant(method = "renderSky", constant = @Constant(floatValue = 20.0f))
    private float injected(float x) {
        return Festivals.MOON_FESTIVAL.isNow() ? 0.0f : x;
    }
}
