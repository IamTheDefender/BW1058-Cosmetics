package xyz.iamthedefender.cosmetics.data.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.database.DatabaseType;
import xyz.iamthedefender.cosmetics.api.database.IDatabase;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLite implements IDatabase {

    private final Cosmetics plugin;
    @Getter
    public HikariDataSource dataSource;
    public SQLite(Cosmetics plugin){
        this.plugin = plugin;
        connect();
        createTable();
    }


    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.SQLITE;
    }

    public void connect(){
        boolean needConnecting = dataSource == null;
        if (!needConnecting){
            try {
                dataSource.getConnection().createStatement();
            } catch (SQLException e) {
                needConnecting = true;
            }
        }

        if (needConnecting){
            String jdbcUrl = "jdbc:sqlite:" + plugin.getHandler().getAddonPath() + "/cosmeticsData.db";
            try{
                Class.forName("org.sqlite.JDBC");
            }catch (ClassNotFoundException e) {
               Bukkit.getLogger().severe("SQLite class not found! SQLite is not supported!");
            }
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername("");
            config.setPassword("");
            config.setConnectionTestQuery("SELECT 1");
            config.setConnectionTimeout(Integer.MAX_VALUE);
            config.setMaximumPoolSize(100);
            config.setPoolName("COSMETICS-SQLITE");
            dataSource = new HikariDataSource(config);
            try {
                dataSource.getConnection();
            } catch (SQLException e) {
                Bukkit.getLogger().severe("There was an error getting the connection for database! error: " + e.getMessage());
            }
        }
    }

    public void createTable(){
        if (dataSource != null){
            try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS cosmetics_player_data (" +
                        "uuid TEXT PRIMARY KEY," +
                        "bed_destroy TEXT," +
                        "wood_skin TEXT," +
                        "victory_dance TEXT," +
                        "shopkeeper_skin TEXT," +
                        "glyph TEXT," +
                        "spray TEXT," +
                        "projectile_trail TEXT," +
                        "kill_message TEXT," +
                        "final_kill_effect TEXT," +
                        "island_topper TEXT," +
                        "death_cry TEXT" +
                        ")");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_owned_data (" +
                        "uuid TEXT PRIMARY KEY," +
                        "bed_destroy INT," +
                        "death_cry INT," +
                        "final_kill_effect INT," +
                        "glyph INT," +
                        "island_topper INT," +
                        "kill_message INT," +
                        "projectile_trail INT," +
                        "shopkeeper_skin INT," +
                        "spray INT," +
                        "victory_dance INT," +
                        "wood_skin INT" +
                        ")");
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Failed to create player-data table: " + e.getMessage());
            }
        }
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
