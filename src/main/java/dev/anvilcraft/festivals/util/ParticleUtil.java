package dev.anvilcraft.festivals.util;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.util.RandomSource;

public class ParticleUtil {
    public static void createParticleSingleShape(
        double x,
        double y,
        double z,
        RandomSource random,
        CreateParticle createParticle,
        double[][] particles,
        IntList colors,
        IntList fadeColors,
        boolean trail,
        boolean flicker
    ) {
        double rotate = random.nextFloat() * (float) Math.PI * 2;
        for (double[] particle : particles) {
            double dx = particle[0];
            double dy = particle[1];
            double dz = dx * Math.sin(rotate);
            dx *= Math.cos(rotate);
            createParticle.run(x, y, z, dx, dy, dz, colors, fadeColors, trail, flicker);
        }
    }

    @FunctionalInterface
    public interface CreateParticle {
        void run(
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
    }
}
