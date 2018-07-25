package ru.frostdelta.bungeereports.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.frostdelta.bungeereports.EventHandler;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.holders.GetReportsHolder;

import java.util.ArrayList;
import java.util.HashMap;

public class GetReportsUI {

    private Loader plugin;

    public GetReportsUI(Loader instance){

        plugin = instance;

    }

    public HashMap<Integer, String> sender = new HashMap<Integer, String>();

    public void openGUI(Player p, int count, ArrayList<String> senderList, ArrayList<String> reasonList, ArrayList<String> playerList, ArrayList<String> comment) {

        if(!playerList.isEmpty()) {

            int slots = 9;
            while (count > slots) {
                slots = slots + 9;
                if (count < slots) {
                    break;
                }
            }

            Inventory inv = Bukkit.createInventory(new GetReportsHolder(), slots, "GetReports");



            for (int x = 0; count > x; x++) {

                ItemStack skull = new ItemStack(Material.SKULL_ITEM);
                ItemMeta itemMeta = skull.getItemMeta();
                itemMeta.setDisplayName(playerList.get(x));

                ArrayList<String> lore = new ArrayList<String>();

                lore.add("Репорт отправлен: " + senderList.get(x));
                lore.add("Причина: " + reasonList.get(x));
                lore.add("Комментарий: " + comment.get(x));
                itemMeta.setLore(lore);


                skull.setItemMeta(itemMeta);
                sender.put(x,senderList.get(x));

                inv.setItem(x, skull);
            }

            //Какой же это костыль, самому стыдно.
            EventHandler handler = new EventHandler(sender);
            p.openInventory(inv);
        }else p.sendMessage(ChatColor.GREEN + "В данный момент активных репортов нет!");

    }

}
