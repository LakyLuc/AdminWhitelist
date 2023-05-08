package de.lcpcraft.lucas.adminwhitelist.listeners;

import de.lcpcraft.lucas.adminwhitelist.AdminWhitelist;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerLoginEvent e) {
        if (e.getResult().equals(PlayerLoginEvent.Result.KICK_WHITELIST) && (AdminWhitelist.adminOnline() || AdminWhitelist.isAdmin(e.getPlayer().getUniqueId())))
            e.allow();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (Bukkit.getServer().hasWhitelist() && !AdminWhitelist.adminOnline(e.getPlayer().getUniqueId())) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.isWhitelisted() && !AdminWhitelist.isAdmin(onlinePlayer.getUniqueId()))
                    onlinePlayer.kick(AdminWhitelist.kickMessage(), PlayerKickEvent.Cause.WHITELIST);
            }
        }
    }
}
