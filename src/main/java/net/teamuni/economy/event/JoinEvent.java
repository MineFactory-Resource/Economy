package net.teamuni.economy.event;

import net.teamuni.economy.data.MoneyManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Set;

public class JoinEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerUuid = player.getUniqueId().toString();
        Set<String> playerSet = MoneyManager.get().getConfigurationSection("player.money").getKeys(false);

        if (!playerSet.contains(playerUuid)) {
            playerSet.add(playerUuid);
            MoneyManager.get().set("player.money." + playerUuid, 0);
            Bukkit.getLogger().info("[Uconomy] " + player.getName() + "님의 돈 정보를 생성하였습니다.");
        }
    }
}
