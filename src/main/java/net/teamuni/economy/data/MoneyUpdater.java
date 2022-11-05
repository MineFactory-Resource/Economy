package net.teamuni.economy.data;

import java.util.UUID;

public interface MoneyUpdater {
    void updatePlayerStats(PlayerData playerData);
    PlayerData loadPlayerStats(UUID uuid);
    boolean hasAccount(UUID uuid);
}
