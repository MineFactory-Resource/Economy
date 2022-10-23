package net.teamuni.economy.data;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.teamuni.economy.Uconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;

public class EconomyManager implements Economy {
    private final Uconomy main;
    public EconomyManager(Uconomy instance) {
        this.main = instance;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        DecimalFormat df = new DecimalFormat("#,####");
        StringBuilder result = new StringBuilder();

        String[] unit = {"", "만", "억", "조", "경"};
        long number = (long) amount;
        int count = 0;
        String[] numberForm = df.format(number).split(",");

        for (int i = numberForm.length; i > 0; i--) {
            if (Long.parseLong(numberForm[i - 1]) != 0) {
                result.insert(0, Long.parseLong(numberForm[i - 1]) + unit[count]);
            }
            count++;
        }
        return result.toString();
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
        return main.getMoneyManager().get().getConfigurationSection("player").isSet(player.getUniqueId().toString());
    }

    @Deprecated
    public boolean hasAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return main.getMoneyManager().get().getConfigurationSection("player").isSet(player.getUniqueId().toString());
    }

    @Deprecated
    public double getBalance(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        assert player != null;
        String playerUuid = player.getUniqueId().toString();
        return main.getMoneyManager().get().getLong("player." + playerUuid);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        String playerUuid = player.getUniqueId().toString();
        return main.getMoneyManager().get().getLong("player." + playerUuid);
    }

    @Deprecated
    public double getBalance(String playerName, String world) {
        Player player = Bukkit.getPlayer(playerName);
        assert player != null;
        String playerUuid = player.getUniqueId().toString();
        return main.getMoneyManager().get().getLong("player." + playerUuid);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        String playerUuid = player.getUniqueId().toString();
        return main.getMoneyManager().get().getLong("player." + playerUuid);
    }

    @Deprecated
    public boolean has(String playerName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return getBalance(player) >= (long) amount;
    }

    @Deprecated
    public boolean has(String playerName, String worldName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return getBalance(player) >= (long) amount;
    }

    @Deprecated
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (!hasAccount(player)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "The player doesn't has an Account!");
        }
        double withdrawedMoney = getBalance(player) - amount;
        main.getMoneyManager().get().set("player." + player.getUniqueId(), (long) withdrawedMoney);

        return new EconomyResponse(amount, withdrawedMoney, EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Deprecated
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        if (!hasAccount(player)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "The player doesn't has an Account!");
        }
        double withdrawedMoney = getBalance(player) - amount;
        main.getMoneyManager().get().set("player." + player.getUniqueId(), (long) withdrawedMoney);

        return new EconomyResponse(amount, withdrawedMoney, EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Deprecated
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if (!hasAccount(player)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "The player doesn't has an Account!");
        }
        double depositedMoney = getBalance(player) + amount;
        main.getMoneyManager().get().set("player." + player.getUniqueId(), (long) depositedMoney);

        return new EconomyResponse(amount, depositedMoney, EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Deprecated
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        if (!hasAccount(player)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "The player doesn't has an Account!");
        }
        double depositedMoney = getBalance(player) + amount;
        main.getMoneyManager().get().set("player." + player.getUniqueId(), (long) depositedMoney);

        return new EconomyResponse(amount, depositedMoney, EconomyResponse.ResponseType.SUCCESS, "");
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
        main.getMoneyManager().get().set("player." + player.getUniqueId(), 0);
        return false;
    }

    @Deprecated
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        main.getMoneyManager().get().set("player." + player.getUniqueId(), 0);
        return false;
    }
}
