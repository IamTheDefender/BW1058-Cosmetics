package xyz.iamthedefender.cosmetics.category.islandtoppers.util;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public class BlockData {

    private final Material material;
    private final byte data;

    public BlockData(Material material, byte data) {
        this.material = material;
        this.data = data;
    }
}