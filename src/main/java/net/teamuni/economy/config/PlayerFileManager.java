package net.teamuni.economy.config;

import net.teamuni.economy.Uconomy;
import net.teamuni.economy.data.PlayerFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerFileManager {

    private final Uconomy main;
    private File dataFolder = null;
    private final Map<UUID, PlayerFile> playerFileMap = new HashMap<>();
    public PlayerFileManager(Uconomy instance) {
        this.main = instance;
        createDataFolder();
    }

    public void createDataFolder() {
        this.dataFolder = new File(main.getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
    }

    public void createPlayerFile(UUID uuid) {
        File file = new File(dataFolder.getPath(), uuid.toString() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        for (String money : main.getConfig().getStringList("EconomyID")) {
            fileConfiguration.set(money, 0);
        }
        this.playerFileMap.put(uuid, new PlayerFile(file, fileConfiguration));
    }

    public void save(UUID uuid) {
        File file = this.playerFileMap.get(uuid).file();
        FileConfiguration fileConfiguration = this.playerFileMap.get(uuid).fileConfiguration();
        if (file == null | fileConfiguration == null | main.isMySQLUse()) return;
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(UUID uuid) {
        if (main.isMySQLUse() | this.playerFileMap.containsKey(uuid)) return;
        File file = new File(dataFolder.getPath(), uuid.toString() + ".yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        this.playerFileMap.put(uuid, new PlayerFile(file, fileConfiguration));
    }

    public void remove(UUID uuid) {
        this.playerFileMap.remove(uuid);
    }

    public FileConfiguration get(UUID uuid) {
        return this.playerFileMap.get(uuid).fileConfiguration();
    }

    public boolean isExist(UUID uuid) {
        File file = new File(dataFolder.getPath(), uuid.toString() + ".yml");
        return file.exists();
    }
}
