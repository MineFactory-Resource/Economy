package net.teamuni.economy.vault;

import net.milkbowl.vault.economy.Economy;
import net.teamuni.economy.Uconomy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class HookIntoVault {
    private final Uconomy plugin = Uconomy.getInstance;
    private Economy provider;

    public void hook() {
        provider = plugin.economyManager;
        Bukkit.getServicesManager().register(Economy.class, provider, this.plugin, ServicePriority.High);
        Bukkit.getLogger().info("Vault is hooked into " + plugin.getName() + " sucessfully!");
    }

    public void unhook() {
        Bukkit.getServicesManager().unregister(Economy.class, this.provider);
        Bukkit.getLogger().info("Vault is unhooked from " + plugin.getName() + " sucessfully!");
    }
}
