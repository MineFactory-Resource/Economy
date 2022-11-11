package net.teamuni.economy.data;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface MoneyUpdater {
    void updatePlayerStats(PlayerData playerData);
    PlayerData loadPlayerStats(UUID uuid);
    boolean hasAccount(UUID uuid);
    boolean createPlayerAccount(OfflinePlayer player);
}
