package net.teamuni.economy.command;

import net.teamuni.economy.Uconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UconomyCmd implements CommandExecutor {
    private final Map<String, List<String>> messageListMap = new HashMap<>();
    private final Uconomy main;
    private final DecimalFormat df = new DecimalFormat("###,###");

    public UconomyCmd(Uconomy instance) {
        this.main = instance;
        this.messageListMap.putAll(instance.getMessageManager().getMessages());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (command.getName().equalsIgnoreCase("돈")) {
                if (args.length > 0) {
                    switch (args[0]) {
                        case "확인":
                            switch (args.length) {
                                case 1 ->
                                        main.getMessageManager().sendPlayerMoneyInfo(player);
                                case 2 -> {
                                    if (!player.hasPermission("ucon.manage")) {
                                        main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("not_available_command"));
                                        return false;
                                    }
                                    OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
                                    if (target == null) {
                                        main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("incorrect_player_name"));
                                        return false;
                                    }
                                    if (!hasAccount(target.getUniqueId())) {
                                        main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("incorrect_player_name"));
                                        return false;
                                    }
                                    main.getMessageManager().sendPlayerMoneyInfo(player, target);
                                }
                                default ->
                                        main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("not_available_command"));
                            }
                            break;
                        case "보내기":
                            if (args.length != 4) {
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("not_available_command"));
                                return false;
                            }
                            OfflinePlayer recipient = Bukkit.getOfflinePlayerIfCached(args[1]);

                            if (recipient == null) {
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("incorrect_player_name"));
                                return false;
                            }
                            if (!hasAccount(recipient.getUniqueId())) {
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("incorrect_player_name"));
                                return false;
                            }
                            if (recipient == player) {
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("attempt_to_deposit_to_oneself"));
                                return false;
                            }
                            if (!main.getConfig().getStringList("EconomyID").contains(args[2])) {
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("invalid_economyID"));
                            }
                            if (!args[3].matches("[0-9]+")) {
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("invalid_syntax"));
                                return false;
                            }
                            if (!main.getMoneyManager().has(player, args[2], Long.parseLong(args[3]))) {
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("money_shortage"));
                                return false;
                            }
                            if (Long.parseLong(args[3]) < main.getConfig().getLong("minimum_amount")) {
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("minimum_amount_caution")
                                        , "%value_of_minimum%", df.format(main.getConfig().getLong("minimum_amount")));
                                return false;
                            }
                            main.getMoneyManager().withdraw(player, args[2], Long.parseLong(args[3]));
                            main.getMoneyManager().deposit(recipient, args[2], Long.parseLong(args[3]));

                            main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("transaction_confirm_to_sender")
                                    , "%economy_id%" , args[2]
                                    , "%name_of_recipient%", recipient.getName()
                                    , "%sent_money%", df.format(Long.parseLong(args[3]))
                                    , "%sender_money_after_transaction%", df.format(main.getMoneyManager().getBalance(player, args[2])));

                            if (recipient.isOnline()) {
                                Player onlineRecipient = recipient.getPlayer();
                                assert onlineRecipient != null;
                                main.getMessageManager().sendTranslatedMessage(onlineRecipient, this.messageListMap.get("transaction_confirm_to_recipient")
                                        , "%economy_id%" , args[2]
                                        , "%name_of_sender%", player.getName()
                                        , "%received_money%", df.format(Long.parseLong(args[3]))
                                        , "%recipient_money_after_transaction%", df.format(main.getMoneyManager().getBalance(recipient, args[2])));
                            }
                            break;
                        case "지급", "차감", "설정":
                            if (!player.hasPermission("ucon.manage")) {
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("not_available_command"));
                                return false;
                            }
                            if (args.length != 4) {
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("not_available_command"));
                                return false;
                            }
                            OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

                            if (target == null) {
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("incorrect_player_name"));
                                return false;
                            }
                            if (!hasAccount(target.getUniqueId())) {
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("incorrect_player_name"));
                                return false;
                            }
                            if (!main.getConfig().getStringList("EconomyID").contains(args[2])) {
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("invalid_economyID"));
                                return false;
                            }
                            if (!args[3].matches("[0-9]+")) {
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("invalid_syntax"));
                                return false;
                            }
                            if (args[0].equalsIgnoreCase("지급")) {
                                main.getMoneyManager().deposit(target, args[2], Long.parseLong(args[3]));

                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("increase_player_money")
                                        , "%economy_id%" , args[2]
                                        , "%name_of_player%", target.getName()
                                        , "%increased_money%", df.format(Long.parseLong(args[3])));
                                return false;
                            }
                            if (args[0].equalsIgnoreCase("차감")) {
                                main.getMoneyManager().withdraw(target, args[2], Long.parseLong(args[3]));

                                if (main.getMoneyManager().getBalance(target, args[2]) < 0) {
                                    main.getMoneyManager().set(target, args[2], 0);
                                }
                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("decrease_player_money")
                                        , "%economy_id%" , args[2]
                                        , "%name_of_player%", target.getName()
                                        , "%decreased_money%", df.format(Long.parseLong(args[3])));
                                return false;
                            }
                            if (args[0].equalsIgnoreCase("설정")) {
                                main.getMoneyManager().set(target, args[2], Long.parseLong(args[3]));

                                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("set_player_money")
                                        , "%economy_id%" , args[2]
                                        , "%name_of_player%", target.getName()
                                        , "%set_money%", df.format(Long.parseLong(args[3])));
                                return false;
                            }
                            break;
                        default:
                            main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("not_available_command"));
                            break;
                    }
                } else {
                    if (player.hasPermission("ucon.manage")) {
                        main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("money_command_guide_for_op"));
                    } else {
                        main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("money_command_guide"));
                    }
                }
                return false;
            }
            if (command.getName().equalsIgnoreCase("uconomy") && args[0].equalsIgnoreCase("reload") && player.hasPermission("ucon.reload")) {
                main.reloadConfig();
                main.getMessageManager().reload();
                this.messageListMap.putAll(main.getMessageManager().getMessages());
                main.getMessageManager().sendTranslatedMessage(player, this.messageListMap.get("reload_message"));
            }
            return false;
        }
        return false;
    }

    private boolean hasAccount(UUID uuid) {
        return main.getMoneyUpdater().hasAccount(uuid);
    }
}
