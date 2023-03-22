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

            switch (args.length) {
                case 1 -> {
                    if (player.hasPermission("ucon.manage")) {
                        tabCompleteMessage.add("확인");
                        tabCompleteMessage.add("보내기");
                        tabCompleteMessage.add("지급");
                        tabCompleteMessage.add("차감");
                        tabCompleteMessage.add("설정");
                        tabCompleteMessage.add("리로드");
                    } else {
                        tabCompleteMessage.add("확인");
                        tabCompleteMessage.add("보내기");
                    }
                    return tabCompleteMessage;
                }
                case 2 -> {
                    if (args[0].equalsIgnoreCase("확인")
                            && player.hasPermission("ucon.manage")
                            || isCommand(args[0])) {
                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                            tabCompleteMessage.add(onlinePlayers.getName());
                        }
                        return tabCompleteMessage;
                    }
                }
                case 3 -> {
                    if (isCommand(args[0])) {
                        tabCompleteMessage.addAll(economyIDs);
                    }
                    return tabCompleteMessage;
                }
                case 4 -> {
                    if (isCommand(args[0])) {
                        tabCompleteMessage.add("금액");
                    }
                    return tabCompleteMessage;
                }
                default -> {
                    return null;
                }
            }
        }
        return null;
    }

    private boolean isCommand(String command) {
        return command.equalsIgnoreCase("보내기")
                || command.equalsIgnoreCase("지급")
                || command.equalsIgnoreCase("차감")
                || command.equalsIgnoreCase("설정");
    }
}