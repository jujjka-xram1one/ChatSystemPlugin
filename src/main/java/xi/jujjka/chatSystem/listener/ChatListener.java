package xi.jujjka.chatSystem.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xi.jujjka.chatSystem.ChatSystem;
import xi.jujjka.chatSystem.util.ChatColorUtil;
import xi.jujjka.chatSystem.util.LoggerUtil;
import xi.jujjka.chatSystem.util.MySQL;
import xi.jujjka.chatSystem.util.SoundBlocker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    private static final Pattern QUOTES_PATTERN = Pattern.compile("\"([^\"]*)\"");

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        String color = MySQL.getPlayerColor(player.getUniqueId().toString());
        NamedTextColor emoteColor = ChatColorUtil.getColor(color);

        LoggerUtil.logChat(player.getName(), message, ChatSystem.getInstance());

        Component finalMessage;

        if (message.startsWith("*")) {
            finalMessage = formatEmote(message.substring(1).trim(), emoteColor);
        } else if (message.contains("\"")) {
            finalMessage = formatSpeech(player, message);
        } else {
            finalMessage = Component.text("\"" + message + "\"", NamedTextColor.WHITE);
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().distance(player.getLocation()) <= 15) {
                p.sendMessage(SoundBlocker.isBlocked(player, p)
                        ? Component.text("*boğuk bir gürültü*")
                        : Component.text(player.getName() + ": ").append(finalMessage));
            }
        }
        event.setCancelled(true);
    }

    private Component formatSpeech(Player player, String message) {
        Matcher matcher = QUOTES_PATTERN.matcher(message);
        Component result = Component.text("");
        int lastEnd = 0;
        while (matcher.find()) {
            result = result.append(Component.text(message.substring(lastEnd, matcher.start()), NamedTextColor.WHITE));
            String quotedText = matcher.group(1);
            result = quotedText.startsWith("*")
                    ? result.append(formatEmote(quotedText.substring(1).trim(), ChatColorUtil.getColor(MySQL.getPlayerColor(player.getUniqueId().toString()))))
                    : result.append(Component.text(quotedText, NamedTextColor.WHITE, TextDecoration.ITALIC));
            lastEnd = matcher.end();
        }
        return lastEnd < message.length()
                ? result.append(Component.text(message.substring(lastEnd), NamedTextColor.WHITE))
                : result;
    }

    private Component formatEmote(String message, NamedTextColor color) {
        Matcher matcher = QUOTES_PATTERN.matcher(message);
        Component result = Component.text("");
        int lastEnd = 0;
        while (matcher.find()) {
            result = result.append(Component.text(message.substring(lastEnd, matcher.start()), color));
            result = result.append(Component.text(matcher.group(1), NamedTextColor.WHITE, TextDecoration.ITALIC));
            lastEnd = matcher.end();
        }
        return lastEnd < message.length()
                ? result.append(Component.text(message.substring(lastEnd), color))
                : result;
    }
}
