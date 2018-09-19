package ru.frostdelta.bungeereports.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.frostdelta.bungeereports.Loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatLogger implements Listener {
    Loader plugin;

    public ChatLogger(Loader instance){

        plugin = instance;

    }

    private static Map<Player, ArrayList<String>> chatLog = new HashMap<Player, ArrayList<String>>();
    private static List<String> allChatMessages = new ArrayList<String>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        if(getLeatestChatMessages().size() > plugin.getConfig().getInt("chat.limit")){
            getLeatestChatMessages().add(e.getPlayer().getName() + ": " + e.getMessage());
        }else {
            if (!getLeatestChatMessages().isEmpty()) {
                getLeatestChatMessages().remove(0);
                getLeatestChatMessages().add(e.getPlayer().getName() + ": " + e.getMessage());
            }
        }
        if(getChatLog().containsKey(player)){
            if(plugin.getConfig().getInt("chat.limit") > getChatLog().get(player).size()){
                getChatLog().get(player).add(e.getMessage());
            }
            getChatLog().get(player).remove(0);
            getChatLog().get(player).add(e.getMessage());
        }else{
         getChatLog().put(player, new ArrayList<String>());
         getChatLog().get(player).add(e.getMessage());
        }
    }

    public static List<String> getLeatestChatMessages() {
        return allChatMessages;
    }

    public static Map<Player, ArrayList<String>> getChatLog() {
        return chatLog;
    }
}
