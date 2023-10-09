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
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
        @NotNull String alias, @NotNull String[] args) {
        Player player = (Player) sender;

        if (!command.getName().equalsIgnoreCase("돈")) {
            return null;
        }

        List<String> tabCompleteMessage = new ArrayList<>();

        switch (args.length) {
            case 1 -> addFirstArgCommands(player, tabCompleteMessage);
            case 2 -> addSecondArgCommands(player, args[0], tabCompleteMessage);
            case 3 -> addThirdArgCommands(player, args[0], tabCompleteMessage);
            case 4 -> addFourthArgCommands(player, args[0], tabCompleteMessage);
        }

        return tabCompleteMessage.isEmpty() ? null : tabCompleteMessage;
    }

    private void addFirstArgCommands(Player player, List<String> commands) {
        if (player.hasPermission("ucon.manage")) {
            commands.add("확인");
            commands.add("보내기");
            commands.add("지급");
            commands.add("차감");
            commands.add("설정");
            commands.add("리로드");
            commands.add("도움말");
        } else {
            commands.add("확인");
            commands.add("보내기");
        }
    }

    private void addSecondArgCommands(Player player, String arg, List<String> commands) {
        if (canUseOpCommand(player, arg)
            || arg.equalsIgnoreCase("보내기")
            || arg.equalsIgnoreCase("확인")) {
            Bukkit.getOnlinePlayers().forEach(p -> commands.add(p.getName()));
        }
    }

    private void addThirdArgCommands(Player player, String arg, List<String> commands) {
        if (canUseOpCommand(player, arg)
            || arg.equalsIgnoreCase("보내기")) {
            commands.addAll(economyIDs);
        }
    }

    private void addFourthArgCommands(Player player, String arg, List<String> commands) {
        if (canUseOpCommand(player, arg)
            || arg.equalsIgnoreCase("보내기")) {
            commands.add("금액");
        }
    }

    private boolean canUseOpCommand(Player player, String command) {
        return player.hasPermission("ucon.manage")
            && (command.equalsIgnoreCase("지급")
            || command.equalsIgnoreCase("차감")
            || command.equalsIgnoreCase("설정"));
    }
}
