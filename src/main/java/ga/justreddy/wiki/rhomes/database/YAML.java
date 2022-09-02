package ga.justreddy.wiki.rhomes.database;

import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.utils.Cuboid;
import ga.justreddy.wiki.rhomes.utils.Utils;
import java.io.File;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

public class YAML implements Database {

  private final File folder;

  public YAML() {
    folder = new File(RHomes.getHomes().getDataFolder().getAbsolutePath() + "/data/storage");
    if (!folder.exists()) folder.mkdir();
  }

  @SneakyThrows
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
            section.getString("name"),
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

  @Override
  public void createHome(String name, Player player, Location location) {
    if (doesHomeExist(name, player)) {

    }
  }

  @Override
  public void deleteHome(String name, Player player) {

  }

  @Override
  public void setPrivate(String name, Player player, boolean _private) {

  }

  @Override
  public boolean doesHomeExist(String name, OfflinePlayer player) {
    File file = new File(RHomes.getHomes().getDataFolder().getAbsolutePath() + "/data/storage/" + player.getUniqueId().toString() + ".yml");
    return false;
  }

  @Override
  public boolean isPrivate(String name, OfflinePlayer player) {
    if (!doesHomeExist(name, player)) return true;



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

  private boolean getHomeName(String name, OfflinePlayer player) {
    File file = new File(RHomes.getHomes().getDataFolder().getAbsolutePath() + "/data/storage/" + player.getUniqueId().toString() + ".yml");
    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    ConfigurationSection section = config.getConfigurationSection("homes");
    for (String key : section.getKeys(false)) {
      ConfigurationSection configurationSection = section.getConfigurationSection(key);
      if (configurationSection.getString("name").contains(name)) return true;
    }
    return false;
  }

}
