package net.teamuni.economy.event;

import net.teamuni.economy.Uconomy;
import net.teamuni.economy.data.MoneyUpdater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class JoinAndQuit implements Listener {
    private final Uconomy main;
    public JoinAndQuit(Uconomy instance) {
        this.main = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        MoneyUpdater moneyUpdater = main.getMoneyUpdater();

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (!moneyUpdater.hasAccount(playerUUID)) {
                moneyUpdater.createPlayerAccount(player);
            }
            main.getPlayerFileManager().load(playerUUID);
            main.getPlayerDataManager().getCache(playerUUID);
        });
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        main.getPlayerFileManager().remove(event.getPlayer().getUniqueId());
    }
}
