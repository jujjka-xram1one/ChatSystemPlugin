package xi.jujjka.chatSystem;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import xi.jujjka.chatSystem.commands.*;
import xi.jujjka.chatSystem.listener.Chat$Listener;
import xi.jujjka.chatSystem.listener.ColorPickerListener;
import xi.jujjka.chatSystem.util.MySQL;

public final class  ChatSystem extends JavaPlugin {

    private static ChatSystem instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        setupDatabase();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        MySQL.close();
    }

    private void setupDatabase() {
        MySQL.connect();
        MySQL.createTable();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new Chat$Listener(), this);
        getServer().getPluginManager().registerEvents(new ColorPickerListener(), this);
    }

    private void registerCommands() {
        registerCommand("do", new DoCommand());
        registerCommand("ba", new BaCommand());
        registerCommand("fs", new FsCommand());
        registerCommand("ff", new FfCommand());
        registerCommand("kf", new KfCommand());
        registerCommand("ooc", new OocCommand());
        registerCommand("gooc", new GoocCommand());
        registerCommand("renkler", new RenklerCommand());
    }

    private void registerCommand(String name, CommandExecutor executor) {
        if (getCommand(name) != null) {
            getCommand(name).setExecutor(executor);
        }
    }

    public static ChatSystem getInstance() {
        return instance;
    }
}
