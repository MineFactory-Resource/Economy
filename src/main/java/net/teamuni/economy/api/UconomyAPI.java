package net.teamuni.economy.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.teamuni.economy.Uconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UconomyAPI {
    private static Uconomy getInstance() {
        return Uconomy.getPlugin(Uconomy.class);
    }

    public static long getBalance(UUID uuid, String economyID) {
        return getInstance().getPlayerDataManager().getCache(uuid).getMoneyMap().get(economyID);
    }

    public static long getBalance(@NotNull OfflinePlayer player, String economyID) {
        return getInstance().getPlayerDataManager().getCache(player.getUniqueId()).getMoneyMap().get(economyID);
    }

    public static void withdraw(UUID uuid, String economyID, long amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        getInstance().getMoneyManager().withdrawPlayer(player, economyID, amount);
    }

    public static void withdraw(@NotNull OfflinePlayer player, String economyID, long amount) {
        getInstance().getMoneyManager().withdrawPlayer(player, economyID, amount);
    }

    public static void deposit(UUID uuid, String economyID, long amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        getInstance().getMoneyManager().depositPlayer(player, economyID, amount);
    }

    public static void deposit(@NotNull OfflinePlayer player, String economyID, long amount) {
        getInstance().getMoneyManager().depositPlayer(player, economyID, amount);
    }

    public static void setBalance(UUID uuid, String economyID, long amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        long money = getInstance().getPlayerDataManager().getCache(uuid).getMoneyMap().get(economyID);
        getInstance().getMoneyManager().withdrawPlayer(player, economyID, money);
        getInstance().getMoneyManager().depositPlayer(player, economyID, amount);
    }

    public static void setBalance(@NotNull OfflinePlayer player, String economyID, long amount) {
        long money = getInstance().getPlayerDataManager().getCache(player.getUniqueId()).getMoneyMap().get(economyID);
        getInstance().getMoneyManager().withdrawPlayer(player, economyID, money);
        getInstance().getMoneyManager().depositPlayer(player, economyID, amount);
    }

    public static boolean has(UUID uuid, String economyID, long amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        return getInstance().getMoneyManager().has(player, economyID, amount);
    }

    public static boolean has(@NotNull OfflinePlayer player, String economyID, long amount) {
        return getInstance().getMoneyManager().has(player, economyID, amount);
    }

    public static boolean hasAccount(UUID uuid) {
        return getInstance().getMoneyUpdater().hasAccount(uuid);
    }

    public static boolean hasAccount(@NotNull OfflinePlayer player) {
        return getInstance().getMoneyUpdater().hasAccount(player.getUniqueId());
    }
}
