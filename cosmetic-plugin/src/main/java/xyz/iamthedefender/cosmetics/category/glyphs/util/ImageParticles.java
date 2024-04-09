

package xyz.iamthedefender.cosmetics.category.glyphs.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ImageParticles
{
    private final Map<Vector, Color> particles;
    private Vector anchor;
    private double ratio;
    private final BufferedImage image;
    private final int clearence;

    public ImageParticles(final BufferedImage image, final int scanQuality) {
        this.particles = new HashMap<>();
        this.anchor = new Vector(0, 0, 0);
        this.ratio = 0.1;
        this.clearence = 100;
        this.image = image;
        this.renderParticles(Math.abs(scanQuality));
    }

    public void setAnchor(final int x, final int y) {
        this.anchor = new Vector(x, y, 0);
    }

    public void setDisplayRatio(final double ratio) {
        this.ratio = ratio;
    }

    public Map<Location, Color> getParticles(final Location location, final double pitch, final double yaw) {
        final Map<Location, Color> map = new HashMap<Location, Color>();
        for (final Vector vector : this.particles.keySet()) {
            final Vector difference = vector.clone().subtract(this.anchor).multiply(this.ratio);
            Vector v = this.rotateAroundAxisX(difference, pitch);
            v = this.rotateAroundAxisY(v, yaw);
            final Location spot = location.clone().add(difference);
            map.put(spot, this.particles.get(vector));
        }
        return map;
    }

    public Map<Location, Color> getParticles(final Location location) {
        return this.getParticles(location, location.getPitch(), location.getYaw());
    }

    private void renderParticles(final int sensitivity) {
        final int height = this.image.getHeight();
        for (int width = this.image.getWidth(), x = 0; x < width; x += sensitivity) {
            for (int y = 0; y < height; y += sensitivity) {
                final int rgb = this.image.getRGB(x, y);
                if (-rgb > this.clearence) {
                    final java.awt.Color javaColor = new java.awt.Color(rgb);
                    final Vector vector = new Vector(width - 1 - x, height - 1 - y, 0);
                    this.particles.put(vector, Color.fromRGB(javaColor.getRed(), javaColor.getGreen(), javaColor.getBlue()));
                }
            }
        }
    }

    private Vector rotateAroundAxisX(final Vector v, double angle) {
        angle = Math.toRadians(angle);
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        final double y = v.getY() * cos - v.getZ() * sin;
        final double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    private Vector rotateAroundAxisY(final Vector v, double angle) {
        angle = -angle;
        angle = Math.toRadians(angle);
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        final double x = v.getX() * cos + v.getZ() * sin;
        final double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }
}
