package xyz.iamthedefender.cosmetics.util;

import com.hakan.core.HCore;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SkullUtil {
    private static final Map<String, ItemStack> skullCache = new HashMap<>();

    public static ItemStack makeTextureSkull(String base64Texture) {
        return HCore.skullBuilder(base64Texture).build();
    }
}
