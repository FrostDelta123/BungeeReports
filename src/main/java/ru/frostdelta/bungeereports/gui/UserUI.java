package ru.frostdelta.bungeereports.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.holders.UserHolder;

public class UserUI {


    private Loader plugin;

    public UserUI(Loader instance){

        plugin = instance;

    }


    public void openGUI(Player p, int count, String[] playerList) {

        int slots = 9;
        while (count > slots) {
            slots = slots + 9;
            if(count < slots){
                break;
            }
        }

        Inventory inv = Bukkit.createInventory(new UserHolder(), slots, "Reports");

                int x = 0;

                for (String player : playerList) {

                    ItemStack skull = new ItemStack(Material.SKULL_ITEM);
                    ItemMeta itemMeta = skull.getItemMeta();
                    itemMeta.setDisplayName(player);
                    skull.setItemMeta(itemMeta);

                    inv.setItem(x, skull);

                    x++;
                }
             if(p.hasPermission("bungeereports.player")){
                 p.openInventory(inv);
                 return;
             }else p.sendMessage(ChatColor.RED + "У вас нет прав!");

    }

}
