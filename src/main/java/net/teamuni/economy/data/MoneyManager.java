package net.teamuni.economy.data;

import net.teamuni.economy.Uconomy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MoneyManager {
    private static final Uconomy main = Uconomy.getPlugin(Uconomy.class);
    private static File file;
    private static FileConfiguration commandsFile;

    public static void createMoneyDataYml() {
        file = new File(main.getDataFolder(), "moneydata.yml");

        if (!file.exists()) {
            main.saveResource("moneydata.yml", false);
        }
        commandsFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return commandsFile;
    }

    public static void save() {
        try {
            commandsFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reload() {
        commandsFile = YamlConfiguration.loadConfiguration(file);
    }
}
