package de.lcpcraft.lucas.adminwhitelist;

import de.lcpcraft.lucas.adminwhitelist.commands.AdminWhitelistCommand;
import de.lcpcraft.lucas.adminwhitelist.listeners.JoinListener;
import de.lcpcraft.lucas.adminwhitelist.utils.Metrics;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class AdminWhitelist extends JavaPlugin {

    public static AdminWhitelist plugin;
    private static YamlConfiguration config;
    private static File configFile;

    @Override
    public void onEnable() {
        plugin = this;
        new Metrics(this, 18392);

        File pluginFolder = new File(plugin.getDataFolder().getParentFile(), "AdminWhitelist");
        configFile = new File(pluginFolder, "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.isSet("whitelist_admins")) {
            config.addDefault("whitelist_admins", new ArrayList<>());
            config.addDefault("prefix", "§1[§9AdminWhitelist§1] §r");
            config.addDefault("kick_message", "§cDu bist nicht auf der Whitelist!");
            config.addDefault("no_permission", "§cDieser Befehl kann nur von der Konsole ausgeführt werden!");
            config
                    .options()
                    .header(
                            "Configuration file of AdminWhitelist by LakyLuc")
                    .copyDefaults(true);
            try {
                config.save(configFile);
            } catch (IOException ignored) {
            }
        }

        getCommand("adminwhitelist").setExecutor(new AdminWhitelistCommand());
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

        if (!adminOnline())
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.isWhitelisted() && !isAdmin(onlinePlayer.getUniqueId()))
                    onlinePlayer.kick(kickMessage(), PlayerKickEvent.Cause.WHITELIST);
            }
    }

    @Override
    public void onDisable() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.isWhitelisted() && !isAdmin(onlinePlayer.getUniqueId()))
                onlinePlayer.kick(kickMessage(), PlayerKickEvent.Cause.WHITELIST);
        }
    }

    public static List<String> getAdmins() {
        return config.getStringList("whitelist_admins");
    }

    public static boolean isAdmin(UUID id) {
        return getAdmins().contains(id.toString());
    }

    public static void addAdmin(UUID id) {
        List<String> admins = getAdmins();
        admins.add(id.toString());
        config.set("whitelist_admins", admins);
        try {
            config.save(configFile);
        } catch (IOException ignored) {
        }
    }

    public static void removeAdmin(UUID id) {
        List<String> admins = getAdmins();
        admins.remove(id.toString());
        config.set("whitelist_admins", admins);
        try {
            config.save(configFile);
        } catch (IOException ignored) {
        }
    }

    public static boolean adminOnline() {
        return adminOnline(null);
    }

    public static boolean adminOnline(UUID exempt) {
        for (String admin : getAdmins()) {
            if (admin != null && Bukkit.getPlayer(UUID.fromString(admin)) != null && Bukkit.getPlayer(UUID.fromString(admin)).isOnline() && (exempt == null || !admin.equals(exempt.toString())))
                return true;
        }
        return false;
    }

    public static String prefix() {
        return Objects.requireNonNull(config.getString("prefix"));
    }

    public static String noPermission() {
        return Objects.requireNonNull(config.getString("no_permission"));
    }

    public static Component kickMessage() {
        return Component.text(Objects.requireNonNull(config.getString("kick_message")));
    }
}
