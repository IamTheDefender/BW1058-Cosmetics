package xyz.iamthedefender.cosmetics.api.configuration;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import xyz.iamthedefender.cosmetics.api.util.Utility;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.stream.Collectors;

// Why copy code from bedwars1058 Config Manager?
// well because proxy have PluginConfig and bedwars1058 have ConfigManager

public class ConfigManager {
    private YamlConfiguration yml;
    private File config;
    private String name;
    private boolean firstTime = false;

    public ConfigManager(Plugin plugin, String name, String dir) {
        File d = new File(dir);
        if (!d.exists() && !d.mkdirs()) {
            plugin.getLogger().log(Level.SEVERE, "Could not create " + d.getPath());
        } else {
            this.config = new File(dir, name + ".yml");
            if (!this.config.exists()) {
                this.firstTime = true;
                plugin.getLogger().log(Level.INFO, "Creating " + this.config.getPath());

                try {
                    if (!this.config.createNewFile()) {
                        plugin.getLogger().log(Level.SEVERE, "Could not create " + this.config.getPath());
                        return;
                    }
                } catch (IOException var6) {
                    var6.printStackTrace();
                }
            }

            this.yml = YamlConfiguration.loadConfiguration(this.config);
            this.yml.options().copyDefaults(true);
            this.name = name;
        }
    }

    public void reload() {
        this.yml = YamlConfiguration.loadConfiguration(this.config);
    }


    public void set(String path, Object value) {
        this.yml.set(path, value);
        this.save();
    }

    public YamlConfiguration getYml() {
        return this.yml;
    }

    public void save() {
        try {
            this.yml.save(this.config);
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public List getList(String path) {
        return this.yml.getStringList(path).stream().map((s) -> {
            return s.replace("&", "§");
        }).collect(Collectors.toList());
    }


    public void setItemStack(String path, ItemStack itemStack){
        setIfNotFound(path, itemStack.getType().toString() + ":" + itemStack.getDurability());
        save();
    }

    public void setItemStack(String path, ItemStack itemStack, String base64){
        setIfNotFound(path, itemStack.getType().toString() + ":" + itemStack.getDurability() + ":" + base64);
        save();
    }

    private void setIfNotFound(String path, Object data) {
        if (getYml().get(path) == null){
            set(path, data);
            save();
        }
    }
    public ItemStack getItemStack(String path) {
        ItemStack item = null;
        String material = null;
        try {
            String[] data = getString(path).split(":", 2);
            material = data[0];
            if (material.equalsIgnoreCase("player_head") || material.equalsIgnoreCase("skull_item")){
                String[] data2 = getString(path).split(":", 3);
                String base64 = data2[2];
                item = getCustomSkull(base64);
                return item;
            }
            int damage = Integer.parseInt(data[1]);
            item = XMaterial.matchXMaterial(material.toUpperCase()).get().parseItem();
            assert item != null;
            if (damage != 0){
                item.setDurability((short) damage);
            }
        } catch (NoSuchElementException e2) {
            e2.printStackTrace();
            Bukkit.getLogger().severe("Looks like the material " + material + " is invalid!");
        }
        return item;
    }

    public static ItemStack getItemStack(FileConfiguration config, String path) {
        ItemStack item = null;
        String material = null;
        try {
            String[] data = config.getString(path).split(":", 2);
            material = data[0];
            if (material.equalsIgnoreCase("player_head") || material.equalsIgnoreCase("skull_item")){
                String[] data2 = config.getString(path).split(":", 3);
                String base64 = data2[2];
                item = getCustomSkull(base64);
                return item;
            }
            int damage = Integer.parseInt(data[1]);
            item = XMaterial.matchXMaterial(material.toUpperCase()).get().parseItem();
            assert item != null;
            item.setDurability((short) damage);
        } catch (NoSuchElementException e2) {
            e2.printStackTrace();
            Bukkit.getLogger().severe("Looks like the material " + material + " is invalid!");
        }
        return item;
    }

    public static ItemStack getCustomSkull(String base64) {
        return Utility.getApi().getVersionSupport().getSkull(base64);
    }

    public boolean getBoolean(String path) {
        return this.yml.getBoolean(path);
    }

    public int getInt(String path) {
        return this.yml.getInt(path);
    }

    public String getString(String path) {
        return this.yml.getString(path);
    }

    public boolean isFirstTime() {
        return this.firstTime;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void addDefaults(YamlConfiguration defaultConfig) {
        getYml().addDefaults(defaultConfig);
    }
}