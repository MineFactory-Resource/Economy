package net.teamuni.economy;

import net.teamuni.economy.config.MessageManager;
import net.teamuni.economy.data.MoneyManager;
import net.teamuni.economy.event.JoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Uconomy extends JavaPlugin {

    @Override
    public void onEnable() {
        MoneyManager.createMoneyDataYml();
        MessageManager.createMessagesYml();
        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
    }

    @Override
    public void onDisable() {
        MoneyManager.save();
    }
}
