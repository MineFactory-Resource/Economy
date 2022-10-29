package net.teamuni.economy.event;

import net.teamuni.economy.Uconomy;
import net.teamuni.economy.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {
    private final Uconomy main;
    public JoinEvent(Uconomy instance) {
        this.main = instance;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PlayerDataManager playerDataManager = main.getPlayerDataManager();
            if (playerDataManager.getCacheIfPresent(player.getUniqueId()) == null) {
                playerDataManager.getCache(player.getUniqueId());
            }
            if (!main.getEconomyManager().hasAccount(player)) {
                main.getEconomyManager().createPlayerAccount(player);
                Bukkit.getLogger().info("[Uconomy] " + player.getName() + "님의 돈 정보를 생성하였습니다.");
            }
        });
    }
}
