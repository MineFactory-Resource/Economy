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

public final class Uconomy extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        MoneyManager.createMoneyDataYml();
        MessageManager.createMessagesYml();
        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
        getCommand("돈").setTabCompleter(new CommandTabCompleter());
        getCommand("uconomy").setTabCompleter(new CommandTabCompleter());
    }

    @Override
    public void onDisable() {
        MoneyManager.save();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            DecimalFormat df = new DecimalFormat("###,###");

            if (command.getName().equalsIgnoreCase("돈")) {
                if (args.length > 0) {
                    switch (args[0]) {
                        case "확인":
                            if (args.length == 1) {
                                messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "현재 " + ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.WHITE + "님이 보유하고 있는 돈은 " + ChatColor.GOLD +
                                        df.format(MoneyManager.get().getLong("player.money." + player.getUniqueId())) + ChatColor.WHITE + "원입니다.");
                            } else {
                                messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "올바르지 않은 명령어입니다.");
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
                                                    messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.LIGHT_PURPLE + args[1] + ChatColor.WHITE + "님에게 " +
                                                            ChatColor.GOLD + args[2] + "만큼의 돈을 보냈습니다.");
                                                    messageForm(recipient, ChatColor.YELLOW + "[알림] " + ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.WHITE + "님으로부터 " +
                                                            ChatColor.GOLD + args[2] + "만큼의 돈을 받았습니다.");
                                                    player.sendMessage(ChatColor.GREEN + "잔액: " + updatedPlayerMoney);
                                                    recipient.sendMessage(ChatColor.GREEN + "잔액: " + updatedRecipientMoney);
                                                } else {
                                                    messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "최소 " + ChatColor.GOLD + getConfig().getLong("minimum_amount") +
                                                            ChatColor.WHITE + "원 이상부터 보낼 수 있습니다.");
                                                }
                                            } else {
                                                messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "돈이 부족합니다.");
                                            }
                                        } else {
                                            messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "숫자가 들어가야 하는 자리에 문자가 들어갈 수 없습니다.");
                                        }
                                    } else {
                                        messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "자신에게 돈을 보낼 수 없습니다.");
                                    }
                                } else {
                                    messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "입력하신 플레이어는 존재하지 않거나 오프라인 상태입니다.");
                                }
                            } else {
                                messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "명령어를 실행할 수 없습니다.");
                            }
                            break;
                        case "지급":
                            if (player.hasPermission("ucon.manage") && args.length == 3) {
                                if (Bukkit.getPlayer(args[1]) != null && MoneyManager.get().getConfigurationSection("player.money").getKeys(false).contains(Bukkit.getPlayer(args[1]).getUniqueId().toString())) {
                                    if (args[2].matches("[0-9]+")) {
                                        long increasedPlayerMoney = MoneyManager.get().getLong("player.money." + Bukkit.getPlayer(args[1]).getUniqueId()) + Long.parseLong(args[2]);
                                        MoneyManager.get().set("player.money." + Bukkit.getPlayer(args[1]).getUniqueId(), increasedPlayerMoney);
                                        messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.LIGHT_PURPLE + args[1] + ChatColor.WHITE + "님에게 " + ChatColor.GOLD + df.format(Long.parseLong(args[2])) + ChatColor.WHITE + "원을 지급하였습니다.");
                                    } else {
                                        messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "숫자가 들어가야 하는 자리에 문자가 들어갈 수 없습니다.");
                                    }
                                } else {
                                    messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "입력하신 플레이어는 존재하지 않거나 오프라인 상태입니다.");
                                }
                            } else {
                                messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "명령어를 실행할 수 없습니다.");
                            }
                            break;
                        case "차감":
                            if (player.hasPermission("ucon.manage") && args.length == 3) {
                                if (Bukkit.getPlayer(args[1]) != null && MoneyManager.get().getConfigurationSection("player.money").getKeys(false).contains(Bukkit.getPlayer(args[1]).getUniqueId().toString())) {
                                    if (args[2].matches("[0-9]+")) {
                                        long decreasedPlayerMoney = MoneyManager.get().getLong("player.money." + Bukkit.getPlayer(args[1]).getUniqueId()) - Long.parseLong(args[2]);
                                        MoneyManager.get().set("player.money." + Bukkit.getPlayer(args[1]).getUniqueId(), decreasedPlayerMoney);
                                        messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.LIGHT_PURPLE + args[1] + ChatColor.WHITE + "님의 돈을 " + ChatColor.GOLD + df.format(Long.parseLong(args[2])) + ChatColor.WHITE + "원 차감하였습니다.");
                                    } else {
                                        messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "숫자가 들어가야 하는 자리에 문자가 들어갈 수 없습니다.");
                                    }
                                } else {
                                    messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "입력하신 플레이어는 존재하지 않거나 오프라인 상태입니다.");
                                }
                            } else {
                                messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "명령어를 실행할 수 없습니다.");
                            }
                            break;
                        case "설정":
                            if (player.hasPermission("ucon.manage") && args.length == 3) {
                                if (Bukkit.getPlayer(args[1]) != null && MoneyManager.get().getConfigurationSection("player.money").getKeys(false).contains(Bukkit.getPlayer(args[1]).getUniqueId().toString())) {
                                    if (args[2].matches("[0-9]+")) {
                                        MoneyManager.get().set("player.money." + Bukkit.getPlayer(args[1]).getUniqueId(), Long.parseLong(args[2]));
                                        messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.LIGHT_PURPLE + args[1] + ChatColor.WHITE + "님의 돈을 " + ChatColor.GOLD + df.format(Long.parseLong(args[2])) + ChatColor.WHITE + "원으로 설정하였습니다.");
                                    } else {
                                        messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "숫자가 들어가야 하는 자리에 문자가 들어갈 수 없습니다.");
                                    }
                                } else {
                                    messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "입력하신 플레이어는 존재하지 않거나 오프라인 상태입니다.");
                                }
                            } else {
                                messageForm(player, ChatColor.YELLOW + "[알림] " + ChatColor.WHITE + "명령어를 실행할 수 없습니다.");
                            }
                            break;
                    }
                } else {
                    for (String guideMessages : MessageManager.get().getStringList("money_command_guide")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', guideMessages));
                    }
                }
                return false;
            }
            if (command.getName().equalsIgnoreCase("uconomy") && player.hasPermission("ucon.reload")) {
                if (args[0].equalsIgnoreCase("reload")) {
                    reloadConfig();
                    MoneyManager.save();
                    MessageManager.reload();
                    player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Uconomy has been reloaded!");
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
}
