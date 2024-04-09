package xyz.iamthedefender.cosmetics.api.util.config;

public enum ConfigType {
    DEATH_CRIES("DeathCries"),
    BED_DESTROYS("BedBreakEffect"),
    FINAL_KILL_EFFECTS("FinalKillEffects"),
    GLYPHS("Glyphs"),
    ISLAND_TOPPERS("IslandToppers"),
    KILL_MESSAGES("KillMessages"),
    PROJECTILE_TRAILS("ProjectileTrails"),
    SHOP_KEEPER_SKINS("ShopKeeperSkins"),
    SPRAYS("Sprays"),
    VICTORY_DANCES("VictoryDances"),
    WOOD_SKINS("WoodSkins"),

    Main_Config("config"),
    Main_Menu("MainMenu");

    private final String fileName;

    ConfigType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
