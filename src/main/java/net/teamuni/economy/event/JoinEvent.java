package net.teamuni.economy.event;

import net.teamuni.economy.Uconomy;
import net.teamuni.economy.data.PlayerDataManagerMySQL;
import net.teamuni.economy.data.PlayerDataManagerYML;
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
        PlayerDataManagerMySQL mySQLManager = main.getPlayerDataManagerMySQL();
        PlayerDataManagerYML ymlManager = main.getPlayerDataManagerYML();

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (main.isMySQLUse()) {
                if (!mySQLManager.hasAccount(playerUUID)) {
                    mySQLManager.createPlayerAccount(player);
                }
                mySQLManager.getCache(playerUUID);
            } else {
                if (!ymlManager.hasAccount(playerUUID)) {
                    ymlManager.createPlayerAccount(player);
                }
                ymlManager.getCache(playerUUID);
            }
        });
    }
}
