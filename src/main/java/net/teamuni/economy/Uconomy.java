package net.teamuni.economy;

import org.bukkit.plugin.java.JavaPlugin;

public final class Uconomy extends JavaPlugin {

    @Override
    public void onEnable() {
        MoneyManager.createMoneyDataYml();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
