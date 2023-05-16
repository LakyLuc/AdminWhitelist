package de.lcpcraft.lucas.adminwhitelist.utils;

import de.lcpcraft.lucas.adminwhitelist.AdminWhitelist;
import de.lcpcraft.lucas.adminwhitelist.utils.modrinth.ProjectVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

public class Updater {
    private static final String URL = "https://api.modrinth.com/v2/";
    public static ProjectVersion latestVersion;

    public static void checkForUpdates() {
        String gameVersion = Bukkit.getMinecraftVersion();
        ProjectVersion latestVersion = getLatestVersion(gameVersion, AdminWhitelist.updateChannel());
        if (latestVersion == null) {
            Bukkit.getConsoleSender().sendMessage(AdminWhitelist.prefix() + "§cFailed to check for updates!");
            return;
        }
        String pluginVersion = Bukkit.getPluginManager().getPlugin("AdminWhitelist").getPluginMeta().getVersion();
        DefaultArtifactVersion latest = new DefaultArtifactVersion(latestVersion.version_number);
        DefaultArtifactVersion current = new DefaultArtifactVersion(pluginVersion);
        if (latest.compareTo(current) > 0) {
            Updater.latestVersion = latestVersion;
            Bukkit.getConsoleSender().sendMessage(AdminWhitelist.prefix() + "§aA new version of AdminWhitelist is available: §e" + latestVersion.version_number);
            Bukkit.getConsoleSender().sendMessage(AdminWhitelist.prefix() + "§aDownload it at §e"
                    + AdminWhitelist.MODRINTH_LINK.replace("%version%", latestVersion.version_number));
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("adminwhitelist.update"))
                    sendUpdateMessage(onlinePlayer);
            }
        }
    }

    public static void sendUpdateMessage(Player player) {
        if (latestVersion != null) {
            Component link = Component.text("Modrinth").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL,
                            AdminWhitelist.MODRINTH_LINK.replace("%version%", latestVersion.version_number)))
                    .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("§7Changelog:\n" + latestVersion.changelog)));
            Component message = Component.text(AdminWhitelist.prefix() + "§aA new version of AdminWhitelist is available §7("
                    + latestVersion.version_number + ") §aDownload it at ").append(link);
            player.sendMessage(message);
        }
    }

    private static ProjectVersion getLatestVersion(String gameVersion, String versionType) {
        RequestBuilder request = new RequestBuilder(URL + "project/" + AdminWhitelist.MODRINTH_ID
                + "/version?loaders=[%22paper%22]&game_versions=[%22" + gameVersion + "%22]");
        HttpsURLConnection connection = request.execute();
        try {
            if (connection.getResponseCode() == 200) {
                ProjectVersion[] versions = request.projectVersions();
                if (versions != null) {
                    for (ProjectVersion version : versions) {
                        if (versionType.equals("alpha"))
                            return version;
                        else if (version.version_type.equals(versionType))
                            return version;
                        else if (versionType.equals("beta") && version.version_type.equals("release"))
                            return version;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
