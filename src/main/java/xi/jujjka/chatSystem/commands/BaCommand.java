package xi.jujjka.chatSystem.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xi.jujjka.chatSystem.util.ChatColorUtil;
import xi.jujjka.chatSystem.util.MySQL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaCommand implements CommandExecutor {

    private static final Pattern QUOTE_PATTERN = Pattern.compile("\"([^\"]*)\"");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        String message = String.join(" ", args).trim();
        if (message.isEmpty()) return false;

        String color = MySQL.getPlayerColor(player.getUniqueId().toString());
        NamedTextColor emoteColor = ChatColorUtil.getColor(color);

        Component finalMessage = processMessage(player, message, emoteColor);

        player.getWorld().getPlayers().forEach(p -> {
            if (p.getLocation().distance(player.getLocation()) <= 30) {
                p.sendMessage(finalMessage);
            }
        });

        return true;
    }

    private Component processMessage(Player player, String message, NamedTextColor emoteColor) {
        Component messageComponent;

        if (message.startsWith("*")) {
            message = message.substring(1).trim();
            messageComponent = formatMessage(player, "[BAĞIRARAK] ", message, emoteColor);
        } else {
            messageComponent = formatMessage(player, "[BAĞIRARAK] ", message, NamedTextColor.WHITE);
        }

        return Component.text(player.getName() + ": ", NamedTextColor.WHITE)
                .append(messageComponent);
    }

    private Component formatMessage(Player player, String prefix, String message, NamedTextColor color) {
        Component result = Component.text(prefix, color);

        Matcher matcher = QUOTE_PATTERN.matcher(message);
        int lastEnd = 0;

        result = result.append(Component.text("\""));

        while (matcher.find()) {
            result = result.append(Component.text(message.substring(lastEnd, matcher.start()), color));
            result = result.append(Component.text(matcher.group(1), NamedTextColor.WHITE, TextDecoration.ITALIC));
            lastEnd = matcher.end();
        }

        if (lastEnd < message.length()) {
            result = result.append(Component.text(message.substring(lastEnd), color));
        }

        result = result.append(Component.text("\""));
        return result;
    }
}







