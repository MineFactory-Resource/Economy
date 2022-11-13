package net.teamuni.economy.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.teamuni.economy.Uconomy;
import net.teamuni.economy.data.PlayerData;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public class UconomyPlaceholders extends PlaceholderExpansion {
    private final DecimalFormat df = new DecimalFormat("###,###");
    private final Uconomy main = Uconomy.getPlugin(Uconomy.class);

    @Override
    public @NotNull String getAuthor() {
        return "";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "uconomy";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        for (String id : main.getConfig().getStringList("EconomyID")) {
            if (params.equalsIgnoreCase("balance_" + id)) {
                PlayerData cacheIfPresent = main.getPlayerDataManager().getCacheIfPresent(player.getUniqueId());
                return cacheIfPresent == null ? "0" : df.format(cacheIfPresent.getMoneyMap().get(id));
            }
        }
        if (params.equalsIgnoreCase("minimum_value")) {
            return df.format(main.getConfig().getLong("minimum_amount"));
        }
        return null;
    }
}
