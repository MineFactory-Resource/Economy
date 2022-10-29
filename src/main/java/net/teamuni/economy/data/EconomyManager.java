package net.teamuni.economy.data;

import net.teamuni.economy.Uconomy;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.text.DecimalFormat;
import java.util.*;

public class EconomyManager {
    private final Uconomy main;
    public EconomyManager(Uconomy instance) {
        this.main = instance;
    }

    public String format(long amount) {
        DecimalFormat df = new DecimalFormat("#,####");
        StringBuilder result = new StringBuilder();

        String[] unit = {"", "만", "억", "조", "경"};
        int count = 0;
        String[] numberForm = df.format(amount).split(",");

        for (int i = numberForm.length; i > 0; i--) {
            if (Long.parseLong(numberForm[i - 1]) != 0) {
                result.insert(0, Long.parseLong(numberForm[i - 1]) + unit[count]);
            }
            count++;
        }
        return result.toString();
    }

    public boolean hasAccount(OfflinePlayer player) {
        if (!main.isMySQLUse()) {
            ConfigurationSection section = main.getMoneyManager().get().getConfigurationSection("player");
            if (section == null) return false;
            return section.isSet(player.getUniqueId().toString());
        }
        return true;
    }

    public boolean hasAccount(OfflinePlayer player, String worldName) {
        if (!main.isMySQLUse()) {
            ConfigurationSection section = main.getMoneyManager().get().getConfigurationSection("player");
            if (section == null) return false;
            return section.isSet(player.getUniqueId().toString());
        }
        return true;
    }

    public long getBalance(OfflinePlayer player) {
        String playerUuid = player.getUniqueId().toString();
        if (main.isMySQLUse()) {
            return main.getPlayerDataManager().getCache(UUID.fromString(playerUuid)).getMoney();
        } else {
            return main.getMoneyManager().get().getLong("player." + playerUuid);
        }
    }

    public long getBalance(OfflinePlayer player, String world) {
        String playerUuid = player.getUniqueId().toString();
        if (main.isMySQLUse()) {
            return main.getPlayerDataManager().getCache(UUID.fromString(playerUuid)).getMoney();
        } else {
            return main.getMoneyManager().get().getLong("player." + playerUuid);
        }
    }

    public boolean has(OfflinePlayer player, long amount) {
        return getBalance(player) >= amount;
    }


    public boolean has(OfflinePlayer player, String worldName, long amount) {
        return getBalance(player) >= amount;
    }

    public void withdrawPlayer(OfflinePlayer player, long amount) {
        long withdrewMoney = getBalance(player) - amount;
        if (main.isMySQLUse()) {
            main.getPlayerDataManager().getCache(player.getUniqueId()).afterWithdraw(withdrewMoney);
        } else {
            main.getMoneyManager().get().set("player." + player.getUniqueId(), withdrewMoney);
        }
    }

    public void withdrawPlayer(OfflinePlayer player, String worldName, long amount) {
        long withdrewMoney = getBalance(player) - amount;
        if (main.isMySQLUse()) {
            main.getPlayerDataManager().getCache(player.getUniqueId()).afterWithdraw(withdrewMoney);
        } else {
            main.getMoneyManager().get().set("player." + player.getUniqueId(), withdrewMoney);
        }
    }

    public void depositPlayer(OfflinePlayer player, long amount) {
        long depositedMoney = getBalance(player) + amount;
        if (main.isMySQLUse()) {
            main.getPlayerDataManager().getCache(player.getUniqueId()).afterDeposit(depositedMoney);
        } else {
            main.getMoneyManager().get().set("player." + player.getUniqueId(), depositedMoney);
        }
    }

    public void depositPlayer(OfflinePlayer player, String worldName, long amount) {
        long depositedMoney = getBalance(player) + amount;
        if (main.isMySQLUse()) {
            main.getPlayerDataManager().getCache(player.getUniqueId()).afterDeposit(depositedMoney);
        } else {
            main.getMoneyManager().get().set("player." + player.getUniqueId(), depositedMoney);
        }
    }

    public void createPlayerAccount(OfflinePlayer player) {
        if (!main.isMySQLUse()) {
            main.getMoneyManager().get().set("player." + player.getUniqueId(), 0);
        }
    }

    public void createPlayerAccount(OfflinePlayer player, String worldName) {
        if (!main.isMySQLUse()) {
            main.getMoneyManager().get().set("player." + player.getUniqueId(), 0);
        }
    }
}
