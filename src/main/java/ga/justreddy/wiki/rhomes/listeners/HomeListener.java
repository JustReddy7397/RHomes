package ga.justreddy.wiki.rhomes.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.Queues;
import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.database.Home;
import ga.justreddy.wiki.rhomes.utils.Cuboid;
import ga.justreddy.wiki.rhomes.utils.Utils;
import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

// Setting this off for now.
public class HomeListener implements Listener {

  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    Player player = e.getPlayer();
    if (!RHomes.getHomes().getSettingsConfig().getConfig().getBoolean("home-protection.enabled"))
      return;
    for (Home home : RHomes.getHomes().getHomeList()) {
      UUID owner = UUID.fromString(home.getUuid());
      if (home.getClaimArea() == null) continue;
      if (owner.equals(player.getUniqueId())) continue;
      if (!home.getClaimArea().contains(e.getBlock().getLocation())) continue;
      e.setCancelled(true);
      Utils.sendMessage(
          player,
          RHomes.getHomes()
              .getMessagesConfig()
              .getConfig()
              .getString("protection.block-break")
              .replaceAll(
                  "<player>", Bukkit.getOfflinePlayer(UUID.fromString(home.getUuid())).getName()));
    }
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent e) {
    Player player = e.getPlayer();
    if (!RHomes.getHomes().getSettingsConfig().getConfig().getBoolean("home-protection.enabled"))
      return;
    for (Home home : RHomes.getHomes().getHomeList()) {
      UUID owner = UUID.fromString(home.getUuid());
      if (home.getClaimArea() == null) continue;
      if (owner.equals(player.getUniqueId())) continue;
      if (!home.getClaimArea().contains(e.getBlockPlaced().getLocation())) continue;
      e.setCancelled(true);
      Utils.sendMessage(
          player,
          RHomes.getHomes()
              .getMessagesConfig()
              .getConfig()
              .getString("protection.block-place")
              .replaceAll(
                  "<player>", Bukkit.getOfflinePlayer(UUID.fromString(home.getUuid())).getName()));
    }
  }

  @EventHandler
  public void onBlockInteract(PlayerInteractEvent e) {
    if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
    Player player = e.getPlayer();
    if (!RHomes.getHomes().getSettingsConfig().getConfig().getBoolean("home-protection.enabled"))
      return;
    for (Home home : RHomes.getHomes().getHomeList()) {
      UUID owner = UUID.fromString(home.getUuid());
      if (home.getClaimArea() == null) continue;
      if (owner.equals(player.getUniqueId())) continue;
      if (!home.getClaimArea().contains(e.getClickedBlock().getLocation())) continue;
      if (!RHomes.getHomes()
          .getSettingsConfig()
          .getConfig()
          .getStringList("home-protection.interaction-blocks")
          .contains(
              XMaterial.matchXMaterial(e.getClickedBlock().getType().name().toUpperCase())
                  .get()
                  .name()
                  .toUpperCase())) continue;
      e.setCancelled(true);
      Utils.sendMessage(
          player,
          RHomes.getHomes()
              .getMessagesConfig()
              .getConfig()
              .getString("protection.interact")
              .replaceAll("<player>", Bukkit.getOfflinePlayer(owner).getName())
              .replaceAll("<interaction>", e.getClickedBlock().getType().name().toUpperCase()));
    }
  }

  @EventHandler
  public void onEntityDamage(EntityDamageEvent e) {

    if (!RHomes.getHomes().getSettingsConfig().getConfig().getBoolean("home-protection.enabled") || !RHomes.getHomes().getSettingsConfig().getConfig().getBoolean("home-protection.pvp.enabled"))
      return;

    if (e.getCause() != DamageCause.ENTITY_ATTACK) return;

    if (!(e.getEntity() instanceof Player)) return;

    Player player = (Player) e.getEntity();
    for (Home home : RHomes.getHomes().getHomeList()) {
      UUID owner = UUID.fromString(home.getUuid());
      if (home.getClaimArea() == null) continue;
      if (!home.getClaimArea().contains(player.getLocation())) continue;

      if (home.getUuid().equals(player.getUniqueId().toString()) && RHomes.getHomes().getSettingsConfig().getConfig().getBoolean("home-protection.pvp.pvp-inverted")) {
        e.setCancelled(true);
      }

    }

  }

  /*  @EventHandler
  public void onEntityDamage(EntityDamageByEntityEvent e) {
    if (!RHomes.getHomes().getSettingsConfig().getConfig().getBoolean("home-protection.enabled"))
      return;

    if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
      final Player player = (Player) e.getEntity();
      final Player damager = (Player) e.getDamager();
      for (Home home : RHomes.getHomes().getHomeList()) {
        UUID owner = UUID.fromString(home.getUuid());
        if (home.getClaimArea() == null) continue;
        if (owner.equals(player.getUniqueId())) continue;
        if (!home.getClaimArea().contains(player.getLocation())) continue;
        // TODO
      }
    }
  }*/
}
