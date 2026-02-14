package dev.anvilcraft.festivals.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.anvilcraft.festivals.features.IFeature;
import dev.anvilcraft.festivals.util.ParticleUtil;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FireworkParticles.Starter.class)
public abstract class FireworkParticles$StarterMixin extends Particle {
    @Shadow
    public abstract void createParticle(
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed,
        IntList colors,
        IntList fadeColors,
        boolean trail,
        boolean twinkle
    );

    protected FireworkParticles$StarterMixin(ClientLevel clientLevel, double x, double y, double z) {
        super(clientLevel, x, y, z);
    }

    @WrapOperation(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/particle/FireworkParticles$Starter;createParticleShape(D[[DLit/unimi/dsi/fastutil/ints/IntList;Lit/unimi/dsi/fastutil/ints/IntList;ZZZ)V"
        )
    )
    public void modifyParticle(
        FireworkParticles.Starter instance,
        double speed,
        double[][] oldParticles,
        IntList colors,
        IntList fadeColors,
        boolean trail,
        boolean flicker,
        boolean isCreeper,
        Operation<Void> original
    ) {
        double[][] particles = IFeature.execute(IFeature::getFireworkParticle);
        if (particles == null) {
            ((FireworkParticles$StarterAccessor) instance).invokeCreateParticleShape(
                speed,
                oldParticles,
                colors,
                fadeColors,
                trail,
                flicker,
                isCreeper
            );
        } else {
            ParticleUtil.createParticleSingleShape(
                this.x,
                this.y,
                this.z,
                this.random,
                this::createParticle,
                particles,
                colors,
                fadeColors,
                trail,
                flicker
            );
        }
    }
}
