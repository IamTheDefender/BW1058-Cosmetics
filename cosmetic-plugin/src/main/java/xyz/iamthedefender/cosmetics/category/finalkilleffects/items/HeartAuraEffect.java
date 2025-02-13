package xyz.iamthedefender.cosmetics.category.finalkilleffects.items;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.FinalKillEffect;
import xyz.iamthedefender.cosmetics.api.particle.ParticleWrapper;
import xyz.iamthedefender.cosmetics.api.versionsupport.IVersionSupport;

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
        IVersionSupport versionSupport = CosmeticsPlugin.getInstance().getVersionSupport();
        ParticleWrapper particleWrapper = ParticleWrapper.getParticle("HEART").orElse(null);

        if (particleWrapper == null) {
            CosmeticsPlugin.getInstance().getLogger().severe("Particle not found: HEART" + " - " + versionSupport.getVersion());
            return;
        }

        if (!onlyVictim) {
            versionSupport.displayParticle(null, location, particleWrapper, 100, 0.01f);
            versionSupport.displayParticle(null, location, particleWrapper, 100, 0.01f, 0, 0.1f, 0);
            versionSupport.displayParticle(null, location, particleWrapper, 100, 0.01f, 0.0f, 0.2f, 0);
            versionSupport.displayParticle(null, location, particleWrapper, 100, 0.01f, 0.0f, 0.3f, 0);
            versionSupport.displayParticle(null, location, particleWrapper, 100, 0.01f, 0.0f, 0.4f, 0.1f);
            versionSupport.displayParticle(null, location, particleWrapper, 100, 0.01f, 0.0f, 0.5f, 0.3f);
            versionSupport.displayParticle(null, location, particleWrapper, 100, 0.01f, 0.1f, 0.0f, 0);
            versionSupport.displayParticle(null, location, particleWrapper, 100, 0.01f, 0.2f, 0.3f, 0);
            versionSupport.displayParticle(null, location, particleWrapper, 100, 0.01f, 0.3f, 0.0f, 0);
        } else {
            versionSupport.displayParticle(victim, location, particleWrapper, 100, 0.01f);
            versionSupport.displayParticle(victim, location, particleWrapper, 100, 0.01f, 0, 0.1f, 0);
            versionSupport.displayParticle(victim, location, particleWrapper, 100, 0.01f, 0.0f, 0.2f, 0);
            versionSupport.displayParticle(victim, location, particleWrapper, 100, 0.01f, 0.0f, 0.3f, 0);
            versionSupport.displayParticle(victim, location, particleWrapper, 100, 0.01f, 0.0f, 0.4f, 0.1f);
            versionSupport.displayParticle(victim, location, particleWrapper, 100, 0.01f, 0.0f, 0.5f, 0.3f);
            versionSupport.displayParticle(victim, location, particleWrapper, 100, 0.01f, 0.1f, 0.0f, 0);
            versionSupport.displayParticle(victim, location, particleWrapper, 100, 0.01f, 0.2f, 0.3f, 0);
            versionSupport.displayParticle(victim, location, particleWrapper, 100, 0.01f, 0.3f, 0.0f, 0);
        }
    }
}