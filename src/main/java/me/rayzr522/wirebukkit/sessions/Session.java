package me.rayzr522.wirebukkit.sessions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Session {
    private final UUID id;

    private List<Location> points = new ArrayList<>();

    public Session(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public boolean addPoint(Location point) {
        return points.add(point);
    }

    public List<Location> getPoints() {
        return points;
    }

    public void finalizeSession() {
        if (points.size() < 2) {
            throw new IllegalStateException("There must be at least 2 points in the session to finalize!");
        }

        Location last, next = points.get(0);

        for (int i = 1; i < points.size(); i++) {
            last = next.clone();
            next = points.get(i);

            Vector distance = next.toVector().subtract(last.toVector());
            Vector direction = distance.clone().normalize();
            double mag = distance.lengthSquared();

            last.subtract(direction);
            for (double d = 0.0; d * d < mag; d++) {
                last.add(direction);
                last.getBlock().setType(Material.DIAMOND_BLOCK);
                last.getBlock().getRelative(BlockFace.UP).setType(Material.REDSTONE_WIRE);
            }
        }
    }
}
