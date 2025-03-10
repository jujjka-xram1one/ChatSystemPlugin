package xi.jujjka.chatSystem.util;

import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class SoundBlocker {
    public static boolean isBlocked(Player speaker, Player listener) {
        if (speaker.equals(listener)) {
            return false;
        }

        RayTraceResult result = speaker.getWorld().rayTraceBlocks(
                speaker.getEyeLocation(),
                listener.getEyeLocation().toVector().subtract(speaker.getEyeLocation().toVector()).normalize(),
                speaker.getEyeLocation().distance(listener.getEyeLocation())
        );

        return result != null;
    }
}