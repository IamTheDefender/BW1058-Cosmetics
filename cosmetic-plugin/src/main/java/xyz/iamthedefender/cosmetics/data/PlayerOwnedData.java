package xyz.iamthedefender.cosmetics.data;

import lombok.Getter;
import lombok.Setter;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.*;
import xyz.iamthedefender.cosmetics.api.database.IDatabase;
import xyz.iamthedefender.cosmetics.util.StartupUtils;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Getter
public class PlayerOwnedData{
    private final UUID uuid;
    @Setter
    private int bedDestroy, deathCry, finalKillEffect, glyph, islandTopper, killMessage, projectileTrail, shopkeeperSkin, spray, victoryDance, woodSkin;

    public PlayerOwnedData (UUID uuid) {
        this.uuid = uuid;
        load();
    }

    public void load() {
        try {
            Connection connection = Cosmetics.getInstance().getRemoteDatabase().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM player_owned_data WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                bedDestroy = result.getInt("bed_destroy");
                deathCry = result.getInt("death_cry");
                finalKillEffect = result.getInt("final_kill_effect");
                glyph = result.getInt("glyph");
                islandTopper = result.getInt("island_topper");
                killMessage = result.getInt("kill_message");
                projectileTrail = result.getInt("projectile_trail");
                shopkeeperSkin = result.getInt("shopkeeper_skin");
                spray = result.getInt("spray");
                victoryDance = result.getInt("victory_dance");
                woodSkin = result.getInt("wood_skin");
                statement.close();
                connection.close();
            }else{
                demo();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void demo(){
        String sql = "INSERT INTO player_owned_data (uuid, bed_destroy, death_cry, final_kill_effect, glyph, island_topper, kill_message, projectile_trail, shopkeeper_skin, spray, victory_dance, wood_skin) " +
        "VALUES (?, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);";

        try{
            Connection connection = Cosmetics.getInstance().getRemoteDatabase().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            statement.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            Connection connection = Cosmetics.getInstance().getRemoteDatabase().getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE player_owned_data SET bed_destroy = ?, death_cry = ?, final_kill_effect = ?, glyph = ?, island_topper = ?, kill_message = ?, projectile_trail = ?, shopkeeper_skin = ?, spray = ?, victory_dance = ?, wood_skin = ? WHERE uuid = ?;");
            statement.setInt(1, bedDestroy);
            statement.setInt(2, deathCry);
            statement.setInt(3, finalKillEffect);
            statement.setInt(4, glyph);
            statement.setInt(5, islandTopper);
            statement.setInt(6, killMessage);
            statement.setInt(7, projectileTrail);
            statement.setInt(8, shopkeeperSkin);
            statement.setInt(9, spray);
            statement.setInt(10, victoryDance);
            statement.setInt(11, woodSkin);
            statement.setString(12, uuid.toString());
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void updateOwned(){
        setBedDestroy(0);
        setDeathCry(0);
        setFinalKillEffect(0);
        setGlyph(0);
        setIslandTopper(0);
        setKillMessage(0);
        setProjectileTrail(0);
        setShopkeeperSkin(0);
        setSpray(0);
        setVictoryDance(0);
        setWoodSkin(0);
        for (BedDestroy destroy : StartupUtils.bedDestroyList) {
            if (Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.BedBreakEffects.getPermissionFormat() + "." + destroy.getIdentifier())
            || Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.BedBreakEffects.getPermissionFormat() + ".*")) {
                this.setBedDestroy(this.getBedDestroy() + 1);
            }
        }
        for (DeathCry deathCr : StartupUtils.deathCryList) {
            if (Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.DeathCries.getPermissionFormat() + "." + deathCr.getIdentifier())
                    || Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.DeathCries.getPermissionFormat() + ".*") ){
                this.setDeathCry(this.getDeathCry() + 1);
            }
        }
        for (FinalKillEffect killEffect : StartupUtils.finalKillList) {
            if (Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.FinalKillEffects.getPermissionFormat() + "." + killEffect.getIdentifier())
                    || Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.FinalKillEffects.getPermissionFormat() + ".*") ){
                this.setFinalKillEffect(this.getFinalKillEffect() + 1);
            }
        }
        for (Glyph glyph : StartupUtils.glyphsList) {
            if (Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.Glyphs.getPermissionFormat() + "." + glyph.getIdentifier())
                    || Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.Glyphs.getPermissionFormat() + ".*") ){
                this.setGlyph(this.getGlyph() + 1);
            }
        }
        for (IslandTopper topper : StartupUtils.islandTopperList) {
            if (Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.IslandTopper.getPermissionFormat() + "." + topper.getIdentifier())
                    || Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.IslandTopper.getPermissionFormat() + ".*")) {
                this.setIslandTopper(this.getIslandTopper() + 1);
            }
        }
        for (KillMessage message : StartupUtils.killMessageList) {
            if (Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.KillMessage.getPermissionFormat() + "." + message.getIdentifier())
                    || Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.KillMessage.getPermissionFormat() + ".*")) {
                this.setKillMessage(this.getKillMessage() + 1);
            }
        }


        for (ProjectileTrail trail : StartupUtils.projectileTrailList) {
            if (Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.ProjectileTrails.getPermissionFormat() + "." + trail.getIdentifier())
                    || Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.ProjectileTrails.getPermissionFormat() + ".*")) {
                this.setProjectileTrail(this.getProjectileTrail() + 1);
            }
        }

        for (ShopKeeperSkin shopKeeperSkin : StartupUtils.shopKeeperSkinList) {
            if (Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.ShopKeeperSkin.getPermissionFormat() + "." + shopKeeperSkin.getIdentifier())
                    || Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.BedBreakEffects.getPermissionFormat() + ".*") ){
                this.setShopkeeperSkin(this.getShopkeeperSkin() + 1);
            }
        }

        for (Spray spray1 : StartupUtils.sprayList) {
            if (Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.Sprays.getPermissionFormat() + "." + spray1.getIdentifier())
            || Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.Sprays.getPermissionFormat() + ".*")){
                this.setSpray(this.getSpray() + 1);
            }
        }

        for (VictoryDance dance : StartupUtils.victoryDancesList) {
            if (Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.VictoryDances.getPermissionFormat() + "." + dance.getIdentifier())
                    || Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.VictoryDances.getPermissionFormat() + ".*")) {
                this.setVictoryDance(this.getVictoryDance() + 1);
            }
        }

        for (WoodSkin skin : StartupUtils.woodSkinsList) {
            if (Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.WoodSkins.getPermissionFormat() + "." + skin.getIdentifier())
                    || Bukkit.getPlayer(uuid).hasPermission(CosmeticsType.WoodSkins.getPermissionFormat() + ".*")) {
                this.setWoodSkin(this.getWoodSkin() + 1);
            }
        }
    }
}
