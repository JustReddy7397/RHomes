package ga.justreddy.wiki.rhomes.database;

import ga.justreddy.wiki.rhomes.RHomes;
import java.io.File;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

public class YAML implements Database {

  private final File file;

  public YAML() {
    file = new File(RHomes.getHomes().getDataFolder().getAbsolutePath() + "/data/storage");
    if (!file.exists()) file.mkdir();
  }

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
  public boolean doesHomeExist(String name, OfflinePlayer player) {
    return false;
  }

  @Override
  public boolean isPrivate(String name, OfflinePlayer player) {
    return false;
  }

  @Override
  public void teleportToHome(String name, Player player, OfflinePlayer homeOwner) {

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
  public void setHighBound(String name, Player player, String highBound) {

  }

  @Override
  public void setLowBound(String name, Player player, String lowBound) {

  }

  @Override
  public void setClaimArea(String name, Player player) {

  }

  @Override
  public Home getHomeByName(String name, Player player) {
    return null;
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
