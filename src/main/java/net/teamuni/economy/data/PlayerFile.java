package net.teamuni.economy.data;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public record PlayerFile(File file, FileConfiguration fileConfiguration) {
}
