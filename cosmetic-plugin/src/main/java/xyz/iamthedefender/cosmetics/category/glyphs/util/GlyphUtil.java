

package xyz.iamthedefender.cosmetics.category.glyphs.util;

import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import com.hakan.core.HCore;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.util.Run;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

public class GlyphUtil
{
    
    public static void sendGlyphs(File file, Location loc) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (final IOException e) {
            Cosmetics.getInstance().getLogger().log(Level.SEVERE, "Unable to read glyph file! please check your config!");
        }
        ImageParticles particles = new ImageParticles(image, 1);
        particles.setAnchor(50, 10);
        particles.setDisplayRatio(0.1);
        Map<Location, Color> particle = particles.getParticles(loc, loc.getPitch(), 180.0f);

        for (Location spot : particle.keySet()) {
            Run.sync(() -> sendRedstoneParticle(null, spot, particle.get(spot)));
        }
    }

    public static void sendRedstoneParticle(Player player, Location location, Color color){
        Cosmetics.getInstance().getVersionSupport().displayRedstoneParticle(player, location, color);
       /* Old Particle send code
        boolean oneEight = false;

        try {
            Class.forName("org.bukkit.Particle");
        } catch (ClassNotFoundException ex) {
            oneEight = true;
        }

        if(oneEight){

            return;
        }

        XParticle particle = XParticle.DUST;

        ParticleDisplay.of(particle).withColor(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue()))
                .withExtra(1.0f)
                .onlyVisibleTo(player)
                .spawn(location);
        */
    }
}