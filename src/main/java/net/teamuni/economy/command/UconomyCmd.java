package net.teamuni.economy.command;

import net.teamuni.economy.Uconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UconomyCmd implements CommandExecutor {
    private final Map<String, List<String>> messageListMap = new HashMap<>();
    private final Uconomy main;
    private final DecimalFormat df = new DecimalFormat("###,###");
    public UconomyCmd(Uconomy instance) {
        this.main = instance;
        getMessages();
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
                                        main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("check_my_money")
                                                , "%name_of_player%", player.getName()
                                                , "%player_money%", df.format(main.getEconomyManager().getBalance(player)));
                                case 2 -> {
                                    if (!player.hasPermission("ucon.manage")) {
                                        main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("not_available_command"));
                                        return false;
                                    }
                                    OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
                                    if (target == null || main.getEconomyManager().hasAccount(target)) {
                                        main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("incorrect_player_name"));
                                        return false;
                                    }
                                    main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("check_the_other_player_money")
                                            , "%name_of_player%", target.getName()
                                            , "%player_money%", df.format(main.getEconomyManager().getBalance(target)));
                                }
                                default ->
                                        main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("not_available_command"));
                            }
                            break;
                        case "보내기":
                            if (args.length != 3) {
                                main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("not_available_command"));
                                return false;
                            }
                            OfflinePlayer recipient = Bukkit.getOfflinePlayerIfCached(args[1]);

                            if (recipient == null || main.getEconomyManager().hasAccount(recipient)) {
                                main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("incorrect_player_name"));
                                return false;
                            }
                            if (recipient == player) {
                                main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("attempt_to_deposit_to_oneself"));
                                return false;
                            }
                            if (!args[2].matches("[0-9]+")) {
                                main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("invalid_syntax"));
                                return false;
                            }
                            if (!main.getEconomyManager().has(player, Long.parseLong(args[2]))) {
                                main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("money_shortage"));
                                return false;
                            }
                            if (Long.parseLong(args[2]) < main.getConfig().getLong("minimum_amount")) {
                                main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("minimum_amount_caution")
                                        , "%value_of_minimum%", df.format(main.getConfig().getLong("minimum_amount")));
                                return false;
                            }
                            main.getEconomyManager().withdrawPlayer(player, Long.parseLong(args[2]));
                            main.getEconomyManager().depositPlayer(recipient, Long.parseLong(args[2]));

                            main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("transaction_confirm_to_sender")
                                    , "%name_of_recipient%", recipient.getName()
                                    , "%sent_money%", df.format(Long.parseLong(args[2]))
                                    , "%sender_money_after_transaction%", df.format(main.getEconomyManager().getBalance(player)));

                            if (recipient.isOnline()) {
                                Player onlineRecipient = recipient.getPlayer();
                                assert onlineRecipient != null;
                                main.getMessageManager().sendTranslatedMsgs(onlineRecipient, this.messageListMap.get("transaction_confirm_to_recipient")
                                        , "%name_of_sender%", player.getName()
                                        , "%received_money%", df.format(Long.parseLong(args[2]))
                                        , "%recipient_money_after_transaction%", df.format(main.getEconomyManager().getBalance(recipient)));
                            }
                            break;
                        case "지급", "차감", "설정":
                            if (!player.hasPermission("ucon.manage")) {
                                main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("not_available_command"));
                                return false;
                            }
                            if (args.length != 3) {
                                main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("not_available_command"));
                                return false;
                            }
                            OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

                            if (target == null || main.getEconomyManager().hasAccount(target)) {
                                main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("incorrect_player_name"));
                                return false;
                            }
                            if (!args[2].matches("[0-9]+")) {
                                main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("invalid_syntax"));
                                return false;
                            }
                            if (args[0].equalsIgnoreCase("지급")) {
                                main.getEconomyManager().depositPlayer(target, Long.parseLong(args[2]));

                                main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("increase_player_money")
                                        , "%name_of_player%", target.getName()
                                        , "%increased_money%", df.format(Long.parseLong(args[2])));
                                return false;
                            }
                            if (args[0].equalsIgnoreCase("차감")) {
                                main.getEconomyManager().withdrawPlayer(target, Long.parseLong(args[2]));

                                if (main.getEconomyManager().getBalance(target) < 0) {
                                    main.getEconomyManager().depositPlayer(target, main.getEconomyManager().getBalance(target) * -1);
                                }
                                main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("decrease_player_money")
                                        , "%name_of_player%", target.getName()
                                        , "%decreased_money%", df.format(Long.parseLong(args[2])));
                                return false;
                            }
                            if (args[0].equalsIgnoreCase("설정")) {
                                main.getEconomyManager().withdrawPlayer(target, main.getEconomyManager().getBalance(target));
                                main.getEconomyManager().depositPlayer(target, Long.parseLong(args[2]));

                                main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("set_player_money")
                                        , "%name_of_player%", target.getName()
                                        , "%set_money%", df.format(Long.parseLong(args[2])));
                                return false;
                            }
                            break;
                        default:
                            main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("not_available_command"));
                            break;
                    }
                } else {
                    if (player.hasPermission("ucon.manage")) {
                        main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("money_command_guide_for_op"));
                    } else {
                        main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("money_command_guide"));
                    }
                }
                return false;
            }
            if (command.getName().equalsIgnoreCase("uconomy") && args[0].equalsIgnoreCase("reload") && player.hasPermission("ucon.reload")) {
                main.reloadConfig();
                main.getMoneyManager().save();
                main.getMessageManager().reload();
                getMessages();
                main.getMessageManager().sendTranslatedMsgs(player, this.messageListMap.get("reload_message"));
            }
            return false;
        }
        return false;
    }

    public void getMessages() {
        ConfigurationSection section = main.getMessageManager().get().getConfigurationSection("Messages");
        if (section == null) return;
        for (String key : section.getKeys(false)) {
            List<String> messages = section.getStringList(key);
            this.messageListMap.put(key, messages);
        }
    }
}
