package net.teamuni.economy.vault;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.teamuni.economy.data.MoneyManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

@Getter
public class EconomyManager implements Economy {

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    @Override
    public boolean hasBankSupport() {
        File file = new File(Bukkit.getPluginManager().getPlugin("Uconomy").getDataFolder(), "moneydata.yml");
        return file.exists();
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Deprecated
    public boolean hasAccount(String playerName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return MoneyManager.get().getConfigurationSection("player").isSet(player.getUniqueId().toString());
    }

    @Deprecated
    public boolean hasAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return MoneyManager.get().getConfigurationSection("player").isSet(player.getUniqueId().toString());
    }

    @Deprecated
    public double getBalance(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        assert player != null;
        String playerUuid = player.getUniqueId().toString();
        return MoneyManager.get().getLong("player." + playerUuid);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        String playerUuid = player.getUniqueId().toString();
        return MoneyManager.get().getLong("player." + playerUuid);
    }

    @Deprecated
    public double getBalance(String playerName, String world) {
        Player player = Bukkit.getPlayer(playerName);
        assert player != null;
        String playerUuid = player.getUniqueId().toString();
        return MoneyManager.get().getLong("player." + playerUuid);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        String playerUuid = player.getUniqueId().toString();
        return MoneyManager.get().getLong("player." + playerUuid);
    }

    @Deprecated
    public boolean has(String playerName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return false;
    }

    @Deprecated
    public boolean has(String playerName, String worldName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return false;
    }

    @Deprecated
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return null;
    }

    @Deprecated
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return null;
    }

    @Deprecated
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return null;
    }

    @Deprecated
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return null;
    }

    @Deprecated
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Deprecated
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Deprecated
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Deprecated
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return false;
    }

    @Deprecated
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }
}
