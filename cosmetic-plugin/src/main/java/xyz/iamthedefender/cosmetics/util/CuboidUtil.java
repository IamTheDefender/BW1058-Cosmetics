package xyz.iamthedefender.cosmetics.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class CuboidUtil {
    private Location loc1;

    private Location loc2;

    World world;

    int topBlockX;

    int bottomBlockX;

    int topBlockY;

    int bottomBlockY;

    int topBlockZ;

    int bottomBlockZ;

    List<Block> blocks;

    public CuboidUtil(Location loc, int radius) {
        this.world = loc.getWorld();
        this.topBlockX = Math.max(loc.clone().add(radius, 0.0D, 0.0D).getBlockX(), loc.clone().subtract(radius, 0.0D, 0.0D).getBlockX());
        this.bottomBlockX = Math.min(loc.clone().add(radius, 0.0D, 0.0D).getBlockX(), loc.clone().subtract(radius, 0.0D, 0.0D).getBlockX());
        this.topBlockY = Math.max(loc.clone().add(0.0D, radius, 0.0D).getBlockY(), loc.clone().subtract(0.0D, radius, 0.0D).getBlockY());
        this.bottomBlockY = Math.min(loc.clone().add(0.0D, radius, 0.0D).getBlockY(), loc.clone().subtract(0.0D, radius, 0.0D).getBlockY());
        this.topBlockZ = Math.max(loc.clone().add(0.0D, 0.0D, radius).getBlockZ(), loc.clone().subtract(0.0D, 0.0D, radius).getBlockZ());
        this.bottomBlockZ = Math.min(loc.clone().add(0.0D, 0.0D, radius).getBlockZ(), loc.clone().subtract(0.0D, 0.0D, radius).getBlockZ());
        this.blocks = getBlocks();
    }

    public boolean isInCached(Block block) {
        return this.blocks.contains(block);
    }

    public List<Block> getCachedBlocks() {
        return this.blocks;
    }

    public List<Block> getAirBlocks() {
        List<Block> blocks = new ArrayList<>();
        for (int x = this.bottomBlockX; x <= this.topBlockX; x++) {
            for (int z = this.bottomBlockZ; z <= this.topBlockZ; z++) {
                for (int y = this.bottomBlockY; y <= this.topBlockY; y++) {
                    Block block = this.world.getBlockAt(x, y, z);
                    if (block.getType().equals(Material.AIR))
                        blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();
        for (int x = this.bottomBlockX; x <= this.topBlockX; x++) {
            for (int z = this.bottomBlockZ; z <= this.topBlockZ; z++) {
                for (int y = this.bottomBlockY; y <= this.topBlockY; y++) {
                    Block block = this.world.getBlockAt(x, y, z);
                    if (!block.getType().equals(Material.AIR))
                        blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public boolean isIn(Location loc) {
        return isIn(loc.getBlock());
    }

    public boolean isIn(Block block) {
        return getBlocks().contains(block);
    }

    public Location getLoc1() {
        return this.loc1;
    }

    public Location getLoc2() {
        return this.loc2;
    }
}

