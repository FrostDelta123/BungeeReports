package ru.frostdelta.bungeereports;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.action.Action;
import ru.frostdelta.bungeereports.executor.Executor;

public class ScreenManager {

    Loader plugin;
    public ScreenManager(Loader instance){
        plugin = instance;
    }

    public void addScreenshot(Player targetPlayer, Player player){

        if (targetPlayer.isOnline()) {

            Executor.getRequestQueue().put(targetPlayer.getName(), player.getName());
            Executor.getActionQueue().put(targetPlayer.getName(), Action.SCREENSHOT);
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(Action.SCREENSHOT.getActionName());
            out.writeUTF(plugin.getConfig().getString("mod.clientID"));
            plugin.sendMessage(targetPlayer, out);

        } else {
            player.sendMessage(ChatColor.RED + "&cИгрок " + targetPlayer + " оффлайн!");
        }
    }

    public void getScreenshot(String name, Player player){
        Network db = new Network();
        String screenshots = db.getScreenshots(name);
        if (!screenshots.isEmpty()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(Action.SCREENSHOTS.getActionName());
            out.writeUTF(name);
            out.writeUTF(db.getScreenshots(name));
            plugin.sendMessage(player, out);

        } else {
            player.sendMessage(ChatColor.RED + "&cДля начала сделай скрин экрана.");
        }
    }

}
