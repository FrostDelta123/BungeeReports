package ru.frostdelta.bungeereports.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.frostdelta.bungeereports.Loader;

import java.util.List;

public class ReasonsUI {

    private Loader plugin;

    public ReasonsUI(Loader instance){

        plugin = instance;

    }


    public void openGUI(Player p) {

        List<String> reasons = plugin.getConfig().getStringList("reasons");


        int slots = 9;
        while (reasons.toArray().length > slots) {
            slots = slots + 9;
            if(reasons.toArray().length < slots){
                break;
            }
        }

        Inventory inv = Bukkit.createInventory(null, slots, "Reasons");

            int x = 0;

            for (String reason : reasons) {

                ItemStack skull = new ItemStack(Material.SKULL_ITEM);

                ItemMeta itemMeta = skull.getItemMeta();
                itemMeta.setDisplayName(reason);
                skull.setItemMeta(itemMeta);
                inv.setItem(x, skull);

                x++;
            }


        p.openInventory(inv);

    }

}
