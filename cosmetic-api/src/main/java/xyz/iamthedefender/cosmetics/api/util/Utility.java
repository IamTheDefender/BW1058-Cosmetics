

package xyz.iamthedefender.cosmetics.api.util;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hakan.core.utils.ColorUtil;
import lombok.Getter;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.iamthedefender.cosmetics.api.CosmeticsAPI;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Utility {
    @Getter
    private static final CosmeticsAPI api = Bukkit.getServicesManager().getRegistration(CosmeticsAPI.class).getProvider();

    /**
     * @param s the main string for example "Click me"
     * @param st the message when they hover.
     * @return a hover able message.
     */

    public static TextComponent hoverablemsg(String s, String st) {
        final TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', s));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', st)).create()));
        return message;
    }


    public static JavaPlugin getPlugin() {
        return api.getPlugin();
    }


    /**
     * Get Message from Language file of bedwars1058 for player object, works with Bedwars1058 and BedwarsProxy
     * @param p player object
     * @param path Path where the string is located.
     * @return the result
     */
    public static String getMSGLang(Player p, String path) {
        return ColorUtil.colored(api.getHandler().getLanguageUtil().getMessage(p, path));
    }

    /**
     * Get a list from language file according to the player selected language iso. Works with Bedwars1058 and BedwarsProxy
     * @param p player object
     * @param path path for the list in the YAML file.
     * @return the result
     */
    public static List<String> getListLang(Player p, String path) {
       return api.getHandler().getLanguageUtil().getMessageList(p, path)
               .stream().map(ColorUtil::colored).collect(Collectors.toList());
    }

    /**
     * Save the object to the path in language file, works with BedWars1058 and BedWarsProxy
     * @param path Path where the object will be saved.
     * @param ob object
     */
    public static void saveIfNotExistsLang(String path, Object ob) {
        api.getHandler().getLanguageUtil().saveIfNotExists(path, ob);
    }

    /**
     * Check's if the player is in an Arena.
     * @param p player to check for.
     * @return true if player is in arena, false otherwise.
     */
    public static boolean isInArena(Player p) {
        if (api.isProxy())
            return false;
        return api.getHandler().getArenaUtil().getArenaByPlayer(p) != null;
    }


    /**
     * Get skin of a player from their name
     * @param name name of the player
     * @return String[]{texture, signature}
     */
    public static String[] getFromName(String name) {
        try {
            URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
            String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();

            return new String[]{texture, signature};
        } catch (IOException e) {
            api.getPlugin().getLogger().warning("Could not get skin data from session servers!");
            return null;
        }
    }


    /**
     * Save a file from plugin JAR to a folder
     * @param inputStream inputStream of the file
     * @param fileName name of the file
     * @param folder folder where the file will be saved
     */
    public static void saveFileFromInputStream(InputStream inputStream, String fileName, File folder)  {
        File file = new File(folder, fileName);
        if (file.exists()) return;
        try (OutputStream outputStream = Files.newOutputStream(file.toPath())) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean isWoodOrLogBlock(Material mat) {
        return (mat.toString().contains("WOOD") || mat.toString().contains("PLANKS") || mat.toString().contains("LOG")) && mat.isBlock();
    }

    public static List<Block> getSphere(Location loc, int radius) {
        List<Block> blocks = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.sqrt(x * x + y * y + z * z) <= radius) {
                        blocks.add(loc.clone().add(x, y, z).getBlock());
                    }
                }
            }
        }
        return blocks;
    }
}