package xyz.iamthedefender.cosmetics.category.projectiletrails.util;

import com.hakan.core.HCore;
import com.hakan.core.particle.Particle;
import com.hakan.core.particle.type.ParticleType;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.ProjectileTrail;
import xyz.iamthedefender.cosmetics.util.StartupUtils;
import xyz.iamthedefender.cosmetics.api.util.config.ConfigUtils;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ProjectileEffectsUtil {


	/**
	 * Sends particle effect for the given entity.
	 *
	 * @param e The entity for which to send the effect
	 * @param p The player who owns the entity
	 */
	public static void sendEffect(Entity e, Player p) {
		CosmeticsType type = CosmeticsType.ProjectileTrails;
		String selected = Cosmetics.getInstance().getApi().getSelectedCosmetic(p, type);
		String effect = ConfigUtils.getProjectileTrails().getString(type.getSectionKey() + "." + selected + ".particle");
		Color color = ConfigUtils.getProjectileTrails().getYml().getColor(type.getSectionKey() + "." + selected + ".color");
		ProjectileTrail projectileTrail = null;
		for(ProjectileTrail trail : StartupUtils.projectileTrailList){
			if (selected.equals(trail.getIdentifier())){
				projectileTrail = trail;
			}
		}
		if (projectileTrail != null && projectileTrail.getField(FieldsType.RARITY, p) != RarityType.NONE){
			Particle particle;
			if (color == null) {
				particle = new Particle(ParticleType.valueOf(effect), 1, 0, new Vector(0, 0, 0));
			} else {
				particle = new Particle(ParticleType.valueOf(effect), 1, 0, new Vector(0, 0, 0), color);
			}
			HCore.playParticle(e.getLocation(), particle);
		}
	}

}
