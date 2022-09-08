package net.teamuni.economy;

import net.teamuni.economy.config.MessageManager;
import net.teamuni.economy.data.MoneyManager;
import net.teamuni.economy.event.JoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public final class Uconomy extends JavaPlugin {

    List<String> reloadMessageList;
    List<String> commandGuideMessageList;
    List<String> opCommandGuideMessageList;
    List<String> notAvailableCommandMessageList;
    List<String> incorrectPlayerNameMessageList;
    List<String> invaildSyntaxMessageList;
    List<String> moneyShortageMessageList;
    List<String> attemptToDepositToOneselfMessageList;
    List<String> checkMyMoneyMessageList;
    List<String> checkTheOtherPlayerMoneyMessageList;
    List<String> transactionConfirmToSenderMessageList;
    List<String> transactionConfirmToRecipientMessageList;
    List<String> minimumAmountCautionMessageList;
    List<String> increasePlayerMoneyMessageList;
    List<String> decreasePlayerMoneyMessageList;
    List<String> setPlayerMoneyMessageList;

    DecimalFormat df = new DecimalFormat("###,###");

    @Override
    public void onEnable() {
        saveDefaultConfig();
        MoneyManager.createMoneyDataYml();
        MessageManager.createMessagesYml();
        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
        getCommand("돈").setTabCompleter(new CommandTabCompleter());
        getCommand("uconomy").setTabCompleter(new CommandTabCompleter());
        getMessages();
    }

    @Override
    public void onDisable() {
        MoneyManager.save();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("돈")) {
                if (args.length > 0) {
                    switch (args[0]) {
                        case "확인":
                            switch (args.length) {
                                case 1:
                                    for (String checkMyMoneyMessages : checkMyMoneyMessageList) {
                                        String translatedMessages = checkMyMoneyMessages
                                                .replace("%name_of_player%", player.getName())
                                                .replace("%player_money%", df.format(MoneyManager.get().getLong("player." + player.getUniqueId())));
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', translatedMessages));
                                    }
                                    break;
                                case 2:
                                    if (player.hasPermission("ucon.manage")) {
                                        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
                                        if (target != null && MoneyManager.get().getConfigurationSection("player").getKeys(false).contains(target.getUniqueId().toString())) {
                                            for (String checkTheOtherPlayerMoneyMessages : checkTheOtherPlayerMoneyMessageList) {
                                                String translatedMessages = checkTheOtherPlayerMoneyMessages
                                                        .replace("%name_of_player%", target.getName())
                                                        .replace("%player_money%", df.format(MoneyManager.get().getLong("player." + target.getUniqueId())));
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', translatedMessages));
                                            }
                                        } else {
                                            for (String incorrectPlayerNameMessages : incorrectPlayerNameMessageList) {
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', incorrectPlayerNameMessages));
                                            }
                                        }
                                    } else {
                                        for (String notAvailableCommandMessages : notAvailableCommandMessageList) {
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', notAvailableCommandMessages));
                                        }
                                    }
                                    break;
                                default:
                                    for (String notAvailableCommandMessages : notAvailableCommandMessageList) {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', notAvailableCommandMessages));
                                    }
                                    break;
                            }
                            break;
                        case "보내기":
                            if (args.length == 3) {
                                OfflinePlayer recipient = Bukkit.getOfflinePlayerIfCached(args[1]);
                                if (recipient != null && MoneyManager.get().getConfigurationSection("player").getKeys(false).contains(recipient.getUniqueId().toString())) {
                                    if (recipient != player) {
                                        if (args[2].matches("[0-9]+")) {
                                            if (MoneyManager.get().getLong("player." + player.getUniqueId()) >= Long.parseLong(args[2])) {
                                                if (Long.parseLong(args[2]) >= getConfig().getLong("minimum_amount")) {
                                                    long updatedPlayerMoney = MoneyManager.get().getLong("player." + player.getUniqueId()) - Long.parseLong(args[2]);
                                                    long updatedRecipientMoney = MoneyManager.get().getLong("player." + recipient.getUniqueId()) + Long.parseLong(args[2]);
                                                    MoneyManager.get().set("player." + player.getUniqueId(), updatedPlayerMoney);
                                                    MoneyManager.get().set("player." + recipient.getUniqueId(), updatedRecipientMoney);
                                                    for (String transactionConfirmToSenderMessages : transactionConfirmToSenderMessageList) {
                                                        String translatedMessages = transactionConfirmToSenderMessages
                                                                .replace("%name_of_recipient%", recipient.getName())
                                                                .replace("%sent_money%", df.format(Long.parseLong(args[2])))
                                                                .replace("%sender_money_after_transaction%", df.format(updatedPlayerMoney));
                                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', translatedMessages));
                                                    }
                                                    if (recipient.isOnline()) {
                                                        for (String transactionConfirmToRecipientMessages : transactionConfirmToRecipientMessageList) {
                                                            String translatedMessages = transactionConfirmToRecipientMessages
                                                                    .replace("%name_of_sender%", player.getName())
                                                                    .replace("%received_money%", df.format(Long.parseLong(args[2])))
                                                                    .replace("%recipient_money_after_transaction%", df.format(updatedRecipientMoney));
                                                            Player onlineRecipient = Bukkit.getPlayer(args[1]);
                                                            assert onlineRecipient != null;
                                                            onlineRecipient.sendMessage(ChatColor.translateAlternateColorCodes('&', translatedMessages));
                                                        }
                                                    }
                                                } else {
                                                    for (String minimumAmountCautionMessages : minimumAmountCautionMessageList) {
                                                        String translatedMessages = minimumAmountCautionMessages
                                                                .replace("%value_of_minimum%", df.format(getConfig().getLong("minimum_amount")));
                                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', translatedMessages));
                                                    }
                                                }
                                            } else {
                                                for (String moneyShortageMessages : moneyShortageMessageList) {
                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', moneyShortageMessages));
                                                }
                                            }
                                        } else {
                                            for (String invalidSyntaxMessages : invaildSyntaxMessageList) {
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidSyntaxMessages));
                                            }
                                        }
                                    } else {
                                        for (String attemptToDepositToOneselfMessages : attemptToDepositToOneselfMessageList) {
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', attemptToDepositToOneselfMessages));
                                        }
                                    }
                                } else {
                                    for (String incorrectPlayerNameMessages : incorrectPlayerNameMessageList) {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', incorrectPlayerNameMessages));
                                    }
                                }
                            } else {
                                for (String notAvailableCommandMessages : notAvailableCommandMessageList) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', notAvailableCommandMessages));
                                }
                            }
                            break;
                        case "지급":
                            if (player.hasPermission("ucon.manage") && args.length == 3) {
                                OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
                                if (target != null && MoneyManager.get().getConfigurationSection("player").getKeys(false).contains(target.getUniqueId().toString())) {
                                    if (args[2].matches("[0-9]+")) {
                                        long increasedPlayerMoney = MoneyManager.get().getLong("player." + target.getUniqueId()) + Long.parseLong(args[2]);
                                        MoneyManager.get().set("player." + Bukkit.getOfflinePlayer(args[1]).getUniqueId(), increasedPlayerMoney);
                                        for (String increasePlayerMoneyMessages : increasePlayerMoneyMessageList) {
                                            String translatedMessages = increasePlayerMoneyMessages
                                                    .replace("%name_of_player%", target.getName())
                                                    .replace("%increased_money%", df.format(Long.parseLong(args[2])));
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', translatedMessages));
                                        }
                                    } else {
                                        for (String invalidSyntaxMessages : invaildSyntaxMessageList) {
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidSyntaxMessages));
                                        }
                                    }
                                } else {
                                    for (String incorrectPlayerNameMessages : incorrectPlayerNameMessageList) {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', incorrectPlayerNameMessages));
                                    }
                                }
                            } else {
                                for (String notAvailableCommandMessages : notAvailableCommandMessageList) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', notAvailableCommandMessages));
                                }
                            }
                            break;
                        case "차감":
                            if (player.hasPermission("ucon.manage") && args.length == 3) {
                                OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
                                if (target != null && MoneyManager.get().getConfigurationSection("player").getKeys(false).contains(target.getUniqueId().toString())) {
                                    if (args[2].matches("[0-9]+")) {
                                        long decreasedPlayerMoney = MoneyManager.get().getLong("player." + target.getUniqueId()) - Long.parseLong(args[2]);
                                        if (decreasedPlayerMoney < 0) {
                                            MoneyManager.get().set("player." + target.getUniqueId(), 0);
                                        } else {
                                            MoneyManager.get().set("player." + target.getUniqueId(), decreasedPlayerMoney);
                                        }
                                        for (String decreasePlayerMoneyMessages : decreasePlayerMoneyMessageList) {
                                            String translatedMessages = decreasePlayerMoneyMessages
                                                    .replace("%name_of_player%", target.getName())
                                                    .replace("%decreased_money%", df.format(Long.parseLong(args[2])));
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', translatedMessages));
                                        }
                                    } else {
                                        for (String invalidSyntaxMessages : invaildSyntaxMessageList) {
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidSyntaxMessages));
                                        }
                                    }
                                } else {
                                    for (String incorrectPlayerNameMessages : incorrectPlayerNameMessageList) {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', incorrectPlayerNameMessages));
                                    }
                                }
                            } else {
                                for (String notAvailableCommandMessages : notAvailableCommandMessageList) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', notAvailableCommandMessages));
                                }
                            }
                            break;
                        case "설정":
                            if (player.hasPermission("ucon.manage") && args.length == 3) {
                                OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
                                if (target != null && MoneyManager.get().getConfigurationSection("player").getKeys(false).contains(target.getUniqueId().toString())) {
                                    if (args[2].matches("[0-9]+")) {
                                        MoneyManager.get().set("player." + target.getUniqueId(), Long.parseLong(args[2]));
                                        for (String setPlayerMoneyMessages : setPlayerMoneyMessageList) {
                                            String translatedMessages = setPlayerMoneyMessages
                                                    .replace("%name_of_player%", target.getName())
                                                    .replace("%set_money%", df.format(Long.parseLong(args[2])));
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', translatedMessages));
                                        }
                                    } else {
                                        for (String invalidSyntaxMessages : invaildSyntaxMessageList) {
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidSyntaxMessages));
                                        }
                                    }
                                } else {
                                    for (String incorrectPlayerNameMessages : incorrectPlayerNameMessageList) {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', incorrectPlayerNameMessages));
                                    }
                                }
                            } else {
                                for (String notAvailableCommandMessages : notAvailableCommandMessageList) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', notAvailableCommandMessages));
                                }
                            }
                            break;
                        default:
                            for (String notAvailableCommandMessages : notAvailableCommandMessageList) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', notAvailableCommandMessages));
                            }
                            break;
                    }
                } else {
                    if (player.hasPermission("ucon.manage")) {
                        for (String guideMessages : opCommandGuideMessageList) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', guideMessages));
                        }
                    } else {
                        for (String guideMessages : commandGuideMessageList) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', guideMessages));
                        }
                    }
                }
                return false;
            }
            if (command.getName().equalsIgnoreCase("uconomy") && player.hasPermission("ucon.reload")) {
                if (args[0].equalsIgnoreCase("reload")) {
                    reloadConfig();
                    getMessages();
                    MoneyManager.save();
                    MessageManager.reload();
                    for (String reloadMessages : reloadMessageList) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', reloadMessages));
                    }
                }
                return false;
            }
        }
        return false;
    }

    public void getMessages() {
        try {
            reloadMessageList = MessageManager.get().getStringList("reload_message");
            commandGuideMessageList = MessageManager.get().getStringList("money_command_guide");
            opCommandGuideMessageList = MessageManager.get().getStringList("money_command_guide_for_op");
            notAvailableCommandMessageList = MessageManager.get().getStringList("not_available_command");
            incorrectPlayerNameMessageList = MessageManager.get().getStringList("incorrect_player_name");
            invaildSyntaxMessageList = MessageManager.get().getStringList("invaild_syntax");
            moneyShortageMessageList = MessageManager.get().getStringList("money_shortage");
            attemptToDepositToOneselfMessageList = MessageManager.get().getStringList("attempt_to_deposit_to_oneself");
            checkMyMoneyMessageList = MessageManager.get().getStringList("check_my_money");
            checkTheOtherPlayerMoneyMessageList = MessageManager.get().getStringList("check_the_other_player_money");
            transactionConfirmToSenderMessageList = MessageManager.get().getStringList("transaction_confirm_to_sender");
            transactionConfirmToRecipientMessageList = MessageManager.get().getStringList("transaction_confirm_to_recipient");
            minimumAmountCautionMessageList = MessageManager.get().getStringList("minimum_amount_caution");
            increasePlayerMoneyMessageList = MessageManager.get().getStringList("increase_player_money");
            decreasePlayerMoneyMessageList = MessageManager.get().getStringList("decrease_player_money");
            setPlayerMoneyMessageList = MessageManager.get().getStringList("set_player_money");
        } catch (NullPointerException e) {
            e.printStackTrace();
            getLogger().info("messages.yml에서 메시지를 불러오는 도중 문제가 발생했습니다.");
        }
    }
}
