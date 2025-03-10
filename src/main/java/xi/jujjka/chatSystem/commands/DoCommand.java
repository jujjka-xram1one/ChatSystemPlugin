package xi.jujjka.chatSystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import xi.jujjka.chatSystem.util.ChatColorUtil;
import xi.jujjka.chatSystem.util.MySQL;

public class DoCommand implements CommandExecutor {

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
            if (p.getLocation().distance(player.getLocation()) <= 15) {
                p.sendMessage(finalMessage);
            }
        });

        return true;
    }

    private Component processMessage(Player player, String message, NamedTextColor emoteColor) {
        Component messageComponent;

        if (message.startsWith("*")) {
            message = message.substring(1).trim();
            messageComponent = formatMessage(player, "[BİLGİ] ", message, emoteColor);
        } else if (message.startsWith("\"")) {
            message = message.substring(1, message.length() - 1).trim();
            messageComponent = formatMessage(player, "[BİLGİ] ", "\"" + message + "\"", NamedTextColor.WHITE);
        } else {
            messageComponent = formatMessage(player, "[BİLGİ] ", message, NamedTextColor.WHITE);
        }

        return messageComponent;
    }

    private Component formatMessage(Player player, String prefix, String message, NamedTextColor color) {
        return Component.text(player.getName() + ": ", NamedTextColor.WHITE)
                .append(Component.text(prefix, color))
                .append(Component.text(message, color));
    }
}
