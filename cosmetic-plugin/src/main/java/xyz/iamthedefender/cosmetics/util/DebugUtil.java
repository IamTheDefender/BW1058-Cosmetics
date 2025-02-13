package xyz.iamthedefender.cosmetics.util;

import xyz.iamthedefender.cosmetics.CosmeticsPlugin;

public class DebugUtil {

    public static void addMessage(Object message){
        if (CosmeticsPlugin.getInstance().getConfig().getBoolean("Debug")) {
            CosmeticsPlugin.getInstance().getLogger().info("DEBUG: " + message);
        }
    }
}
