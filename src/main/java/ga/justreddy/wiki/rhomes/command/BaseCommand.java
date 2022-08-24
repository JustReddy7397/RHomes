package ga.justreddy.wiki.rhomes.command;

import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.command.commands.CreateCommand;
import ga.justreddy.wiki.rhomes.command.commands.DeleteCommand;
import ga.justreddy.wiki.rhomes.command.commands.EditCommand;
import ga.justreddy.wiki.rhomes.command.commands.ReloadCommand;
import ga.justreddy.wiki.rhomes.command.commands.SetPrivateCommand;
import ga.justreddy.wiki.rhomes.command.commands.TeleportCommand;
import ga.justreddy.wiki.rhomes.command.commands.TestCommand;
import ga.justreddy.wiki.rhomes.command.commands.VisitCommand;
import ga.justreddy.wiki.rhomes.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class BaseCommand implements TabExecutor {

  @Getter private static final List<ICommand> commands = new ArrayList<>();

  public BaseCommand() {
    addCommands(new CreateCommand(), new DeleteCommand(), new TeleportCommand(), new SetPrivateCommand(), new EditCommand(), new VisitCommand(), new ReloadCommand(), new TestCommand());
    for (ICommand command : commands) {
      if (command.permission() != null && Bukkit.getPluginManager().getPermission(command.permission()) == null) {
        Bukkit.getPluginManager().addPermission(new Permission(command.permission()));
      }
    }
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!(sender instanceof Player)) return true;


    Player player = (Player) sender;
    if (args.length > 0) {
      for (ICommand cmd : commands) {
        if (cmd.name().equals(args[0])) {
          if (cmd.permission() != null && !player.hasPermission(cmd.permission())) {
            Utils.sendMessage(
                player,
                RHomes.getHomes().getMessagesConfig().getConfig().getString("error.no-permission"));
            return true;
          }
          cmd.run(player, args);
          return true;
        }
      }
    } else if (args.length == 0) {
      return true;
    }
    return true;
  }

  private static void addCommands(ICommand... cmds) {
    commands.addAll(Arrays.asList(cmds));
  }

  @Override
  public List<String> onTabComplete(
      CommandSender sender, Command command, String alias, String[] args) {
    // if(!(sender instanceof Player)) return null;
    Player player = (Player) sender;
    if (args.length == 1) {
      List<String> list = new ArrayList<>();
      for (ICommand cmd : commands) {
        if (list.contains(cmd.name()) || !player.hasPermission(cmd.permission())) continue;
        list.add(cmd.name());
      }
      return list;
    }
    if (args.length == 2) {
      if (args[0].equalsIgnoreCase("teleport")) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < RHomes.getHomes().getDatabase().getHomes(player).size(); i++) {
          list.add(RHomes.getHomes().getDatabase().getHomes(player).get(i).getName());
        }
        return list;
      } else if (args[0].equalsIgnoreCase("delete")) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < RHomes.getHomes().getDatabase().getHomes(player).size(); i++) {
          list.add(RHomes.getHomes().getDatabase().getHomes(player).get(i).getName());
        }
        return list;
      }else if (args[0].equalsIgnoreCase("setprivate")) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < RHomes.getHomes().getDatabase().getHomes(player).size(); i++) {
          list.add(RHomes.getHomes().getDatabase().getHomes(player).get(i).getName());
        }
        return list;
      }
    }
    return null;
  }
}
