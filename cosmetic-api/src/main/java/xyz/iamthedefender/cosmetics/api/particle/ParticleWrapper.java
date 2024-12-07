package xyz.iamthedefender.cosmetics.api.particle;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedParticle;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;


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

}
