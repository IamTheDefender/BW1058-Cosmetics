package xyz.iamthedefender.cosmetics.util;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class VaultUtils {

    public static Permission getPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null){
            getLogger().severe("You need permission plugin or this plugin will break!");
            return null;
        }
        return rsp.getProvider();
    }

    public static Economy getEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null){
            getLogger().severe("You need economy plugin or this plugin will break!");
            return null;
        }
        return rsp.getProvider();
    }
}
