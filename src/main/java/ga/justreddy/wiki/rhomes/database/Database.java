package ga.justreddy.wiki.rhomes.database;

import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface Database {

  void loadData();

  void createHome(String name, Player player, Location location);

  void deleteHome(String name, Player player);

  void setPrivate(String name, Player player, boolean _private);

  boolean doesHomeExist(String name, Player player);

  void teleportToHome(String name, Player player);

  void addPlayerToBlackList(String name, Player player, OfflinePlayer target);

  void removePlayerFromBlackList(String name, Player player, OfflinePlayer target);

  void renameHome(String name, Player player, String newName);

  void setDisplayName(String name, Player player, String newDisplayName);

  List<Home> getHomes(Player player);

  List<UUID> getBlacklisted(String name, Player player);

}
