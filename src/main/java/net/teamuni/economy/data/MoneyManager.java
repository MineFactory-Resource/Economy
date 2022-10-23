package net.teamuni.economy.data;

import net.teamuni.economy.Uconomy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MoneyManager {
    private final Uconomy main;
    private File file = null;
    private FileConfiguration commandsFile = null;
    public MoneyManager(Uconomy instance) {
        this.main = instance;
    }
    public void createMoneyDataYml() {
        file = new File(main.getDataFolder(), "moneydata.yml");

        if (!file.exists()) {
            main.saveResource("moneydata.yml", false);
        }
        commandsFile = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return commandsFile;
    }

    public void save() {
        try {
            commandsFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        commandsFile = YamlConfiguration.loadConfiguration(file);
    }
}
