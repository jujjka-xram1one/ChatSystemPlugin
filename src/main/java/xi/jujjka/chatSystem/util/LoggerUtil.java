package xi.jujjka.chatSystem.util;

import xi.jujjka.chatSystem.ChatSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerUtil {

    private static final String LOG_FILE_NAME = "chat_logs.log";

    public static void logChat(String playerName, String message, ChatSystem plugin) {
        message = message.replaceAll("(?i)§[0-9a-fk-or]", "");

        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File logFile = new File(dataFolder, LOG_FILE_NAME);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logMessage = String.format("[%s] %s: %s\n", timestamp, playerName, message);

        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(logMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}