package xi.jujjka.chatSystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import xi.jujjka.chatSystem.ChatSystem;

public class MySQL {

    private static Connection connection;

    public static void connect() {
        FileConfiguration config = ChatSystem.getInstance().getConfig();
        try {
            if (connection == null || connection.isClosed()) {
                String url = "jdbc:" + config.getString("database.type") + "://" +
                        config.getString("database.host") + ":" +
                        config.getString("database.port") + "/" +
                        config.getString("database.database");
                connection = DriverManager.getConnection(url, config.getString("database.user"), config.getString("database.password"));
                Bukkit.getLogger().info("Successfully connected to MySQL.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Failed to connect to MySQL.");
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                Bukkit.getLogger().info("MySQL connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Error closing MySQL connection.");
        }
    }

    public static String getPlayerColor(String uuid) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT color FROM player_colors WHERE player_uuid = ?");
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("color");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Error fetching player color.");
        }
        return "gray";
    }

    public static void setPlayerColor(String uuid, String color) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("REPLACE INTO player_colors (player_uuid, color) VALUES (?, ?)");
            statement.setString(1, uuid);
            statement.setString(2, color);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Error setting player color.");
        }
    }

    public static void createTable() {
        try {
            PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_colors (" +
                    "player_uuid VARCHAR(36) PRIMARY KEY, " +
                    "color VARCHAR(20) NOT NULL)");
            statement.executeUpdate();
            Bukkit.getLogger().info("Table 'player_colors' created or already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Error creating 'player_colors' table.");
        }
    }
}
