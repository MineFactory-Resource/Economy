package net.teamuni.economy;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.teamuni.economy.config.MessageManager;
import net.teamuni.economy.data.MoneyManager;
import net.teamuni.economy.event.JoinEvent;
import net.teamuni.economy.vault.EconomyManager;
import net.teamuni.economy.vault.HookIntoVault;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

@Getter
public final class Uconomy extends JavaPlugin {

    private static Uconomy instance;
    private static EconomyManager manager;
    private static HookIntoVault hookIntoVault;

    public static Uconomy getInstance() {
        return instance;
    }

    public static EconomyManager getEconomyManager() {
        return manager;
    }

    List<String> reloadMessageList;
    List<String> commandGuideMessageList;
    List<String> opCommandGuideMessageList;
    List<String> notAvailableCommandMessageList;
    List<String> incorrectPlayerNameMessageList;
    List<String> invalidSyntaxMessageList;
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
        instance = this;
        manager = new EconomyManager();
        hookIntoVault = new HookIntoVault();
        hookIntoVault.hook();

        if (!setupEconomy()) {
            getLogger().info("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        MoneyManager.save();
        hookIntoVault.unhook();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        return rsp != null;
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
                                                .replace("%player_money%", df.format(getEconomyManager().getBalance(player)));
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', translatedMessages));
                                    }
                                    break;
                                case 2:
                                    if (!player.hasPermission("ucon.manage")) {
                                        for (String notAvailableCommandMessages : notAvailableCommandMessageList) {
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', notAvailableCommandMessages));
                                        }
                                        return false;
                                    }
                                    OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

                                    if (target == null || !getEconomyManager().hasAccount(target)) {
                                        for (String incorrectPlayerNameMessages : incorrectPlayerNameMessageList) {
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', incorrectPlayerNameMessages));
                                        }
                                        return false;
                                    }

                                    for (String checkTheOtherPlayerMoneyMessages : checkTheOtherPlayerMoneyMessageList) {
                                        String translatedMessages = checkTheOtherPlayerMoneyMessages
                                                .replace("%name_of_player%", target.getName())
                                                .replace("%player_money%", df.format(getEconomyManager().getBalance(target)));
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', translatedMessages));
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
                            if (args.length != 3) {
                                for (String notAvailableCommandMessages : notAvailableCommandMessageList) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', notAvailableCommandMessages));
                                }
                                return false;
                            }
                            OfflinePlayer recipient = Bukkit.getOfflinePlayerIfCached(args[1]);

                            if (recipient == null || !getEconomyManager().hasAccount(recipient)) {
                                for (String incorrectPlayerNameMessages : incorrectPlayerNameMessageList) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', incorrectPlayerNameMessages));
                                }
                                return false;
                            }
                            if (recipient == player) {
                                for (String attemptToDepositToOneselfMessages : attemptToDepositToOneselfMessageList) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', attemptToDepositToOneselfMessages));
                                }
                                return false;
                            }
                            if (!args[2].matches("[0-9]+")) {
                                for (String invalidSyntaxMessages : invalidSyntaxMessageList) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidSyntaxMessages));
                                }
                                return false;
                            }
                            if (!getEconomyManager().has(player, Double.parseDouble(args[2]))) {
                                for (String moneyShortageMessages : moneyShortageMessageList) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', moneyShortageMessages));
                                }
                                return false;
                            }
                            if (Long.parseLong(args[2]) < getConfig().getLong("minimum_amount")) {
                                for (String minimumAmountCautionMessages : minimumAmountCautionMessageList) {
                                    String translatedMessages = minimumAmountCautionMessages
                                            .replace("%value_of_minimum%", df.format(getConfig().getLong("minimum_amount")));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', translatedMessages));
                                }
                                return false;
                            }
                            long updatedPlayerMoney = (long) getEconomyManager().getBalance(player) - Long.parseLong(args[2]);
                            long updatedRecipientMoney = (long) getEconomyManager().getBalance(recipient) + Long.parseLong(args[2]);
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
                                    Player onlineRecipient = recipient.getPlayer();
                                    assert onlineRecipient != null;
                                    onlineRecipient.sendMessage(ChatColor.translateAlternateColorCodes('&', translatedMessages));
                                }
                            }
                            break;
                        case "지급":
                        case "차감":
                        case "설정":
                            if (!player.hasPermission("ucon.manage")) {
                                for (String notAvailableCommandMessages : notAvailableCommandMessageList) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', notAvailableCommandMessages));
                                }
                                return false;
                            }
                            if (args.length != 3) {
                                for (String notAvailableCommandMessages : notAvailableCommandMessageList) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', notAvailableCommandMessages));
                                }
                                return false;
                            }
                            OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

                            if (target == null || !getEconomyManager().hasAccount(target)) {
                                for (String incorrectPlayerNameMessages : incorrectPlayerNameMessageList) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', incorrectPlayerNameMessages));
                                }
                                return false;
                            }
                            if (!args[2].matches("[0-9]+")) {
                                for (String invalidSyntaxMessages : invalidSyntaxMessageList) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidSyntaxMessages));
                                }
                                return false;
                            }
                            if (args[0].equalsIgnoreCase("지급")) {
                                long increasedPlayerMoney = (long) getEconomyManager().getBalance(target) + Long.parseLong(args[2]);
                                MoneyManager.get().set("player." + target.getUniqueId(), increasedPlayerMoney);

                                for (String increasePlayerMoneyMessages : increasePlayerMoneyMessageList) {
                                    String translatedMessages = increasePlayerMoneyMessages
                                            .replace("%name_of_player%", target.getName())
                                            .replace("%increased_money%", df.format(Long.parseLong(args[2])));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', translatedMessages));
                                }
                                return false;
                            }
                            if (args[0].equalsIgnoreCase("차감")) {
                                long decreasedPlayerMoney = (long) getEconomyManager().getBalance(target) - Long.parseLong(args[2]);

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
                                return false;
                            }
                            if (args[0].equalsIgnoreCase("설정")) {
                                MoneyManager.get().set("player." + target.getUniqueId(), Long.parseLong(args[2]));

                                for (String setPlayerMoneyMessages : setPlayerMoneyMessageList) {
                                    String translatedMessages = setPlayerMoneyMessages
                                            .replace("%name_of_player%", target.getName())
                                            .replace("%set_money%", df.format(Long.parseLong(args[2])));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', translatedMessages));
                                }
                                return false;
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
            if (command.getName().equalsIgnoreCase("uconomy") && args[0].equalsIgnoreCase("reload") && player.hasPermission("ucon.reload")) {
                reloadConfig();
                MoneyManager.save();
                MessageManager.reload();
                getMessages();
                for (String reloadMessages : reloadMessageList) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', reloadMessages));
                }
            }
            return false;
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
            invalidSyntaxMessageList = MessageManager.get().getStringList("invalid_syntax");
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
