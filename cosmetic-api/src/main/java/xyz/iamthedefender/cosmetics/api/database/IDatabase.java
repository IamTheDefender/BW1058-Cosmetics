package xyz.iamthedefender.cosmetics.api.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;

// TODO migrate load and save method to this
public interface IDatabase {

    DatabaseType getDatabaseType();

    void connect();
    void createTable();
    HikariDataSource getDataSource();
    Connection getConnection();
}

