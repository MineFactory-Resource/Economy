package net.teamuni.economy.database;

import net.teamuni.economy.Uconomy;
import net.teamuni.economy.data.MoneyUpdater;
import net.teamuni.economy.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public class YMLDatabase implements MoneyUpdater {
    private final Uconomy main;
    private final ConfigurationSection section;

    public YMLDatabase(Uconomy instance) {
        this.main = instance;
        this.section = instance.getMoneyManager().get().getConfigurationSection("player");
    }

    @Override
    public void updatePlayerStats(PlayerData stats) {
        section.set(stats.getUuid(), stats.getMoney());
    }

    @Override
    public PlayerData loadPlayerStats(UUID uuid) {
        String playerUUID = uuid.toString();
        if (section.isSet(uuid.toString())) {
            long money = section.getLong(playerUUID);
            return new PlayerData(playerUUID, money);
        }
        return new PlayerData(playerUUID, 0);
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        if (section == null) return false;
        return section.isSet(uuid.toString());
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        main.getMoneyManager().get().set("player." + player.getUniqueId(), 0);
        Bukkit.getLogger().info("[Uconomy] " + player.getName() + "님의 돈 정보를 생성하였습니다.");
        return true;
    }
}
