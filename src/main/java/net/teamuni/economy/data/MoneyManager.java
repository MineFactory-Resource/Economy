package net.teamuni.economy.data;

import net.teamuni.economy.Uconomy;
import org.bukkit.OfflinePlayer;

import java.text.DecimalFormat;
import java.util.UUID;

public class MoneyManager {
    private final Uconomy main;

    public MoneyManager(Uconomy instance) {
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

    public long getBalance(OfflinePlayer player, String economyID) {
        UUID playerUUID = player.getUniqueId();
        return main.getPlayerDataManager().getCache(playerUUID).getMoneyMap().get(economyID);
    }

    public boolean has(OfflinePlayer player, String economyID, long amount) {
        return getBalance(player, economyID) >= amount;
    }

    public void withdraw(OfflinePlayer player, String economyID, long amount) {
        long withdrewMoney = getBalance(player, economyID) - amount;

        if (withdrewMoney < 0) {
            withdrewMoney = 0;
        }

        main.getPlayerDataManager().getCache(player.getUniqueId()).set(economyID, withdrewMoney);
    }

    public void deposit(OfflinePlayer player, String economyID, long amount) {
        long depositedMoney = getBalance(player, economyID) + amount;
        main.getPlayerDataManager().getCache(player.getUniqueId()).set(economyID, depositedMoney);
    }

    public void set(OfflinePlayer player, String economyID, long amount) {
        main.getPlayerDataManager().getCache(player.getUniqueId()).set(economyID, amount);
    }
}
