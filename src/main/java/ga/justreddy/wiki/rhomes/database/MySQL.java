package ga.justreddy.wiki.rhomes.database;

import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MySQL implements Database {

  @Override
  public void loadData() {

  }

  @Override
  public void createHome(String name, Player player, Location location) {

  }

  @Override
  public void deleteHome(String name, Player player) {

  }

  @Override
  public void setPrivate(String name, Player player, boolean _private) {

  }

  @Override
  public boolean doesHomeExist(String name, Player player) {
    return false;
  }

  @Override
  public void teleportToHome(String name, Player player) {

  }

  @Override
  public void addPlayerToBlackList(String name, Player player, OfflinePlayer target) {

  }

  @Override
  public void removePlayerFromBlackList(String name, Player player, OfflinePlayer target) {

  }

  @Override
  public void renameHome(String name, Player player, String newName) {

  }

  @Override
  public void setDisplayName(String name, Player player, String newDisplayName) {

  }

  @Override
  public List<Home> getHomes(Player player) {
    return null;
  }

  @Override
  public List<UUID> getBlacklisted(String name, Player player) {
    return null;
  }
}
