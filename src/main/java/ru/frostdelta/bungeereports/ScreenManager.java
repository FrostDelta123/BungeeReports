package ru.frostdelta.bungeereports;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.action.Action;
import ru.frostdelta.bungeereports.executor.Executor;
import ru.frostdelta.bungeereports.utils.Messages;

public class ScreenManager {

    public static void addScreenshot(Player targetPlayer, Player player){

        BungeeReports plugin = BungeeReports.inst();
        if (targetPlayer.isOnline()) {

            Executor.getRequestQueue().put(targetPlayer.getName(), player.getName());
            Executor.getActionQueue().put(targetPlayer.getName(), Action.SCREENSHOT);
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(Action.SCREENSHOT.getActionName());
            out.writeUTF(plugin.getConfig().getString("mod.clientID"));
            plugin.sendMessage(targetPlayer, out);

        } else {
            player.sendMessage(ChatColor.RED + "&cPlayer " + targetPlayer + " оffline!");
        }
    }

    public static void getScreenshot(String name, Player player){
        BungeeReports plugin = BungeeReports.inst();
        String screenshots = Network.getScreenshots(name);
        if (!screenshots.isEmpty()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(Action.SCREENSHOTS.getActionName());
            out.writeUTF(name);
            out.writeUTF(screenshots);
            plugin.sendMessage(player, out);

        } else {
            player.sendMessage(Messages.SCREEN_CMMAND_ERROR);
        }
    }

}
