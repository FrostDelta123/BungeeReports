package ru.frostdelta.bungeereports.pluginMessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import ru.frostdelta.bungeereports.action.Action;

import java.util.HashMap;
import java.util.Map;

public class LockerListener implements PluginMessageListener {

    public static Map<Player, Player> requestMap = new HashMap<>();

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (channel.equals("Locker")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            Action action = Action.getAction(in.readUTF());
            if(!action.equals(Action.CLASSES)){
                return;
            }
            String link = in.readUTF();
            if(requestMap.containsKey(player)){
                requestMap.get(player).sendMessage(ChatColor.GOLD + "Loaded classes of " + ChatColor.RED + player.getName() + " " + ChatColor.GOLD + link);
             requestMap.remove(player);
            }
        }
    }

}
