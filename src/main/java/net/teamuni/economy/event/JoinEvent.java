package net.teamuni.economy.event;

import net.teamuni.economy.Uconomy;
import net.teamuni.economy.database.MySQLDatabase;
import net.teamuni.economy.database.YMLDatabase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class JoinEvent implements Listener {
    private final Uconomy main;
    public JoinEvent(Uconomy instance) {
        this.main = instance;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        MySQLDatabase mySQLDatabase = main.getMySQLDatabase();
        YMLDatabase ymlDatabase = main.getYmlDatabase();

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (main.isMySQLUse()) {
                if (!mySQLDatabase.hasAccount(playerUUID)) {
                    mySQLDatabase.createPlayerAccount(player);
                }
            } else {
                if (!ymlDatabase.hasAccount(playerUUID)) {
                    ymlDatabase.createPlayerAccount(player);
                }
            }
            main.getPlayerDataManager().getCache(playerUUID);
        });
    }
}
