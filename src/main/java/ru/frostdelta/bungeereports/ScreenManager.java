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

        Loader plugin = Loader.inst();
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
        Loader plugin = Loader.inst();
        String screenshots = Network.getScreenshots(name);
        if (!screenshots.isEmpty()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(Action.SCREENSHOTS.getActionName());
            out.writeUTF(name);
            out.writeUTF(Network.getScreenshots(name));
            plugin.sendMessage(player, out);

        } else {
            player.sendMessage(Messages.SCREEN_CMMAND_ERROR);
        }
    }

}
