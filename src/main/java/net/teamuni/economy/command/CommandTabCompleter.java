package net.teamuni.economy.command;

import net.teamuni.economy.Uconomy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {
    private final List<String> economyIDs = new ArrayList<>();
    public CommandTabCompleter(Uconomy instance) {
        this.economyIDs.addAll(instance.getConfig().getStringList("EconomyID"));
    }
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("돈")) {
            List<String> tabCompleteMessage = new ArrayList<>();

            if (args.length == 1) {
                if (player.hasPermission("ucon.manage")) {
                    tabCompleteMessage.add("확인");
                    tabCompleteMessage.add("보내기");
                    tabCompleteMessage.add("지급");
                    tabCompleteMessage.add("차감");
                    tabCompleteMessage.add("설정");
                } else {
                    tabCompleteMessage.add("확인");
                    tabCompleteMessage.add("보내기");
                }
                return tabCompleteMessage;

            } else if (args[0].equalsIgnoreCase("확인") && player.hasPermission("ucon.manage")) {
                if (args.length == 2) {
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        tabCompleteMessage.add(onlinePlayers.getName());
                    }
                }
                return tabCompleteMessage;

            } else if (args[0].equalsIgnoreCase("보내기")) {
                if (args.length == 2) {
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        tabCompleteMessage.add(onlinePlayers.getName());
                    }
                } else if (args.length == 3) {
                    tabCompleteMessage.addAll(economyIDs);
                } else if (args.length == 4) {
                    tabCompleteMessage.add("금액");
                }
                return tabCompleteMessage;

            } else if (args[0].equalsIgnoreCase("지급") || args[0].equalsIgnoreCase("차감") || args[0].equalsIgnoreCase("설정")) {
                if (player.hasPermission("ucon.manage")) {
                    if (args.length == 2) {
                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                            tabCompleteMessage.add(onlinePlayers.getName());
                        }
                    } else if (args.length == 3) {
                        tabCompleteMessage.addAll(economyIDs);
                    } else if (args.length == 4) {
                        tabCompleteMessage.add("금액");
                    }
                }
                return tabCompleteMessage;
            }

        } else if (command.getName().equalsIgnoreCase("uconomy")) {
            List<String> tabCompleteMessage = new ArrayList<>();

            if (args.length == 1) {
                tabCompleteMessage.add("reload");
            }
            return tabCompleteMessage;
        }
        return null;
    }
}