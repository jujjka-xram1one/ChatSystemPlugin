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
        Component baseComponent = Component.text(player.getName() + ": ", NamedTextColor.WHITE)
                .append(Component.text("[BİLGİ] ", emoteColor));

        if (message.startsWith("*")) {
            message = message.substring(1).trim();
        }

        return baseComponent.append(handleQuotes(message, emoteColor));
    }

    private Component handleQuotes(String message, NamedTextColor emoteColor) {
        String[] parts = message.split("\"", -1);
        Component messageComponent = Component.empty();

        // Adding the opening quote at the beginning
        messageComponent = messageComponent.append(Component.text("\""));

        for (int i = 0; i < parts.length; i++) {
            if (i % 2 == 0) {
                messageComponent = messageComponent.append(Component.text(parts[i], emoteColor));
            } else {
                messageComponent = messageComponent.append(Component.text(parts[i], NamedTextColor.WHITE).decorate(net.kyori.adventure.text.format.TextDecoration.ITALIC));
            }
        }

        // Adding the closing quote at the end
        messageComponent = messageComponent.append(Component.text("\""));

        return messageComponent;
    }
}
