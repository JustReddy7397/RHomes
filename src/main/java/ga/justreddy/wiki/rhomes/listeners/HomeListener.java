package ga.justreddy.wiki.rhomes.listeners;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.database.Home;
import ga.justreddy.wiki.rhomes.utils.Utils;
import java.util.Locale;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


// Setting this off for now.
public class HomeListener implements Listener {

  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    Player player = e.getPlayer();
    if (!RHomes.getHomes().getSettingsConfig().getConfig().getBoolean("home-protection.enabled"))
      return;
    for (Home h : RHomes.getHomes().getDatabase().getHomes(player)) {
      if (!h.getUuid().equals(player.getUniqueId().toString())
          && h.getClaimArea().contains(player.getLocation())) {
        e.setCancelled(true);
        Utils.sendMessage(
            player,
            RHomes.getHomes()
                .getMessagesConfig()
                .getConfig()
                .getString("protection.block-break")
                .replaceAll(
                    "<player>", Bukkit.getOfflinePlayer(UUID.fromString(h.getUuid())).getName()));
      }
    }
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent e) {
    Player player = e.getPlayer();
    if (!RHomes.getHomes().getSettingsConfig().getConfig().getBoolean("home-protection.enabled"))
      return;
    for (Home h : RHomes.getHomes().getDatabase().getHomes(player)) {
      if (!h.getUuid().equals(player.getUniqueId().toString())
          && h.getClaimArea().contains(player.getLocation())) {
        e.setCancelled(true);
        Utils.sendMessage(
            player,
            RHomes.getHomes()
                .getMessagesConfig()
                .getConfig()
                .getString("protection.block-place")
                .replaceAll(
                    "<player>", Bukkit.getOfflinePlayer(UUID.fromString(h.getUuid())).getName()));
      }
    }
  }

  @EventHandler
  public void onBlockInteract(PlayerInteractEvent e) {
    if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

    Player player = e.getPlayer();
    if (!RHomes.getHomes().getSettingsConfig().getConfig().getBoolean("home-protection.enabled"))
      return;
    for (Home h : RHomes.getHomes().getDatabase().getHomes(player)) {
      Location location = player.getLocation();
      if (h.getClaimArea() == null) continue;
      if (h.getClaimArea().isOwner(player.getUniqueId())) continue;
      if (!h.getClaimArea().contains(location)) continue;
      e.setCancelled(true);
      Utils.sendMessage(
          player,
          RHomes.getHomes()
              .getMessagesConfig()
              .getConfig()
              .getString("protection.interact")
              .replaceAll(
                  "<player>", Bukkit.getOfflinePlayer(UUID.fromString(h.getUuid())).getName())
              .replaceAll("<interaction>", e.getClickedBlock().getType().name().toUpperCase()));
    }
  }

}
