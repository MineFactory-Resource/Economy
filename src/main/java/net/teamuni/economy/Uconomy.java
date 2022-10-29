package net.teamuni.economy;

import lombok.Getter;
import net.teamuni.economy.command.CommandTabCompleter;
import net.teamuni.economy.command.UconomyCmd;
import net.teamuni.economy.config.MessageManager;
import net.teamuni.economy.data.MoneyManager;
import net.teamuni.economy.data.PlayerDataManager;
import net.teamuni.economy.database.MySQLDatabase;
import net.teamuni.economy.event.JoinEvent;
import net.teamuni.economy.data.EconomyManager;
import net.teamuni.economy.hooks.UconomyPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Getter
public final class Uconomy extends JavaPlugin {
    private EconomyManager economyManager;
    private MessageManager messageManager;
    private MoneyManager moneyManager;
    private MySQLDatabase database;
    private PlayerDataManager playerDataManager;
    private boolean isMySQLUse = false;

    @Override
    public void onEnable() {
        this.economyManager = new EconomyManager(this);
        this.messageManager = new MessageManager(this);
        this.moneyManager = new MoneyManager(this);
        this.messageManager.createMessagesYml();
        this.moneyManager.createMoneyDataYml();
        saveDefaultConfig();
        this.isMySQLUse = Boolean.parseBoolean(getConfig().getString("mysql_use"));
        if (isMySQLUse) {
            try {
                this.database = new MySQLDatabase(
                        getConfig().getString("MySQL.Host"), getConfig().getInt("MySQL.Port"),
                        getConfig().getString("MySQL.Database"), getConfig().getString("MySQL.Parameters"),
                        getConfig().getString("MySQL.Username"), getConfig().getString("MySQL.Password"));
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "데이터베이스 연결에 실패하였습니다.", e);
                this.database = null;
            }
            this.playerDataManager = new PlayerDataManager(this);
            Bukkit.getPluginManager().registerEvents(this.playerDataManager, this);
        }
        Bukkit.getPluginManager().registerEvents(new JoinEvent(this), this);
        getCommand("돈").setExecutor(new UconomyCmd(this));
        getCommand("uconomy").setExecutor(new UconomyCmd(this));
        getCommand("돈").setTabCompleter(new CommandTabCompleter());
        getCommand("uconomy").setTabCompleter(new CommandTabCompleter());
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new UconomyPlaceholders().register();
        }
    }

    @Override
    public void onDisable() {
        this.moneyManager.save();
        if (isMySQLUse) {
            this.playerDataManager.removeAll();
        }
    }
}