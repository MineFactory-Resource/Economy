package net.teamuni.economy.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.teamuni.economy.data.PlayerData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

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

    public void updatePlayerStats(PlayerData stats) {
        try {
            Connection connection = this.sql.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE uc_stats SET money = ? WHERE uuid = ?");
            statement.setLong(1, stats.getMoney());
            statement.setString(2, stats.getUuid());

            try (connection; statement) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerData loadPlayerStats(UUID uuid) {
        try {
            Connection connection = this.sql.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT money FROM uc_stats WHERE uuid = ?");
            statement.setString(1, uuid.toString());

            try (connection; statement) {
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    return new PlayerData(uuid.toString(), result.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PlayerData(uuid.toString(), 0);
    }
}
