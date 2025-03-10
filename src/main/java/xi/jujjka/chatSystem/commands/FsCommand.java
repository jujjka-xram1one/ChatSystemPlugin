package xi.jujjka.chatSystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import xi.jujjka.chatSystem.util.ChatColorUtil;
import xi.jujjka.chatSystem.util.MySQL;

public class FsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        String message = String.join(" ", args).trim();
        if (message.isEmpty()) return false;

        String emoteColor = MySQL.getPlayerColor(player.getUniqueId().toString());
        NamedTextColor color = ChatColorUtil.getColor(emoteColor);

        Component finalMessage = processMessage(player, message, color);

        player.getWorld().getPlayers().forEach(p -> {
            if (p.getLocation().distance(player.getLocation()) <= 2) {
                p.sendMessage(finalMessage);
            }
        });

        return true;
    }

    private Component processMessage(Player player, String message, NamedTextColor color) {
        Component playerName = Component.text(player.getName(), NamedTextColor.WHITE);
        Component finalMessage;

        if (message.startsWith("*")) {
            message = message.substring(1).trim();
            finalMessage = formatMessage(player, "[Fısıldayarak] ", message, color);
        } else if (message.startsWith("'''")) {
            message = message.substring(3).trim();
            finalMessage = formatMessage(player, "[Fısıldayarak] ", message, NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, true);
        } else {
            finalMessage = formatMessage(player, "[Fısıldayarak] ", message, NamedTextColor.WHITE);
        }

        return finalMessage;
    }

    private Component formatMessage(Player player, String prefix, String message, NamedTextColor color) {
        return Component.text(prefix, color)
                .append(Component.text(player.getName(), NamedTextColor.WHITE))
                .append(Component.text(": ", NamedTextColor.WHITE))
                .append(Component.text(message, color));
    }
}
