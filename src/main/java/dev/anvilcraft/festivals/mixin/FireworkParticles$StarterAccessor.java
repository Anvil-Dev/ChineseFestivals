package dev.anvilcraft.festivals.mixin;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.particle.FireworkParticles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FireworkParticles.Starter.class)
public interface FireworkParticles$StarterAccessor {
    @Invoker("createParticleShape")
    void invokeCreateParticleShape(double speed, double[][] coords, IntList colors, IntList fadeColors, boolean trail, boolean twinkle, boolean isCreeper);
}
