package xyz.iamthedefender.cosmetics.util;

import xyz.iamthedefender.cosmetics.Cosmetics;

public class DebugUtil {

    public static void addMessage(Object message){
        if (Cosmetics.getInstance().getConfig().getBoolean("Debug")) {
            Cosmetics.getInstance().getLogger().info("DEBUG: " + message);
        }
    }
}
