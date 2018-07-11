package ru.frostdelta.bungeereports;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.frostdelta.bungeereports.gui.PunishUI;
import ru.frostdelta.bungeereports.gui.ReasonsUI;

import java.util.HashMap;
import java.util.Map;

public class EventHandler implements Listener {

    private Loader plugin;

    public EventHandler(Loader instance){

        plugin = instance;

    }

    Network Network = new Network();
    UpdateReport update = new UpdateReport();

    private Map<String, String> map = new HashMap<String, String>();
    private Map<String, String> ban = new HashMap<String, String>();
    private Map<String, String> comment = new HashMap<String, String>();
    private static Map<Integer, String> send = new HashMap<Integer, String>();

    public EventHandler(HashMap<Integer, String> sender) {

        EventHandler.send = sender;

    }


    @org.bukkit.event.EventHandler
    public void asyncChatEvent(AsyncPlayerChatEvent e){
        String player = e.getPlayer().getName();
       if(comment.containsKey(player)){
           String reason = comment.get(player);
           Network.addReport(player, map.get(player),reason, e.getMessage());
           map.remove(player);
           comment.remove(player);

           e.getPlayer().sendMessage(ChatColor.GREEN + "Репорт успешно отправлен!");

           e.setCancelled(true);
       }
    }



    @org.bukkit.event.EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();

        if(e.getInventory().getName().equalsIgnoreCase("punishmenu")){

            String s = p.getOpenInventory().getItem(4).getItemMeta().getDisplayName();

            if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Принять")){

                update.updateReport(ban.get(p.getName()), s, "accept");
                ban.remove(p.getName());
                p.getOpenInventory().close();
                p.sendMessage(ChatColor.GREEN + "Репорт принят");
            }else{
                update.updateReport(ban.get(p.getName()), s, "reject");
                ban.remove(p.getName());
                p.getOpenInventory().close();
                p.sendMessage(ChatColor.RED + "Репорт отклонён!");
            }

            e.setCancelled(true);
        }

        if(e.getInventory().getName().equalsIgnoreCase("getreports")){

            PunishUI PunishUI = new PunishUI(plugin);

            String s = send.get(e.getSlot());

            ban.put(e.getWhoClicked().getName(), e.getCurrentItem().getItemMeta().getDisplayName());
            PunishUI.openGUI(e.getCurrentItem().getItemMeta().getDisplayName(), p, s);

            e.setCancelled(true);
        }
        if(e.getInventory().getName().equalsIgnoreCase("reports")){

            ReasonsUI ReasonsUI = new ReasonsUI(plugin);
            map.put(e.getWhoClicked().getName(), e.getCurrentItem().getItemMeta().getDisplayName());
            ReasonsUI.openGUI(p);

            e.setCancelled(true);
        }
        if(e.getInventory().getName().equalsIgnoreCase("reasons")){

            if(!plugin.getConfig().getBoolean("comments")) {

                Network.addReport(e.getWhoClicked().getName(), map.get(e.getWhoClicked().getName()), e.getCurrentItem().getItemMeta().getDisplayName(),"");
                map.remove(e.getWhoClicked().getName());
                e.getWhoClicked().getOpenInventory().close();
                p.sendMessage(ChatColor.GREEN + "Репорт успешно отправлен!");

            }else{
                comment.put(e.getWhoClicked().getName(), e.getCurrentItem().getItemMeta().getDisplayName());
                e.getWhoClicked().getOpenInventory().close();
                e.getWhoClicked().sendMessage(ChatColor.RED + "Введите комментарий в чат!");
            }

            e.setCancelled(true);
        }
    }

}
