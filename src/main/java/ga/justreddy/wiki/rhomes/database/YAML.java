package ga.justreddy.wiki.rhomes.database;

import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.utils.Cuboid;
import ga.justreddy.wiki.rhomes.utils.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class YAML implements Database {

  private final File folder;

  public YAML() {
    folder = new File(RHomes.getHomes().getDataFolder().getAbsolutePath() + "/data/storage");
    if (!folder.exists()) folder.mkdir();
  }

  @Override
  public void loadData() {
    File[] files = folder.listFiles();
    if (files == null) return;
    for (File file : files) {
      if (!file.getName().endsWith(".yml")) continue;
      String uuid = file.getName().replace(".yml", "");
      FileConfiguration config = YamlConfiguration.loadConfiguration(file);
/*      config.set("name", Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());
      config.save(file);*/
      ConfigurationSection configurationSection = config.getConfigurationSection("homes");
        for (String key : configurationSection.getKeys(false)) {
          ConfigurationSection section = configurationSection.getConfigurationSection(key);
          String highBound = section.getString("highbound");
          String lowBound = section.getString("lowbound");

          Cuboid cuboid = null;

          if (highBound != null && lowBound != null) {
            Location highBoundLocation = Utils.getLocation(highBound);
            Location lowBoundLocation = Utils.getLocation(lowBound);
            cuboid = new Cuboid(UUID.fromString(uuid), highBoundLocation, lowBoundLocation);
          }

          Home home = new Home(
              key,
              section.getString("displayname"),
              uuid,
              section.getString("location"),
              section.getBoolean("private"),
              section.getLong("created")
          );

          home.setClaimArea(cuboid);
          RHomes.getHomes().getHomeList().add(home);
        }

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

    String stringLocation = Utils.toLocation(location);

    FileConfiguration config = getConfigByFile(player.getUniqueId().toString());
    config.set("homes." + name + ".name", name);
    config.set("homes." + name + ".displayname", name);
    config.set("homes." + name + ".location", stringLocation);
    config.set("homes." + name + ".private", true);
    config.set("homes." + name + ".created", new Date().getTime());
    config.set("homes." + name + ".blacklisted", new ArrayList<>());
    config.set("homes." + name + ".highbound", null);
    config.set("homes." + name + ".lowbound", null);
    config.save(getHomeByFile(player.getUniqueId().toString()));
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

    FileConfiguration config = getConfigByFile(player.getUniqueId().toString());
    config.set("homes." + name, null);
    config.save(getHomeByFile(player.getUniqueId().toString()));
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

    FileConfiguration config = getConfigByFile(player.getUniqueId().toString());
    config.set("homes." + name + ".private", _private);
    config.save(getHomeByFile(player.getUniqueId().toString()));
    Home home =
        RHomes.getHomes().getHomeList().stream()
            .filter(h -> h.getUuid().equals(player.getUniqueId().toString()))
            .filter(h -> h.getName().equals(name))
            .findFirst()
            .get();
    home.setPrivate(_private);
  }

  @Override
  public boolean doesHomeExist(String name, OfflinePlayer player) {
    return RHomes.getHomes().getHomeList().stream().filter(home -> home.getUuid().equals(player.getUniqueId().toString()))
        .anyMatch(home -> home.getName().equals(name));
  }

  @Override
  public boolean isPrivate(String name, OfflinePlayer player) {
    if (!doesHomeExist(name, player)) return true;
    return RHomes.getHomes().getHomeList().stream().filter(home -> home.getName().equals(name) && home.getUuid().equals(player.getUniqueId().toString())).anyMatch(Home::isPrivate);
  }

  @Override
  public void teleportToHome(String name, Player player, OfflinePlayer homeOwner) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(
          player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }

    FileConfiguration config = getConfigByFile(homeOwner.getUniqueId().toString());
    Location location = Utils.getLocation(config.getString("homes." + name + ".location"));

    List<String> blackListedUsers = config.getStringList("blacklisted");

    if (blackListedUsers.contains(player.getUniqueId().toString())) {
      Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("homes.blacklisted"));
      return;
    }


    if (isPrivate(name, homeOwner) && !getHomeByFile(player.getUniqueId().toString()).getName().replace(".yml", "").equals(player.getUniqueId().toString())) {
      Utils.sendMessage(player,
          RHomes.getHomes().getMessagesConfig()
              .getConfig()
              .getString("error.private")
      );
      return;
    }

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

    FileConfiguration config = getConfigByFile(player.getUniqueId().toString());
    List<String> blackListedUsers = config.getStringList("homes." + name + ".blacklisted");
    if (blackListedUsers.contains(target.getUniqueId().toString())) {
      Utils.sendMessage(player,
          RHomes.getHomes()
              .getMessagesConfig()
              .getConfig()
              .getString("homes.already-blacklisted")
              .replaceAll("<player>", target.getName())
      );
      return;
    }

    blackListedUsers.add(target.getUniqueId().toString());
    config.set("homes." + name + ".blacklised", blackListedUsers);
    config.save(getHomeByFile(player.getUniqueId().toString()));
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

    FileConfiguration config = getConfigByFile(player.getUniqueId().toString());
    List<String> blackListedUsers = config.getStringList("homes." + name + ".blacklisted");
    if (!blackListedUsers.contains(target.getUniqueId().toString())) {
      Utils.sendMessage(player,
          RHomes.getHomes()
              .getMessagesConfig()
              .getConfig()
              .getString("homes.not-blacklisted")
              .replaceAll("<player>", target.getName())
      );
      return;
    }

    blackListedUsers.remove(target.getUniqueId().toString());
    config.set("homes." + name + ".blacklised", blackListedUsers);
    config.save(getHomeByFile(player.getUniqueId().toString()));
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
    FileConfiguration configuration = getConfigByFile(player.getUniqueId().toString());
      for (Map.Entry<String, Object> entry : configuration.getConfigurationSection("homes." + name).getValues(true).entrySet()) {
        configuration.set("homes." + newName + "." + entry.getKey(), entry.getValue());
    }
    configuration.set("homes." + name, null);
    configuration.save(getHomeByFile(player.getUniqueId().toString()));
    Utils.sendMessage(
        player, RHomes.getHomes().getMessagesConfig().getConfig().getString("homes.renamed").replaceAll("<name>", newName));
    Home home =
        RHomes.getHomes().getHomeList().stream()
            .filter(h -> h.getUuid().equals(player.getUniqueId().toString()) && h.getName().equals(name))
                .findFirst().get();
    System.out.println(home);
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
    FileConfiguration configuration = getConfigByFile(player.getUniqueId().toString());
    configuration.set("homes." + name + ".displayname", newDisplayName);
    configuration.save(getHomeByFile(player.getUniqueId().toString()));
    Utils.sendMessage(
        player, RHomes.getHomes().getMessagesConfig().getConfig().getString("homes.renamed-displayname").replaceAll("<displayname>", newDisplayName));
    Home home =
        RHomes.getHomes().getHomeList().stream()
            .filter(h -> h.getUuid().equals(player.getUniqueId().toString()) && h.getName().equals(name))
            .findFirst().get();
    System.out.println(home);
    home.setDisplayName(newDisplayName);
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
    return RHomes.getHomes().getHomeList().stream()
        .filter(home -> home.getUuid().equals(player.getUniqueId().toString()) && home.getName().equals(name)).findFirst().orElse(null);
  }

  @Override
  public List<Home> getHomes(Player player) {
    return RHomes.getHomes().getHomeList()
        .stream().filter(home -> home.getUuid().equals(player.getUniqueId().toString())).collect(
            Collectors.toList());
  }

  @Override
  public List<UUID> getBlacklisted(String name, Player player) {
    List<UUID> blacklisted = new ArrayList<>();
    FileConfiguration configuration = getConfigByFile(player.getUniqueId().toString());
    for (String uuid : configuration.getStringList("homes." + name + ".blacklisted")) {
      blacklisted.add(UUID.fromString(uuid));
    }
    return blacklisted;
  }

  private File getHomeByFile(String uuid) {
    return new File(RHomes.getHomes().getDataFolder().getAbsolutePath() + "/data/storage/" + uuid + ".yml");
  }

  private FileConfiguration getConfigByFile(String uuid) {
    return YamlConfiguration.loadConfiguration(getHomeByFile(uuid));
  }

}
