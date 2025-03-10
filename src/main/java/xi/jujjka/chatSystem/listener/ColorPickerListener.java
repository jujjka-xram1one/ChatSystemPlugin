package xi.jujjka.chatSystem.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import xi.jujjka.chatSystem.util.MySQL;

public class ColorPickerListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals("Choose Emote Color")) {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot >= 0 && slot < 14) {
                String selectedColor = getColorFromSlot(slot);
                MySQL.setPlayerColor(player.getUniqueId().toString(), selectedColor);
                player.sendMessage("You selected " + selectedColor + " as your emote color!");
            }
        }
    }

    private String getColorFromSlot(int slot) {
        String[] colors = {
                "red", "green", "blue", "yellow", "orange", "pink", "purple", "cyan",
                "light_blue", "magenta", "lime", "brown", "white", "black"
        };
        return colors[slot];
    }
}