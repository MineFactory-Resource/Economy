package net.teamuni.economy.event;

import net.teamuni.economy.Uconomy;
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
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (main.isMySQLUse()) {
                main.getPlayerDataManagerMySQL().getCache(playerUUID);
            } else {
                main.getPlayerDataManagerYML().getCache(playerUUID);
            }
        });
    }
}
