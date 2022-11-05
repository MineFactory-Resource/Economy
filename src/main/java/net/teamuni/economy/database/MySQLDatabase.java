package net.teamuni.economy.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLDatabase {
    private final HikariDataSource sql;

    public MySQLDatabase(String host, int port, String database, String parameters, String user, String password) {
        HikariConfig config = new HikariConfig();
        config.setUsername(user);
        config.setPassword(password);
        StringBuilder sb = new StringBuilder("jdbc:mysql://")
                .append(host).append(":").append(port).append("/").append(database);
        if (!parameters.isEmpty()) {
            sb.append(parameters);
        }
        config.setJdbcUrl(sb.toString());

        this.sql = new HikariDataSource(config);

        try {
            initTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initTable() throws SQLException {
        try (Connection connection = this.sql.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS uc_stats(uuid varchar(36) primary key, money BIGINT)";
                statement.execute(sql);
            }
        }
    }

    public Connection getConnection() throws SQLException {
        return this.sql.getConnection();
    }
}
