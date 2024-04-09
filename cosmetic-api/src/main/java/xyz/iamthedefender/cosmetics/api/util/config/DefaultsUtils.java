package xyz.iamthedefender.cosmetics.api.util.config;

import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.iamthedefender.cosmetics.api.util.Utility;

import java.io.InputStream;
import java.io.InputStreamReader;

public class DefaultsUtils {

    /**
     * This method will save default values from the
     * plugin jar yml for example, from Glyph.yml to
     * the file located in the plugin folder, example
     * resources/Glyph.yml -> pluginFolder/Categories/Glyph.yml
     */
    public void saveAllDefaults(){
        for(ConfigType type : ConfigType.values()){
            ConfigManager config = ConfigUtils.get(type);
            InputStream defaultConfigStream = Utility.getPlugin().getResource(type.getFileName() + ".yml");
            if (defaultConfigStream != null) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream));
                config.addDefaults(defaultConfig);
                config.save();
            }
            config.reload();
            config.save();
            String extrasPath = "Extras.fill-empty.";
            if (config.isFirstTime() && !config.getBoolean(extrasPath + "enabled")){
                config.set(extrasPath + "enabled", true);
                config.set(extrasPath + "item", "BLACK_STAINED_GLASS_PANE:0");
                config.save();
                config.reload();
            }
        }
    }

}
