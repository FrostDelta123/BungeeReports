package ru.frostdelta.bungeereports.gui;

import net.minecraft.server.v1_12_R1.ItemSkull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.frostdelta.bungeereports.loader;
import ru.frostdelta.bungeereports.pluginMessage.getPlayerCount;
import ru.frostdelta.bungeereports.pluginMessage.pluginMessage;

public class openUI {


    private loader plugin;

    public openUI(loader instance){

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

        Inventory inv = Bukkit.createInventory(null, slots, "Reports");

                int x = 0;

                for (String player : playerList) {

                    ItemStack skull = new ItemStack(Material.SKULL_ITEM);
                    ItemMeta itemMeta = skull.getItemMeta();
                    itemMeta.setDisplayName(player);
                    skull.setItemMeta(itemMeta);

                    inv.setItem(x, skull);

                    x++;
                }

            p.openInventory(inv);
    }

}
