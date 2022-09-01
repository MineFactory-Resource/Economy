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
        if (command.getName().equalsIgnoreCase("돈")) {
            List<String> tabComleteMessage = new ArrayList<>();

            if (args.length == 1) {
                tabComleteMessage.add("확인");
                tabComleteMessage.add("보내기");
            }
            if (args[0].equalsIgnoreCase("보내기") && args.length == 2) {
                for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                    tabComleteMessage.add(onlinePlayers.getName());
                }
            }
            if (args.length == 3) {
                tabComleteMessage.add("숫자 값");
            }
            return tabComleteMessage;
        }
        return null;
    }
}

