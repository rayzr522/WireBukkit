package me.rayzr522.wirebukkit.managers;

import me.rayzr522.wirebukkit.WireBukkit;
import me.rayzr522.wirebukkit.sessions.Session;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SessionManager implements Listener {
    private Map<UUID, Session> sessionMap = new LinkedHashMap<>();

    public SessionManager(WireBukkit plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public Optional<Session> getSession(UUID player) {
        return Optional.ofNullable(sessionMap.get(player));
    }

    public Session createSession(UUID player) {
        return sessionMap.computeIfAbsent(player, Session::new);
    }

    public void removeSession(UUID player) {
        sessionMap.remove(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        removeSession(e.getPlayer().getUniqueId());
    }
}
