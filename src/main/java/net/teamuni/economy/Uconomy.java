package net.teamuni.economy;

import net.teamuni.economy.config.MessageManager;
import net.teamuni.economy.data.MoneyManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Uconomy extends JavaPlugin {

    @Override
    public void onEnable() {
        MoneyManager.createMoneyDataYml();
        MessageManager.createMessagesYml();
    }

    @Override
    public void onDisable() {
        MoneyManager.save();
    }
}
