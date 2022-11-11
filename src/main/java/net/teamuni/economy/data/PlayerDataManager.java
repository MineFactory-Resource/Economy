package net.teamuni.economy.data;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import net.teamuni.economy.Uconomy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.Executors;

public class PlayerDataManager implements Listener {
    private final LoadingCache<UUID, PlayerData> cache;

    public PlayerDataManager(Uconomy instance) {
        this.cache = CacheBuilder.newBuilder().removalListener(
                        RemovalListeners.asynchronous((RemovalListener<UUID, PlayerData>) notify -> {
                            PlayerData data = notify.getValue();
                            MoneyUpdater updater = instance.getMoneyUpdater();
                            if (data == null | updater == null) return;

                            updater.updatePlayerStats(data);
                        }, Executors.newFixedThreadPool(5)))
                .build(new CacheLoader<>() {

                    @Override
                    public @NotNull PlayerData load(@NotNull UUID uuid) {
                        MoneyUpdater updater = instance.getMoneyUpdater();
                        if (updater == null) {
                            return new PlayerData(uuid.toString(), 0);
                        }
                        return updater.loadPlayerStats(uuid);
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
}