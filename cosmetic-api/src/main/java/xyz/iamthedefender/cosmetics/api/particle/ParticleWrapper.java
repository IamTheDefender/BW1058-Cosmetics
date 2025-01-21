package xyz.iamthedefender.cosmetics.api.particle;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedParticle;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.iamthedefender.cosmetics.api.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.util.Utility;
import xyz.iamthedefender.cosmetics.api.versionsupport.IVersionSupport;

import java.util.Objects;
import java.util.Optional;


@Getter
public class ParticleWrapper {

    private final @Nullable EnumWrappers.Particle wrapperParticle;
    private final @Nullable WrappedParticle<?> newWrapperParticle;

    public ParticleWrapper(@Nullable EnumWrappers.Particle wrapperParticle, @Nullable WrappedParticle<?> newWrapperParticle) {
        this.wrapperParticle = wrapperParticle;
        this.newWrapperParticle = newWrapperParticle;

        if (wrapperParticle == null && newWrapperParticle == null) {
            throw new IllegalArgumentException("Both arguments cannot be null!");
        }
    }

    public ParticleWrapper(@Nullable EnumWrappers.Particle wrapperParticle) {
        this(wrapperParticle, null);
    }

    public ParticleWrapper(@Nullable WrappedParticle<?> newWrapperParticle) {
        this(null, newWrapperParticle);
    }

    public @NotNull IVersionSupport support() {
        return Utility.getApi().getVersionSupport();
    }

    public static @NotNull Optional<ParticleWrapper> getParticle(@NotNull String name) {
        Objects.requireNonNull(name, "The particle name cannot be null!");

        name = name.toUpperCase();

        ParticleWrapper particleWrapper;

        try {
            Class.forName("org.bukkit.Particle");

            particleWrapper = new ParticleWrapper(WrappedParticle.create(org.bukkit.Particle.valueOf(name), null));
        } catch (Exception exception) {
            try {
                particleWrapper = new ParticleWrapper(EnumWrappers.Particle.valueOf(name));
                Objects.requireNonNull(particleWrapper.getWrapperParticle());

                if (!Utility.getApi().getVersionSupport().isValidParticle(particleWrapper.getWrapperParticle().getName()))
                    throw new RuntimeException("Invalid particle: " + particleWrapper.getWrapperParticle().getName());

            }catch (Exception exception1) {
                particleWrapper = null;
            }
        }


        return Optional.ofNullable(particleWrapper);
    }
}
