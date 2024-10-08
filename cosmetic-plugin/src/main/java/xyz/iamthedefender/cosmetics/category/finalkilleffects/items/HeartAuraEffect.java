package xyz.iamthedefender.cosmetics.category.finalkilleffects.items;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.HCore;
import com.hakan.core.particle.Particle;
import com.hakan.core.particle.type.ParticleType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.FinalKillEffect;

import java.util.Arrays;
import java.util.List;

public class HeartAuraEffect extends FinalKillEffect {
    @Override
    public ItemStack getItem() {
        return XMaterial.DIAMOND.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "heart-aura";
    }

    @Override
    public String getDisplayName() {
        return "Heart Aura";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList("&7Spawns tons of heart at", "&7location of victim!");
    }

    @Override
    public int getPrice() {
        return 5000;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.COMMON;
    }

    @Override
    public void execute(Player killer, Player victim, Location location, boolean onlyVictim) {
        sendParticles(victim, location, onlyVictim);
    }

    private void sendParticles(Player victim, Location location, boolean onlyVictim) {
        if (!onlyVictim) {
            HCore.playParticle(location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.0f, 0.0f, 0.0f)));
            HCore.playParticle(location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.0f, 0.1f, 0.0f)));
            HCore.playParticle(location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.0f, 0.2f, 0.0f)));
            HCore.playParticle(location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.0f, 0.3f, 0.1f)));
            HCore.playParticle(location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.0f, 0.4f, 0.3f)));
            HCore.playParticle(location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.4f, 0.5f, 0.0f)));
            HCore.playParticle(location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.1f, 0.0f, 0.0f)));
            HCore.playParticle(location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.2f, 0.3f, 0.0f)));
        } else {
            HCore.playParticle(victim, location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.0f, 0.0f, 0.0f)));
            HCore.playParticle(victim, location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.0f, 0.1f, 0.0f)));
            HCore.playParticle(victim, location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.0f, 0.2f, 0.0f)));
            HCore.playParticle(victim, location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.0f, 0.3f, 0.1f)));
            HCore.playParticle(victim, location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.0f, 0.4f, 0.3f)));
            HCore.playParticle(victim, location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.4f, 0.5f, 0.0f)));
            HCore.playParticle(victim, location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.1f, 0.0f, 0.0f)));
            HCore.playParticle(victim, location, new Particle(ParticleType.HEART, 100, 0.01, new Vector(0.2f, 0.3f, 0.0f)));
        }
    }
}