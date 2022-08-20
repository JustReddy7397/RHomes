package ga.justreddy.wiki.rhomes.database;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Database {

  void createDatabase();

  void createHome(String name, Player player, Location location);

  void deleteHome(String name, Player player);

  void setPrivate(String name, Player player, boolean _private);

  boolean doesHomeExist(String name, Player player);

  void teleportToHome(String name, Player player);

  List<Home> getHomes(Player player);

}
