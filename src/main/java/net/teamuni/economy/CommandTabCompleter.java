package net.teamuni.economy;

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
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("돈")) {
            List<String> tabComleteMessage = new ArrayList<>();

            if (args.length == 1) {
                if (player.hasPermission("ucon.manage")) {
                    tabComleteMessage.add("확인");
                    tabComleteMessage.add("보내기");
                    tabComleteMessage.add("지급");
                    tabComleteMessage.add("차감");
                    tabComleteMessage.add("설정");
                } else {
                    tabComleteMessage.add("확인");
                    tabComleteMessage.add("보내기");
                }
            }
            if (args[0].equalsIgnoreCase("보내기")) {
                if (args.length == 2) {
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        tabComleteMessage.add(onlinePlayers.getName());
                    }
                }
                if (args.length == 3) {
                    tabComleteMessage.add("금액");
                }
            }
            if (args[0].equalsIgnoreCase("지급") || args[0].equalsIgnoreCase("차감") || args[0].equalsIgnoreCase("설정")) {
                if (player.hasPermission("ucon.manage")) {
                    if (args.length == 2) {
                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                            tabComleteMessage.add(onlinePlayers.getName());
                        }
                    }
                    if (args.length == 3) {
                        tabComleteMessage.add("금액");
                    }
                }
            }
            return tabComleteMessage;
        }
        if (command.getName().equalsIgnoreCase("uconomy")) {
            List<String> tabComleteMessage = new ArrayList<>();

            if (args.length == 1) {
                tabComleteMessage.add("reload");
            }
            return tabComleteMessage;
        }
        return null;
    }
}