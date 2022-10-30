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
    private final Uconomy main;
    private File file = null;
    private FileConfiguration messagesFile = null;
    public MessageManager(Uconomy instance) {
        this.main = instance;
    }

    public void createMessagesYml() {
        if (this.file == null) {
            this.file = new File(main.getDataFolder(), "messages.yml");
        }
        if (!file.exists()) {
            main.saveResource("messages.yml", false);
        }
        this.messagesFile = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return messagesFile;
    }

    public void save() {
        if (this.file == null || this.messagesFile == null) return;
        try {
            this.messagesFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        if (this.file == null) {
            this.file = new File(main.getDataFolder(), "messages.yml");
        }
        this.messagesFile = YamlConfiguration.loadConfiguration(file);
    }

    public void sendTranslatedMsgs(Player player, List<String> msgList) {
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

    public void sendTranslatedMsgs(Player player, List<String> msgList, String msg, String transMsg) {
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

    public void sendTranslatedMsgs(Player player, List<String> msgList, String msg, String transMsg, String msg2, String transMsg2) {
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

    public void sendTranslatedMsgs(Player player, List<String> msgList, String msg, String transMsg, String msg2, String transMsg2, String msg3, String transMsg3) {
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
