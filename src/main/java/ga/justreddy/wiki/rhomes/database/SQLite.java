package ga.justreddy.wiki.rhomes.database;

import ga.justreddy.wiki.rhomes.utils.Utils;
import ga.justreddy.wiki.rhomes.RHomes;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SQLite implements Database {

  private final Connection connection;

  @SneakyThrows
  public SQLite() {
    File file = new File(RHomes.getHomes().getDataFolder().getAbsolutePath() + "/data/");
    if (!file.exists()) file.mkdirs();
    this.connection = DriverManager.getConnection("jdbc:sqlite:" + RHomes.getHomes().getDataFolder().getAbsolutePath() + "/data/database.db");
  }

  @SneakyThrows
  @Override
  public void createDatabase() {
    connection.createStatement().executeUpdate(
        "CREATE TABLE IF NOT EXISTS r_homes (uuid VARCHAR(100), name VARCHAR(100), displayname VARCHAR(100), location VARCHAR(100), private BOOLEAN(100), created LONG(100))"
    );
  }

  @SneakyThrows
  @Override
  public void createHome(String name, Player player, Location location) {
    if (doesHomeExist(name, player)) {
      Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.already-created"));
      return;
    }

    PreparedStatement statement = connection.prepareStatement("INSERT INTO r_homes (uuid, name, displayname, location, private, created) VALUES (?,?,?,?,?,?)");
    statement.setString(1, player.getUniqueId().toString());
    statement.setString(2, name);
    statement.setString(3, name);
    statement.setString(4, Utils.toLocation(location));
    statement.setBoolean(5, false);
    statement.setLong(6, new Date().getTime());
    statement.executeUpdate();
    statement.close();
    Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("homes.created").replaceAll("<name>", name));
  }

  @SneakyThrows
  @Override
  public void deleteHome(String name, Player player) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }

    connection.createStatement().executeUpdate("DELETE FROM r_homes WHERE uuid='"+player.getUniqueId().toString()+"' AND name='"+name+"'");

    Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("homes.deleted").replaceAll("<name>", name));

  }

  @Override
  public void setPrivate(String name, Player player, boolean _private) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }
  }

  @SneakyThrows
  @Override
  public boolean doesHomeExist(String name, Player player) {
    ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM r_homes WHERE uuid='"+player.getUniqueId().toString()+"' AND name='"+name+"'");
    return rs.next();
  }

  @SneakyThrows
  @Override
  public void teleportToHome(String name, Player player) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }

    ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM r_homes WHERE uuid='"+player.getUniqueId().toString()+"' AND name='"+name+"'");

    Location location = Utils.getLocation(rs.getString("location"));

    if(!Utils.isLocationSafe(location) && !RHomes.getHomes().getTeleportList().contains(player.getUniqueId())) {
      for (String line : RHomes.getHomes().getMessagesConfig().getConfig().getStringList("homes.unsafe")) {
        Utils.sendMessage(player, line.replaceAll("<violation>",
            String.join(", ", RHomes.getHomes().getSettingsConfig().getConfig().getStringList("bad-blocks.blocks"))));
      }
      RHomes.getHomes().getTeleportList().add(player.getUniqueId());
      Bukkit.getScheduler().runTaskLaterAsynchronously(RHomes.getHomes(), () -> RHomes.getHomes().getTeleportList().remove(player.getUniqueId()), 200);
      return;
    }

    player.teleport(location);
    RHomes.getHomes().getTeleportList().remove(player.getUniqueId());

  }

  @SneakyThrows
  @Override
  public List<Home> getHomes(Player player) {
    List<Home> list = new ArrayList<>();
    ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM r_homes WHERE uuid='"+player.getUniqueId().toString()+"'");
    while (rs.next()) {
      list.add(
          new Home(rs.getString("name"),
              rs.getString("displayname"),
              rs.getString("uuid"),
              rs.getString("location"),
              rs.getBoolean("private"),
              rs.getLong("created"))
      );
    }
    return list;
  }
}