package ga.justreddy.wiki.rhomes.command;

import org.bukkit.entity.Player;

public interface ICommand {

  String name();

  String description();

  String syntax();

  String permission();

  void run(Player player, String[] args);

}
