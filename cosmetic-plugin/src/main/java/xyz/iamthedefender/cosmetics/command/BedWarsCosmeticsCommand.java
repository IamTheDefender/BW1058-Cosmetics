package xyz.iamthedefender.cosmetics.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.handler.ISetupSession;
import xyz.iamthedefender.cosmetics.api.menu.SystemGui;
import xyz.iamthedefender.cosmetics.api.util.ColorUtil;
import xyz.iamthedefender.cosmetics.api.util.Utility;
import xyz.iamthedefender.cosmetics.api.util.config.ConfigType;
import xyz.iamthedefender.cosmetics.api.util.config.ConfigUtils;
import xyz.iamthedefender.cosmetics.menu.CategoryMenu;
import xyz.iamthedefender.cosmetics.menu.MainMenu;
import xyz.iamthedefender.cosmetics.util.MainMenuUtils;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

@CommandAlias("bwc|bedwarscosmetics|cosmetics|cos|bwcos|bwcosmetic|bwcosmetics")
@Description("Main command for the cosmetics plugin.")
public class BedWarsCosmeticsCommand extends BaseCommand {

    private final CosmeticsPlugin plugin = CosmeticsPlugin.getInstance();
    
    @Subcommand("help")
    @CommandPermission("bwcosmetics.help")
    public void helpCommand(Player player) {
        player.sendMessage(ColorUtil.translate("&7-> &6BedWars1058-Cosmetics Addon &7- &cCommands &7<-"));
        player.sendMessage(" ");
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc reload    &8- &eclick for details", "Reloads the all the YAML's"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc menu    &8- &eclick for details", "Opens the Main Menu"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc km    &8- &eclick for details", "Opens the Kill message GUI"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc shopkeeper    &8- &eclick for details", "Opens the ShopKeeperSkin GUI"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc sprays    &8- &eclick for details", "Opens the Sprays GUI"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc dc    &8- &eclick for details", "Opens the Death Cries GUI"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc glyphs    &8- &eclick for details", "Opens the Glyphs GUI"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc bbe    &8- &eclick for details", "Opens the Bed Break Effect GUI"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc finalke    &8- &eclick for details", "Opens the Final Kill Effect GUI"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc pt    &8- &eclick for details", "Opens the Projectile Trails GUI"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc vd    &8- &eclick for details", "Opens the Victory Dance GUI"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc ws    &8- &eclick for details", "Opens the Wood Skins GUI"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc it    &8- &eclick for details", "Opens the Island Toppers GUI"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc setIslandTopperPosition" +
                "    &8- &eclick for details", "Set the island topper location for an team in an arena"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc setupPlayerLocation    &8- &eclick for details", "Set the player location for preview"));
        player.spigot().sendMessage(Utility.hoverablemsg("&6-> &7/bwc setupPreviewLocation    &8- &eclick for details", "Set the preview location"));

    }


    @Subcommand("set")
    @CommandPermission("bwcosmetics.admin")
    public void setCommand(CommandSender sender, CosmeticsType cosmeticsType, String cosmeticID, String playerName) {
        Player player = Bukkit.getPlayer(playerName);

        if (player == null || !player.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Invalid or offline player: " + playerName);
            return;
        }

        plugin.getApi().setSelectedCosmetic(player, cosmeticsType, cosmeticID);
        sender.sendMessage(ColorUtil.translate("&aSuccess! Note, this command will not check if cosmeticsID is valid!"));
    }


    @Subcommand("reload")
    @CommandPermission("bwcosmetics.reload")
    public void reloadCommand(CommandSender sender) {
        sender.sendMessage("§aReloading the YAML's, please wait...");
        StartupUtils.updateConfigs();
        MainMenuUtils.saveLores();
        for(ConfigType configType : ConfigType.values()){
            ConfigUtils.get(configType).reload();
        }
        sender.sendMessage("§aReloaded the YAML's!");
    }


    @Subcommand("menu")
    public void menuCommand(Player player) {
        if (Utility.isInArena(player)) throw new ConditionFailedException("You cannot do that while in a game!");

        new MainMenu(player).open(player);
    }

    @Subcommand("km")
    public void onKmMenu(Player player) {
        openMenu(player, CosmeticsType.KillMessage);
    }

    @Subcommand("shopkeeper")
    public void onShopkeeperMenu(Player player) {
        openMenu(player, CosmeticsType.ShopKeeperSkin);
    }

    @Subcommand("sprays")
    public void onSpraysMenu(Player player) {
        openMenu(player, CosmeticsType.Sprays);
    }

    @Subcommand("dc")
    public void onDcMenu(Player player) {
        openMenu(player, CosmeticsType.DeathCries);
    }

    @Subcommand("glyphs")
    public void onGlyphsMenu(Player player) {
        openMenu(player, CosmeticsType.Glyphs);
    }

    @Subcommand("bbe")
    public void onBbeMenu(Player player) {
        openMenu(player, CosmeticsType.BedBreakEffects);
    }

    @Subcommand("finalke")
    public void onFinalkeMenu(Player player) {
        openMenu(player, CosmeticsType.FinalKillEffects);
    }

    @Subcommand("pt")
    public void onPtMenu(Player player) {
        openMenu(player, CosmeticsType.ProjectileTrails);
    }

    @Subcommand("vd")
    public void onVdMenu(Player player) {
        openMenu(player, CosmeticsType.VictoryDances);
    }

    @Subcommand("ws")
    public void onWsMenu(Player player) {
        openMenu(player, CosmeticsType.WoodSkins);
    }

    @Subcommand("it")
    public void onItMenu(Player player) {
        openMenu(player, CosmeticsType.IslandTopper);
    }

    @Subcommand("setIslandTopperPosition")
    @CommandPermission("bwcosmetics.admin")
    public void onSetIslandTopperPosition(Player player, String teamName) {
        ISetupSession setupSession = plugin.getHandler().getSetupSession(player.getUniqueId());

        if (setupSession == null) {
            player.sendMessage(ChatColor.RED + "You need to be in setup when you use this command!");
            return;
        }

        ConfigurationSection section = setupSession.getConfig().getConfigurationSection("Team." + teamName);
        if (section == null) {
            player.sendMessage(ColorUtil.translate("&cYou need to setup teams before you do this command!"));
            return;
        }

        String configPath = "Team." + teamName;
        setupSession.saveConfigLoc(configPath + ".IslandTopper.location", player.getLocation());

        ArmorStand hologramStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0, 3, 0), EntityType.ARMOR_STAND);
        hologramStand.setGravity(false);
        hologramStand.setVisible(false);
        hologramStand.setCustomNameVisible(true);

        ChatColor color = getColorForTeam(section, teamName);

        hologramStand.setCustomName(color + teamName + " " + ChatColor.GOLD + "ISLAND TOPPER SET");
        player.sendMessage(ChatColor.GREEN + "Done! saved your current location as Island Topper location for team " + teamName);
    }

    @Subcommand("setupPreviewLocation")
    @CommandPermission("bwcosmetics.admin")
    public void onSetPreviewLocation(Player player) {
        saveLocation(player, "cosmetic-preview.cosmetic-location");
        player.sendMessage(ChatColor.GREEN + "Done! saved your current location as preview location.");
    }

    @Subcommand("setupPlayerLocation")
    @CommandPermission("bwcosmetics.admin")
    public void onSetPlayerLocation(Player player) {
        saveLocation(player, "cosmetic-preview.player-location");
        player.sendMessage(ChatColor.GREEN + "Done! saved your current location as player location for preview.");
    }

    private void openMenu(Player player, CosmeticsType cosmeticsType) {
        if (Utility.isInArena(player)) {
            player.sendMessage(ChatColor.RED + "You cannot do that while in a game!");
            return;
        }

        String title = cosmeticsType.getFormatedName();
        if (CosmeticsPlugin.isPlaceholderAPI()) {
            title = PlaceholderAPI.setPlaceholders(player, title);
        }

        SystemGui inv = new CategoryMenu(cosmeticsType, title);
        inv.open(player);
    }

    private ChatColor getColorForTeam(ConfigurationSection section, String teamName) {
        if (teamName.equalsIgnoreCase("Pink")) {
            return ChatColor.LIGHT_PURPLE;
        }

        try {
            return ChatColor.valueOf(section.getString("Color"));
        } catch (Exception e) {
            return ChatColor.GOLD;
        }
    }

    private void saveLocation(Player player, String path) {
        Location location = player.getLocation();
        ConfigManager config = ConfigUtils.getMainConfig();
        config.set(path + ".world", location.getWorld().getName());
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        config.set(path + ".yaw", location.getYaw());
        config.set(path + ".pitch", location.getPitch());
        config.save();
        config.reload();
    }

}