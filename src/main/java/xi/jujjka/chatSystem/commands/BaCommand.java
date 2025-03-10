package xi.jujjka.chatSystem.commands;

import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import xi.jujjka.chatSystem.util.ChatColorUtil;
import xi.jujjka.chatSystem.util.MySQL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaCommand implements CommandExecutor {

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
        } else if (message.startsWith("'''")) {
            message = message.substring(3).trim();
            messageComponent = formatMessage(player, "[BAĞIRARAK] ", message, emoteColor).decoration(TextDecoration.ITALIC, true);
        } else {
            messageComponent = formatMessage(player, "[BAĞIRARAK] ", message, NamedTextColor.WHITE);
        }

        return messageComponent;
    }

    private Component formatMessage(Player player, String prefix, String message, NamedTextColor color) {
        return Component.text(player.getName() + ": ", NamedTextColor.WHITE)
                .append(Component.text(prefix, color))
                .append(formatTextWithQuotes(message, color));
    }

    private Component formatTextWithQuotes(String message, NamedTextColor color) {
        Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(message);
        Component result = Component.text("");
        int lastEnd = 0;

        while (matcher.find()) {
            result = result.append(Component.text(message.substring(lastEnd, matcher.start()), color));

            String quotedText = matcher.group(1);
            result = result.append(Component.text(quotedText, NamedTextColor.WHITE, TextDecoration.ITALIC));

            lastEnd = matcher.end();
        }

        if (lastEnd < message.length()) {
            result = result.append(Component.text(message.substring(lastEnd), color));
        }

        return result;
    }
}
