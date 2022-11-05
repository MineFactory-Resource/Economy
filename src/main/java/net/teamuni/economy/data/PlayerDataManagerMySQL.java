package net.teamuni.economy.data;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import net.teamuni.economy.Uconomy;
import net.teamuni.economy.database.MySQLDatabase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.Executors;

public class PlayerDataManagerMySQL implements Listener, MoneyUpdater {
    private final Uconomy main;
    private final LoadingCache<UUID, PlayerData> cache;

    public PlayerDataManagerMySQL(Uconomy instance) {
        this.main = instance;
        this.cache = CacheBuilder.newBuilder().removalListener(
                        RemovalListeners.asynchronous((RemovalListener<UUID, PlayerData>) notify -> {
                            PlayerData data = notify.getValue();
                            MySQLDatabase database = instance.getDatabase();
                            if (data == null) return;
                            if (database == null) return;

                            updatePlayerStats(data);
                        }, Executors.newFixedThreadPool(5)))
                .build(new CacheLoader<>() {

                    @Override
                    public @NotNull PlayerData load(@NotNull UUID uuid) {
                        MySQLDatabase database = instance.getDatabase();
                        if (database == null) {
                            return new PlayerData(uuid.toString(), 0);
                        }
                        return loadPlayerStats(uuid);
                    }
                });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        remove(event.getPlayer());
    }

    public void remove(Player player) {
        this.cache.invalidate(player.getUniqueId());
        this.cache.cleanUp();
    }

    public void removeAll() {
        this.cache.invalidateAll();
        this.cache.cleanUp();
    }

    @NotNull
    public PlayerData getCache(@NotNull UUID uuid) {
        return this.cache.getUnchecked(uuid);
    }

    @Nullable
    public PlayerData getCacheIfPresent(@NotNull UUID uuid) {
        return this.cache.getIfPresent(uuid);
    }

    @Override
    public void updatePlayerStats(PlayerData stats) {
        try {
            Connection connection = main.getDatabase().getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO uc_stats (uuid, money) VALUE (?, ?) ON DUPLICATE KEY UPDATE money = ?");
            statement.setString(1, stats.getUuid());
            statement.setLong(2, stats.getMoney());
            statement.setLong(3, stats.getMoney());

            try (connection; statement) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PlayerData loadPlayerStats(UUID uuid) {
        try {
            Connection connection = main.getDatabase().getConnection();
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

    @Override
    public boolean hasAccount(UUID uuid) {
        try {
            Connection connection = main.getDatabase().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM uc_stats WHERE uuid = ?");
            statement.setString(1, uuid.toString());

            try (connection; statement) {
                ResultSet result = statement.executeQuery();
                return result.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        try {
            Connection connection = main.getDatabase().getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO uc_stats (uuid, money) VALUE (?, ?)");
            statement.setString(1, player.getUniqueId().toString());
            statement.setLong(2, 0);

            try (connection; statement) {
                statement.executeUpdate();
                Bukkit.getLogger().info("[Uconomy] " + player.getName() + "님의 돈 정보를 생성하였습니다.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

