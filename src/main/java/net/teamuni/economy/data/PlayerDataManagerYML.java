package net.teamuni.economy.data;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import net.teamuni.economy.Uconomy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.Executors;

public class PlayerDataManagerYML implements Listener, MoneyUpdater {
    private final LoadingCache<UUID, PlayerData> cache;
    private final ConfigurationSection section;

    public PlayerDataManagerYML(Uconomy instance) {
        this.section = instance.getMoneyManager().get().getConfigurationSection("Player");
        this.cache = CacheBuilder.newBuilder().removalListener(
                        RemovalListeners.asynchronous((RemovalListener<UUID, PlayerData>) notify -> {
                            PlayerData data = notify.getValue();
                            if (data == null) return;
                            if (section == null) return;
                            updatePlayerStats(data);
                        }, Executors.newFixedThreadPool(5)))
                .build(new CacheLoader<>() {

                    @Override
                    public @NotNull PlayerData load(@NotNull UUID uuid) {
                        if (section == null) {
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
        section.set(stats.getUuid(), stats.getMoney());
    }

    @Override
    public PlayerData loadPlayerStats(UUID uuid) {
        String playerUUID = uuid.toString();
        if (section.isSet(uuid.toString())) {
            long money = section.getLong(playerUUID);
            return new PlayerData(playerUUID, money);
        }
        return new PlayerData(playerUUID, 0);
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        Uconomy main = Uconomy.getPlugin(Uconomy.class);
        ConfigurationSection section = main.getMoneyManager().get().getConfigurationSection("player");
        if (section == null) return false;
        return section.isSet(uuid.toString());
    }
}
