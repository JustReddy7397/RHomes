package ga.justreddy.wiki.rhomes.database;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rhomes.utils.Cuboid;
import ga.justreddy.wiki.rhomes.utils.Utils;
import ga.justreddy.wiki.rhomes.RHomes;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class SQLite implements Database {

  private final Connection connection;

  @SneakyThrows
  public SQLite() {
    File file = new File(RHomes.getHomes().getDataFolder().getAbsolutePath() + "/data/");
    if (!file.exists()) file.mkdirs();
    this.connection =
        DriverManager.getConnection(
            "jdbc:sqlite:"
                + RHomes.getHomes().getDataFolder().getAbsolutePath()
                + "/data/database.db");
  }

  @SneakyThrows
  @Override
  public void loadData() {
    connection
        .createStatement()
        .executeUpdate(
            "CREATE TABLE IF NOT EXISTS r_homes (uuid VARCHAR(100), "
                + "name VARCHAR(100), "
                + "displayname VARCHAR(100),"
                + " location VARCHAR(100),"
                + " private BOOLEAN(100), "
                + "created LONG(100),"
                + "blacklisted LONGTEXT,"
                + "highbound VARCHAR(100),"
                + "lowbound VARCHAR(100))");

    // TODO
    ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM r_homes");

    while (resultSet.next()) {

      String highBound = resultSet.getString("highbound");
      String lowBound = resultSet.getString("lowbound");

      Cuboid cuboid = null;
      if (lowBound != null && highBound != null) {
        Location highBoundLocation = Utils.getLocation(highBound);
        Location lowBoundLocation = Utils.getLocation(lowBound);
        cuboid = new Cuboid(UUID.fromString(resultSet.getString("uuid")), highBoundLocation, lowBoundLocation);
      }

      Home home = new Home(
          resultSet.getString("name"),
          resultSet.getString("displayname"),
          resultSet.getString("uuid"),
          resultSet.getString("location"),
          resultSet.getBoolean("private"),
          resultSet.getLong("created")
      );
      home.setClaimArea(cuboid);
      RHomes.getHomes().getHomeList()
          .add(home);
    }


  }

  @SneakyThrows
  @Override
  public void createHome(String name, Player player, Location location) {
    if (doesHomeExist(name, player)) {
      Utils.sendMessage(
          player,
          RHomes.getHomes().getMessagesConfig().getConfig().getString("error.already-created"));
      return;
    }

    PreparedStatement statement =
        connection.prepareStatement(
            "INSERT INTO r_homes (uuid, name, displayname, location, private, created, blacklisted, highbound, lowbound) VALUES (?,?,?,?,?,?,?,?,?)");
    statement.setString(1, player.getUniqueId().toString());
    statement.setString(2, name);
    statement.setString(3, name);
    statement.setString(4, Utils.toLocation(location));
    statement.setBoolean(5, true);
    statement.setLong(6, new Date().getTime());
    statement.setString(7, "");
    statement.setString(8, "");
    statement.setString(9, "");
    statement.executeUpdate();
    statement.close();
    Utils.sendMessage(
        player,
        RHomes.getHomes()
            .getMessagesConfig()
            .getConfig()
            .getString("homes.created")
            .replaceAll("<name>", name));
    Home home = new Home(
        name,
        name,
        player.getUniqueId().toString(),
        Utils.toLocation(location),
         true,
        new Date().getTime()
    );
    RHomes.getHomes().getHomeList().add(home);
  }

  @SneakyThrows
  @Override
  public void deleteHome(String name, Player player) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(
          player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }

    connection
        .createStatement()
        .executeUpdate(
            "DELETE FROM r_homes WHERE uuid='"
                + player.getUniqueId().toString()
                + "' AND name='"
                + name
                + "'");

    Utils.sendMessage(
        player,
        RHomes.getHomes()
            .getMessagesConfig()
            .getConfig()
            .getString("homes.deleted")
            .replaceAll("<name>", name));
  }

  @SneakyThrows
  @Override
  public void setPrivate(String name, Player player, boolean _private) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(
          player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }

    connection
        .createStatement()
        .executeUpdate("UPDATE r_homes SET private='"+_private+"' WHERE name='"+name+"' AND uuid='"+player.getUniqueId().toString()+"'");

    Utils.sendMessage(
        player,
        RHomes.getHomes()
            .getMessagesConfig()
            .getConfig()
            .getString("homes.private")
            .replaceAll("<private>", (_private ? "private" : "public")));

    Home home =
        RHomes.getHomes().getHomeList().stream()
            .filter(h -> h.getUuid().equals(player.getUniqueId().toString()))
            .filter(h -> h.getName().equals(name))
            .findFirst()
            .get();

    home.setPrivate(_private);
  }

  @SneakyThrows
  @Override
  public boolean doesHomeExist(String name, OfflinePlayer player) {
    ResultSet rs =
        connection
            .createStatement()
            .executeQuery(
                "SELECT * FROM r_homes WHERE uuid='"
                    + player.getUniqueId().toString()
                    + "' AND name='"
                    + name
                    + "'");
    return rs.next();
  }

  @SneakyThrows
  @Override
  public boolean isPrivate(String name, OfflinePlayer player) {
    ResultSet rs =
        connection
            .createStatement()
            .executeQuery(
                "SELECT * FROM r_homes WHERE uuid='"
                    + player.getUniqueId().toString()
                    + "' AND name='"
                    + name
                    + "'");
    return rs.getBoolean("private");
  }

  @SneakyThrows
  @Override
  public void teleportToHome(String name, Player player, OfflinePlayer homeOwner) {
    if (!doesHomeExist(name, homeOwner)) {
      Utils.sendMessage(
          player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }

    ResultSet rs =
        connection
            .createStatement()
            .executeQuery(
                "SELECT * FROM r_homes WHERE uuid='"
                    + homeOwner.getUniqueId().toString()
                    + "' AND name='"
                    + name
                    + "'");


    String[] blackListedUsers = rs.getString("blacklisted").split(";");

    for (String uuid : blackListedUsers) {
      if (uuid.equals(player.getUniqueId().toString())) {
        Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("homes.blacklisted"));
        return;
      }
    }

    if (isPrivate(name, homeOwner) && !rs.getString("uuid").equals(player.getUniqueId().toString())) {
      Utils.sendMessage(player,
          RHomes.getHomes().getMessagesConfig()
              .getConfig()
              .getString("error.private")
      );
      return;
    }
    Location location = Utils.getLocation(rs.getString("location"));

    if (!Utils.isLocationSafe(location)
        && !RHomes.getHomes().getTeleportList().contains(player.getUniqueId())) {
      for (String line :
          RHomes.getHomes().getMessagesConfig().getConfig().getStringList("homes.unsafe")) {
        Utils.sendMessage(
            player,
            line.replaceAll(
                "<violation>",
                String.join(
                    ", ",
                    RHomes.getHomes()
                        .getSettingsConfig()
                        .getConfig()
                        .getStringList("bad-blocks.blocks"))));
      }
      RHomes.getHomes().getTeleportList().add(player.getUniqueId());
      Bukkit.getScheduler()
          .runTaskLaterAsynchronously(
              RHomes.getHomes(),
              () -> RHomes.getHomes().getTeleportList().remove(player.getUniqueId()),
              200);
      return;
    }

    player.teleport(location);
    RHomes.getHomes().getTeleportList().remove(player.getUniqueId());
  }

  @SneakyThrows
  @Override
  public void addPlayerToBlackList(String name, Player player, OfflinePlayer target) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(
          player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }

    ResultSet rs =
        connection
            .createStatement()
            .executeQuery(
                "SELECT * FROM r_homes WHERE uuid='"
                    + player.getUniqueId().toString()
                    + "' AND name='"
                    + name
                    + "'");
    String blacklisted = rs.getString("blacklisted");

    blacklisted = blacklisted + target.getUniqueId().toString() + ";";

    connection
        .createStatement()
        .executeUpdate(
            "UPDATE r_homes SET blacklisted='"
                + blacklisted
                + "' WHERE name='"
                + name
                + "' AND uuid='"
                + player.getUniqueId().toString()
                + "'");
    Utils.sendMessage(
        player,
        RHomes.getHomes()
            .getMessagesConfig()
            .getConfig()
            .getString("homes.blacklist-add")
            .replaceAll("<player>", target.getName()));
  }

  @SneakyThrows
  @Override
  public void removePlayerFromBlackList(String name, Player player, OfflinePlayer target) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(
          player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }

    ResultSet rs =
        connection
            .createStatement()
            .executeQuery(
                "SELECT * FROM r_homes WHERE uuid='"
                    + player.getUniqueId().toString()
                    + "' AND name='"
                    + name
                    + "'");
    String blacklisted = rs.getString("blacklisted");

    blacklisted = blacklisted.replaceAll(target.getUniqueId().toString() + ";", "");

    connection
        .createStatement()
        .executeUpdate(
            "UPDATE r_homes SET blacklisted='"
                + blacklisted
                + "' WHERE name='"
                + name
                + "' AND uuid='"
                + player.getUniqueId().toString()
                + "'");
    Utils.sendMessage(
        player,
        RHomes.getHomes()
            .getMessagesConfig()
            .getConfig()
            .getString("homes.blacklist-remove")
            .replaceAll("<player>", target.getName()));
  }

  @SneakyThrows
  @Override
  public void renameHome(String name, Player player, String newName) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(
          player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }

    connection
        .createStatement()
        .executeUpdate(
            "UPDATE r_homes SET name='"
                + newName
                + "' WHERE name='"
                + name
                + "' AND uuid='"
                + player.getUniqueId().toString()
                + "'");
    Utils.sendMessage(
        player, RHomes.getHomes().getMessagesConfig().getConfig().getString("homes.renamed").replaceAll("<name>", newName));
    Home home =
        RHomes.getHomes().getHomeList().stream()
            .filter(h -> h.getUuid().equals(player.getUniqueId().toString()) && h.getName().equals(name))
            .findFirst().get();

    home.setName(newName);
  }

  @SneakyThrows
  @Override
  public void setDisplayName(String name, Player player, String newDisplayName) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(
          player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }

    connection
        .createStatement()
        .executeUpdate(
            "UPDATE r_homes SET displayname='"
                + newDisplayName
                + "' WHERE name='"
                + name
                + "' AND uuid='"
                + player.getUniqueId().toString()
                + "'");

    Utils.sendMessage(
        player, RHomes.getHomes().getMessagesConfig().getConfig().getString("homes.renamed-displayname").replaceAll("<displayname>", newDisplayName));
    Home home =
        RHomes.getHomes().getHomeList().stream()
            .filter(h -> h.getUuid().equals(player.getUniqueId().toString()) && h.getName().equals(name))
            .findFirst().get();
    home.setDisplayName(newDisplayName);
  }

  @SneakyThrows
  @Override
  public void setHighBound(String name, Player player, String highBound) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(
          player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }

    connection.createStatement().executeUpdate(
        "UPDATE r_homes SET highbound='"+highBound+"' WHERE name='"+name+"' AND uuid='"+player.getUniqueId().toString()+"'"
    );

    player.sendMessage("Highbound set");
  }

  @SneakyThrows
  @Override
  public void setLowBound(String name, Player player, String lowBound) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(
          player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }

    connection.createStatement().executeUpdate(
        "UPDATE r_homes SET lowbound='"+lowBound+"' WHERE name='"+name+"' AND uuid='"+player.getUniqueId().toString()+"'"
    );

    player.sendMessage("Lowbound set");
  }

  @SneakyThrows
  @Override
  public void setClaimArea(String name, Player player) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(
          player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }
    ResultSet rs =
        connection
            .createStatement()
            .executeQuery(
                "SELECT * FROM r_homes WHERE uuid='"
                    + player.getUniqueId().toString()
                    + "' AND name='"
                    + name
                    + "'");

    Location highBound = Utils.getLocation(rs.getString("highbound"));
    Location lowBound = Utils.getLocation(rs.getString("lowbound"));
    Cuboid cuboid = new Cuboid(player.getUniqueId(), highBound, lowBound);
    Home home = getHomeByName(name, player);
    home.setClaimArea(cuboid);
    System.out.println("Cuboid set");
  }

  @Override
  public Home getHomeByName(String name, Player player) {
    return getHomes(player).stream().filter(home1 -> home1.getName().equals(name)).findFirst().orElse(null);
  }

  @SneakyThrows
  @Override
  public List<Home> getHomes(Player player) {
    List<Home> list = new ArrayList<>();
    ResultSet rs =
        connection
            .createStatement()
            .executeQuery(
                "SELECT * FROM r_homes WHERE uuid='" + player.getUniqueId().toString() + "'");
    while (rs.next()) {
      String highBound = rs.getString("highbound");
      String lowBound = rs.getString("lowbound");

      Cuboid cuboid = null;
      if (lowBound != null && highBound != null) {
        Location highBoundLocation = Utils.getLocation(highBound);
        Location lowBoundLocation = Utils.getLocation(lowBound);
        cuboid = new Cuboid(UUID.fromString(rs.getString("uuid")), highBoundLocation, lowBoundLocation);
      }
      Home home = new Home(
          rs.getString("name"),
          rs.getString("displayname"),
          rs.getString("uuid"),
          rs.getString("location"),
          rs.getBoolean("private"),
          rs.getLong("created"));
      home.setClaimArea(cuboid);
      list.add(
          home
      );
    }
    return list;
  }

  @SneakyThrows
  @Override
  public List<UUID> getBlacklisted(String name, Player player) {
    List<UUID> list = new ArrayList<>();
    ResultSet rs =
        connection
            .createStatement()
            .executeQuery(
                "SELECT * FROM r_homes WHERE uuid='"
                    + player.getUniqueId().toString()
                    + "' AND name='"
                    + name
                    + "'");
    if (!rs.next()) return new ArrayList<>();
    String[] split = rs.getString("blacklisted").split(";");

    for (String stringUuid : split) {
      stringUuid = stringUuid.replaceAll(";", "");
      UUID uuid = UUID.fromString(stringUuid);
      list.add(uuid);
    }
    return list;
  }
}
