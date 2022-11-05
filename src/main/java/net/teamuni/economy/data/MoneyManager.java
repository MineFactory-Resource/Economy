package net.teamuni.economy.data;

import net.teamuni.economy.Uconomy;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.UUID;

public class MoneyManager {
    private final Uconomy main;
    private File file = null;
    private FileConfiguration moneyDataFile = null;
    public MoneyManager(Uconomy instance) {
        this.main = instance;
    }
    public void createMoneyDataYml() {
        file = new File(main.getDataFolder(), "moneydata.yml");

        if (!file.exists()) {
            main.saveResource("moneydata.yml", false);
        }
        moneyDataFile = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return moneyDataFile;
    }

    public void save() {
        if (this.file == null || this.moneyDataFile == null) return;
        try {
            moneyDataFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        if (this.file == null) {
            this.file = new File(main.getDataFolder(), "moneydata.yml");
        }
        moneyDataFile = YamlConfiguration.loadConfiguration(file);
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

    public long getBalance(OfflinePlayer player) {
        UUID playerUUID = player.getUniqueId();
        if (main.isMySQLUse()) {
            return main.getPlayerDataManagerMySQL().getCache(playerUUID).getMoney();
        } else {
            return main.getPlayerDataManagerYML().getCache(playerUUID).getMoney();
        }
    }

    public boolean has(OfflinePlayer player, long amount) {
        return getBalance(player) >= amount;
    }

    public void withdrawPlayer(OfflinePlayer player, long amount) {
        long withdrewMoney = getBalance(player) - amount;
        if (main.isMySQLUse()) {
            main.getPlayerDataManagerMySQL().getCache(player.getUniqueId()).afterWithdraw(withdrewMoney);
        } else {
            main.getPlayerDataManagerYML().getCache(player.getUniqueId()).afterWithdraw(withdrewMoney);
        }
    }

    public void depositPlayer(OfflinePlayer player, long amount) {
        long depositedMoney = getBalance(player) + amount;
        if (main.isMySQLUse()) {
            main.getPlayerDataManagerMySQL().getCache(player.getUniqueId()).afterDeposit(depositedMoney);
        } else {
            main.getPlayerDataManagerYML().getCache(player.getUniqueId()).afterDeposit(depositedMoney);
        }
    }
}
