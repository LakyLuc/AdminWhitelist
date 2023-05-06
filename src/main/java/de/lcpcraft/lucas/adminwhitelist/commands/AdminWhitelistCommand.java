package de.lcpcraft.lucas.adminwhitelist.commands;

import de.lcpcraft.lucas.adminwhitelist.AdminWhitelist;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminWhitelistCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            if (strings.length == 1 && strings[0].equalsIgnoreCase("list")) {
                List<String> admins = AdminWhitelist.getAdmins();
                if (admins.size() == 0)
                    commandSender.sendMessage(AdminWhitelist.prefix() + "§cEs sind keine Admins festgelegt!");
                else {
                    List<String> adminNames = new ArrayList<>();
                    for (String admin : admins)
                        adminNames.add("§e" + admin + " §7(" + Bukkit.getOfflinePlayer(UUID.fromString(admin)).getName() + "§7)");
                    commandSender.sendMessage(AdminWhitelist.prefix() + "§aEs sind folgende Admins festgelegt: " + String.join("§8, ", adminNames));
                }
            } else if (strings.length == 2 && strings[0].equalsIgnoreCase("add")) {
                try {
                    UUID id = UUID.fromString(strings[1]);
                    if (AdminWhitelist.isAdmin(id))
                        commandSender.sendMessage(AdminWhitelist.prefix() + "§cDer Spieler mit der UUID §e" + id + " §cist bereits Admin!");
                    else {
                        AdminWhitelist.addAdmin(id);
                        commandSender.sendMessage(AdminWhitelist.prefix() + "§aDer Spieler mit der UUID §e" + id + " §aist nun Admin!");
                    }
                } catch (IllegalArgumentException e) {
                    commandSender.sendMessage(AdminWhitelist.prefix() + "§cDie UUID ist ungültig!");
                    return false;
                }
            } else if (strings.length == 2 && strings[0].equalsIgnoreCase("remove")) {
                try {
                    UUID id = UUID.fromString(strings[1]);
                    if (!AdminWhitelist.isAdmin(id))
                        commandSender.sendMessage(AdminWhitelist.prefix() + "§cDer Spieler mit der UUID §e" + id + " §cist kein Admin!");
                    else {
                        AdminWhitelist.removeAdmin(id);
                        commandSender.sendMessage(AdminWhitelist.prefix() + "§aDer Spieler mit der UUID §e" + id + " §aist nun kein Admin mehr!");
                    }
                } catch (IllegalArgumentException e) {
                    commandSender.sendMessage(AdminWhitelist.prefix() + "§cDie UUID ist ungültig!");
                    return false;
                }
            } else
                commandSender.sendMessage(AdminWhitelist.prefix() + "§cVerwendung: §e/adminwhitelist <list|add|remove> <UUID>");
        } else
            commandSender.sendMessage(AdminWhitelist.prefix() + AdminWhitelist.noPermission());
        return false;
    }
}
