package xi.jujjka.chatSystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class RenklerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        openColorSelectionGUI(player);
        return true;
    }

    public void openColorSelectionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 18, "Choose Emote Color");

        ItemStack[] dyeItems = {
                new ItemStack(Material.RED_DYE),
                new ItemStack(Material.GREEN_DYE),
                new ItemStack(Material.BLUE_DYE),
                new ItemStack(Material.YELLOW_DYE),
                new ItemStack(Material.ORANGE_DYE),
                new ItemStack(Material.PINK_DYE),
                new ItemStack(Material.PURPLE_DYE),
                new ItemStack(Material.CYAN_DYE),
                new ItemStack(Material.LIGHT_BLUE_DYE),
                new ItemStack(Material.MAGENTA_DYE),
                new ItemStack(Material.LIME_DYE),
                new ItemStack(Material.BROWN_DYE),
                new ItemStack(Material.WHITE_DYE),
                new ItemStack(Material.BLACK_DYE),
        };

        String[] colorNames = {
                "§cRed", "§2Green", "§9Blue", "§eYellow", "§6Orange", "§dPink", "§5Purple",
                "§bCyan", "§3Light Blue", "§dMagenta", "§aLime", "§7Brown", "§fWhite", "§0Black"
        };

        Map<String, NamedTextColor> colorMap = new HashMap<>();
        colorMap.put("red", NamedTextColor.RED);
        colorMap.put("green", NamedTextColor.GREEN);
        colorMap.put("blue", NamedTextColor.BLUE);
        colorMap.put("yellow", NamedTextColor.YELLOW);
        colorMap.put("orange", NamedTextColor.GOLD);
        colorMap.put("pink", NamedTextColor.LIGHT_PURPLE);
        colorMap.put("purple", NamedTextColor.DARK_PURPLE);
        colorMap.put("cyan", NamedTextColor.AQUA);
        colorMap.put("light_blue", NamedTextColor.BLUE);
        colorMap.put("magenta", NamedTextColor.LIGHT_PURPLE);
        colorMap.put("lime", NamedTextColor.GREEN);
        colorMap.put("brown", NamedTextColor.GRAY);
        colorMap.put("white", NamedTextColor.WHITE);
        colorMap.put("black", NamedTextColor.BLACK);

        for (int i = 0; i < dyeItems.length; i++) {
            ItemMeta meta = dyeItems[i].getItemMeta();
            if (meta != null) {
                meta.setDisplayName(colorNames[i]);
                dyeItems[i].setItemMeta(meta);
            }

            int position = (i < 9) ? i : i + 2;
            gui.setItem(position, dyeItems[i]);
        }

        player.openInventory(gui);
    }
}