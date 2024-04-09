package xyz.iamthedefender.cosmetics.support.bedwars.handler.bedwars2023;

import com.tomkeuper.bedwars.BedWars;
import xyz.iamthedefender.cosmetics.Cosmetics;
import org.bukkit.Bukkit;

public class BedWars2023 {
    private final Cosmetics plugin;

    public BedWars2023(Cosmetics plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (Bukkit.getPluginManager().getPlugin("BedWars2023") != null) {
            BedWars.getAPI().getAddonsUtil().registerAddon(new BedWars2023Addon(plugin));
        }
    }
}