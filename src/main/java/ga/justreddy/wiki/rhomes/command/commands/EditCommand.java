package ga.justreddy.wiki.rhomes.command.commands;

import ga.justreddy.wiki.rhomes.command.ICommand;
import ga.justreddy.wiki.rhomes.menus.menus.SelectHomeMenu;
import org.bukkit.entity.Player;

public class EditCommand implements ICommand {

  @Override
  public String name() {
    return "edit";
  }

  @Override
  public String description() {
    return "Edit one of your houses";
  }

  @Override
  public String syntax() {
    return "/home edit";
  }

  @Override
  public String permission() {
    return "rhomes.command.edit";
  }

  @Override
  public void run(Player player, String[] args) {
    new SelectHomeMenu().open(player);
  }
}
