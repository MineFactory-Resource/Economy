package net.teamuni.economy.vault;

import net.milkbowl.vault.economy.Economy;
import net.teamuni.economy.Uconomy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class HookIntoVault {
    private final Uconomy plugin = Uconomy.getInstance();
    private final EconomyManager econ = Uconomy.getEconomyManager();
    private Economy provider;

    public void hook() {
        provider = econ;
        Bukkit.getServicesManager().register(Economy.class, provider, this.plugin, ServicePriority.High);
        Bukkit.getLogger().info("[Uconomy] Vault is hooked into " + plugin.getName() + " sucessfully!");
    }

    public void unhook() {
        Bukkit.getServicesManager().unregister(Economy.class, this.provider);
        Bukkit.getLogger().info("[Uconomy] Vault is unhooked from " + plugin.getName() + " sucessfully!");
    }
}
