package net.teamuni.economy.event;

import net.teamuni.economy.Uconomy;
import net.teamuni.economy.data.MoneyUpdater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class JoinEvent implements Listener {
    private final Uconomy main;
    public JoinEvent(Uconomy instance) {
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
            if (!main.isMySQLUse()) {
                main.getPlayerFileManager().load(playerUUID);
            }
            main.getPlayerDataManager().getCache(playerUUID);
        });
    }
}
