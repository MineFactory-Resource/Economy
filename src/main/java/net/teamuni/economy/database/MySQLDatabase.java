package net.teamuni.economy.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.teamuni.economy.data.PlayerData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLDatabase {
    private final HikariDataSource sql;

    public MySQLDatabase(String host, int port, String database, String parameters, String user, String password) {
        HikariConfig config = new HikariConfig();
        config.setUsername(user);
        config.setPassword(password);
        var sb = new StringBuilder("jdbc:mysql://")
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

    public PlayerData findPlayerStatsByUUID(String uuid) throws SQLException {
        Connection connection = this.sql.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM uc_stats WHERE uuid = ?");
        statement.setString(1, uuid);
        ResultSet resultSet = statement.executeQuery();

        try (connection; statement; resultSet) {
            if (resultSet.next()) {
                return new PlayerData(uuid, resultSet.getLong(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getPlayerMoney(String uuid) throws SQLException {
        Connection connection = this.sql.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM uc_stats WHERE uuid = ?");
        statement.setString(1, uuid);
        ResultSet resultSet = statement.executeQuery();

        try (connection; statement; resultSet) {
            if (resultSet.next()) {
                return resultSet.getLong(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void createPlayerStats(PlayerData stats) throws SQLException {
        Connection connection = this.sql.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO uc_stats(uuid, money) VALUES (?, ?)");
        statement.setString(1, stats.uuid());
        statement.setLong(2, stats.money());

        try (connection; statement) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerStats(PlayerData stats) throws SQLException {
        Connection connection = this.sql.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE uc_stats SET money = ? WHERE uuid = ?");
        statement.setLong(1, stats.money());
        statement.setString(2, stats.uuid());

        try (connection; statement) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
