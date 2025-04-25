package xi.jujjka.chatSystem.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xi.jujjka.chatSystem.util.MySQL;

import java.util.ArrayList;
import java.util.List;

public class KfCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        List<Player> nearbyPlayers = new ArrayList<>();
        for (Entity entity : player.getNearbyEntities(1, 1, 1)) {
            if (entity instanceof Player) {
                nearbyPlayers.add((Player) entity);
            }
        }
        nearbyPlayers.add(player);

        if (nearbyPlayers.isEmpty()) {
            return false;
        }

        String message = String.join(" ", args).trim();
        if (message.isEmpty()) return false;

        String color = MySQL.getPlayerColor(player.getUniqueId().toString());
        NamedTextColor emoteColor;
        try {
            emoteColor = NamedTextColor.NAMES.value(color.toUpperCase());
        } catch (IllegalArgumentException e) {
            emoteColor = NamedTextColor.WHITE;
        }

        Component emoteMessage = Component.text(player.getName() + " eğilip yanındakinin kulağına bir şeyler fısıldadı. ", NamedTextColor.WHITE)
                .append(Component.text(message, emoteColor));

        for (Player nearbyPlayer : nearbyPlayers) {
            nearbyPlayer.sendMessage(emoteMessage.decoration(TextDecoration.ITALIC, true));
        }

        player.getWorld().getPlayers().forEach(p -> {
            if (p.getLocation().distance(player.getLocation()) <= 15) {
                p.sendMessage(emoteMessage.decoration(TextDecoration.ITALIC, true));
            }
        });

        return true;
    }
}

