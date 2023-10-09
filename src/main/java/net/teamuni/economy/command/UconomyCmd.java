package net.teamuni.economy.command;

import net.teamuni.economy.Uconomy;
import net.teamuni.economy.config.MessageManager;
import net.teamuni.economy.data.MoneyManager;
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
    private final MoneyManager moneyManager;
    private final MessageManager messageManager;

    public UconomyCmd(Uconomy instance) {
        this.main = instance;
        this.moneyManager = instance.getMoneyManager();
        this.messageManager = instance.getMessageManager();
        this.messageListMap.putAll(this.messageManager.getMessages());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
        @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (!command.getName().equalsIgnoreCase("돈")) {
            return false;
        }

        if (args.length > 0 && args.length <= 4) {
            switch (args[0]) {
                case "도움말" -> {
                    if (player.hasPermission("ucon.manage")) {
                        sendTranslatedMessage(player, "money_command_guide_for_op");
                    } else {
                        sendTranslatedMessage(player, "money_command_guide");
                    }
                }

                case "확인" -> {
                    if (args.length == 2) {
                        if (hasNoPermission(player)) {
                            return false;
                        }

                        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

                        if (isInvalidPlayer(player, target)) {
                            return false;
                        }

                        assert target != null;

                        this.messageManager.sendPlayerMoneyInfo(player, target);
                    } else {
                        sendTranslatedMessage(player, "not_available_command");
                    }
                }

                case "보내기" -> {
                    OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

                    if (isInvalidCommand(player, target, args)) {
                        return false;
                    }

                    String economyID = args[2];
                    long amount = Long.parseLong(args[3]);

                    if (isOneSelf(player, target)) {
                        return false;
                    }

                    if (hasNotEnoughMoney(player, economyID, amount)) {
                        return false;
                    }

                    if (isInvalidAmount(player, amount)) {
                        return false;
                    }

                    assert target != null;

                    this.moneyManager.withdraw(player, economyID, amount);
                    this.moneyManager.deposit(target, economyID, amount);
                    this.messageManager.sendTranslatedMessage(player,
                        this.messageListMap.get("transaction_confirm_to_sender")
                        , "%economy_id%", economyID
                        , "%name_of_recipient%", target.getName()
                        , "%sent_money%", df.format(amount)
                        , "%sender_money_after_transaction%",
                        df.format(this.moneyManager.getBalance(player, economyID)));

                    if (target.isOnline()) {
                        Player onlineTarget = target.getPlayer();
                        this.messageManager.sendTranslatedMessage(onlineTarget,
                            this.messageListMap.get("transaction_confirm_to_recipient")
                            , "%economy_id%", economyID
                            , "%name_of_sender%", player.getName()
                            , "%received_money%", df.format(amount)
                            , "%recipient_money_after_transaction%", df.format(
                                this.moneyManager.getBalance(target, economyID)));
                    }
                }

                case "지급", "차감", "설정" -> {
                    if (hasNoPermission(player)) {
                        return false;
                    }

                    OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

                    if (isInvalidCommand(player, target, args)) {
                        return false;
                    }

                    String economyID = args[2];
                    long amount = Long.parseLong(args[3]);
                    
                    assert target != null;
                    
                    switch (args[0]) {
                        case "지급" -> {
                            this.moneyManager.deposit(target, economyID, amount);
                            this.messageManager.sendTranslatedMessage(player,
                                this.messageListMap.get("increase_player_money")
                                , "%economy_id%", economyID
                                , "%name_of_player%", target.getName()
                                , "%increased_money%", df.format(amount));
                        }

                        case "차감" -> {
                            this.moneyManager.withdraw(target, economyID, amount);
                            this.messageManager.sendTranslatedMessage(player,
                                this.messageListMap.get("decrease_player_money")
                                , "%economy_id%", economyID
                                , "%name_of_player%", target.getName()
                                , "%decreased_money%", df.format(amount));
                        }

                        case "설정" -> {
                            this.moneyManager.set(target, economyID, amount);
                            this.messageManager.sendTranslatedMessage(player,
                                this.messageListMap.get("set_player_money")
                                , "%economy_id%", economyID
                                , "%name_of_player%", target.getName()
                                , "%set_money%", df.format(amount));
                        }
                    }
                }

                case "리로드" -> {
                    if (player.hasPermission("ucon.reload")) {
                        main.reloadConfig();
                        this.messageManager.reload();
                        this.messageListMap.putAll(this.messageManager.getMessages());
                        sendTranslatedMessage(player, "reload_message");
                        return false;
                    }
                }

                default -> sendTranslatedMessage(player, "not_available_command");
            }
        } else if (args.length == 0) {
            this.messageManager.sendPlayerMoneyInfo(player);
        } else {
            sendTranslatedMessage(player, "not_available_command");
        }
        return false;
    }

    private void sendTranslatedMessage(Player player, String messageID) {
        List<String> message = this.messageListMap.get(messageID);
        this.messageManager.sendTranslatedMessage(player, message);
    }

    private boolean isInvalidCommand(Player player, OfflinePlayer target, String[] args) {
        return isInvalidLength(player, args)
            || isInvalidPlayer(player, target)
            || isInvalidEconomyID(player, args)
            || isNotNumber(player, args);
    }

    private boolean hasAccount(UUID uuid) {
        return main.getMoneyUpdater().hasAccount(uuid);
    }

    private boolean hasNoPermission(Player player) {
        if (!player.hasPermission("ucon.manage")) {
            sendTranslatedMessage(player, "not_available_command");
            return true;
        }
        return false;
    }

    private boolean isInvalidPlayer(Player player, OfflinePlayer target) {
        if (target == null || !hasAccount(target.getUniqueId())) {
            sendTranslatedMessage(player, "incorrect_player_name");
            return true;
        }
        return false;
    }

    private boolean isInvalidLength(Player player, String[] args) {
        if (args.length != 4) {
            sendTranslatedMessage(player, "not_available_command");
            return true;
        }
        return false;
    }

    private boolean isInvalidEconomyID(Player player, String[] args) {
        if (!main.getConfig().getStringList("EconomyID").contains(args[2])) {
            sendTranslatedMessage(player, "invalid_economyID");
            return true;
        }
        return false;
    }

    private boolean isNotNumber(Player player, String[] args) {
        if (!args[3].matches("[0-9]+")) {
            sendTranslatedMessage(player, "invalid_syntax");
            return true;
        }
        return false;
    }

    private boolean hasNotEnoughMoney(Player player, String economyID, long amount) {
        if (!this.moneyManager.has(player, economyID, amount)) {
            this.messageManager.sendTranslatedMessage(player,
                this.messageListMap.get("money_shortage"));
            return true;
        }
        return false;
    }

    private boolean isInvalidAmount(Player player, long amount) {
        if (amount < main.getConfig().getLong("minimum_amount")) {
            this.messageManager.sendTranslatedMessage(player,
                this.messageListMap.get("minimum_amount_caution")
                , "%value_of_minimum%", df.format(main.getConfig().getLong("minimum_amount")));
            return true;
        }
        return false;
    }

    private boolean isOneSelf(Player player, OfflinePlayer target) {
        if (target == player) {
            sendTranslatedMessage(player, "attempt_to_deposit_to_oneself");
            return true;
        }
        return false;
    }
}
