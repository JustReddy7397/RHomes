package ga.justreddy.wiki.rhomes.database;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MySQL implements Database {

  @Override
  public void createDatabase() {

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
  public List<Home> getHomes(Player player) {
    return null;
  }
}
