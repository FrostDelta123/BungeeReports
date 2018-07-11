package ru.frostdelta.bungeereports;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import ru.frostdelta.bungeereports.gui.moderUI;
import ru.frostdelta.bungeereports.gui.punishUI;
import ru.frostdelta.bungeereports.gui.reasonsUI;
import ru.frostdelta.bungeereports.rewards.giveReward;

import java.util.HashMap;

public class eventHandler implements Listener {

    private loader plugin;

    public eventHandler(loader instance){

        plugin = instance;

    }

    network network = new network();
    updatereport update = new updatereport();

    private HashMap<String, String> map = new HashMap<String, String>();
    private HashMap<String, String> ban = new HashMap<String, String>();
    private HashMap<String, String> comment = new HashMap<String, String>();
    private static HashMap<Integer, String> send = new HashMap<Integer, String>();

    public eventHandler(HashMap<Integer, String> sender) {

        eventHandler.send = sender;

    }


    @EventHandler
    public void asyncChatEvent(AsyncPlayerChatEvent e){
        String player = e.getPlayer().getName();
       if(comment.containsKey(player)){
           String reason = comment.get(player);
           network.addReport(player, map.get(player),reason, e.getMessage());
           map.remove(player);
           comment.remove(player);

           e.getPlayer().sendMessage(ChatColor.GREEN + "Репорт успешно отправлен!");

           e.setCancelled(true);
       }
    }



    @EventHandler
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

            punishUI punishUI = new punishUI(plugin);

            String s = send.get(e.getSlot());

            ban.put(e.getWhoClicked().getName(), e.getCurrentItem().getItemMeta().getDisplayName());
            punishUI.openGUI(e.getCurrentItem().getItemMeta().getDisplayName(), p, s);

            e.setCancelled(true);
        }
        if(e.getInventory().getName().equalsIgnoreCase("reports")){

            reasonsUI reasonsUI = new reasonsUI(plugin);
            map.put(e.getWhoClicked().getName(), e.getCurrentItem().getItemMeta().getDisplayName());
            reasonsUI.openGUI(p);

            e.setCancelled(true);
        }
        if(e.getInventory().getName().equalsIgnoreCase("reasons")){

            if(!plugin.getConfig().getBoolean("comments")) {

                network.addReport(e.getWhoClicked().getName(), map.get(e.getWhoClicked().getName()), e.getCurrentItem().getItemMeta().getDisplayName(),"");
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
