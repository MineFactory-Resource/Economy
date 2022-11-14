package net.teamuni.economy.config;

import me.clip.placeholderapi.PlaceholderAPI;
import net.teamuni.economy.Uconomy;
import net.teamuni.economy.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void sendTranslatedMessage(Player player, List<String> msgList) {
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

    public void sendTranslatedMessage(Player player, List<String> msgList, String msg, String transMsg) {
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

    public void sendTranslatedMessage(Player player, List<String> msgList, String msg, String transMsg, String msg2, String transMsg2) {
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

    public void sendTranslatedMessage(Player player, List<String> msgList, String msg1, String transMsg1, String msg2, String transMsg2, String msg3, String transMsg3) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            List<String> translatedMsgList = PlaceholderAPI.setPlaceholders(player, msgList);
            for (String msg : translatedMsgList) {
                String customMsg = msg
                        .replace(msg1, transMsg1)
                        .replace(msg2, transMsg2)
                        .replace(msg3, transMsg3);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', customMsg));
            }
        } else {
            for (String msg : msgList) {
                String customMsg = msg
                        .replace(msg1, transMsg1)
                        .replace(msg2, transMsg2)
                        .replace(msg3, transMsg3);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', customMsg));
            }
        }
    }

    public void sendTranslatedMessage(Player player, List<String> msgList, String msg1, String transMsg1
            , String msg2, String transMsg2, String msg3, String transMsg3, String msg4, String transMsg4) {

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            List<String> translatedMsgList = PlaceholderAPI.setPlaceholders(player, msgList);
            for (String msg : translatedMsgList) {
                String customMsg = msg
                        .replace(msg1, transMsg1)
                        .replace(msg2, transMsg2)
                        .replace(msg3, transMsg3)
                        .replace(msg4, transMsg4);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', customMsg));
            }
        } else {
            for (String msg : msgList) {
                String customMsg = msg
                        .replace(msg1, transMsg1)
                        .replace(msg2, transMsg2)
                        .replace(msg3, transMsg3)
                        .replace(msg4, transMsg4);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', customMsg));
            }
        }
    }

    public void sendPlayerMoneyInfo(Player player) {
        PlayerData data = main.getPlayerDataManager().getCache(player.getUniqueId());
        List<String> msgList = getMessages().get("check_my_money");
        DecimalFormat df = new DecimalFormat("###,###");

        for (Map.Entry<String, Long> entry : data.getMoneyMap().entrySet()) {
            String placeholder = "%player_money_" + entry.getKey() + "%";
            msgList.replaceAll(s -> s.contains(placeholder) ? s.replace(placeholder, df.format(entry.getValue())) : s);
        }

        sendTranslatedMessage(player, msgList
                , "%name_of_player%", player.getName());
    }

    public void sendPlayerMoneyInfo(Player messageReceiver, OfflinePlayer player) {
        PlayerData data = main.getPlayerDataManager().getCache(player.getUniqueId());
        List<String> msgList = getMessages().get("check_the_other_player_money");
        DecimalFormat df = new DecimalFormat("###,###");

        for (Map.Entry<String, Long> entry : data.getMoneyMap().entrySet()) {
            String placeholder = "%player_money_" + entry.getKey() + "%";
            msgList.replaceAll(s -> s.contains(placeholder) ? s.replace(placeholder, df.format(entry.getValue())) : s);
        }

        sendTranslatedMessage(messageReceiver, msgList
                , "%name_of_player%", player.getName());
    }

    public Map<String, List<String>> getMessages() {
        Map<String, List<String>> messageListMap = new HashMap<>();
        ConfigurationSection section = main.getMessageManager().get().getConfigurationSection("Messages");
        if (section == null) return null;
        for (String key : section.getKeys(false)) {
            List<String> messages = section.getStringList(key);
            messageListMap.put(key, messages);
        }
        return messageListMap;
    }
}
