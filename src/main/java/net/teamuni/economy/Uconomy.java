package net.teamuni.economy;

import net.teamuni.economy.config.MessageManager;
import net.teamuni.economy.data.MoneyManager;
import net.teamuni.economy.event.JoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public final class Uconomy extends JavaPlugin {

    List<String> reloadMessageList;
    List<String> commandGuideMessageList;
    List<String> opCommandGuideMessageList;
    List<String> notAvailableCommandMessageList;
    List<String> incorrectPlayerNameMessageList;
    List<String> invaildSyntaxMessageList;
    List<String> moneyShortageMessageList;
    List<String> attemptToDepositToOneselfMessageList;

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
                                    messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "현재 " + ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.WHITE + "님이 보유하고 있는 돈은 " + ChatColor.GOLD +
                                            df.format(MoneyManager.get().getLong("player.money." + player.getUniqueId())) + ChatColor.WHITE + "원입니다.");
                                    break;
                                case 2:
                                    if (player.hasPermission("ucon.manage")) {
                                        Player target = Bukkit.getPlayer(args[1]);
                                        if (target != null && MoneyManager.get().getConfigurationSection("player.money").getKeys(false).contains(target.getUniqueId().toString())) {
                                            messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "현재 " + ChatColor.LIGHT_PURPLE + target.getName() + ChatColor.WHITE + "님이 보유하고 있는 돈은 " + ChatColor.GOLD +
                                                    df.format(MoneyManager.get().getLong("player.money." + target.getUniqueId())) + ChatColor.WHITE + "원입니다.");
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
                                Player recipient = Bukkit.getPlayer(args[1]);
                                if (recipient != null && MoneyManager.get().getConfigurationSection("player.money").getKeys(false).contains(recipient.getUniqueId().toString())) {
                                    if (recipient != player) {
                                        if (args[2].matches("[0-9]+")) {
                                            if (MoneyManager.get().getLong("player.money." + player.getUniqueId()) >= Long.parseLong(args[2])) {
                                                if (Long.parseLong(args[2]) >= getConfig().getLong("minimum_amount")) {
                                                    long updatedPlayerMoney = MoneyManager.get().getLong("player.money." + player.getUniqueId()) - Long.parseLong(args[2]);
                                                    long updatedRecipientMoney = MoneyManager.get().getLong("player.money." + recipient.getUniqueId()) + Long.parseLong(args[2]);
                                                    MoneyManager.get().set("player.money." + player.getUniqueId(), updatedPlayerMoney);
                                                    MoneyManager.get().set("player.money." + recipient.getUniqueId(), updatedRecipientMoney);
                                                    messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.LIGHT_PURPLE + recipient.getName() + ChatColor.WHITE + "님에게 " +
                                                            ChatColor.GOLD + df.format(Long.parseLong(args[2])) + ChatColor.WHITE + "원을 보냈습니다.");
                                                    messageForm(recipient, ChatColor.YELLOW + "[알림] " + ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.WHITE + "님으로부터 " +
                                                            ChatColor.GOLD + df.format(Long.parseLong(args[2])) + ChatColor.WHITE + "원을 받았습니다.");
                                                    player.sendMessage(ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "잔액: " + ChatColor.GOLD + df.format(updatedPlayerMoney) + ChatColor.WHITE + "원");
                                                    player.sendMessage("");
                                                    recipient.sendMessage(ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "잔액: " + ChatColor.GOLD + df.format(updatedRecipientMoney) + ChatColor.WHITE + "원");
                                                    recipient.sendMessage("");
                                                } else {
                                                    messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "최소 " + ChatColor.GOLD + df.format(getConfig().getLong("minimum_amount")) +
                                                            ChatColor.WHITE + "원 이상부터 보낼 수 있습니다.");
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
                                if (Bukkit.getPlayer(args[1]) != null && MoneyManager.get().getConfigurationSection("player.money").getKeys(false).contains(Bukkit.getPlayer(args[1]).getUniqueId().toString())) {
                                    if (args[2].matches("[0-9]+")) {
                                        long increasedPlayerMoney = MoneyManager.get().getLong("player.money." + Bukkit.getPlayer(args[1]).getUniqueId()) + Long.parseLong(args[2]);
                                        MoneyManager.get().set("player.money." + Bukkit.getPlayer(args[1]).getUniqueId(), increasedPlayerMoney);
                                        messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.LIGHT_PURPLE + Bukkit.getPlayer(args[1]).getName() + ChatColor.WHITE + "님에게 " + ChatColor.GOLD + df.format(Long.parseLong(args[2])) + ChatColor.WHITE + "원을 지급하였습니다.");
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
                                if (Bukkit.getPlayer(args[1]) != null && MoneyManager.get().getConfigurationSection("player.money").getKeys(false).contains(Bukkit.getPlayer(args[1]).getUniqueId().toString())) {
                                    if (args[2].matches("[0-9]+")) {
                                        long decreasedPlayerMoney = MoneyManager.get().getLong("player.money." + Bukkit.getPlayer(args[1]).getUniqueId()) - Long.parseLong(args[2]);
                                        if (decreasedPlayerMoney < 0) {
                                            MoneyManager.get().set("player.money." + Bukkit.getPlayer(args[1]).getUniqueId(), 0);
                                        } else {
                                            MoneyManager.get().set("player.money." + Bukkit.getPlayer(args[1]).getUniqueId(), decreasedPlayerMoney);
                                        }
                                        messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.LIGHT_PURPLE + Bukkit.getPlayer(args[1]).getName() + ChatColor.WHITE + "님의 돈을 " + ChatColor.GOLD + df.format(Long.parseLong(args[2])) + ChatColor.WHITE + "원 차감하였습니다.");
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
                                if (Bukkit.getPlayer(args[1]) != null && MoneyManager.get().getConfigurationSection("player.money").getKeys(false).contains(Bukkit.getPlayer(args[1]).getUniqueId().toString())) {
                                    if (args[2].matches("[0-9]+")) {
                                        MoneyManager.get().set("player.money." + Bukkit.getPlayer(args[1]).getUniqueId(), Long.parseLong(args[2]));
                                        messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.LIGHT_PURPLE + Bukkit.getPlayer(args[1]).getName() + ChatColor.WHITE + "님의 돈을 " + ChatColor.GOLD + df.format(Long.parseLong(args[2])) + ChatColor.WHITE + "원으로 설정하였습니다.");
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

    public void messageForm(Player player, String string) {
        player.sendMessage("");
        player.sendMessage(string);
        player.sendMessage("");
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
        } catch (NullPointerException e) {
            e.printStackTrace();
            getLogger().info("messages.yml에서 메시지를 불러오는 도중 문제가 발생했습니다.");
        }
    }
}
