package xyz.iamthedefender.cosmetics.support.bedwars.handler.bedwars2023;

import com.tomkeuper.bedwars.api.addon.Addon;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import xyz.iamthedefender.cosmetics.Cosmetics;

public class BedWars2023Addon extends Addon {
    private final Cosmetics plugin;

    public BedWars2023Addon(Cosmetics plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String getName() {
        return plugin.getDescription().getName();
    }

    @Override
    public String getDescription() {
        return plugin.getDescription().getDescription();
    }

    @Override
    public void load() {
        Bukkit.getPluginManager().enablePlugin(plugin);
    }

    @Override
    public void unload() {
        Bukkit.getPluginManager().disablePlugin(plugin);
    }
}