package ga.justreddy.wiki.rhomes.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/** Used for protection */
@Getter
public class Cuboid {

  private final UUID owner;
  private final Location highPoints;
  private final Location lowPoints;
  private final World world;
  private final int x1;
  private final int y1;
  private final int z1;
  private final int x2;
  private final int y2;
  private final int z2;
  private final Set<Chunk> chunks;

  public Cuboid(UUID owner, Location highPoints, Location lowPoints) {
    this.owner = owner;
    this.world = highPoints.getWorld();
    this.x1 = Math.min(highPoints.getBlockX(), lowPoints.getBlockX());
    this.y1 = Math.min(highPoints.getBlockY(), lowPoints.getBlockY());
    this.z1 = Math.min(highPoints.getBlockZ(), lowPoints.getBlockZ());
    this.x2 = Math.max(highPoints.getBlockX(), lowPoints.getBlockX());
    this.y2 = Math.max(highPoints.getBlockY(), lowPoints.getBlockY());
    this.z2 = Math.max(highPoints.getBlockZ(), lowPoints.getBlockZ());
    this.highPoints = new Location(this.world, this.x2, this.y2, this.z2);
    this.lowPoints = new Location(this.world, this.x1, this.y1, this.z1);
    this.chunks = new HashSet<>();
    for (int x = x1; x < x2; x++) {
      for (int y = y1; y < y2; y++) {
        for (int z = z1; z < z2; z++) {
          Chunk chunk = world.getChunkAt(x, z);
          if (!chunk.isLoaded()) chunk.load();
          chunks.add(chunk);
        }
      }
    }
  }

  public boolean contains(Location location) {
    return location.getWorld() == world
        && location.getBlockX() >= x1
        && location.getBlockX() <= x2
        && location.getBlockY() <= y2
        && location.getBlockZ() >= z1
        && location.getBlockZ() <= z2;
  }

  public boolean isInChunk(Location location) {
    Chunk c = chunks.stream().filter(chunk -> chunk.getX() == location.getX() && location.getZ() == location.getZ()).findFirst().orElse(null);
    return c != null;
  }

  public boolean isOwner(UUID uuid) {
    return owner.equals(uuid);
  }

  public boolean test() {
    if (highPoints.distance(lowPoints) > 5) {

      return true;
    }
    return false;
  }

  /**
   * This method is used to clear everything in the cuboid <br>
   * This is super dangerous and should only be accessed by the administrators <br>
   * You can NOT reverse this action
   */
  public void clear() {
    for (int x = x1; x < x2; x++) {
      for (int y = y1; y < y2; y++) {
        for (int z = z1; z < z2; z++) {
          Block block = this.world.getBlockAt(x, y, z);
          if (block.getType() != Material.AIR) {
            block.setType(Material.AIR);
          }
        }
      }
    }
  }
}
