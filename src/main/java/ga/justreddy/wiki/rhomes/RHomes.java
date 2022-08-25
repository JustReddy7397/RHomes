package ga.justreddy.wiki.rhomes;

import ga.justreddy.wiki.rhomes.command.BaseCommand;
import ga.justreddy.wiki.rhomes.config.YamlConfig;
import ga.justreddy.wiki.rhomes.database.Database;
import ga.justreddy.wiki.rhomes.database.Home;
import ga.justreddy.wiki.rhomes.database.MongoDB;
import ga.justreddy.wiki.rhomes.database.MySQL;
import ga.justreddy.wiki.rhomes.database.SQLite;
import ga.justreddy.wiki.rhomes.database.YAML;
import ga.justreddy.wiki.rhomes.dependency.DLoader;
import ga.justreddy.wiki.rhomes.dependency.base.Dependency;
import ga.justreddy.wiki.rhomes.listeners.HomeListener;
import ga.justreddy.wiki.rhomes.menus.MenuEvent;
import ga.justreddy.wiki.rhomes.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

public final class RHomes extends JavaPlugin {

  @Getter
  private YamlConfig databaseConfig;
  @Getter
  private YamlConfig messagesConfig;
  @Getter
  private YamlConfig settingsConfig;

  @Getter
  private Database database;

  @Getter
  private List<UUID> teleportList;

  @Getter
  private List<Home> homeList;

  @Override
  public void onLoad() {
    try{
      DLoader.getInstance().onLoad();
      DLoader.getInstance().load(new Dependency("SQLite", "3.34.0", "org.xerial", "sqlite-jdbc"));
      DLoader.getInstance().load(new Dependency("MongoDB-Driver", "3.12.11", "org.mongodb", "mongodb-driver"));
      DLoader.getInstance().load(new Dependency("MongoDB-Core", "3.12.11", "org.mongodb", "mongodb-driver-core"));
      DLoader.getInstance().load(new Dependency("MongoDB-Bson", "3.12.11", "org.mongodb", "bson"));
    }catch (ClassCastException | ExceptionInInitializerError ignored) {

    }
  }

  @Override
  public void onEnable() {
    if (!loadConfigs()) return;
    this.homeList = new ArrayList<>();
    if (databaseConfig.getConfig().getString("storage").equalsIgnoreCase("sql")) {
      database = new SQLite();
    } else if (databaseConfig.getConfig().getString("storage").equalsIgnoreCase("mysql")) {
      database = new MySQL();
    } else if (databaseConfig.getConfig().getString("storage").equalsIgnoreCase("mongodb")) {
      database = new MongoDB(databaseConfig.getConfig().getString("mongo.uri"));
      } else if (databaseConfig.getConfig().getString("storage").equalsIgnoreCase("yaml")) {
      database = new YAML();
    } else {
      database = new SQLite();
    }
    database.loadData();
    getCommand("homes").setExecutor(new BaseCommand());
    getCommand("homes").setTabCompleter(new BaseCommand());
    this.teleportList = new ArrayList<>();;
    getServer().getPluginManager().registerEvents(new MenuEvent(), this);
    getServer().getPluginManager().registerEvents(new HomeListener(), this);
  }

  @Override
  public void onDisable() {
    teleportList.clear();
    homeList.clear();
  }

  public static RHomes getHomes() {
    return JavaPlugin.getPlugin(RHomes.class);
  }

  private boolean loadConfigs() {

    String config = "Configuration File";

    try{

      config = "database.yml";
      databaseConfig = new YamlConfig(config);
      config = "messages.yml";
      messagesConfig = new YamlConfig(config);
      config = "settings.yml";
      settingsConfig = new YamlConfig(config);

    }catch (InvalidConfigurationException | IOException ex) {
      Utils.error(ex, "An error has occurred while loading the " + config, true);
    }

    return true;
  }

}
