package net.teamuni.economy.config;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.teamuni.economy.Uconomy;
import net.teamuni.economy.data.MoneyManager;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public class UconomyPlaceholders extends PlaceholderExpansion {

    DecimalFormat df = new DecimalFormat("###,###");

    private final Uconomy main = Uconomy.getInstance();

    @Override
    public @NotNull String getAuthor() {
        return "";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "Uconomy";
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
        if (params.equalsIgnoreCase("balance")) {
            return df.format(MoneyManager.get().getLong("player." + player.getUniqueId()));
        }
        if (params.equalsIgnoreCase("minimum_value")) {
            return df.format(main.getConfig().getLong("minimum_amount"));
        }
        if (params.equalsIgnoreCase("transfered_money")) {
            return df.format(main.getAmount().get("changedValue"));
        }
        if (params.equalsIgnoreCase("sender_name")) {
            if (main.getSenderName().get("sender").isEmpty()) {
                return null;
            } else {
                return main.getSenderName().get("sender");
            }
        }
        if (params.equalsIgnoreCase("recipient_name")) {
            if (main.getReicipientName().get("recipient").isEmpty()) {
                return null;
            } else {
                return main.getReicipientName().get("recipient");
            }
        }
        return null;
    }
}
