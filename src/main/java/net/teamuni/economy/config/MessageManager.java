package net.teamuni.economy.config;

import me.clip.placeholderapi.PlaceholderAPI;
import net.teamuni.economy.Uconomy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    public static void sendTranslatedMsgs(Player player, List<String> msgList) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            List<String> translatedMsgs = PlaceholderAPI.setPlaceholders(player, msgList);
            for (String msgs : translatedMsgs) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msgs));
            }
        } else {
            for (String msgs : msgList) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msgs));
            }
        }
    }

    public static void sendTranslatedMsgs(Player player, List<String> msgList, String msg, String transMsg) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            List<String> translatedMsgs = PlaceholderAPI.setPlaceholders(player, msgList);
            for (String msgs : translatedMsgs) {
                String customMsgs = msgs
                        .replace(msg, transMsg);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', customMsgs));
            }
        } else {
            for (String msgs : msgList) {
                String customMsgs = msgs
                        .replace(msg, transMsg);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', customMsgs));
            }
        }
    }

    public static void sendTranslatedMsgs(Player player, List<String> msgList, String msg, String transMsg, String msg2, String transMsg2) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            List<String> translatedMsgs = PlaceholderAPI.setPlaceholders(player, msgList);
            for (String msgs : translatedMsgs) {
                String customMsgs = msgs
                        .replace(msg, transMsg)
                        .replace(msg2, transMsg2);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', customMsgs));
            }
        } else {
            for (String msgs : msgList) {
                String customMsgs = msgs
                        .replace(msg, transMsg)
                        .replace(msg2, transMsg2);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', customMsgs));
            }
        }
    }

    public static void sendTranslatedMsgs(Player player, List<String> msgList, String msg, String transMsg, String msg2, String transMsg2, String msg3, String transMsg3) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            List<String> translatedMsgs = PlaceholderAPI.setPlaceholders(player, msgList);
            for (String msgs : translatedMsgs) {
                String customMsgs = msgs
                        .replace(msg, transMsg)
                        .replace(msg2, transMsg2)
                        .replace(msg3, transMsg3);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', customMsgs));
            }
        } else {
            for (String msgs : msgList) {
                String customMsgs = msgs
                        .replace(msg, transMsg)
                        .replace(msg2, transMsg2)
                        .replace(msg3, transMsg3);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', customMsgs));
            }
        }
    }
}
