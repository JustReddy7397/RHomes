package ga.justreddy.wiki.rhomes.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ga.justreddy.wiki.rhomes.utils.Utils;
import ga.justreddy.wiki.rhomes.RHomes;
import java.util.List;
import java.util.UUID;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MongoDB implements Database {

  private MongoCollection<Document> collection;

  public MongoDB(String URI) {
    MongoClient client = MongoClients.create(URI);
    MongoDatabase database = client.getDatabase("rhomes");
    collection = database.getCollection("homes");
  }

  @Override
  public void loadData() {
    // Empty Block
  }

  @Override
  public void createHome(String name, Player player, Location location) {
    if (doesHomeExist(name, player)) {
      Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.already-created"));
      return;
    }
    Document document = new Document("name", name)
        .append("displayname", name)
        .append("uuid", player.getUniqueId().toString())
        .append("location", Utils.toLocation(location))
        .append("private", false);
    collection.insertOne(document);
    Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("homes.created").replaceAll("<name>", name));
  }

  @Override
  public void deleteHome(String name, Player player) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }
  }

  @Override
  public void setPrivate(String name, Player player, boolean _private) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }
  }

  @Override
  public boolean doesHomeExist(String name, Player player) {
    Document document = collection.find(new Document("uuid", name).append("uuid", player.getUniqueId().toString())).first();
    return document != null;
  }

  @Override
  public void teleportToHome(String name, Player player) {
    if (!doesHomeExist(name, player)) {
      Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.not-exists"));
      return;
    }

    Document document = collection.find(new Document("uuid", name).append("uuid", player.getUniqueId().toString())).first();

    Location location = Utils.getLocation(document.getString("location"));

    // TODO: region check

    player.teleport(location);
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
