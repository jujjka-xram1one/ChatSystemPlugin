package xi.jujjka.chatSystem.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xi.jujjka.chatSystem.util.ChatColorUtil;

public class GoocCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        String message = String.join(" ", args).trim();
        if (message.isEmpty()) return false;

        NamedTextColor messageColor = ChatColorUtil.getColor("white");
        Component finalMessage = Component.text(player.getName() + " [GOOC]: " + "\"" + message + "\"", messageColor);

        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(finalMessage));

        return true;
    }
}

