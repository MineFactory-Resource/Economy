package net.teamuni.economy.hooks;

import net.milkbowl.vault.economy.Economy;
import net.teamuni.economy.Uconomy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class HookIntoVault {
    private Economy provider;
    private final Uconomy main;
    public HookIntoVault(Uconomy instance) {
        this.main = instance;
    }

    public void hook() {
        this.provider = main.getEconomyManager();
        Bukkit.getServicesManager().register(Economy.class, this.provider, this.main, ServicePriority.High);
        Bukkit.getLogger().info("[Uconomy] Vault is hooked into " + this.main.getName() + " sucessfully!");
    }

    public void unhook() {
        Bukkit.getServicesManager().unregister(Economy.class, this.provider);
        Bukkit.getLogger().info("[Uconomy] Vault is unhooked from " + this.main.getName() + " sucessfully!");
    }
}
