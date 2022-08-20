package ga.justreddy.wiki.rhomes.menus.menus;

import ga.justreddy.wiki.rhomes.menus.AbstractMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EditHomeMenu extends AbstractMenu {

  private final String name;

  public EditHomeMenu(String name) {
    this.name = name;
  }

  @Override
  public String name() {
    return "&aEdit home: " + name;
  }

  @Override
  public int size() {
    return 36;
  }

  @Override
  public void handle(InventoryClickEvent e) {

  }

  @Override
  public void setMenuItems(Player player) {

  }
}
