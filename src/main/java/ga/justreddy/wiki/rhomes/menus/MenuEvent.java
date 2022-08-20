package ga.justreddy.wiki.rhomes.menus;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuEvent implements Listener {

  @EventHandler
  public void onMenuClick(InventoryClickEvent e){
    final InventoryHolder holder = e.getInventory().getHolder();
    if(holder instanceof AbstractMenu) {
      e.setCancelled(true);
      if(e.getCurrentItem() == null) return;
      ((AbstractMenu) holder).handle(e);
    }
  }


}
