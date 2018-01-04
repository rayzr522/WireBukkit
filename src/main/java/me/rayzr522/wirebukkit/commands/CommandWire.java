package me.rayzr522.wirebukkit.commands;

import me.rayzr522.wirebukkit.WireBukkit;
import me.rayzr522.wirebukkit.managers.SessionManager;
import me.rayzr522.wirebukkit.sessions.Session;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandWire implements CommandExecutor {
    private WireBukkit plugin;

    public CommandWire(WireBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.tr("command.fail.only-players"));
            return true;
        }

        if (!plugin.checkPermission(sender, "command", true)) {
            return true;
        }

        SessionManager sessionManager = plugin.getSessionManager();
        Player player = (Player) sender;

        Session session = sessionManager.getSession(player.getUniqueId()).orElseGet(() -> {
            player.sendMessage(plugin.tr("session.created"));
            return sessionManager.createSession(player.getUniqueId());
        });

        if (args.length < 1) {
            player.sendMessage(plugin.tr("command.wire.usage"));
            return true;
        }

        String sub = args[0].toLowerCase();
        if (sub.equals("reset")) {
            sessionManager.createSession(player.getUniqueId());
            player.sendMessage(plugin.tr("command.wire.session-reset"));
        } else if (sub.equals("add")) {
            Location blockLocation = player.getLocation().getBlock().getLocation();
            session.addPoint(blockLocation);
            player.sendMessage(plugin.tr("command.wire.point-added", blockLocation.toVector().toString()));
        } else if (sub.equals("finalize")) {
            if (session.getPoints().size() < 2) {
                player.sendMessage(plugin.tr("command.wire.need-more-points"));
                return true;
            }

            session.finalizeSession();
            player.sendMessage(plugin.tr("command.wire.finalized"));
            sessionManager.removeSession(player.getUniqueId());
        }

        return true;
    }
}
