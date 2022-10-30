package net.teamuni.economy.event;

import net.teamuni.economy.Uconomy;
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
            if (main.isMySQLUse()) {
                main.getPlayerDataManager().getCache(player.getUniqueId());
            }
            if (main.getMoneyManager().hasAccount(player)) {
                main.getMoneyManager().createPlayerAccount(player);
                Bukkit.getLogger().info("[Uconomy] " + player.getName() + "님의 돈 정보를 생성하였습니다.");
            }
        });
    }
}
