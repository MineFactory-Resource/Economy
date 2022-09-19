package net.teamuni.economy;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.teamuni.economy.config.MessageManager;
import net.teamuni.economy.data.MoneyManager;
import net.teamuni.economy.event.JoinEvent;
import net.teamuni.economy.hooks.HookIntoVault;
import net.teamuni.economy.data.EconomyManager;
import net.teamuni.economy.hooks.UconomyPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

@Getter
public final class Uconomy extends JavaPlugin {

    private static Uconomy instance;
    private static EconomyManager manager;
    private static HookIntoVault hookIntoVault;

    public static Uconomy getInstance() {
        return instance;
    }

    public static EconomyManager getEconomyManager() {
        return manager;
    }

    List<String> reloadMessageList;
    List<String> commandGuideMessageList;
    List<String> opCommandGuideMessageList;
    List<String> notAvailableCommandMessageList;
    List<String> incorrectPlayerNameMessageList;
    List<String> invalidSyntaxMessageList;
    List<String> moneyShortageMessageList;
    List<String> attemptToDepositToOneselfMessageList;
    List<String> checkMyMoneyMessageList;
    List<String> checkTheOtherPlayerMoneyMessageList;
    List<String> transactionConfirmToSenderMessageList;
    List<String> transactionConfirmToRecipientMessageList;
    List<String> minimumAmountCautionMessageList;
    List<String> increasePlayerMoneyMessageList;
    List<String> decreasePlayerMoneyMessageList;
    List<String> setPlayerMoneyMessageList;

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
        instance = this;
        manager = new EconomyManager();
        hookIntoVault = new HookIntoVault();
        hookIntoVault.hook();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new UconomyPlaceholders().register();
        }

        if (!setupEconomy()) {
            getLogger().info("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        MoneyManager.save();
        hookIntoVault.unhook();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        return rsp != null;
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
                                    MessageManager.sendTranslatedMsgs(player, checkMyMoneyMessageList
                                            , "%name_of_player%", player.getName()
                                            , "%player_money%", df.format(getEconomyManager().getBalance(player)));
                                    break;
                                case 2:
                                    if (!player.hasPermission("ucon.manage")) {
                                        MessageManager.sendTranslatedMsgs(player, notAvailableCommandMessageList);
                                        return false;
                                    }
                                    OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

                                    if (target == null || !getEconomyManager().hasAccount(target)) {
                                        MessageManager.sendTranslatedMsgs(player, incorrectPlayerNameMessageList);
                                        return false;
                                    }
                                    MessageManager.sendTranslatedMsgs(player, checkTheOtherPlayerMoneyMessageList
                                            , "%name_of_player%", target.getName()
                                            , "%player_money%", df.format(getEconomyManager().getBalance(target)));
                                    break;
                                default:
                                    MessageManager.sendTranslatedMsgs(player, notAvailableCommandMessageList);
                                    break;
                            }
                            break;
                        case "보내기":
                            if (args.length != 3) {
                                MessageManager.sendTranslatedMsgs(player, notAvailableCommandMessageList);
                                return false;
                            }
                            OfflinePlayer recipient = Bukkit.getOfflinePlayerIfCached(args[1]);

                            if (recipient == null || !getEconomyManager().hasAccount(recipient)) {
                                MessageManager.sendTranslatedMsgs(player, incorrectPlayerNameMessageList);
                                return false;
                            }
                            if (recipient == player) {
                                MessageManager.sendTranslatedMsgs(player, attemptToDepositToOneselfMessageList);
                                return false;
                            }
                            if (!args[2].matches("[0-9]+")) {
                                MessageManager.sendTranslatedMsgs(player, invalidSyntaxMessageList);
                                return false;
                            }
                            if (!getEconomyManager().has(player, Double.parseDouble(args[2]))) {
                                MessageManager.sendTranslatedMsgs(player, moneyShortageMessageList);
                                return false;
                            }
                            if (Long.parseLong(args[2]) < getConfig().getLong("minimum_amount")) {
                                MessageManager.sendTranslatedMsgs(player, minimumAmountCautionMessageList
                                        , "%value_of_minimum%", df.format(getConfig().getLong("minimum_amount")));
                                return false;
                            }
                            getEconomyManager().withdrawPlayer(player, Double.parseDouble(args[2]));
                            getEconomyManager().depositPlayer(recipient, Double.parseDouble(args[2]));

                            MessageManager.sendTranslatedMsgs(player, transactionConfirmToSenderMessageList
                                    , "%name_of_recipient%", recipient.getName()
                                    , "%sent_money%", df.format(Long.parseLong(args[2]))
                                    , "%sender_money_after_transaction%", df.format(getEconomyManager().getBalance(player)));

                            if (recipient.isOnline()) {
                                Player onlineRecipient = recipient.getPlayer();
                                assert onlineRecipient != null;
                                MessageManager.sendTranslatedMsgs(onlineRecipient, transactionConfirmToRecipientMessageList
                                        , "%name_of_sender%", player.getName()
                                        , "%received_money%", df.format(Long.parseLong(args[2]))
                                        , "%recipient_money_after_transaction%", df.format(getEconomyManager().getBalance(recipient)));
                            }
                            break;
                        case "지급":
                        case "차감":
                        case "설정":
                            if (!player.hasPermission("ucon.manage")) {
                                MessageManager.sendTranslatedMsgs(player, notAvailableCommandMessageList);
                                return false;
                            }
                            if (args.length != 3) {
                                MessageManager.sendTranslatedMsgs(player, notAvailableCommandMessageList);
                                return false;
                            }
                            OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

                            if (target == null || !getEconomyManager().hasAccount(target)) {
                                MessageManager.sendTranslatedMsgs(player, incorrectPlayerNameMessageList);
                                return false;
                            }
                            if (!args[2].matches("[0-9]+")) {
                                MessageManager.sendTranslatedMsgs(player, invalidSyntaxMessageList);
                                return false;
                            }
                            if (args[0].equalsIgnoreCase("지급")) {
                                getEconomyManager().depositPlayer(target, Double.parseDouble(args[2]));

                                MessageManager.sendTranslatedMsgs(player, increasePlayerMoneyMessageList
                                        , "%name_of_player%", target.getName()
                                        , "%increased_money%", df.format(Long.parseLong(args[2])));
                                return false;
                            }
                            if (args[0].equalsIgnoreCase("차감")) {
                                getEconomyManager().withdrawPlayer(target, Double.parseDouble(args[2]));

                                if (getEconomyManager().getBalance(target) < 0) {
                                    getEconomyManager().depositPlayer(target, getEconomyManager().getBalance(target) * -1);
                                }
                                MessageManager.sendTranslatedMsgs(player, decreasePlayerMoneyMessageList
                                        , "%name_of_player%", target.getName()
                                        , "%decreased_money%", df.format(Long.parseLong(args[2])));
                                return false;
                            }
                            if (args[0].equalsIgnoreCase("설정")) {
                                getEconomyManager().withdrawPlayer(target, getEconomyManager().getBalance(target));
                                getEconomyManager().depositPlayer(target, Double.parseDouble(args[2]));

                                MessageManager.sendTranslatedMsgs(player, setPlayerMoneyMessageList
                                        , "%name_of_player%", target.getName()
                                        , "%set_money%", df.format(Long.parseLong(args[2])));
                                return false;
                            }
                            break;
                        default:
                            MessageManager.sendTranslatedMsgs(player, notAvailableCommandMessageList);
                            break;
                    }
                } else {
                    if (player.hasPermission("ucon.manage")) {
                        MessageManager.sendTranslatedMsgs(player, opCommandGuideMessageList);
                    } else {
                        MessageManager.sendTranslatedMsgs(player, commandGuideMessageList);
                    }
                }
                return false;
            }
            if (command.getName().equalsIgnoreCase("uconomy") && args[0].equalsIgnoreCase("reload") && player.hasPermission("ucon.reload")) {
                reloadConfig();
                MoneyManager.save();
                MessageManager.reload();
                getMessages();
                MessageManager.sendTranslatedMsgs(player, reloadMessageList);
            }
            return false;
        }
        return false;
    }

    public void getMessages() {
        try {
            reloadMessageList = MessageManager.get().getStringList("reload_message");
            commandGuideMessageList = MessageManager.get().getStringList("money_command_guide");
            opCommandGuideMessageList = MessageManager.get().getStringList("money_command_guide_for_op");
            notAvailableCommandMessageList = MessageManager.get().getStringList("not_available_command");
            incorrectPlayerNameMessageList = MessageManager.get().getStringList("incorrect_player_name");
            invalidSyntaxMessageList = MessageManager.get().getStringList("invalid_syntax");
            moneyShortageMessageList = MessageManager.get().getStringList("money_shortage");
            attemptToDepositToOneselfMessageList = MessageManager.get().getStringList("attempt_to_deposit_to_oneself");
            checkMyMoneyMessageList = MessageManager.get().getStringList("check_my_money");
            checkTheOtherPlayerMoneyMessageList = MessageManager.get().getStringList("check_the_other_player_money");
            transactionConfirmToSenderMessageList = MessageManager.get().getStringList("transaction_confirm_to_sender");
            transactionConfirmToRecipientMessageList = MessageManager.get().getStringList("transaction_confirm_to_recipient");
            minimumAmountCautionMessageList = MessageManager.get().getStringList("minimum_amount_caution");
            increasePlayerMoneyMessageList = MessageManager.get().getStringList("increase_player_money");
            decreasePlayerMoneyMessageList = MessageManager.get().getStringList("decrease_player_money");
            setPlayerMoneyMessageList = MessageManager.get().getStringList("set_player_money");
        } catch (NullPointerException e) {
            e.printStackTrace();
            getLogger().info("messages.yml에서 메시지를 불러오는 도중 문제가 발생했습니다.");
        }
    }
}