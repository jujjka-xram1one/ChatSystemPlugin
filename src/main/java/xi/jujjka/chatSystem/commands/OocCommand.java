package xi.jujjka.chatSystem.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OocCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        String message = String.join(" ", args).trim();
        if (message.isEmpty()) return false;

        Component finalMessage = Component.text(player.getName() + " [OOC]: " + message, NamedTextColor.WHITE);

        player.getWorld().getPlayers().forEach(p -> {
            if (p.getLocation().distance(player.getLocation()) <= 20) {
                p.sendMessage(finalMessage);
            }
        });

        return true;
    }
}
