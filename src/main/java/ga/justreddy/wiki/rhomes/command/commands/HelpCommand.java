package ga.justreddy.wiki.rhomes.command.commands;

import ga.justreddy.wiki.rhomes.command.BaseCommand;
import ga.justreddy.wiki.rhomes.command.ICommand;
import ga.justreddy.wiki.rhomes.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;

public class HelpCommand implements ICommand {

  @Override
  public String name() {
    return "help";
  }

  @Override
  public String description() {
    return "All commands you have access to are displayed here";
  }

  @Override
  public String syntax() {
    return "/home help [page]";
  }

  @Override
  public String permission() {
    return null;
  }

  @Override
  public void run(Player player, String[] args) {
    List<String> list = new ArrayList<>();
    int page;
    try {
      page = Integer.parseInt(args[1]);
    } catch (NumberFormatException | IndexOutOfBoundsException ex) {
      page = 1;
    }

    for (ICommand cmd : BaseCommand.getCommands()) {
      if (cmd.permission() != null && !player.hasPermission(cmd.permission())) continue;
      if (list.contains(cmd.syntax())) continue;
      list.add(
          Utils.format(
              "&b" + cmd.syntax() + " &7-" + " &e" + cmd.description()
          )
      );
    }

    ChatPaginator.ChatPage paginator = ChatPaginator.paginate(String.join("\n", list), page, ChatPaginator.AVERAGE_CHAT_PAGE_WIDTH, 5);
    TextComponent next = new TextComponent(Utils.format("&e&l>>"));
    next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home help " + (page+1)));
    TextComponent previous = new TextComponent(Utils.getCenteredMessage("<center>&e&l<<</center>"));
    previous.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ("/home help ") + (page-1)));
    if (page > paginator.getTotalPages()) return;
    Utils.sendMessage(player, "&6%line%");

    if (page == 1) {
      TextComponent main = new TextComponent(Utils.getCenteredMessage("<center> &6HELP (Page %page%/%maxpage%)</center> ").replace("%page%", String.valueOf(page)).replace("%maxpage%", String.valueOf(paginator.getTotalPages())));
      player.spigot().sendMessage(main, next);
    } else if (page == paginator.getTotalPages()) {
      TextComponent main = new TextComponent(Utils.format(" &6HELP (Page %page%/%maxpage%) ").replace("%page%", String.valueOf(page)).replace("%maxpage%", String.valueOf(paginator.getTotalPages())));
      player.spigot().sendMessage(previous, main);
    } else {
      TextComponent main = new TextComponent(Utils.format(" &6HELP (Page %page%/%maxpage%) ").replace("%page%", String.valueOf(page)).replace("%maxpage%", String.valueOf(paginator.getTotalPages())));
      player.spigot().sendMessage(previous, main, next);
    }

    for (String line : paginator.getLines()) {
      Utils.sendMessage(player, line);
    }
    Utils.sendMessage(player, "&6%line%");


  }
}
