package net.teamuni.economy.config;

import me.clip.placeholderapi.PlaceholderAPI;
import net.teamuni.economy.Uconomy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MessageManager {
    private static final Uconomy main = Uconomy.getPlugin(Uconomy.class);
    private static File file;
    private static FileConfiguration commandsFile;

    public static void createMessagesYml() {
        file = new File(main.getDataFolder(), "messages.yml");

        if (!file.exists()) {
            main.saveResource("messages.yml", false);
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

    public static void sendTranslatedMsgs(Player player, OfflinePlayer recipient, List<String> msgList) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            try {
                List<String> translatedMsgs = PlaceholderAPI.setPlaceholders(recipient, msgList);
                for (String msgs : translatedMsgs) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msgs));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            for (String msgs : msgList) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msgs));
            }
        }
    }
}
