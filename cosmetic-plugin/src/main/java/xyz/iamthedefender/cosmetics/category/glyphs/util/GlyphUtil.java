

package xyz.iamthedefender.cosmetics.category.glyphs.util;

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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

public class GlyphUtil
{
    
    public static boolean sendGlyphs(File file, Location loc) {
        BufferedImage image = null;
        AtomicBoolean failed = new AtomicBoolean(false);

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
            if (failed.get()) continue;

            Run.sync(() -> {
                try {
                    sendRedstoneParticle(null, spot, particle.get(spot));
                }catch (Exception exception) {
                    failed.set(true);
                    exception.printStackTrace();
                }
            });
        }

        return !failed.get();
    }

    public static void sendRedstoneParticle(Player player, Location location, Color color){
        Cosmetics.getInstance().getVersionSupport().displayRedstoneParticle(player, location, color);
    }
}