package xyz.iamthedefender.cosmetics.menu;

import com.cryptomorin.xseries.XItemStack;
import com.hakan.core.HCore;
import com.hakan.core.ui.inventory.InventoryGui;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.util.Utility;
import xyz.iamthedefender.cosmetics.util.MainMenuUtils;

import java.util.List;

public class MainMenu extends InventoryGui {

    FileConfiguration config = Cosmetics.getInstance().menuData.getYml();

    public MainMenu(Player player) {
        super("none", Utility.getMSGLang(player, "cosmetics.gui-title") , 6, InventoryType.CHEST);
    }

    @Override
    public void onOpen(@NotNull Player player) {
        String loc = "Main-Menu";
        String langLoc = "cosmetics.main-menu";
        for(String name : config.getConfigurationSection(loc).getKeys(false)) {
            try {
                ItemStack itemStack = ConfigManager.getItemStack(config, loc + "." + name + ".item");
                List<String> lore = Utility.getListLang(player, langLoc + "." + name + ".lore");
                String itemName = Utility.getMSGLang(player, langLoc + "." + name + ".name");
                int slot = config.getInt(loc + "." + name + ".slot");
                List<String> lores = MainMenuUtils.formatLore(lore, player);
                boolean disabled = config.getBoolean(loc + "." + name + ".disabled");

                // Translate for XItemStack
                ConfigurationSection configurationSection = new MemoryConfiguration();
                configurationSection.set("lore", lore);
                configurationSection.set("name", itemName);

                if (itemStack != null && !disabled) {

                    super.setItem(slot, XItemStack.edit(itemStack, configurationSection, s -> s, null), (e) -> {
                        MainMenuUtils.openMenus((Player) e.getWhoClicked(), name);
                    });
                }
            }catch (Exception exception){
                Bukkit.getLogger().warning("There was an error with main menu item: " + name);
                throw new RuntimeException(exception);
            }
        }
        String extrasPath = "Extras.fill-empty.";
        if (config.getBoolean(extrasPath + "enabled")){
            ItemStack stack = ConfigManager.getItemStack(config, extrasPath + "item");
            while (toInventory().firstEmpty() != -1){
                setItem(toInventory().firstEmpty(), HCore.itemBuilder(stack).name(true, "&r").build());
            }
        }
    }
}
